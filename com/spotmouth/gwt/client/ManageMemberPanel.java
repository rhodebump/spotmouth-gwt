package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.GroupHolder;
import com.spotmouth.gwt.client.dto.MemberHolder;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/23/12
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManageMemberPanel extends SpotBasePanel implements SpotMouthPanel {


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.groupsMobile();
        }   else {

            return MyWebApp.resources.groups();
        }

    }

    private TextBox firstNameTextBox = new TextBox();
    private TextBox lastNameTextBox = new TextBox();


    private TextBox emailTextBox = new TextBox();
    private TextBox smsPhoneNumberBox = new TextBox();
//    ClickHandler addMemberHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            //we need to have a email or a smsPhoneNumber
//            boolean validPhone = true;
//            if (isEmpty(smsPhoneNumberBox)) {
//                //getMessagePanel().displayError("SMS Phone number is required");
//                validPhone = false;
//            }
//            boolean validEmail = true;
//            if (isEmpty(emailTextBox)) {
//                //getMessagePanel().displayError("Email address is required");
//                //return;
//                validEmail = false;
//            }
//            if (!validEmail && !validPhone) {
//                getMessagePanel().displayError("A valid phone number or email address is required");
//                return;
//            }
//            // btn.setEnabled(false);
//            addMember();
//        }
//    };

    protected boolean isValid() {
        boolean validPhone = true;
        if (isEmpty(smsPhoneNumberBox)) {
            validPhone = false;
        }
        boolean validEmail = true;
        if (isEmpty(emailTextBox)) {
            validEmail = false;
        }
        if (!validEmail && !validPhone) {
            getMessagePanel().displayError("A valid phone number or email address is required");
        }

        return (!getMessagePanel().isHaveMessages());

    }

    protected void doSave() {

        MemberHolder memberHolder = new MemberHolder();
        if (! isEmpty(emailTextBox)) {
            memberHolder.setEmailAddress(emailTextBox.getValue());
        }
        if (! isEmpty(smsPhoneNumberBox)) {
            memberHolder.setSmsPhoneNumber(smsPhoneNumberBox.getValue());
        }

        //need firstname

        if (! isEmpty(firstNameTextBox)) {
            memberHolder.setFirstName(firstNameTextBox.getValue());
        }

        //need lastname
        if (! isEmpty(lastNameTextBox)) {
            memberHolder.setLastName(lastNameTextBox.getValue());
        }


        postDialog.hide();


        memberCreatedCallback.onSuccess(memberHolder);
    }

    private MemberHolder memberHolder = null;
    private AsyncCallback memberCreatedCallback = null;

    public ManageMemberPanel(MyWebApp mywebapp, MemberHolder memberHolder, AsyncCallback memberCreatedCallback,GroupHolder groupHolder) {
        super(mywebapp);
        this.memberHolder = memberHolder;
        this.memberCreatedCallback = memberCreatedCallback;
        boolean readonly = false;
        if (memberHolder.getId() != null) {
            readonly = true;
        }
        if (readonly) {
            SpotLabel note = new SpotLabel(
                    "A member can not be edited, it can only be deleted.  If you need to make changes, you will need to delete this friend and add the friend again.");
            add(note);
        } else {
            Label requiredLabel = new Label("Please provide either an email address or a phone number");
            add(requiredLabel);
        }
        addGroupHeader(groupHolder);
        emailTextBox = addTextBox("Email", "email", memberHolder.getEmailAddress());
        emailTextBox.setReadOnly(readonly);
        smsPhoneNumberBox = addTextBox("SMS Phone Number", "smsPhoneNumber", memberHolder.getSmsPhoneNumber());
        smsPhoneNumberBox.setReadOnly(readonly);


        firstNameTextBox = addTextBox("First Name", "fn", memberHolder.getFirstName());
        firstNameTextBox.setReadOnly(readonly);
        lastNameTextBox = addTextBox("Last Name", "ln", memberHolder.getLastName());
        lastNameTextBox.setReadOnly(readonly);



        //this would be the user that created and owns the friend
        if (memberHolder.getUserHolder() != null) {
            addTextBox("Username", "username", memberHolder.getUserHolder().getUsername(), true);
        }
        //(Widget widget, String labelText, String name) {
        Label invitationStatusLabel = new Label("" + memberHolder.getEmailInviteSent());
        addFieldset(invitationStatusLabel, "Invitation Status", "inviteStatus");
        Label acceptStatusLabel = new Label("" + memberHolder.getEmailInviteAccepted());
        addFieldset(acceptStatusLabel, "Acceptance Status", "acceptStatus");
        if (!readonly) {
            add(saveButton());
        }
        add(cancelButton());
    }



    public void toggleFirst() {
        emailTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;
    }
}
