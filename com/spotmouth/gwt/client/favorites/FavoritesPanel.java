package com.spotmouth.gwt.client.favorites;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.dto.LocationResult;
import com.spotmouth.gwt.client.dto.SolrDocument;
import com.spotmouth.gwt.client.results.ResultsPanel;


/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/5/12
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class FavoritesPanel extends ResultsPanel implements SpotMouthPanel {
    public String getTitle() {
        return "Favorites";
    }

    public String getPageTitle() {
        return "Favorites";
    }






    public FavoritesPanel() {
    }

    @Override
    protected void handleResult(ULPanel ul, LocationResult locationResult) {
        SolrDocument solrDocument = locationResult.getSolrDocument();
        Long spotId = null;
        spotId = solrDocument.getFirstLong("spotid_l");

        String spotname_s = solrDocument.getFirstString("spotname_s");

        String token =MyWebApp.MANAGE_SPOT_FRIEND + spotId;

        Hyperlink label = new Hyperlink(spotname_s + " Settings",token);
        fixButton(label);
        //label.addClickHandler(manageSpotFriendHandler);
        //clickMapLong.put(label, spotId);
        ul.add(label);
    }

    public FavoritesPanel(MyWebApp mywebapp) {
        super(mywebapp);

        if (MyWebApp.isDesktop()) {
            /*
            <div id="spots-area">

            <span class="fspots">
            <span class="remove"></span>
            <img src="y_e4525243.jpg"/>
            <a href="#">A Technical Hair Systems</a>
            </span>
             */
            FlowPanel spotsAreaFlowPanel = new FlowPanel();

            for (LocationResult locationResult : mywebapp.getFavorities().getLocationResults()) {
                SolrDocument solrDocument = locationResult.getSolrDocument();
                InlineHTML fspots = new InlineHTML();
                fspots.setStyleName("fspots");
                InlineHTML remove = new InlineHTML();
                remove.setStyleName("remove");
                fspots.getElement().appendChild(remove.getElement());



                String imageUrl = solrDocument.getFirstString("image_thumbnail_130x130_url_s");

                Image image = null;
                if (imageUrl == null) {
                    image = new Image( MyWebApp.resources.spot_image_placeholder130x130());

                } else {
                    image = new Image(imageUrl);
                }
                fspots.getElement().appendChild(image.getElement());
                String name = solrDocument.getFirstString("spot_name_s");
                Long spotId  = solrDocument.getFirstLong("spotid_l");
                String targetHistoryToken = MyWebApp.SPOT_DETAIL + spotId;
                String targetHistoryToken2 = "#" + targetHistoryToken;

                Anchor nameAnchor = new Anchor(name,targetHistoryToken2);
                fspots.getElement().appendChild(nameAnchor.getElement());
                spotsAreaFlowPanel.add(fspots);
            }


            FavoritesComposite favoritesComposite = new FavoritesComposite(spotsAreaFlowPanel);
            add(favoritesComposite);



            return;
        }

        GWT.log("there are " + mywebapp.getFavorities().getLocationResults().size() + " favorites");
        if (mywebapp.getFavorities().getLocationResults().isEmpty()) {
            Label mylabel = new Label("No Favorites to display");
            mylabel.setStyleName("h1");
            add(mylabel);
        }
        displayLocationResults(mywebapp.getFavorities(), "Favorites",1);
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


//
//    ClickHandler manageSpotFriendHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget b = (Widget) sender;
//                Long spotId = clickMapLong.get(b);
//                if (spotId != null) {
//                   // stopFollowSpot(spotId);
//                   toggleManageSpotFriend(spotId);
//                    // mywebapp.toggleSpotDetail(spotId);
//                }
//            }
//        }
//    };



//    private void manageSpotFriend(final Long spotId) {
//
//        SpotFriendPanel spotFriendPanel = new SpotFriendPanel(mywebapp);
//        mywebapp.swapCenter(spotFriendPanel);
//
//
//
//    }




//    ClickHandler updateNotificationHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            saveFriend();
//        }
//    };




}
