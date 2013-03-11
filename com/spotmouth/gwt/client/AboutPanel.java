package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.spotmouth.gwt.client.common.SpotBasePanel;

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


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.aboutMobile();
        } else {
            return MyWebApp.resources.about();
        }

    }





    public AboutPanel(MyWebApp mywebapp,String html) {
        super(mywebapp);
        HTML htmlPanel = new HTML();

        htmlPanel.setHTML(html);
        add(htmlPanel);
    }


//    public AboutPanel(MyWebApp mywebapp) {
//        super(mywebapp);
//        HTML htmlPanel = new HTML();
//        String html = MyHtmlResources.INSTANCE.getAboutHtml().getText();
//        htmlPanel.setHTML(html);
//        add(htmlPanel);
//    }

    public void toggleFirst() {
        // TODO Auto-generated method stub
    }


}
