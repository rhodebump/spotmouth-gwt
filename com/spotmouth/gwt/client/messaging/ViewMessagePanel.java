package com.spotmouth.gwt.client.messaging;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
//import com.spotmouth.gwt.client.om.ItemHolder;

public class ViewMessagePanel extends SpotBasePanel {
    public ImageResource getImageResourceSmall() {
        return null;
    }

    public ImageResource getImageResourceBig() {
        return null;
    }
//    @Override
//    public boolean isRootPanel() {
//        return false;
//    }

    public ViewMessagePanel(MyWebApp mywebapp) {
        super(mywebapp);
    }

    protected void initSelectedAddresses() {
        addFieldset(selectedFriendsAndGroupsPanel, "To:", "to");
        refresh();
    }

//    @Override
//    public void refreshSelected() {
//
//        selectedFriendsAndGroupsPanel.clear();
//        clickMapFriendHolder.clear();
//        showSelectedFriends(getItemHolder().getFriendHolders());
//        showSelectedGroups(getItemHolder().getGroupHolders());
//        showSelectedUserGroups(getItemHolder().getUserGroupHolders());
//    }



    // this does not get added to the app via the standard "swapCenter" thing
    public ViewMessagePanel(MyWebApp mywebapp, ItemHolder itemHolder) {
        super(mywebapp);
        setItemHolder(itemHolder);
        Label from = new Label(itemHolder.getUserHolder().getUsername());
        addFieldset(from, "From", "from");
        initSelectedAddresses();
        Label subject = new Label(itemHolder.getTitle());
        addFieldset(subject, "Subject", "subject");
        HTML textData = new HTML(itemHolder.getTextData());
        addFieldset(textData, "Message", "message");
        add(contentsPanel);
        addContentHolder(itemHolder.getContentHolder(), true, true);


        //back to the messages
        add(cancelButton());

    }

    public Button cancelButton() {
        return cancelButton("Back To Messages");
    }

}
