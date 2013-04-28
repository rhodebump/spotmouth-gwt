package com.spotmouth.gwt.client.features;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.about.AboutComposite;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/19/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeaturesPanel extends SpotBasePanel implements SpotMouthPanel {
    public FeaturesPanel() {
    }


    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Features";
    }




    //plain panel, used for features
    public FeaturesPanel(MyWebApp mywebapp) {
        super(mywebapp);
         FeaturesComposite fc = new FeaturesComposite();
        add(fc);
    }







    public void toggleFirst() {
        // TODO Auto-generated method stub
    }




}
