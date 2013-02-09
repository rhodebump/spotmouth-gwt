package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.FriendRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 6/17/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpotFriendPanel extends SpotBasePanel implements SpotMouthPanel {


    public String getTitle() {
        return "Follow Spot";
    }



    ClickHandler removeFromFriendsSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                removeFromFriends();


            }
        }
    };



    ClickHandler followSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                addToFriends();
            }
        }
    };


    private void removeFromFriends() {

        getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSpotHolder(spotHolder);
        friendRequest.setAuthToken(mywebapp.getAuthToken());





        myService.stopFollowSpot(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                    mywebapp.setFavorites(null);
                    mywebapp.toggleFavorites(stopFollowingMessages);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


    AsyncCallback stopFollowingMessages = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("This spot has been removed from your favorites");
        }
    };



    private void addToFriends() {


        getMessagePanel().clear();


        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSpotHolder(spotHolder);
        friendRequest.setAuthToken(mywebapp.getAuthToken());

        friendRequest.getFriendHolder().setDeviceNotifications(deviceCheckBox.getValue());
        friendRequest.getFriendHolder().setEmailNotifications(emailCheckBox.getValue());
        friendRequest.getFriendHolder().setSmsNotifications(smsCheckBox.getValue());



        myService.followSpot(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                    mywebapp.setFavorites(null);
                    mywebapp.toggleFavorites(followingMessages);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


    AsyncCallback followingMessages = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("You are now following this spot");
        }
    };


    private SpotHolder spotHolder = null;

    public SpotFriendPanel(MyWebApp mywebapp,FriendHolder friendHolder,SpotHolder spotHolder) {
        super(mywebapp,false);
        this.spotHolder = spotHolder;
        addSpotHeader(spotHolder);
        VerticalPanel cp = new VerticalPanel();
        cp.add(smsCheckBox);
        cp.add(emailCheckBox);
        cp.add(deviceCheckBox);
        smsCheckBox.setValue(friendHolder.getSmsNotifications());
        emailCheckBox.setValue(friendHolder.getEmailNotifications());
        deviceCheckBox.setValue(friendHolder.getDeviceNotifications());
        addFieldset(cp, "", "na4");
        Label saveNotificationsLabel = new Label("Save Notification Settings");
        fixButton(saveNotificationsLabel);
        saveNotificationsLabel.addClickHandler(followSpotHandler);
        add(saveNotificationsLabel);
        Label removeFavoritesLabel = new Label("Remove From Favorites");
        fixButton(removeFavoritesLabel);
        removeFavoritesLabel.addClickHandler(removeFromFriendsSpotHandler);
        add(removeFavoritesLabel);
        add(cancelButton());



    }
    private CheckBox smsCheckBox = new CheckBox("Receive SMS Notifications");

    private CheckBox emailCheckBox = new CheckBox("Receive Email Notifications");

    private CheckBox deviceCheckBox = new CheckBox("Receive Device Notifications (iphone and android)");


    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getSpotDetailPanel();
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
