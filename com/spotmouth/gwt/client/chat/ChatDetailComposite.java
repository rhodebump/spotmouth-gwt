package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/2/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatDetailComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ChatDetailComposite> {
    }

 //<span class="ac_photo" style="background: url('photo.jpg') no-repeat center center"></span>


    @UiField
    InlineLabel photoSpan;

    public void setImageUrl(String url) {
        String style= "background: url('" + url + "') no-repeat center center";

        photoSpan.getElement().setAttribute("style",style);

    }





    @UiField
   SpanElement locationSpan;

    public void setLocation(String location) {
        locationSpan.setInnerText(location);
    }

    @UiField
    Button editButton;




    @UiField
   Button joinButton;


    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField
   SpanElement startDateSpan;

    public void setStartDate(String name) {
        startDateSpan.setInnerText(name);
    }



    @UiField
   SpanElement endDateSpan;

    public void setEndDate(String name) {
        endDateSpan.setInnerText(name);
    }



    @UiField
   SpanElement descriptionSpan;

    public void setDescription(String name) {
        descriptionSpan.setInnerText(name);
    }



    @UiField
   SpanElement nameSpan;

    public void setName(String name) {
        nameSpan.setInnerText(name);
    }


    public ChatDetailComposite() {
        initWidget(uiBinder.createAndBindUi(this));
            //button class="button">Winners</button>

        //joinButton.setStyleName("button");
       // joinButton.setText("");



      //  editButton.setText("Edit");
      //  editButton.setStyleName("button");
        //editButton.addStyleName("ac_edit");

        //photoSpan.setStyleName("ac_photo");
    }




    private Long chatId = null;

    public void setChatId(Long chatId) {
        this.chatId = chatId;

    }


    @UiHandler("joinButton")
    public void onClick0(ClickEvent event) {
        String token = MyWebApp.JOIN_CHAT + chatId;

        History.newItem(token);
    }




    @UiHandler("editButton")
    public void onClick3(ClickEvent event) {
       String token =  MyWebApp.MANAGE_CHAT + chatId;

        History.newItem(token);
    }



}