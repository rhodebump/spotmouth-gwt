package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/4/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatResultComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ChatResultComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public ChatResultComposite(Image chatImage, Button joinChatButton) {


        this.chatImage = chatImage;
        this.chatImage.setStyleName("cl_item_photo");
        this.joinChatButton = joinChatButton;
        this.joinChatButton.setStyleName("button");
        initWidget(uiBinder.createAndBindUi(this));
        setStyleName("cl_item");

    }




    @UiField(provided=true)
    final Button joinChatButton;



    @UiField
    SpanElement nameSpan;


    public void setChatName(String contestName) {
        nameSpan.setInnerText(contestName);
    }



    @UiField(provided=true)
    final Image chatImage;




    @UiField
    SpanElement descriptionSpan;


    public void setDescription(String replyText) {
        descriptionSpan.setInnerText(replyText);
    }


}