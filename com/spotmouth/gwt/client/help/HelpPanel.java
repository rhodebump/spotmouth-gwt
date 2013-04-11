package com.spotmouth.gwt.client.help;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/3/12
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class HelpPanel extends VerticalPanel {
    private SpotBasePanel spotBasePanel = null;
    private MyWebApp mywebapp = null;

    public HelpPanel(MyWebApp mywebapp, SpotBasePanel spotBasePanel) {
        this.spotBasePanel = spotBasePanel;
        this.mywebapp = mywebapp;

        this.setStyleName("HelpPanel");
        setWidth("100%");
        add(showHelpButton());
    }

    protected Label showHelpButton() {
        Label btn = new Label("Show Help");
        //ImageResource ir  = getImageResource();

        //SpotBasePanel.addImageToButton(btn,MyWebApp.resources.helpButton(),MyWebApp.resources.helpButtonMobile());

        //Image img = new Image(ir);
        btn.addClickHandler(showHelpHandler);
        btn.setStyleName("whiteButton");
        //btn.getElement().appendChild(img.getElement());
        return btn;
    }

//    private ImageResource getImageResource() {
//        ImageResource ir  = null;
//        if (MyWebApp.isSmallFormat()) {
//            ir = MyWebApp.resources.helpButtonSmall();
//
//        } else {
//            ir = MyWebApp.resources.helpButton();
//        }
//        return ir;
//    }

    protected Label hideHelpButton() {
        Label btn = new Label("Hide Help");
        //SpotBasePanel.addImageToButton(btn,MyWebApp.resources.helpButton(),MyWebApp.resources.helpButtonMobile());

        btn.addClickHandler(hideHelpHandler);
        btn.setStyleName("whiteButton");

        return btn;
    }

    protected ClickHandler hideHelpHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            HelpPanel.this.clear();
            HelpPanel.this.add(showHelpButton());
        }
    };
    ClickHandler showHelpHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
//            DataOperationDialog dia = new DataOperationDialog("Test dialog");
//            dia.show();
            HelpPanel.this.clear();
            HelpPanel.this.add(hideHelpButton());
            HTML htmlPanel = new HTML();
            htmlPanel.setHTML(spotBasePanel.getHelpTextResource().getText());
            //htmlPanel.setHTML(spotBasePanel.getHelpText());
            HelpPanel.this.add(htmlPanel);
            HelpPanel.this.add(hideHelpButton());
        }
    };
}
