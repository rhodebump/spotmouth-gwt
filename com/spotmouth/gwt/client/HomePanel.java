package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/*
 * this constructs links to all application menus
 */
public class HomePanel extends SpotBasePanel implements SpotMouthPanel {


    public String getTitle() {
        return "Home";
    }

    ClickHandler androidHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Window.Location.assign("market://details?id=com.spotmouth.android");
        }
    };
    ClickHandler iphoneHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Window.Location.assign("http://itunes.com/app/spotmouth");
        }
    };


    ClickHandler cmsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Window.Location.assign("http://cms.spotmouth.com");
        }
    };


    private void addimage(ClickHandler handler, ImageResource imageResource, String desc,
                          ULPanel ul) {
        final Image image = new Image(imageResource);
        //image.setUrl(href);
        image.addClickHandler(handler);
        image.addStyleName("linky");
        Label label = new Label(desc);
        label.addClickHandler(handler);
        label.addStyleName("linky");
        FlowPanel hp = new FlowPanel();
        hp.add(image);
        hp.add(label);
        ListItem listitem = new ListItem();
        listitem.setStyleName("clearing");
        listitem.add(hp);
        ul.add(listitem);
    }


    private ULPanel getHomeLinks() {
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");

        addimage(
                iphoneHandler,
                mywebapp.resources.spotmouth50x50(),
                "Download the FREE iphone app", ul);
        addimage(
                androidHandler,
                mywebapp.resources.android(),
                "Download the FREE android app", ul);
        return ul;
    }

    public HomePanel(MyWebApp mywebapp) {
        super(mywebapp,false);
        add(getHomeLinks());
    }

    public HomePanel(MyWebApp mywebapp,boolean b) {
        super(mywebapp,false);
        if (!MyWebApp.isDesktop()) {
            add(getHomeLinks2());
        }

    }

    private ULPanel getHomeLinks2() {
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");

        addimage(
                cmsHandler,
                mywebapp.resources.spotmouthLogo(),
                "visit our site", ul);

        return ul;
    }

    public void toggleFirst() {
        // TODO Auto-generated method stub
    }



}
