package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.H2;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.friends.ManageFriendPanel;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 6/19/12
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageUserGroupPanel extends SpotBasePanel implements SpotMouthPanel {


    public String getTitle() {
        return "Manage Group";
    }


    protected ClickHandler addFriendHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("addFriendHandler1");
                Widget widget = (Widget) sender;
                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
                if (friendHolder == null) {
                    GWT.log("friendHolder is null");
                    return;
                }
                userGroupHolder.getFriendHolders().add(friendHolder);
            }
            refresh();
        }
    };



    UserGroupHolder userGroupHolder = null;
    ClickHandler removeFriendFromGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget widget = (Widget) sender;
                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
                List<FriendHolder> list = new ArrayList<FriendHolder>();
                list.addAll(userGroupHolder.getFriendHolders());
                userGroupHolder.getFriendHolders().clear();
                for (FriendHolder fh : list) {
                    if (fh.getKey().equals(friendHolder.getKey())) {
                        //this is the one we are removing
                    } else {
                        userGroupHolder.getFriendHolders().add(fh);
                    }
                }
            }
            refresh();
        }
    };
    ClickHandler addFriendToGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget widget = (Widget) sender;
                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
                if (friendHolder != null) {
                    userGroupHolder.getFriendHolders().add(friendHolder);
                }
            }
            refresh();
        }
    };
//    private void refresh() {
//        selectedFriendsAndGroupsPanel.clear();
//        availableFriendsAndGroupsPanel.clear();
//        showSelectedFriends(userGroupHolder.getFriendHolders());
//        showAvailableFriends(mywebapp.getFriendsAndGroups().getFriendHolders(), userGroupHolder.getFriendHolders(), addFriendToGroupHandler);
//    }

    @Override
    public void refresh() {
        buildMembersULPanel();
        buildAvailableULPanel();
        ugfc.setMemberCount(userGroupHolder.getFriendHolders().size());

    }

    AsyncCallback friendCreatedCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            GWT.log("toggleMessaging callback");
            MobileResponse mobileResponse = (MobileResponse) response;
            for(FriendHolder friendHolder:mobileResponse.getFriendHolders()) {
                userGroupHolder.getFriendHolders().add(friendHolder);
            }
            refresh();
            //mywebapp.toggleBack();
            //we need to swap our past panel back in here
            mywebapp.swapCenter(ManageUserGroupPanel.this);

            getMessagePanel().displayMessage("Friend(s) have been added");
        }
    };
    ClickHandler addNewFriendToGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            FriendHolder friendHolder = new FriendHolder();
            ManageFriendPanel mgp = new ManageFriendPanel(mywebapp, friendHolder, friendCreatedCallback);
            mywebapp.swapCenter(mgp);
        }
    };

    private void buildMembersULPanel() {
        membersULPanel.clear();
        for (FriendHolder friendHolder : userGroupHolder.getFriendHolders()) {
            ListItem listItem = getListItem(friendHolder);
            membersULPanel.add(listItem);
            listItem.addDomHandler(removeFriendFromGroupHandler, ClickEvent.getType());
            clickMapFriendHolder.put(listItem, friendHolder);
        }
    }

    private ListItem getListItem(FriendHolder friendHolder) {
        ListItem listItem = new ListItem();
        listItem.setStyleName("vug_f_list_item");
        InlineLabel vug_ava = new InlineLabel();
        vug_ava.setStyleName("vug_ava");
        vug_ava.addClickHandler(removeFriendFromGroupHandler);
        String imageUrl = null;
        //userHolder is the user who owns the friend,which is me
       // UserHolder userHolder = friendHolder.getUserHolder();
        UserHolder userHolder = friendHolder.getFriendUserHolder();
        if (userHolder != null) {
            ImageScaleHolder imageScaleHolder = getImageScaleHolder(userHolder.getContentHolder(), "320x320");
            if (imageScaleHolder != null) {
                imageUrl = imageScaleHolder.getWebServerAssetHolder().getUrl();
            }
        }
        if (imageUrl == null) {
            imageUrl = "css/ava.png";
        }
        String theStyle = "background: url('" + imageUrl + "') no-repeat center center;";
        vug_ava.getElement().setAttribute("style", theStyle);
        listItem.add(vug_ava);
        H2 h2 = new H2();

            h2.setText(friendHolder.getLabel());


        h2.setStyleName("vug_name");
        listItem.add(h2);
        return listItem;
    }

    private void buildAvailableULPanel() {
        /*
        <ul class="vug_f_list" id="f_list_c">

                     <li class="vug_f_list_item" onmousedown="selFriend(this);">
                         <span class="vug_ava" style="background: url('photo.jpg') no-repeat center center"></span>
                         <h2 class="vug_name">User Name</h2>
                     </li>
        */
        availableULPanel.clear();



        List<FriendHolder> friendsList = new ArrayList<FriendHolder>();
        for (FriendHolder friendHolder : mywebapp.getFriendsAndGroups().getFriendHolders()) {
            if (!inList(userGroupHolder.getFriendHolders(), friendHolder)) {
                friendsList.add(friendHolder);
            }
        }
        for (FriendHolder friendHolder : friendsList) {



            ListItem listItem = getListItem(friendHolder);
            //addDomHandler(handler, ClickEvent.getType());
            listItem.addDomHandler(addFriendHandler2, ClickEvent.getType());
            availableULPanel.add(listItem);
            clickMapFriendHolder.put(listItem, friendHolder);

        }


    }

    private ULPanel availableULPanel = new ULPanel();
    private ULPanel membersULPanel = new ULPanel();

    public ManageUserGroupPanel(MyWebApp mywebapp, UserGroupHolder userGroupHolder) {
        super(mywebapp);
        this.userGroupHolder = userGroupHolder;
        Button saveButton = saveButton();
        Button cancelButton = cancelButton();
        groupNameTextBox = new TextField();
        groupNameTextBox.setValue(userGroupHolder.getName());
        availableULPanel.getElement().setId("f_list_c");
        membersULPanel.getElement().setId("f_sel_list");
        buildMembersULPanel();
        buildAvailableULPanel();
        Button inviteButton = new Button();
        inviteButton.addClickHandler(addNewFriendToGroupHandler);
        this.ugfc = new UserGroupFormComposite(saveButton, cancelButton, groupNameTextBox, membersULPanel, availableULPanel, inviteButton);
        ugfc.setMemberCount(userGroupHolder.getFriendHolders().size());
        add(ugfc);
    }

    private UserGroupFormComposite ugfc = null;

    protected boolean isValid() {
        checkRequired(groupNameTextBox, "Name is required");
        return (!getMessagePanel().isHaveMessages());
    }

    ClickHandler deleteUserGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean delete = Window.confirm("Delete group?");
            if (delete) {
                deleteUserGroup();
            }
        }
    };
    private TextField groupNameTextBox = new TextField();

    public void toggleFirst() {
        groupNameTextBox.setFocus(true);
    }

    AsyncCallback saveMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            GWT.log("messageCallback.onSuccess groups saved");
            getMessagePanel().displayMessage("Group saved.");
        }
    };
    AsyncCallback deleteMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Group deleted.");
        }
    };

    protected void doSave() {
        GWT.log("saveUserGroup");
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setUserGroupHolder(userGroupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        userGroupHolder.setName(groupNameTextBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveUserGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //mywebapp.fetchFriendsAndGroups(messageCallback);
                    mywebapp.setFriendsAndGroups(null);
                    mywebapp.setHomeCallback(saveMessageCallback);
                    History.newItem(MyWebApp.VIEW_USER_GROUPS);
                    // mywebapp.toggleViewUserGroups(saveMessageCallback);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void deleteUserGroup() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setUserGroupHolder(userGroupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteUserGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.getMessagePanel().clear();
                    //getMessagePanel().displayMessage("Group deleted.");
                    mywebapp.setFriendsAndGroups(null);
                    // mywebapp.toggleViewUserGroups(deleteMessageCallback);
                    mywebapp.setHomeCallback(deleteMessageCallback);
                    History.newItem(MyWebApp.VIEW_USER_GROUPS);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

}