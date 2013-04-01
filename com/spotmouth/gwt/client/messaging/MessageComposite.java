package com.spotmouth.gwt.client.messaging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/29/12
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageComposite   extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MessageComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    ULPanel addAddressesUL;


    @UiField(provided = true)
    FlowPanel mpFriendsList;

    @UiField
    Button sendButton;

    @UiField(provided = true)
    TextArea contentTextArea;

    //		<button class="_btn">Send</button>
    @UiHandler("sendButton")
    public void onClick1(ClickEvent event) {

        this.messagingPanel.sendMessage();
    }

    private MessagingPanel messagingPanel = null;

   public MessageComposite(ULPanel addAddressesUL,FlowPanel mpFriendsList,MessagingPanel messagingPanel,  TextArea contentTextArea) {
       this.addAddressesUL = addAddressesUL;
       this.mpFriendsList = mpFriendsList;
       this.contentTextArea = contentTextArea;
       this.messagingPanel = messagingPanel;
       initWidget(uiBinder.createAndBindUi(this));


       contentTextArea.setStyleName("mp_new_msg");
       contentTextArea.setVisibleLines(5);

       sendButton.setStyleName("_btn");
       sendButton.setText("Send");
   }

}
