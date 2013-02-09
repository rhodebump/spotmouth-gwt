package com.spotmouth.gwt.client.friends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotLabel;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.FriendRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/*
* this constructs links to all application menus
*/
public class ManageFriendPanel extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.friendsMobile();
        } else {
            return MyWebApp.resources.friends();
        }

    }

    protected boolean isValid() {
        if (!isEmpty(smsPhoneNumberBox)) {
            if (smsPhoneNumberBox.getValue().length() < 11) {
                getMessagePanel().displayError("The SMS phone number must be at least 11 numbers.");
                getMessagePanel().displayError("Begin with a 1 + the area code + the number");
                getMessagePanel().displayError("Example: 1-412-123-4567");

            }
        } else if (isEmpty(emailTextBox)) {
            getMessagePanel().displayError("A valid phone number or email address is required");
        }
        return (!getMessagePanel().isHaveMessages());
    }
    public String getTitle() {
        return "Friend";
    }

    private FriendHolder friendHolder = null;
    //private SpotHolder spotHolder = null;
    private AsyncCallback friendCreatedCallback = null;

    //spotholder could be null, if it is not null, then the friend would be for the spot
    public ManageFriendPanel(MyWebApp mywebapp, FriendHolder friendHolder, AsyncCallback friendCreatedCallback) {
        super(mywebapp);




        this.friendHolder = friendHolder;
        this.friendCreatedCallback = friendCreatedCallback;



        if (MyWebApp.isDesktop()) {
            //	<input type="text" placeholder="Enter your friend's e-mail" required="true" tabindex="3"/>

            emailTextBox = new TextBox();
            emailTextBox.getElement().setAttribute("placeholder","Enter your friend's e-mail");
            emailTextBox.getElement().setAttribute("required","required");

            emailTextBox.setTabIndex(3);
          //  emailTextBox.getElement().setAttribute("placeholder","Enter your friend's e-mail");

            //	<input type="text" placeholder="Enter your friend's phone number" required="true" tabindex="3"/>

            smsPhoneNumberBox.getElement().setAttribute("placeholder","Enter your friend's phone number");
            smsPhoneNumberBox.getElement().setAttribute("required","required");

            smsPhoneNumberBox.setTabIndex(3);


            Button inviteFriendButton = new Button("Invite");
            inviteFriendButton.setStyleName("btn_blue");
            inviteFriendButton.addClickHandler(saveHandler);

            friendJoinMessageTextArea.setStyleName("f_inp_text");
            friendJoinMessageTextArea.setVisibleLines(3);
            friendJoinMessageTextArea.setTabIndex(4);

            //<input type="text" placeholder="Enter your friend's name" tabindex="1"/>

            friendNameTextBox.getElement().setAttribute("placeholder","Enter your friend's name");

            friendNameTextBox.setTabIndex(1);
            FriendFormComposite ffc = new FriendFormComposite(emailTextBox,smsPhoneNumberBox,inviteFriendButton,friendNameTextBox,friendJoinMessageTextArea);
            add(ffc);
            return;
        }

        //this.spotHolder = spotHolder;
        boolean readonly = false;
        if (friendHolder.getId() != null) {
            readonly = true;
        }
        if (readonly) {
            SpotLabel note = new SpotLabel(
                    "A friend can not be edited, it can only be deleted.  If you need to make changes, you will need to delete this friend and add the friend again.");
            add(note);
        } else {
            Label requiredLabel = new Label("Please provide either an email address or a phone number");
            add(requiredLabel);
        }
        emailTextBox = addTextBox("Email", "email", friendHolder.getEmailAddress());
        emailTextBox.setReadOnly(readonly);
        smsPhoneNumberBox = addTextBox("SMS Phone Number", "smsPhoneNumber", friendHolder.getSmsPhoneNumber());
        smsPhoneNumberBox.setReadOnly(readonly);

        if (friendHolder.getFriendUserHolder() != null) {
            addTextBox("Username", "username", friendHolder.getUserHolder().getUsername(), true);
        }
        Fieldset smsFS = new Fieldset();
        VerticalPanel smsVP = new VerticalPanel();
        smsFS.add(smsVP);
        Label smsInviteStatusLabel = new Label("SMS Invite Status:  " + friendHolder.getSmsInviteSent());
        smsVP.add(smsInviteStatusLabel);
        Label smsAcceptStatusLabel = new Label("SMS Acceptance Status:  " + friendHolder.getSmsAccountValidated());
        smsVP.add(smsAcceptStatusLabel);
        add(smsFS);

        Fieldset smsFS2 = new Fieldset();
        VerticalPanel smsVP2 = new VerticalPanel();
        smsFS2.add(smsVP2);
        add(smsFS2);
        Label emailInviteStatusLabel = new Label("Email Invite Status:  " + friendHolder.getEmailInviteSent());
        smsVP2.add(emailInviteStatusLabel);
        Label emailAcceptStatusLabel = new Label("Email Acceptance Status:  " + friendHolder.getEmailInviteAccepted());
        smsVP2.add(emailAcceptStatusLabel);
        if (!readonly) {
            add(saveButton());
        }
        if (friendHolder.getId() != null) {
            deletebtn.addClickHandler(deleteFriendHandler);
            fixButton(deletebtn);
            add(deletebtn);
        }
        add(cancelButton());
    }

    //Label btn = new Label("Save");
    Label deletebtn = new Label("Delete");
//    ClickHandler saveFriendHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//
//            // btn.setEnabled(false);
//            saveFriend();
//        }
//    };
    ClickHandler deleteFriendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean delete = Window.confirm("Delete your friend?");
            if (delete) {
                //deletebtn.setEnabled(false);
                deleteFriend(friendHolder);
            }
        }
    };
    //private TextBox emailAddressTextBox = new TextBox();
    private TextBox smsPhoneNumberBox = new TextBox();
    private TextBox friendNameTextBox = new TextBox();

    private TextArea friendJoinMessageTextArea = new TextArea();


    /*
            friendHolder.setFriendName(friendNameTextBox.getValue());
        friendHolder.setFriendInviteMessage(friendJoinMessageTextarea.getValue());
     */
    public void toggleFirst() {
        emailTextBox.setFocus(true);
    }







    AsyncCallback messageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().clear();
            getMessagePanel().displayMessage("Friend saved");
            getMessagePanel().displayMessage("Invitation to friend will be sent, once accepted, you will be able to message them.");
        }
    };


    protected void doSave() {

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFriendHolder(friendHolder);
        friendRequest.setAuthToken(mywebapp.getAuthToken());
        friendHolder.setEmailAddress(emailTextBox.getValue());
        friendHolder.setSmsPhoneNumber(smsPhoneNumberBox.getValue());
        friendHolder.setFriendName(friendNameTextBox.getValue());
        friendHolder.setFriendInviteMessage(friendJoinMessageTextArea.getValue());

        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveFriend(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
                if (friendCreatedCallback != null) {
                    friendCreatedCallback.onFailure(caught);
                }
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.fetchFriendsAndGroups(null);
                    if (friendCreatedCallback == null) {

                        mywebapp.setFriendsAndGroups(null);
                       // m//ywebapp.toggleManageFriends(messageCallback);
                        mywebapp.setHomeCallback(messageCallback);
                        History.newItem(MyWebApp.MANAGE_FRIENDS);

                    } else {
                        friendCreatedCallback.onSuccess(mobileResponse.getFriendHolder());
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public boolean isLoginRequired() {
        return true;
    }
}
