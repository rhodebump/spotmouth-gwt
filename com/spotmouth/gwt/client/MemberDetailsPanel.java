package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MemberHolder;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 12/31/11
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberDetailsPanel extends SpotBasePanel implements SpotMouthPanel {
    CheckBox adminCheckbox = null;
    private MemberHolder memberHolder = null;


    private ManageMembersPanel mmp = null;

    public MemberDetailsPanel(MyWebApp mywebapp, MemberHolder memberHolder,ManageMembersPanel mmp) {
        super(mywebapp);
        this.memberHolder = memberHolder;
        this.mmp = mmp;
        //FriendHolder friendHolder = memberHolder.getFriendHolder();
        if (memberHolder.getEmailAddress() != null) {
            TextBox tb = addTextBox("Email", "email", memberHolder.getEmailAddress());
            tb.setReadOnly(true);
        }
        if (memberHolder.getSmsPhoneNumber() != null) {
            TextBox tb = addTextBox("SMS", "sms", memberHolder.getSmsPhoneNumber());
            tb.setReadOnly(true);
        }
        if (memberHolder.getUserHolder() != null) {
            Label label = new Label(memberHolder.getUserHolder().getUsername());
            addFieldset(label, "Username", "username");
        }
        Label adminLabel = new Label("Administrators can approve members of this group");
        add(adminLabel);
        adminCheckbox = addCheckbox2("Administrator?", "admin", memberHolder.isAdmin(), null);
        Label m2 = new Label("Being part of a group means that both the group and the user have to agree to be joined.");
        add(m2);
        addFieldset(new Label("" + memberHolder.getEmailInviteSent()), "Group Invite Status", "inviteStatus");
        //addFieldset(new Label("" + memberHolder.getInviteAccepted()), "Group Accept Status", "acceptStatus");
        // CheckBox groupAcceptStatus = new CheckBox("Group Accept Status");
        //String label, String formname, boolean value, ImageResource imageResource
        groupAcceptStatus = addCheckbox2("Group Accept Status", "groupAcceptStatus", memberHolder.getEmailInviteAccepted(), null);
        addFieldset(new Label("" + memberHolder.getEmailInviteSent()), "User Invite Status", "inviteStatus");
        addFieldset(new Label("" + memberHolder.getEmailInviteAccepted()), "User Accept Status", "acceptStatus");
        //need to do a checkbox for admin
        //save
        add(saveButton());
        //cancel
        add(cancelButton());
    }

    public Button cancelButton() {
        return cancelButton1("Cancel");
    }

    protected ClickHandler cancelHandler1 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //postDialog.hide();

            mywebapp.swapCenter(mmp);
        }
    };

    public Button cancelButton1(String label) {
        Button btn = new Button(label);


        btn.addClickHandler(cancelHandler1);

        addImageToButton(btn, MyWebApp.resources.cancelButton(),MyWebApp.resources.cancelButtonMobile());


        btn.setStyleName("btn_blue");
       // fixButton(btn);
        return btn;
    }



    private CheckBox groupAcceptStatus = null;

    protected void doSave() {
        postDialog.hide();
        memberHolder.setAdmin(adminCheckbox.getValue());
        memberHolder.setEmailInviteAccepted(groupAcceptStatus.getValue());
        //let's just go back a screen
        //mywebapp.toggleBack();

        mywebapp.swapCenter(mmp);
    }
//    @Override
//    public boolean isRootPanel() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
