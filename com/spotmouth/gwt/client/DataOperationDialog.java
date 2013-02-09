package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.icons.SpotImageResource;

public class DataOperationDialog {
    public DataOperationDialog(String message) {
        super();
        //var showPop = function(){
        //    	document.getElementById('popup').style.zIndex = "9999999";
        // 	document.getElementById('popup').style.opacity = "1";
        // }
    }

    public void show() {
        MyWebApp.showPopup();
    }

    public void hide() {
        MyWebApp.hidePopup();
    }

    public void center() {
    }
}
