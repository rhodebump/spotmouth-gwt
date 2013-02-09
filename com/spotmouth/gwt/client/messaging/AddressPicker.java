package com.spotmouth.gwt.client.messaging;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;

//extending message panel since both share a lot of code regarding addresses of friends and groups
//part 2, not extending, was getting too tricky
public class AddressPicker extends SpotBasePanel {
    public boolean showBackToResults() {
        return false;
    }


    public boolean isRootPanel() {
        return false;
    }

    protected void initSelectedAddresses() {
        addFieldset(selectedFriendsAndGroupsPanel, "To", "toaddress");
        refresh();
    }

    private void initAvailableAddresses() {
        //availableFriendsAndGroupsPanel.clear();

        addFieldset(availableFriendsAndGroupsPanel, "Available Friends and Groups", "na");
        refresh();
    }




//    private void refreshSelected() {
//        GWT.log("refreshSelected");
//        selectedFriendsAndGroupsPanel.clear();
//        showSelectedFriends(messagePanel.getItemHolder().getFriendHolders(), removeFriendHandler);
//        showSelectedGroups(messagePanel.getItemHolder().getGroupHolders(), removeGroupHandler);
//        showSelectedUserGroups(messagePanel.getItemHolder().getUserGroupHolders(), removeUserGroupHandler);
//
//        messagePanel.refreshSelected();
//    }



    @Override
    public ItemHolder getItemHolder() {
        return messagePanel.getItemHolder();
    }

    private MessagePanel messagePanel = null;

    public AddressPicker(MyWebApp mywebapp, MessagePanel messagePanel) {
        super(mywebapp);
        add(backToMessageButton());
        this.messagePanel = messagePanel;
        initSelectedAddresses();
        initAvailableAddresses();
        add(backToMessageButton());
    }

    Label backToMessageButton() {
        Label btn = new Label("Back to message");
        btn.addClickHandler(backHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler backHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            messagePanel.refresh();
            //mywebapp.toggleBack();
            mywebapp.swapCenter(messagePanel);

        }
    };




//    ClickHandler removeGroupHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            getMessagePanel().clear();
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget widget = (Widget) sender;
//                GroupHolder groupHolder = clickMapGroupHolder.get(widget);
//                messagePanel.getItemHolder().getGroupHolders().remove(groupHolder);
//            }
//            refreshSelected();
//            refreshAvailable();
//        }
//    };
//
//    ClickHandler removeUserGroupHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            getMessagePanel().clear();
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget widget = (Widget) sender;
//                UserGroupHolder groupHolder = clickMapUserGroupHolder.get(widget);
//                messagePanel.getItemHolder().getUserGroupHolders().remove(groupHolder);
//            }
//            refreshSelected();
//            refreshAvailable();
//        }
//    };


}
