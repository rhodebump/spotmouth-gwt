package com.spotmouth.gwt.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.ListItem;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/29/12
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuComposite extends Composite {
//    interface MyUiBinder extends UiBinder<Widget, MenuComposite> {
//    }
//
//    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiTemplate("MenuComposite.ui.xml")
    interface MenuCompositeBinder extends UiBinder<Widget, MenuComposite> {}
    private static MenuCompositeBinder desktopBinder = GWT.create(MenuCompositeBinder.class);

    @UiTemplate("MenuCompositeM.ui.xml")
    interface MMenuCompositeBinder extends UiBinder<Widget, MenuComposite> {}
    private static MMenuCompositeBinder mobileBinder = GWT.create(MMenuCompositeBinder.class);


    @UiField
    ListItem frequentListItem;

    @UiField
    HTMLPanel frequentPanel;


    @UiField
    HTMLPanel personalPanel;

    @UiField
    ListItem personalListItem;

    @UiField
    ListItem findListItem;

    @UiField
    HTMLPanel findPanel;



    @UiHandler("frequentListItem")
    public void onClickFrequent(ClickEvent event) {
        clearall();
        frequentListItem.setStyleName("_active");
        frequentPanel.setVisible(true);

    }

    private void clearall() {
        personalPanel.setVisible(false);
        frequentPanel.setVisible(false);
        findPanel.setVisible(false);
        toolsPanel.setVisible(false);
        frequentListItem.removeStyleName("_active");
        personalListItem.removeStyleName("_active");
        findListItem.removeStyleName("_active");
        toolsListItem.removeStyleName("_active");

    }

    @UiHandler("personalListItem")
    public void onClick1(ClickEvent event) {
        clearall();
        personalListItem.setStyleName("_active");
        personalPanel.setVisible(true);

    }

    @UiField
    ListItem toolsListItem;

    @UiField
    HTMLPanel toolsPanel;



    @UiHandler("findListItem")
    public void onClickFind(ClickEvent event) {
        clearall();
        findListItem.setStyleName("_active");
        findPanel.setVisible(true);

    }

    @UiHandler("toolsListItem")
    public void onClickTools(ClickEvent event) {
        clearall();
        toolsListItem.setStyleName("_active");
        toolsPanel.setVisible(true);

    }




    @UiField
    SpanElement messageCountSpan;

    public void setMessageCount(Integer messageCount) {
        messageCountSpan.setInnerText(messageCount.toString());
    }


    @UiField
    SpanElement groupCountSpan;


    public void setGroupCount(Integer groupCount) {
        groupCountSpan.setInnerText(groupCount.toString());
    }



    @UiField
    SpanElement friendCountSpan;
    public void setFriendCount(Integer friendCount) {
        friendCountSpan.setInnerText(friendCount.toString());
    }

    @UiField
    SpanElement notificationCountSpan;


    @UiField
    SpanElement personalCountSpan;

    public void setNotificationCount(Integer notificationCount) {
        notificationCountSpan.setInnerText(notificationCount.toString());
    }

    public void setPersonalCount(Integer personalCount) {
        personalCountSpan.setInnerText(personalCount.toString());
    }


    public MenuComposite() {

       // initWidget(uiBinder.createAndBindUi(this));
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            GWT.log("mpage binder");
            initWidget(mobileBinder.createAndBindUi(this));
        }

    }





}