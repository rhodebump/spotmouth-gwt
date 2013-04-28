package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/4/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatResultComposite extends Composite {



    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }



    @UiTemplate("ChatResultComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, ChatResultComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("MChatResultComposite.ui.xml")
    interface MobileBinder extends UiBinder<Widget, ChatResultComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);



    public ChatResultComposite(Image chatImage, Button joinChatButton) {


        this.chatImage = chatImage;
        this.chatImage.setStyleName("chat_item_photo");
        this.joinChatButton = joinChatButton;
        this.joinChatButton.setStyleName("button");
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }
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