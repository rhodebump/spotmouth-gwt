package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;


//buttons in this panel use a iui css that sets the background, so not good to use horizontal panel
public class TopMenuPanel extends FlowPanel {
    String titleBar = "spotmouth";

    public void setTitleBar(String titleBar) {
        this.titleBar = titleBar;
        this.titleLabel.setText(titleBar);
    }

//    public void setBackTitle(String backTitle) {
//        this.backTitle = backTitle;
//        this.backLabel.setText(backTitle);
//    }

   // String backTitle = "Back";

    Label titleLabel = new Label();

//    public Label getBackLabel() {
//        return backLabel;
//    }

    Label backLabel = null;

    MyWebApp mywebapp = null;

    public TopMenuPanel(MyWebApp mywebapp) {
        super();
        this.mywebapp = mywebapp;
        init();
    }

    private void init() {
        clear();
        setWidth("100%");
        this.setStyleName("toolbar");
        titleLabel.setText(titleBar);
        add(homeButton());
        //backLabel = backButton();
        //add(backLabel);
        titleLabel.setStyleName("title");
        add(titleLabel);
        add(menuButton());
    }

    Label homeButton() {
        Label btn = new Label("Home");
        btn.addClickHandler(homeHandler);
        btn.setStyleName("button");
        return btn;
    }

//    Label backButton() {
//       // Button b = new Button();
//
//
//        Label backLabel = new Label("Back");
//       // DOM.setElementAttribute(backLabel.getElement(), "id", "backButton");
//        backLabel.setStyleName("button");
//        backLabel.addClickHandler(backHandler);
//        //btn.setStyleName("button");
//        //btn.setStyleName("backButton");
//        return backLabel;
//    }

    ClickHandler homeHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.HOME);
        }
    };
   // ClickHandler backHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleBack();
//        }
//    };

    Hyperlink menuButton() {
        Hyperlink btn = new Hyperlink("Menu",MyWebApp.MENU);
        //Label btn = new Label("Menu");
       // btn.addClickHandler(menuHandler);
        btn.setStyleName("button");
        btn.addStyleName("last");
        return btn;
    }

//    ClickHandler menuHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//
//            mywebapp.toggleMenu(true);
//        }
//    };
}
