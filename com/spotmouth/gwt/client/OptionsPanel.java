package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.spotmouth.gwt.client.common.SpotBasePanel;

public class OptionsPanel extends SpotBasePanel {
    public OptionsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        add(settingsButton());
    }


//    Button setLocationButton() {
//        return new Button("Location", setLocationHandler);
//    }

//    ClickHandler setLocationHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleSetLocationManually(true);
//        }
//    };




    ClickHandler refreshLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
        }
    };
    ClickHandler settingsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleSettings(true);
        }
    };

    Button settingsButton() {
        return new Button("Settings", settingsHandler);
    }



}
