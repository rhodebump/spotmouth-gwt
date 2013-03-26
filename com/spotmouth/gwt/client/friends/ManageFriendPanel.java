package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.shared.GWT;
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
import com.spotmouth.gwt.client.common.TextField;
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
        } else if (isEmpty(emailTextField)) {
            getMessagePanel().displayError("A valid phone number or email address is required");
        }

        if (!isEmpty(emailTextField)) {
            //is it a valid email address
            //need to split by comma and validate each email address
            String[] emailAddresses = emailTextField.getValue().split(",");

            for (String emailaddress:emailAddresses) {

                boolean valid = emailaddress.matches(emailPattern);
                if (!valid) {
                    getMessagePanel().displayError("Email address " + emailaddress + " is  not invalid.");
                }



            }

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
            emailTextField = new TextField();
            Button inviteFriendButton = new Button();
            inviteFriendButton.addClickHandler(saveHandler);
            FriendFormComposite ffc = new FriendFormComposite(emailTextField, smsPhoneNumberBox, inviteFriendButton, friendNameTextBox, friendJoinMessageTextArea);
            add(ffc);
            return;
        }
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
        emailTextField = addTextBox("Email", "email", friendHolder.getEmailAddress());
        emailTextField.setReadOnly(readonly);
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


    Label deletebtn = new Label("Delete");

    ClickHandler deleteFriendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean delete = Window.confirm("Delete your friend?");
            if (delete) {
                deleteFriend(friendHolder);
            }
        }
    };
    private TextField smsPhoneNumberBox = new TextField();
    private TextField friendNameTextBox = new TextField();
    private TextArea friendJoinMessageTextArea = new TextArea();


    public void toggleFirst() {
        emailTextField.setFocus(true);
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
        friendHolder.setEmailAddress(emailTextField.getValue());
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
                        GWT.log("friendCreatedCallback is null");
                        mywebapp.setFriendsAndGroups(null);
                        mywebapp.setHomeCallback(messageCallback);
                        History.newItem(MyWebApp.MANAGE_FRIENDS);
                    } else {
                        GWT.log("friendCreatedCallback is not null");
                        friendCreatedCallback.onSuccess(mobileResponse);
                    }
                } else {
                    GWT.log("friendCreatedCallback 3");
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


}
