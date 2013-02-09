package com.spotmouth.gwt.client.chat;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.contest.ContestDetailComposite;
import com.spotmouth.gwt.client.dto.SolrDocument;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/2/13
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewChatPanel extends SpotBasePanel implements SpotMouthPanel {


    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return name;
    }


    private String name = null;

    public ImageResource getImageResource() {
        return mywebapp.resources.contests();
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
    //private ContestHolder contestHolder = null;
    private SolrDocument solrDocument = null;


    public ViewChatPanel(MyWebApp mywebapp, SolrDocument solrDocument) {
        super(mywebapp);
        this.solrDocument = solrDocument;
        String imgUrl = solrDocument.getFirstString("image_thumbnail_320x320_url_s");
        String desc = solrDocument.getFirstString("description_s");
         this.name = solrDocument.getFirstString("name");
        String startdate_dt = solrDocument.getFirstString("startdate_dt");
        String enddate_dt = solrDocument.getFirstString("enddate_dt");
        String location = solrDocument.getFirstString("location_s");
        Long itemId = solrDocument.getFirstLong("georepoitemid_l");

        if (MyWebApp.isDesktop()) {

            ChatDetailComposite cdc = new ChatDetailComposite();
            cdc.setName(name);

            cdc.setChatId(itemId);
            cdc.setDescription(desc);
            cdc.setEndDate(enddate_dt);
            cdc.setStartDate(startdate_dt);
            cdc.setLocation(location);
            //image
            cdc.setImageUrl(imgUrl);

            add(cdc);



            return;
        }

        addImageOrig(imgUrl, this, "contestImage");
        //contest image
        //name

        HTML nameLabel = new HTML(name);
        addFieldset(nameLabel, "Chat Name", "desc");
        setTitle(name);
        //description

        HTML descLabel = new HTML(desc);
        addFieldset(descLabel, "Description", "desc");
        //start / end date

        HTML dateRangeLabel = new HTML(startdate_dt + " though " + enddate_dt);
        addFieldset(dateRangeLabel, "Chat Valid From/To Date", "daterange");
        //most votes button
        FlowPanel fp = new FlowPanel();
        addViewVotingResults(fp, solrDocument);
        Fieldset fieldset = new Fieldset();
        fieldset.add(fp);
        add(fieldset);
        //highest average button
        //edit contest

        Hyperlink editContest = new Hyperlink("Manage Chat", MyWebApp.MANAGE_CHAT + itemId);
        //  editContest.addStyleName("whiteButton");
        add(editContest);
    }

    private String title = null;
}