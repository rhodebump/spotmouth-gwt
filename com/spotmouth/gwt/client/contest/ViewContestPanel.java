package com.spotmouth.gwt.client.contest;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.SolrDocument;
import com.google.gwt.user.client.ui.*;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/26/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewContestPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return name;
    }

    private String name = null;

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public ViewContestPanel(MyWebApp mywebapp, SolrDocument solrDocument) {
        super(mywebapp);
        // this.solrDocument = solrDocument;
        String imgUrl = solrDocument.getFirstString("image_thumbnail_320x320_url_s");
        String desc = solrDocument.getFirstString("description_s");
        this.name = solrDocument.getFirstString("name");
        String startdate_dt = solrDocument.getFirstString("startdate_dt");
        String enddate_dt = solrDocument.getFirstString("enddate_dt");
        Long contestId = solrDocument.getFirstLong("contestid_l");
        String radius = solrDocument.getFirstString("radius_s");
        String location = solrDocument.getFirstString("location_s");
        Integer numberofstars_i = solrDocument.getFirstInteger("numberofstars_i");
        Button cancelButton = new Button();
        cancelButton.addClickHandler(backToContests);
        ContestDetailComposite cdc = new ContestDetailComposite(cancelButton);
        cdc.setName(name);
        cdc.setRadius(radius);
        cdc.setContestId(contestId);
        cdc.setDescription(desc);
        cdc.setEndDate(enddate_dt);
        cdc.setStartDate(startdate_dt);
        cdc.setLocation(location);
        cdc.setStarCount(numberofstars_i);
        //image
        if (imgUrl != null) {
            cdc.setImageUrl(imgUrl);
        }
        add(cdc);
    }

    protected ClickHandler backToContests = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //go back to all contests
            History.newItem(MyWebApp.CONTESTS);
        }
    };
    //private String title = null;
}
