package com.spotmouth.gwt.client.profile;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.FriendRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.UserHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/*
* this constructs links to all application menus
*/
public class ViewProfilePanel extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "View Profile";
    }

    private void addToFriend() {
        FriendRequest friendRequest = new FriendRequest();
        FriendHolder friendHolder = new FriendHolder();
        friendRequest.setFriendHolder(friendHolder);
        friendRequest.setAuthToken(mywebapp.getAuthToken());
        friendHolder.setUserHolder(new UserHolder());
        friendHolder.getUserHolder().setId(userHolder.getId());

        //friendHolder.setUserId(userHolder.getId());
        // friendHolder.setEmailAddress(emailTextBox.getValue());
        // friendHolder.setSmsPhoneNumber(smsPhoneNumberBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveFriend(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.fetchFriendsAndGroups(null);
                    mywebapp.getMessagePanel().clear();
                    mywebapp.setFriendsAndGroups(null);
                    getMessagePanel().displayMessage("Friend has been added");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    AsyncCallback loginCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            addToFriend();
        }
    };
    protected ClickHandler addToFriendsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (!mywebapp.isLoggedIn(loginCallback)) {
                addToFriend();
            }
        }
    };
    private UserHolder userHolder = null;

    public ViewProfilePanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);
        this.userHolder = mobileResponse.getUserHolder();

        Image profileImage = getImage(userHolder.getContentHolder(), "320x320");

        if (profileImage == null) {
            profileImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
        }



            Button addToFriendsButton = new Button();
            //addToFriendsButton.setStyleName("pp_add_friends");
            addToFriendsButton.getElement().setId("pp_add_friends");
            addToFriendsButton.addClickHandler(addToFriendsHandler);
            ViewProfileComposite viewProfileComposite = new ViewProfileComposite(addToFriendsButton);
            viewProfileComposite.setAbout(userHolder.getAboutMe());
            viewProfileComposite.setLastName(userHolder.getLastName());
            viewProfileComposite.setFirstName(userHolder.getFirstName());
            viewProfileComposite.setCity(userHolder.getCity());
            viewProfileComposite.setState(userHolder.getState());
            viewProfileComposite.setCountryCode(userHolder.getCountryCode());
        viewProfileComposite.setImageUrl(profileImage.getUrl());
            add(viewProfileComposite);

    }

    @Override
    public void addedToDom() {
        super.addedToDom();
                    /*
            If user comes on page to his friend, button '.pp_add_friends' should be hidden and div '#pp_is_friend' - visible;
             */

        boolean isFriendAlready = isFriendAlready();
        if (isFriendAlready) {
            Element optin = DOM.getElementById("pp_add_friends");
            optin.addClassName("hideme");
            Element element2 = DOM.getElementById("pp_is_friend");
            element2.removeClassName("hideme");

        }



    }

    private boolean isFriendAlready() {
        if (mywebapp.getAuthenticatedUser() == null) {
            return false;
        }
        //are we friend??
        UserHolder meUserHolder = mywebapp.getAuthenticatedUser();
        for(FriendHolder friendHolder:mywebapp.getFriendsAndGroups().getFriendHolders()) {
            UserHolder fuh = friendHolder.getUserHolder();
            if (fuh == null) {
            } else if (fuh.getId().equals(meUserHolder.getId())) {
                return true;
            }
        }

        return false;
    }


    public void toggleFirst() {
    }



}
