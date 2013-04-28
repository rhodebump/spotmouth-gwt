package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/*
 * this constructs links to all application menus
 */
public class UGRPanel extends SpotBasePanel implements SpotMouthPanel {
    public UGRPanel() {
    }

    public String getTitle() {
        return "Policy";
    }

    public UGRPanel(MyWebApp mywebapp) {
        super(mywebapp);
        HTML htmlPanel = new HTML();
        String html = MyHtmlResources.INSTANCE.getUGC().getText();
        htmlPanel.setHTML(html);
        add(htmlPanel);
    }

    public void toggleFirst() {
        // TODO Auto-generated method stub
    }


}
