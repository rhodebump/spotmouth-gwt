package com.spotmouth.gwt.client.messaging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.common.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/29/12
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessagingHomeComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MessagingHomeComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

  //		<button class="btn_blue">New Message</button>


    @UiField(provided=true)
    final ULPanel inUL;

    @UiField(provided=true)
    final ULPanel sentUL;

    @UiField(provided=true)
    final ULPanel draftUL;

    @UiField(provided=true)
    final ULPanel resultsUL;


    @UiField(provided=true)
    final MessageComposite messageComposite;

    @UiField
     Button newMessageButton;



    @UiField
     Anchor showInboxAnchor;


    @UiField
     Anchor showSentAnchor;



    @UiField
     Anchor showDraftAnchor;


    @UiField(provided=true)
    final TextField messageSearchTextBox;

    @UiField(provided=true)
    final Button searchButton;


    //<button class="_btn"><img class="mp_search" src="mp_search.png"/></button>

    private MessagingPanel messagingPanel = null;
    private MyWebApp mywebapp = null;

    public MessagingHomeComposite(MyWebApp mywebapp,ULPanel inUL,ULPanel sentUL,ULPanel draftUL,MessageComposite messageComposite,MessagingPanel messagingPanel,TextField messageSearchTextBox,Button searchButton,ULPanel resultsUL) {
        this.inUL = inUL;
        this.mywebapp = mywebapp;
        this.sentUL = sentUL;
        this.draftUL = draftUL;
        this.resultsUL = resultsUL;
        this.messageComposite = messageComposite;
        this.messageSearchTextBox = messageSearchTextBox;
        this.messageComposite.setVisible(false);
        this.searchButton = searchButton;
        //this.newMessageButton = newMessageButton;
        initWidget(uiBinder.createAndBindUi(this));

        this.newMessageButton.setText("New Message");
        //Button newMessageButton = new Button("New Message");
        this.newMessageButton.setStyleName("btn_blue");
        this.messagingPanel = messagingPanel;

        // <input type="text" placeholder="Find Message"

    }



    @UiHandler("showInboxAnchor")
    public void onClick1(ClickEvent event) {
        hideAll();
        inUL.setVisible(true);
        showTopBar();

    }


    @UiHandler("showSentAnchor")
    public void onClick2(ClickEvent event) {
       hideAll();
        sentUL.setVisible(true);
        showTopBar();

    }




    @UiHandler("showDraftAnchor")
    public void onClick3(ClickEvent event) {
        hideAll();
        draftUL.setVisible(true);
        showTopBar();
    }


    private void hideAll() {
        inUL.setVisible(false);
        sentUL.setVisible(false);
        draftUL.setVisible(false);
        messageComposite.setVisible(false);
        resultsUL.setVisible(false);
        Element mp_top_bar = DOM.getElementById("mp_top_bar");
        mp_top_bar.addClassName("hideme");

        //
    }

    private void showTopBar() {
        Element mp_top_bar = DOM.getElementById("mp_top_bar");
        mp_top_bar.removeClassName("hideme");
    }

    @UiHandler("newMessageButton")
    public void onClick4(ClickEvent event) {
        newMessageClick();

    }


    public void newMessageClick() {
        hideAll();
                 this.messageComposite.setVisible(true);
                 messagingPanel.initMessage();
                 //clear out errors
                 mywebapp.getMessagePanel().clear();
    }

}
