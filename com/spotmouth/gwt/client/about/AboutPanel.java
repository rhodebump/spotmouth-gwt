package com.spotmouth.gwt.client.about;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.contact.ContactComposite;

/*
 * this constructs links to all application menus
 */
public class AboutPanel extends SpotBasePanel implements SpotMouthPanel {
    public AboutPanel() {
    }


    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "About";
    }




    //plain panel, used for features
    public AboutPanel(MyWebApp mywebapp,String html) {
        super(mywebapp);
        HTML htmlPanel = new HTML();

        htmlPanel.setHTML(html);
        add(htmlPanel);
    }




    public AboutPanel(MyWebApp mywebapp) {
        super(mywebapp);
        AboutComposite contactComposite = new AboutComposite();
        add(contactComposite);


    }


    public void toggleFirst() {
        // TODO Auto-generated method stub
    }


}
