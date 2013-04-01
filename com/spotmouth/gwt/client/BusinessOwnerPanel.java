package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.dto.LocationResult;
import com.spotmouth.gwt.client.dto.SolrDocument;
import com.spotmouth.gwt.client.help.HelpResources;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 9/12/12
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class BusinessOwnerPanel extends ResultsPanel implements SpotMouthPanel {
    public String getTitle() {
        return "My Biz";
    }



    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.favoritesMobile();
        } else {
            return MyWebApp.resources.favorites();
        }

    }



    @Override
    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getBizOwner();
    }

    public BusinessOwnerPanel() {
    }

    @Override
    protected void handleResult(ULPanel ul, LocationResult locationResult) {
        SolrDocument solrDocument = locationResult.getSolrDocument();
        Long spotId = null;
        spotId = solrDocument.getFirstLong("spotid_l");

        String spotname_s = solrDocument.getFirstString("spotname_s");

        String token =MyWebApp.MANAGE_SPOT + spotId;

        Hyperlink label = new Hyperlink("Manage " + spotname_s  ,token);
        fixButton(label);
        ul.add(label);
    }

    public BusinessOwnerPanel(MyWebApp mywebapp) {
        super(mywebapp);
        if (mywebapp.getMyBiz().getLocationResults().isEmpty()) {
            Label mylabel = new Label("No Businesses to display");
            mylabel.setStyleName("h1");
            add(mylabel);
        }
        displayLocationResults(mywebapp.getMyBiz(), "My Biz",1);
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
