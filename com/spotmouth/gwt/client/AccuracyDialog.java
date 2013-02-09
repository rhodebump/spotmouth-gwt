package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AccuracyDialog extends DialogBox {
    private MyWebApp myWebApp = null;
    ClickHandler tryAgainHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            myWebApp.refreshLocation();
            AccuracyDialog.this.hide();
        }
    };
    ClickHandler cancelHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            AccuracyDialog.this.hide();
            //myWebApp.locationUpdated();
            //myWebApp.reverseGeocode(null);
        }
    };

    public AccuracyDialog(MyWebApp myWebApp) {
        super();
        com.google.gwt.user.client.Window.scrollTo(0, 0);
        this.myWebApp = myWebApp;
        this.setStyleName("dialog");
        // this.addStyleName("panel");
        Label lbl = new Label(
                "Your GPS device has not provided an accurate reading of your location.  This can happen if you just started your GPS device, or you are inside a building.  You can try again if you wish, or just use this location.");

        VerticalPanel hp = new VerticalPanel();
        hp.setStyleName("panel");
        hp.add(new Label("Warning"));
        hp.add(lbl);
        if (myWebApp.getCurrentLocation() != null) {
            hp.add(new Label("Current Location:" + myWebApp.getCurrentLocation().getFullAddress()));
        }
        Label tryAgainLabel = new Label("Try Again");
        tryAgainLabel.addClickHandler(tryAgainHandler);
        tryAgainLabel.setStyleName("whiteButton");
        hp.add(tryAgainLabel);
        Label cancelLabel = new Label("Use this location");
        cancelLabel.addClickHandler(cancelHandler);
        cancelLabel.setStyleName("whiteButton");
        hp.add(cancelLabel);
        this.setWidget(hp);
        this.center();
    }
}
