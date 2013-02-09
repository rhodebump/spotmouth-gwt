package com.spotmouth.gwt.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.AutoGrowTextArea;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/29/12
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MenuComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


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

        initWidget(uiBinder.createAndBindUi(this));
    }





}