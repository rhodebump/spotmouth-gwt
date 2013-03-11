package com.spotmouth.gwt.client;

import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.H2;
import com.spotmouth.gwt.client.dto.LocationResult;
import com.spotmouth.gwt.client.dto.SolrDocument;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 9/1/12
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DesktopMarkSpotPanel extends MarkSpotPanel  {


    public DesktopMarkSpotPanel(MyWebApp mywebapp) {
        super(mywebapp);
    }


    public void addSpotNotHere() {
        Label lbl = new Label("My Spot is not listed here.");
        lbl.addClickHandler(getNewSpotHandler());
        lbl.setStyleName("linky");
        add(lbl);
    }


//    protected void displayLocationResults2(MobileResponse mobileResponse) {
//        clear();
//        //pickSpotPanel.clear();
//       // add(pickSpotPanel);
//        try {
//            mywebapp.getMessagePanel().displayMessage("Please select the spot that you wish to mark");
//            //ULPanel ul = new ULPanel();
//           // ul.setStyleName("results");
//           // FlowPanel div = new FlowPanel();
//            //divPost74.setStyleName("entry-box");
//            ULPanel ul= null;
//
//            //pickSpotPanel.add(div);
//            addSpotNotHere();
//            for (LocationResult locationResult : mobileResponse.getLocationResults()) {
//                GWT.log("locationResults count "
//                        + mobileResponse.getLocationResults().size());
//                if (locationResult.getLocation() != null) {
//                    GWT.log("it is location");
//                    addLocationDesktop(ul, locationResult, pickLocationHandler, false);
//                } else if (locationResult.getSolrDocument() != null) {
//                    GWT.log("it is result");
//                    addResult2(ul, locationResult );
//                }
//            }
//            // we need to add a link to create a new empty spot, in case we
//            // don't have
//            // the spot
//            addSpotNotHere();
//        } catch (Exception e) {
//            GWT.log("error in displaySpots", e);
//            mywebapp.printStackTrace2(e);
//        }
//    }


        //here we will display a solr result that users can pick to leave a mark upon
    //mode 1 is what we want, so removing
    //i wrote a custom addResult2 method because we are only "picking" stuff
    //and don't need all the other kind of stuff

    protected void addResult22(ULPanel ul, LocationResult locationResult) {
        FlowPanel divPost74 = new FlowPanel();
        add(divPost74);
        divPost74.setStyleName("entry-box");


        SolrDocument solrDocument = locationResult.getSolrDocument();
        VerticalPanel vp = new VerticalPanel();

        //HorizontalPanel hp = new HorizontalPanel();

        Long spotId = solrDocument.getFirstLong("spotid_l");

        //Long itemId = solrDocument.getFirstLong("latest_mark_georepoitemid_l");

        String targetHistoryToken = "#" + MyWebApp.LEAVE_SPOT_MARK + spotId;


        //String targetHistoryToken = "#" + MyWebApp.SPOT_DETAIL + spotId;
        //need to add mark photo
        //addMarkPhoto(solrDocument, targetHistoryToken, hp);
        String val = solrDocument.getFirstString("latest_mark_thumbnail_130x130_url_s");

        if (val == null) {
            val =   solrDocument.getFirstString("image_thumbnail_130x130_url_s");
        }


        Image image = new Image(getSpotImage());

        if (val != null) {
            image = new Image();
            image.setUrl(val);
        }
        image.setStyleName("entry-thumb");
        image.addStyleName("wp-post-image");


        Anchor imageAnchor = new Anchor();
        imageAnchor.setHref(targetHistoryToken);
        imageAnchor.getElement().appendChild(image.getElement());
        //imageAnchor.addClickHandler(handler);

       // image.addClickHandler(handler);
        divPost74.add(imageAnchor);




//
//        VerticalPanel middleTable = new VerticalPanel();
//        hp.add(middleTable);
//        hp.setCellWidth(middleTable, "100%");
//        middleTable.setStyleName("middletable");
        //this will add the first row to the middle table
        //mode is is a results page, whereas 2 is marks for a spot
        String spot_label_s = solrDocument.getFirstString("spot_label_s");
        if (spot_label_s != null) {
            String sl = "@" + spot_label_s.toString();
//            Hyperlink spotLabel = new Hyperlink("@" + sl, targetHistoryToken);
//            spotLabel.setStyleName("spotLabel");
//            middleTable.add(spotLabel);



            H2 h2 = new H2();
            h2.setStyleName("entry-title");
            divPost74.add(h2);
            Anchor anchor  = new Anchor();
            //need to have an href attribute for mouse cursor
            anchor.setHref(targetHistoryToken);
            anchor.removeStyleName("gwt-Anchor");
            anchor.setHTML(sl);

           // anchor.addClickHandler(handler);
            h2.add(anchor);



        }
//        String location_label_s = solrDocument.getFirstString("location_label_s");
//        if (location_label_s != null) {
//            String sl = location_label_s.toString();
//            String targetLink = MyWebApp.ITEM_DETAIL + itemId;
//            Hyperlink spotLabel = new Hyperlink("@" + sl, targetLink);
//            spotLabel.setStyleName("spotLabel");
//            middleTable.add(spotLabel);
//        }
//        Hyperlink distanceLabel = getDistanceHyperlink(locationResult, targetHistoryToken);
//        Hyperlink metersLabel = new Hyperlink("meters away", targetHistoryToken);
//        ComplexPanel distancePanel = getDistancePanel(distanceLabel, metersLabel, locationResult);
//        Hyperlink latest_mark_escapedjavascriptsnippet_s = addHtml2(solrDocument, middleTable, "latest_mark_escapedjavascriptsnippet_s", targetHistoryToken);
//        if (latest_mark_escapedjavascriptsnippet_s != null) {
//            latest_mark_escapedjavascriptsnippet_s.addStyleName("latestMarkEscapedJavascriptSnippet");
//        }
//        addCategories(middleTable, solrDocument);
//        String targetHistoryTokenMarkSpot = MyWebApp.LEAVE_SPOT_MARK + spotId;
//        if (itemId == null) {
//            Hyperlink label2 = new Hyperlink("Be the first to mark this spot!", targetHistoryTokenMarkSpot);
//            label2.addStyleName("linky");
//            label2.addStyleName("befirst");
//            middleTable.add(label2);
//        }
//        String snippet = solrDocument.getFirstString("spot_geoRepoItemescapedjavascriptsnippet_s");
//        if (snippet != null) {
//            Hyperlink html2 = new Hyperlink(snippet, targetHistoryToken);
//            html2.addStyleName("spotGeoRepoItemEscapedJavascriptSnippet");
//        }
//        //2nd image
//        //spot
//        if (!MyWebApp.isSmallFormat()) {
//            if (alex) {
//                Hyperlink userHyperLink = new Hyperlink();
//                userHyperLink.setTargetHistoryToken(MyWebApp.ITEM_DETAIL + itemId);
//                Image image = addImage(solrDocument, hp, "image_thumbnail_130x130_url_s",
//                        userHyperLink, resources.spot_image_placeholder130x130(), resources.spot_image_placeholder130x130Mobile(), "spotimage");
//                setColumnWidth(image, hp);
//            }
//        }
//        hp.add(distancePanel);
//        setColumnWidth(distancePanel, hp);

        FlowPanel entry = new FlowPanel();
        entry.setStyleName("entry");
        divPost74.add(entry);


//        <div class="entry-meta">
//      			<abbr title="May 27, 2010 at 8:42 am">827 days ago</abbr> by
// <a href="http://demo.theme-junkie.com/freshlife/author/admin/" title="Posts by admin" rel="author">admin</a> <span class="entry-comment">
// <a href="http://demo.theme-junkie.com/freshlife/2010/05/27/google-chrome-version-6-in-the-works/#respond" title="Comment on Google Chrome Version 6 in the Works">0</a></span>
//      		</div> <!-- end .entry-meta -->
        FlowPanel entryMeta = new FlowPanel();
        entry.add(entryMeta);
        String username = solrDocument.getFirstString("latest_mark_username_s");
        if (username != null) {
            Long userId = solrDocument.getFirstLong("latest_mark_userid_l");
            String createdAt = solrDocument.getFirstString("latest_mark_create_date_s");
            InlineLabel ago = new InlineLabel("At " + createdAt + " by ");
            entryMeta.add(ago);
            Anchor userAnchor = new Anchor(username);
           // userAnchor.setTarget("#" + MyWebApp.VIEW_USER_PROFILE + userId);
            userAnchor.setHref("#" + MyWebApp.VIEW_USER_PROFILE + userId);
            entryMeta.add(userAnchor);

        }



//        FlowPanel clear1 = new FlowPanel();
//        clear1.setStyleName("clear");
//        entry.add(clear);
        addClear(entry);


       // divPost74.add(entryMeta);



//        FlowPanel clear = new FlowPanel();
//        clear.setStyleName("clear");
//        divPost74.add(clear);
        addClear(divPost74);
    }


}
