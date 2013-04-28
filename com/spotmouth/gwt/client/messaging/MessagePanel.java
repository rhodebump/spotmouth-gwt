package com.spotmouth.gwt.client.messaging;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.MessageRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

public class MessagePanel extends SpotBasePanel implements SpotMouthPanel {
    protected boolean isValid() {
        if (isEmpty(subjectTextBox)) {
            getMessagePanel().displayError("Please specify a message subject.");
            return false;
        }
        return true;
    }



    public String getTitle() {
        return "Message";
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }




    protected void doSave() {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setItemHolder(getItemHolder());
        getItemHolder().setTitle(subjectTextBox.getValue());
        getItemHolder().setTextData(contentTextArea.getValue());
        messageRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveMessage(messageRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
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
//    ClickHandler removeFriendHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            getMessagePanel().clear();
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget widget = (Widget) sender;
//                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
//                List<FriendHolder> list = new ArrayList<FriendHolder>();
//                list.addAll(itemHolder.getFriendHolders());
//                itemHolder.getFriendHolders().clear();
//                //for(FriendHolder friendHolder)
//                //for(int i=0;i < groupHolder.getF)
//                for (FriendHolder fh : list) {
//                    if (fh.getKey().equals(friendHolder.getKey())) {
//                        //this is the one we are removing
//                    } else {
//                        itemHolder.getFriendHolders().add(fh);
//                    }
//                }
//            }
//
//            refreshSelected();
//
//
//        }
//    };

    protected void initSelectedAddresses() {
        addFieldset(selectedFriendsAndGroupsPanel, "To:", "to");
        refresh();
    }

    public MessagePanel(MyWebApp mywebapp) {
        super(mywebapp);
    }

    TextBox subjectTextBox = new TextBox();

    // this does not get added to the app via the standard "swapCenter" thing
    public MessagePanel(MyWebApp mywebapp, ItemHolder itemHolder) {
        this(mywebapp);
        setItemHolder(itemHolder);
        addButtons();
        // to
        add(toButton());
        initSelectedAddresses();
        subjectTextBox = addTextBox("Subject", "title", itemHolder.getTitle());
        Label help = new Label("The subject of this message will not be sent to SMS users.");
        add(help);
        //why in the heck did we not set the following getData??
        contentTextArea = addTextArea("Message", "textData", itemHolder.getTextData(), false);
        Label help2 = new Label("Only the first 140 characters will be sent to SMS users");
        add(help2);
        addMediaFields("Upload images, video for message");
        addButtons();
        // Add an event handler to the form.
    }
    // private AddressPicker addressPicker = null;

    Label toButton() {
        Label btn = new Label("To");
        btn.addClickHandler(toHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler toHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            AddressPicker addressPicker = new AddressPicker(mywebapp,
                    MessagePanel.this);
            mywebapp.swapCenter(addressPicker);
        }
    };
}
