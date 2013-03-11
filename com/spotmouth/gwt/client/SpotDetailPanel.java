package com.spotmouth.gwt.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.mark.SpotMarkComposite;
import com.spotmouth.gwt.client.spot.ContactForm;
import com.spotmouth.gwt.client.detail.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotDetailPanel extends SpotBasePanel implements SpotMouthPanel {
    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getSpotDetailPanel();
    }

    FlowPanel mapHolderPanel = new FlowPanel();
    private Anchor hideMapAnchor = new Anchor("Show Information");
    protected ClickHandler addNewGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //we need to be logged in
            AsyncCallback loginCallback = new AsyncCallback() {
                public void onFailure(Throwable throwable) {
                    getMessagePanel().displayError(throwable.getMessage());
                }

                public void onSuccess(Object response) {
                    toggleAddGroup(spotHolder);
                }
            };
            if (mywebapp.isLoggedIn(loginCallback)) {
                loginCallback.onSuccess(null);
            }
        }
    };

    protected Button getAddGroupButton() {
        Button btn = new Button("Add New Group");
        btn.addClickHandler(addNewGroupHandler);
        //  btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler hideMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mapHolderPanel.clear();
            detail.setVisible(true);
            bottomPanel.setVisible(true);
            hideMapAnchor.setVisible(false);
        }
    };
    ClickHandler viewMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Location location = new Location();
            location.setLatitude(spotHolder.getLatitude());
            location.setLongitude(spotHolder.getLongitude());
            SpotMap spotMap = new SpotMap(mywebapp, location);
            //let's set the detail object to not display
            //add this and a link to enable detail again
            detail.setVisible(false);
            bottomPanel.setVisible(false);
            hideMapAnchor.setVisible(true);
            mapHolderPanel.add(spotMap);
            //mywebapp.swapCenter(spotMap);
        }
    };
    private SpotHolder spotHolder = null;
    private MobileResponse mobileResponse = null;
    private String title = "Spot Detail";
    private String pageTitle = "Spot Detail";

    public String getTitle() {
        return title;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    private String pageDescription = "Page Description";

    public String getPageDescription() {
        return pageDescription;
    }

    private void doDetail() {
//        Anchor backToSearchResultsAnchor = new Anchor();
//        backToSearchResultsAnchor.addClickHandler(backToResultsHandler);
//        backToSearchResultsAnchor.getElement().setAttribute("id","goback");
        Anchor backToSearchResultsAnchor = getBackAnchor();
        Anchor addMyBizAnchor = new Anchor();
        String addLink1 = "#" + MyWebApp.ADD_MY_BIZ + spotHolder.getId();
        addMyBizAnchor.setHref(addLink1);
        String addLink = "#" + MyWebApp.ADD_SPOT_FRIEND + spotHolder.getId();
        Anchor addtoFavoriteAnchor = new Anchor();
        addtoFavoriteAnchor.setHref(addLink);
        Anchor adminSpotAnchor = new Anchor();
        adminSpotAnchor.setHref("#" + MyWebApp.MANAGE_SPOT + spotHolder.getId());
        Anchor phoneAnchor = getPhone(spotHolder.getVoicephone());
        Anchor groupsAnchor = new Anchor();
        groupsAnchor.setHref("#" + MyWebApp.SPOT_GROUPS + spotHolder.getId());
        groupsAnchor.setStyleName("groups");
        Anchor mapAnchor = new Anchor();
        mapAnchor.addClickHandler(viewMapHandler);
        Image mainImage = getImage(spotHolder.getContentHolder(), "320x320");
        if (mainImage == null) {
            mainImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
        }
        Anchor markSpotAnchor = new Anchor();
        markSpotAnchor.setHref("#" + MyWebApp.LEAVE_SPOT_MARK + spotHolder.getId());
        FlowPanel groupsPanel = getGroupsPanel();
        HTML hoursHTML = null;
        if (spotHolder.isPlace()) {
            //hours for places only
            hoursHTML = new HTML("<h1>Hours</h1> <pre>" + spotHolder.getHours() + "</pre>");
            if (isEmpty(spotHolder.getHours())) {
                hoursHTML.setHTML("No hours provided yet.");
            }
        } else {
            hoursHTML = new HTML();
            hoursHTML.setVisible(false);
        }
        Button addGroupButton = getAddGroupButton();
        Anchor websiteAnchor = new Anchor();
        if (spotHolder.getWebsite() != null && spotHolder.getWebsite().length() > 0) {
            // Anchor anchor = new Anchor(spotHolder.getWebsite(), false, spotHolder.getWebsite());
            websiteAnchor.setHref(spotHolder.getWebsite());
            //addFieldset(anchor, "Website", "na",bottomPanel);
        } else {
            websiteAnchor.setVisible(false);
        }
        this.detail = new Detail(backToSearchResultsAnchor, addMyBizAnchor, addtoFavoriteAnchor, adminSpotAnchor, phoneAnchor, groupsAnchor, mapAnchor, mainImage, markSpotAnchor, groupsPanel,
                addGroupButton, hoursHTML, websiteAnchor);
        detail.setName(spotHolder.getName());
        detail.setAddress(spotHolder.getGeocodeInput());
        if (isEmpty(spotHolder.getDescription())) {
            detail.setDescription("No description provided yet");
        } else {
            detail.setDescription(spotHolder.getDescription());
        }
        add(detail);
        hideMapAnchor.setStyleName("hidemap");
        hideMapAnchor.addClickHandler(hideMapHandler);
        hideMapAnchor.setVisible(false);
        add(hideMapAnchor);
        add(mapHolderPanel);
    }

    private Detail detail = null;

    private FlowPanel getGroupsPanel() {
        FlowPanel groupsPanel = new FlowPanel();
        List<GroupHolder> groupHolders = spotHolder.getGroupHolders();
        if (groupHolders.isEmpty()) {
            Label label = new Label("There are currently no groups.");
            groupsPanel.add(label);
        }
        ULPanel ul = new ULPanel();
        //ul.setStyleName("results");
        groupsPanel.add(ul);
        for (GroupHolder groupHolder : groupHolders) {
            //FlowPanel hp = new FlowPanel();
            ListItem li = new ListItem();
            //li.setStyleName("clearing");
            //li.add(hp);
            ul.add(li);
            Label groupLabel = new Label(groupHolder.getName());
            groupLabel.addClickHandler(selectGroupHandler);
            groupMap.put(groupLabel, groupHolder);
            //groupLabel.addStyleName("linky");
            groupLabel.setStyleName("linky");
            // hp.add(groupLabel);
            li.add(groupLabel);
        }
        // groupsPanel.add(addGroupButton());
        return groupsPanel;
    }

    protected Map<Label, GroupHolder> groupMap = new HashMap<Label, GroupHolder>();
    protected ClickHandler selectGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Label) {
                Label b = (Label) sender;
                GroupHolder groupHolder = groupMap.get(b);
                if (groupHolder != null) {
                    //need to popup panel about group and
                    //with button to join
                    //let required a login to go into the grouppanel
                    History.newItem(MyWebApp.GROUP + groupHolder.getId());
                    History.fireCurrentHistoryState();
                }
            }
        }
    };

    public SpotDetailPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp, false);
        if (MyWebApp.isDesktop()) {
            this.mobileResponse = mobileResponse;
            this.spotHolder = mobileResponse.getSpotHolder();
            this.title = spotHolder.getName();
            this.pageTitle = spotHolder.getName() + ", " + spotHolder.getGeocodeInput();
            doDetail();
            doBottom();
        } else {
            //add the menu where
            this.addStyleName("SpotDetailPanel");
            addMenu();
            this.spotHolder = mobileResponse.getSpotHolder();
            Hyperlink markThisSpotLabel = new Hyperlink("Mark This Spot", MyWebApp.LEAVE_SPOT_MARK + spotHolder.getId());
            fixButton(markThisSpotLabel);
            add(markThisSpotLabel);
            displaySpotResponse(mobileResponse);
            groupsPanel.setWidth("100%");
        }
    }

    private void addMenu() {
        FlowPanel topPanel = new FlowPanel();
        topPanel.setStyleName("menugrouping");
        topPanel.addStyleName("clearing");
        add(topPanel);
        //addImageIcon(markSpotHandler, "Mark Spot", MyWebApp.resources.markspot(), MyWebApp.resources.markspotMobile(), topPanel, "Mark this spot");
        addImageIcon(displayGroupsHandler, "Groups", MyWebApp.resources.groups(), MyWebApp.resources.groupsMobile(), topPanel, "View groups for this spot");
        addImageIcon(viewMapHandler, "View Map", MyWebApp.resources.locationmanual(), MyWebApp.resources.locationmanualMobile(), topPanel, "View spot on a map");
        //addImageIcon(followSpotHandler, "Add spot to Favorites", MyWebApp.resources.followSpot(),MyWebApp.resources.followSpotMobile(), topPanel, "Add spot to Favorites");
    }

    private Map<Widget, String> itemMap = new HashMap<Widget, String>();

    private String buildPageDescription() {
        StringBuffer sb = new StringBuffer();
        //location
        sb.append(spotHolder.getGeocodeInput());
        if (!isEmpty(spotHolder.getVoicephone())) {
            sb.append(",Phone: " + spotHolder.getVoicephone());
        }
        //description
        if (!isEmpty(spotHolder.getDescription())) {
            sb.append(spotHolder.getDescription());
        }
        //hours
        if (!isEmpty(spotHolder.getHours())) {
            sb.append(spotHolder.getHours());
        }
        return sb.toString();
    }

    private void displaySpotResponse(MobileResponse mobileResponse) {
        this.mobileResponse = mobileResponse;
        this.spotHolder = mobileResponse.getSpotHolder();
        this.title = spotHolder.getName();
        this.pageTitle = spotHolder.getName() + ", " + spotHolder.getGeocodeInput();
        this.pageDescription = buildPageDescription();
        if (spotHolder.isPlace()) {
            doPlace(spotHolder, true);
        } else {
            doPlate(spotHolder, true);
        }
        HTML description = new HTML(spotHolder.getDescription());
        if ((spotHolder.getDescription() == null) || (spotHolder.getDescription().equals(""))) {
            description.setHTML("No description provided yet.");
        }
        addFieldset(description, "Description", "desc");
        doBottom();
        add(adminSpotButton());
        addToFavorites(spotHolder);
        addToMyBiz(spotHolder);
    }

    FlowPanel bottomPanel = new FlowPanel();

    private void doBottom() {
        add(bottomPanel);
        bottomPanel.add(contentsPanel);
        if (spotHolder.getContentHolder() != null) {
            addContentHolder(spotHolder.getContentHolder(), false, true);
        }
        addYelpDetails(bottomPanel, mobileResponse.getYelpDetails());
        addYahooUpcoming(bottomPanel, mobileResponse.getEvents());
        if (mobileResponse.getYelpDetails() != null) {
            FlowPanel flowPanel = new FlowPanel();
            flowPanel.setStyleName("yelp");
            YelpDetails yelpDetails = mobileResponse.getYelpDetails();
            for (YelpReview yelpReview : yelpDetails.getReviews()) {
                //add user profile pic
                HorizontalPanel hp = new HorizontalPanel();
                if (yelpReview.getUserImageUrl() != null) {
                    Image image = new Image(yelpReview.getUserImageUrl());
                    hp.add(image);
                }
                hp.setWidth("100%");
                VerticalPanel vp = new VerticalPanel();
                Label username = new Label(yelpReview.getUsername());
                vp.add(username);
                InlineLabel snippetLabel = new InlineLabel(yelpReview.getExcerpt());
                String readMore = yelpDetails.getUrl() + "#hrid:" + yelpReview.getId();
                //String readmore = " http://www.yelp.com/biz/rose-tea-cafe-pittsburgh#hrid:w_hsBYKD5LS6Xq2laf8KmA" + yelpReview.getId();
                Anchor anchor = new Anchor(" read more...", readMore);
                snippetLabel.getElement().appendChild(anchor.getElement());
                vp.add(snippetLabel);
                hp.add(vp);
                if (yelpReview.getRatingImageSmallUrl() != null) {
                    hp.add(new Image(yelpReview.getRatingImageSmallUrl()));
                }
                flowPanel.add(hp);
            }
            for (YelpDeal yelpDeal : yelpDetails.getDeals()) {
                HorizontalPanel hp = new HorizontalPanel();
                Image image = new Image(yelpDeal.getImageUrl());
                hp.add(image);
                Anchor title = new Anchor(yelpDeal.getTitle(), yelpDeal.getUrl());
                //hp.add(label);
                VerticalPanel vp = new VerticalPanel();
                vp.add(title);
                vp.add(new Label("Original Price:" + yelpDeal.getFormattedOriginalPrice()));
                vp.add(new Label("Price:" + yelpDeal.getFormattedPrice()));
                vp.add(new Label("Remaining Available:" + yelpDeal.getRemainingCount()));
                hp.add(vp);
                flowPanel.add(hp);
            }
            bottomPanel.add(flowPanel);
            //how about deals??
        }
        addInstagramDetailPage(bottomPanel, mobileResponse.getInstagrams());
        ContestVotingPanel cvp = new ContestVotingPanel(mywebapp, mobileResponse.getContestQueryResponse(), spotHolder.getId());
        bottomPanel.add(cvp);
        FlowPanel spotMarks = new FlowPanel();
        spotMarks.getElement().setId("spot_marks");
        bottomPanel.add(spotMarks);
        FlowPanel marksPanel = new FlowPanel();
        marksPanel.getElement().setId("marks_panel");
        // spotMarks.add(marksPanel);
        addResults(mobileResponse.getEventLocationResults(), spotMarks);
        addResults(mobileResponse.getCouponLocationResults(), spotMarks);
        addResults(mobileResponse.getItemLocationResults(), spotMarks);
        if (spotHolder.getContactEnabled()) {
            bottomPanel.add(contactButton());
        }
    }

    protected void addResults(List<LocationResult> locationResults, FlowPanel spotMarks) {
        for (LocationResult locationResult : locationResults) {
            LocationResult lr = new LocationResult();
           // lr.setSolrDocument(solrDocument);
            addSpotDetailPageResult(locationResult, spotMarks);
        }
    }

    private void debug(SolrDocument solrDocument) {
        GWT.log("Begin");
        for (String key : solrDocument.getMap().keySet()) {
            GWT.log(key + ":" + solrDocument.getFirstString(key));
        }
        GWT.log("End");
    }

    //mode 2, always a mark item to go to
    protected void addSpotDetailPageResult(LocationResult locationResult, FlowPanel spotMarks) {
        SolrDocument solrDocument = locationResult.getSolrDocument();
        ItemHolder itemHolder = locationResult.getItemHolder();
        //debug(solrDocument);
        Long itemId = itemHolder.getId();
        //solrDocument.getFirstLong("georepoitemid_l");
        String targetHistoryToken = "#" + MyWebApp.ITEM_DETAIL + itemId;
        Image userMarkImage = null;
        String userImagePath = solrDocument.getFirstString("user_thumbnail_130x130_url_s");
        if (userImagePath != null) {
            userMarkImage = new Image();
            userMarkImage.setUrl(userImagePath);
        } else {
            userMarkImage = new Image(MyWebApp.resources.anon130x130());
        }
        String userHistoryToken = null;
        Long userId = solrDocument.getFirstLong("userid_");
        if (userId != null) {
            userHistoryToken = MyWebApp.VIEW_USER_PROFILE + userId;
        }
        Anchor userImageAnchor = new Anchor();
        if (userHistoryToken != null) {
            userImageAnchor.setHref(userHistoryToken);
        }
        userImageAnchor.getElement().appendChild(userMarkImage.getElement());
        Anchor markImageAnchor = new Anchor();
        markImageAnchor.setHref(targetHistoryToken);
        markImageAnchor.getElement().appendChild(userMarkImage.getElement());
        String escapedjavascriptsnippet_s = solrDocument.getFirstString("escapedjavascriptsnippet_s");
        Anchor readMoreAnchor = new Anchor(targetHistoryToken);
        readMoreAnchor.setHref(targetHistoryToken);

        FlowPanel markContentPanel = new FlowPanel();
        markContentPanel.getElement().setId("md_mark_photo");



        addImages(itemHolder,markContentPanel);

   //     if (itemHolder.getContentHolder() != null) {
//            for (ContentHolder contentHolder : itemHolder.getContentHolder().getContentHolders()) {
//
//
//                Image image = getImage(contentHolder, "320x320");
//                image.setStyleName("md_pop");
//                SpotSpan spotSpan = new SpotSpan();
//                spotSpan.setStyleName("md_photo_thumb");
//                spotSpan.add(image);
//                markContentPanel.add(spotSpan);
//
//                //we need to add handler to each image so when clicked we bring it up big
//                imageMap.put(image,contentHolder);
//                image.addClickHandler(imageClickHandler);
//            }
//        }



        SpotMarkComposite smc = new SpotMarkComposite(markImageAnchor, readMoreAnchor,markContentPanel);
        smc.setMarkContent(escapedjavascriptsnippet_s);
        String username = solrDocument.getFirstString("username_s");
        if (username == null) {
            username = "Anonymous";
        }
        smc.setUsername(username);






        spotMarks.add(smc);
    }

    ClickHandler displayGroupsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleSpotGroups(spotHolder.getId());
        }
    };
    private VerticalPanel groupsPanel = new VerticalPanel();

    Label contactButton() {
        Label btn = new Label("Contact Spot");
        btn.addClickHandler(contactSpotHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    Label adminSpotButton() {
        Label btn = new Label("Administer Spot");
        btn.addClickHandler(adminSpotHandler);
        fixButton(btn);
        return btn;
    }

    ClickHandler adminSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.MANAGE_SPOT + spotHolder.getId());
        }
    };
    ClickHandler contactSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            SpotHolder spotHolder = mobileResponse.getSpotHolder();
            ContactForm contactForm = new ContactForm(mywebapp, spotHolder);
            mywebapp.swapCenter(contactForm);
        }
    };

    public void toggleFirst() {
        // TODO Auto-generated method stub
    }
}
