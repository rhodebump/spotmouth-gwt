package com.spotmouth.gwt.client.messaging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.DataOperationDialog;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.*;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

import java.util.List;

public class MessagingPanel extends SpotBasePanel implements SpotMouthPanel {
    @Override
    public void refresh() {
        GWT.log("MessagingPanel refresh");
        if (MyWebApp.isDesktop()) {
            clickMapFriendHolder.clear();
            clickMapGroupHolder.clear();
            clickMapUserGroupHolder.clear();
            addAddressesUL.clear();
            showAvailableGroups();
            showAvailableUserGroups();
            showAvailableFriends();
            mpFriendsList.clear();
            showSelectedFriends();
            showSelectedGroups();
            showSelectedUserGroups();
            return;
        }
    }

    private void showSelectedGroups() {
        for (GroupHolder groupHolder : getItemHolder().getGroupHolders()) {
            //<span class="mp_list_friend" title="click to remove" onclick="this.style.display = 'none'">User Name</span>
            InlineLabel mpListFriend = new InlineLabel(groupHolder.getName());
            mpListFriend.setTitle("click to remove");
            mpListFriend.setStyleName("mp_list_friend");
            clickMapGroupHolder.put(mpListFriend, groupHolder);
            mpListFriend.addClickHandler(removeGroupFromItemHandler);
            mpFriendsList.getElement().appendChild(mpListFriend.getElement());
        }
    }

    private void showSelectedFriends() {
        GWT.log("showSelectedFriends");
        for (FriendHolder friendHolder : getItemHolder().getFriendHolders()) {

            GWT.log("adding a selected friend");
            //<span class="mp_list_friend" title="click to remove" onclick="this.style.display = 'none'">User Name</span>
            InlineLabel mpListFriend = new InlineLabel(friendHolder.getUserHolder().getUsername());
            mpListFriend.setTitle("click to remove");
            mpListFriend.setStyleName("mp_list_friend");
            clickMapFriendHolder.put(mpListFriend, friendHolder);
            mpListFriend.addClickHandler(removeFriendFromItemHandler);
            mpFriendsList.add(mpListFriend);
        }
    }

   // private ItemHolder itemHolder = new ItemHolder();

    private void showSelectedUserGroups() {
        for (UserGroupHolder ugh : getItemHolder().getUserGroupHolders()) {
            //<span class="mp_list_friend" title="click to remove" onclick="this.style.display = 'none'">User Name</span>
            InlineLabel mpListFriend = new InlineLabel(ugh.getName());
            mpListFriend.setTitle("click to remove");
            mpListFriend.setStyleName("mp_list_friend");
            clickMapUserGroupHolder.put(mpListFriend, ugh);
            mpListFriend.addClickHandler(removeUserGroupFromItemHandler);
            mpFriendsList.getElement().appendChild(mpListFriend.getElement());
        }
    }

    public boolean showBackToResults() {
        return false;
    }

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.messagingMobile();
        } else {
            return MyWebApp.resources.messaging();
        }
    }

    public String getTitle() {
        return "Messaging";
    }

    public String getPageTitle() {
        return getTitle();
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isRootPanel() {
        return false;
    }

    public MessagingPanel() {
    }

    private ULPanel addAddressesUL = new ULPanel();
    private TextField messageSearchTextBox = new TextField();

    protected ClickHandler searchMessagesHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //need to do a keyword search of users messages using messageSearchTextBox as keyword search


            doSearch();

        }
    };



    private void doSearch() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setAuthToken(mywebapp.getAuthToken());

        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setKeywords(messageSearchTextBox.getValue());

        messageRequest.setSearchParameters(searchParameters);
        final DataOperationDialog gettingResultsDialog = new DataOperationDialog("Getting results");
        gettingResultsDialog.show();
        gettingResultsDialog.center();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();

        myService.searchMessages(messageRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                gettingResultsDialog.hide();
            }

            public void onSuccess(Object result) {
                gettingResultsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                resultsUL.clear();
                addItems3(mobileResponse.getItemHolders(),resultsUL);


                resultsUL.setVisible(true);
            }
        });
    }

    protected void doSave() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setItemHolder(getItemHolder());
        //getItemHolder().setTitle(subjectTextBox.getValue());
        getItemHolder().setTextData(contentTextArea.getValue());


        GWT.log(" itemHolder.setTextData=" +contentTextArea.getValue());


        messageRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveMessage(messageRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                // MobileResponse mobileResponse = (MobileResponse) result;
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setMessages(null);
                    mywebapp.toggleMessaging();
                    if (getItemHolder().getSend()) {
                        getMessagePanel().displayMessage("Message sent");
                    } else {
                        getMessagePanel().displayMessage("Message saved");
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


    public void initMessage() {
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.setMessage(true);

         setItemHolder(itemHolder);

    }


    protected ULPanel resultsUL = new ULPanel();

    public MessagingPanel(MyWebApp mywebapp) {
        super(mywebapp);
        if (MyWebApp.isDesktop()) {
            //		<ul class="mp_messages" id="mp_message">

            initMessage();


            ULPanel inUL = new ULPanel();
            inUL.setStyleName("mp_messages");
            inUL.getElement().setId("mp_message");
            ULPanel sentUL = new ULPanel();
            sentUL.setStyleName("mp_messages");
            sentUL.getElement().setId("mp_message");
            sentUL.setVisible(false);
            ULPanel draftUL = new ULPanel();
            draftUL.setStyleName("mp_messages");
            draftUL.getElement().setId("mp_message");
            draftUL.setVisible(false);


            resultsUL.setStyleName("mp_messages");
            resultsUL.getElement().setId("mp_message");
            resultsUL.setVisible(false);




            //<button class="btn_blue">New Message</button>
            addItems3(mywebapp.getMessages().getInMessages(), inUL);
            addItems3(mywebapp.getMessages().getSentMessages(), sentUL);
            addItems3(mywebapp.getMessages().getDraftMessages(), draftUL);

            refresh();

            mpFriendsList.setStyleName("mp_friends_list");
            MessageComposite messageComposite = new MessageComposite(addAddressesUL, mpFriendsList, this,contentTextArea);


            Button searchButton= new Button();
             searchButton.setStyleName("_btn");
            searchButton.addClickHandler(searchMessagesHandler);

            this.messagingHomeComposite = new MessagingHomeComposite(mywebapp,inUL, sentUL, draftUL, messageComposite, this,messageSearchTextBox,searchButton,resultsUL);
            add(messagingHomeComposite);
            return;
        }
        this.addStyleName("ResultsPanel");
        add(newMessageButton());
        addItems2(mywebapp.getMessages().getInMessages(), "Inbox", viewMessageHandler);
        addItems2(mywebapp.getMessages().getSentMessages(), "Sent", viewMessageHandler);
        addItems2(mywebapp.getMessages().getDraftMessages(), "Draft", editMessageHandler);
    }


    private MessagingHomeComposite messagingHomeComposite = null;

    public MessagingHomeComposite getMessagingHomeComposite() {
      return messagingHomeComposite;
    }

    public void addedToDom() {
        mainScoll();
        messageScroll();
    }

    public static native void mainScoll() /*-{
        $wnd.fleXenv.fleXcrollMain('mp_msgs');
        $wnd.fleXenv.updateScrollBars();
    }-*/;

    public static native void messageScroll() /*-{
        $wnd.fleXenv.fleXcrollMain('mp_find_list');

        $wnd.fleXenv.updateScrollBars();
      //  }
    }-*/;


    FlowPanel mpFriendsList = new FlowPanel();

    private void showAvailableGroups() {
        for (GroupHolder groupHolder : mywebapp.getFriendsAndGroups().getGroupHolders()) {
            ListItem listItem = new ListItem();
            listItem.setStyleName("mp_find_list_friend");
            listItem.setTitle("Add group");
            Image image = null;

            if (image == null) {
                image = new Image(MyWebApp.resources.spot_image_placeholder130x130());
            }
            image.setStyleName("mp_find_list_ava");
            listItem.add(image);
            InlineLabel inlineLabel = new InlineLabel(groupHolder.getName());
            inlineLabel.setStyleName("mp_find_list_name");
            listItem.add(inlineLabel);
            inlineLabel.addClickHandler(addGroupHandler);
            clickMapGroupHolder.put(inlineLabel, groupHolder);
            addAddressesUL.add(listItem);
        }
    }

    private void showAvailableUserGroups() {
        for (UserGroupHolder ugh : mywebapp.getFriendsAndGroups().getUserGroupHolders()) {
            ListItem listItem = new ListItem();
            listItem.setStyleName("mp_find_list_friend");
            listItem.setTitle("Add user group");
            Image image = null;

            if (image == null) {
                image = new Image(MyWebApp.resources.spot_image_placeholder130x130());
            }
            image.setStyleName("mp_find_list_ava");
            listItem.add(image);
            InlineLabel inlineLabel = new InlineLabel(ugh.getName());
            inlineLabel.setStyleName("mp_find_list_name");
            listItem.add(inlineLabel);
            inlineLabel.addClickHandler(addUserGroupHandler);
            clickMapUserGroupHolder.put(inlineLabel, ugh);
            addAddressesUL.add(listItem);
        }
    }

    private void showAvailableFriends() {
        for (FriendHolder friendHolder : mywebapp.getFriendsAndGroups().getFriendHolders()) {
            ListItem listItem = new ListItem();
            listItem.setStyleName("mp_find_list_friend");
            listItem.setTitle("Add friend");
            Image image = null;
            UserHolder userHolder = friendHolder.getUserHolder();
            if (userHolder != null) {
                if (userHolder.getContentHolder() != null) {
                    image = getImage(userHolder.getContentHolder(), "320x320");
                }
            }
            if (image == null) {
                image = new Image(MyWebApp.resources.spot_image_placeholder130x130());
            }
            image.setStyleName("mp_find_list_ava");
            listItem.add(image);
            InlineLabel inlineLabel = new InlineLabel(userHolder.getUsername());
            inlineLabel.setStyleName("mp_find_list_name");
            listItem.add(inlineLabel);
            inlineLabel.addClickHandler(addFriendHandler);
            clickMapFriendHolder.put(inlineLabel, friendHolder);
            addAddressesUL.add(listItem);
        }
    }

    private void addItems3(List<ItemHolder> items, ULPanel ulPanel) {
        for (ItemHolder itemHolder : items) {
            Image imageUser = null;
            if (itemHolder.getUserHolder() != null) {
                ContentHolder contentHolder = itemHolder.getUserHolder().getContentHolder();
                if (contentHolder != null) {
                    imageUser = getImage(contentHolder, "320x320");
                }
            }
            if (imageUser == null) {
                imageUser = new Image(MyWebApp.resources.spot_image_placeholder130x130());
            }
            MessageResultComposite messageResultComposite = new MessageResultComposite(imageUser);
            messageResultComposite.setMessageText(itemHolder.getTextData());
            messageResultComposite.setTimeReceived(itemHolder.getCreateDateDisplay());
            if (itemHolder.getUserHolder() != null) {
                messageResultComposite.setUsername(itemHolder.getUserHolder().getUsername());
            }
            ulPanel.add(messageResultComposite);
        }
    }

    ClickHandler viewMessageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ItemHolder itemHolder = clickMapItemHolder.get(b);
                viewMessage(itemHolder);
            }
        }
    };
    ClickHandler editMessageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ItemHolder itemHolder = clickMapItemHolder.get(b);
                editMessage(itemHolder);
            }
        }
    };

    private void editMessage(ItemHolder itemHolder) {
        MessagePanel messagePanel = new MessagePanel(mywebapp, itemHolder);
        mywebapp.swapCenter(messagePanel);
    }

    ClickHandler composeMessageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ItemHolder itemHolder = new ItemHolder();
            itemHolder.setMessage(true);
            editMessage(itemHolder);
        }
    };

    Label newMessageButton() {
        Label btn = new Label("Compose Message");
        btn.addClickHandler(composeMessageHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }
}
