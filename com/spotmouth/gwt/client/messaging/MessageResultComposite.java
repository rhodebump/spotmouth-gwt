package com.spotmouth.gwt.client.messaging;

import com.spotmouth.gwt.client.dto.ItemHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.dto.NotificationHolder;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/29/12
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageResultComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MessageResultComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    Image userImage;
    @UiField
    Anchor deleteAllByUser;
    @UiField
    Button moreButton;
    // <span class="mp_time"> today 10:25</span>
    @UiField
    SpanElement timeReceivedSpan;

    public void setTimeReceived(String timeReceived) {
        timeReceivedSpan.setInnerText(timeReceived);
    }

    @UiField
    SpanElement messageTextSpan;

    public void setMessageText(String messageText) {
        messageTextSpan.setInnerText(messageText);
    }

    @UiField
    Anchor usernameAnchor;

    public void setUsername(String username) {
        usernameAnchor.setText(username);
    }

   // private ItemHolder itemHolder = null;
    private NotificationHolder notificationHolder = null;
    private MessagingPanel messagingPanel = null;

    public MessageResultComposite(Image userImage,NotificationHolder notificationHolder,MessagingPanel messagingPanel) {
        this.userImage = userImage;
        this.notificationHolder = notificationHolder;
        this.messagingPanel = messagingPanel;
        initWidget(uiBinder.createAndBindUi(this));
        //setStyleName("mp_message");
        // moreButton.setStyleName("_btn");
        // moreButton.setText("...");
        // <span class="mp_time"> today 10:25</span>
        //timeReceivedSpan.setClassName("mp_time");
    }

    @UiHandler("deleteAllByUser")
    public void onClick1(ClickEvent event) {
    }

    @UiHandler("moreButton")
    public void onClick2(ClickEvent event) {
    }

    @UiField
    Anchor deleteMessageAnchor;

    @UiHandler("deleteMessageAnchor")
    public void onClickDeleteMessage(ClickEvent event) {
        messagingPanel.deleteMessage(notificationHolder,this);
    }
//<g:Anchor ui:field="reportSpamAnchor"/> Report spam
    @UiField
    Anchor reportSpamAnchor;

    @UiHandler("reportSpamAnchor")
    public void onClickReportSpam(ClickEvent event) {
        messagingPanel.reportSpam(notificationHolder, this);
    }
}
