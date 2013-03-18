package com.spotmouth.gwt.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 3/15/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TopNav  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, TopNav> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    SimplePanel profilePicPanel;

    public TopNav(SimplePanel profilePicPanel) {
        this.profilePicPanel = profilePicPanel;
        initWidget(uiBinder.createAndBindUi(this));
    }


    public void setUsername(String name) {
        usernameSpan.setInnerText(name);
    }

    @UiField
    SpanElement usernameSpan;



}