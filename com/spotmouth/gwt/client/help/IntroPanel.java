package com.spotmouth.gwt.client.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/28/12
 * Time: 11:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class IntroPanel   extends SpotBasePanel implements SpotMouthPanel {
    public IntroPanel(MyWebApp mywebapp) {
        super(mywebapp, false);
        this.setStyleName("HelpPanel");
        VerticalPanel hp = new VerticalPanel();
        add(hp);
        hp.setWidth("100%");
        HTML htmlPanel = new HTML();
        String html = HelpResources.INSTANCE.get30Secs().getText();
        htmlPanel.setHTML(html);
        hp.add(htmlPanel);
        hp.add(closeButton());
    }


    Label closeButton() {
        Label btn = new Label("Continue");
        btn.addClickHandler(closeHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }


    ClickHandler closeHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
           // IntroPanel.this.hide();
            GWT.log("close handler1");




                mywebapp.pushLocalStorage(MyWebApp.HIDE_INTRO, "true");



            mywebapp.showHome();


        }
    };

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
