package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import java.util.*;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.*;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.AutoGrowTextArea;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.mark.SpotMarkComposite;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

import java.util.HashMap;
import java.util.Map;

public class ResultsPanel extends SpotBasePanel implements SpotMouthPanel {


    public boolean displayLocationForm() {

        return true;
    }






    public ResultsPanel() {

    }
    public String getPageTitle() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    private String title = "Results";

    public void setTitle(String title) {
        this.title = title;
    }








    /*
    changing the following to not have the callback defined now, we will define later
     */

    @Override
    protected void setupRootPanelForm() {
        GWT.log("setupRootPanelForm");
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setAction(mywebapp.getHostUrl() + "/saveMediaFile");

    }







    String searchTitle = "results";

    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getResultsPanel();
    }

    protected SearchParameters searchParameters = new SearchParameters();

    public AsyncCallback showMap = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not show map");
        }

        public void onSuccess(Object response) {
            mywebapp.doMapPage();
            mywebapp.getResultsPanel().buildMap();
        }
    };



    AsyncCallback displayResultsCallback = new AsyncCallback<MobileResponse>() {
        public void onFailure(Throwable throwable) {
            GWT.log("displayResultsCallback onFailure");
            GWT.log("ResultsPanel", throwable);
          //  postDialog.hide();
            getMessagePanel().clear();
            getMessagePanel().displayError("Search failure", throwable);
        }

        public void onSuccess(MobileResponse response) {
            GWT.log("displayResultsCallback onSuccess");
            GWT.log("response.getStatus() =" + response.getStatus());
            GWT.log("response message " + response.getMessage());

           // postDialog.hide();
            if (response.getStatus() == 1) {
                // clear out the current results
                //i am adding some images here for the home screen and
                //this is wiping it out....
                GWT.log("Found "
                        + response.getLocationResults().size()
                        + " results");
                 showResults(response);
                //need to know if it's spots or marks
                mywebapp.getMessagePanel().displayMessage("Found "
                        + response.getLocationResults().size() + " " + searchTitle);

                if (mywebapp.getToggleMapMode().getValue()) {
                    showMap.onSuccess(null);
                }

            } else {
                GWT.log("displayResultsCallback, status is 0 Got a error");
                getMessagePanel().displayErrors(response.getErrorMessages());
            }
        }
    };

    public void showResults(MobileResponse mobileResponse) {
        clear();
        commonInit(mobileResponse);
        displayLocationResults(mobileResponse, null, 1);
        mywebapp.swapCenter(ResultsPanel.this);

    }
    public SearchParameters getSearchParameters() {
        return searchParameters;
    }

    protected void addUserResult(ULPanel ul, LocationResult locationResult) {
        FlowPanel fp = new FlowPanel();
        SolrDocument solrDocument = locationResult.getSolrDocument();
        Long latestMarkUserId = solrDocument.getFirstLong("latest_mark_userid_l");
        // GWT.log("latestMarkUserId=" + latestMarkUserId);
        //SolrDocument result, Panel hp, String fieldName,ImageResource imageResourceBig,  ImageResource imageResourceMobile,String stylename, String targetHistoryToken
        String targetHistoryToken = MyWebApp.VIEW_USER_PROFILE + latestMarkUserId;
        Image userimage = addImage(solrDocument, fp, "latest_mark_user__thumbnail_130x130_url_s",
                MyWebApp.resources.anon130x130(), MyWebApp.resources.anon130x130Mobile(), "userimage", targetHistoryToken);
        String username_s = solrDocument.getFirstString("username_s");
        Label usernameLabel = new Label(username_s);
        fp.add(usernameLabel);
        String aboutme_s = solrDocument.getFirstString("aboutme_s");
        Label aboutMeLabel = new Label(aboutme_s);
        fp.add(aboutMeLabel);
        Long contestId = searchParameters.getContestId();
        String contestTotalVotes = solrDocument.getFirstString("contestTotalVotes_" + contestId + "_l");
        fp.add(new Label("Total Votes:" + contestTotalVotes));
        String contestAverageVote = solrDocument.getFirstString("contestAverageVote_" + contestId + "_d");
        fp.add(new Label("Average Vote:" + contestAverageVote));
        String contestTotalSum = solrDocument.getFirstString("contestTotalSum_" + contestId + "_l");
        //use the searchparameters to get the contest id
        ListItem li = new ListItem();
        li.setStyleName("clearing");
        li.add(fp);
        ul.add(li);
    }

    public static int RESULT_COUNT = 20;
    ClickHandler previousResultsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            // need to clear any messaging
            mywebapp.getMessagePanel().clear();
            searchParameters.setOffset(searchParameters.getOffset() - RESULT_COUNT);
            performSearch();
        }
    };
    ClickHandler nextResultsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.getMessagePanel().clear();
            searchParameters.setOffset(searchParameters.getOffset() + RESULT_COUNT);
            performSearch();
        }
    };

    public ResultsPanel(MyWebApp mywebapp) {
        this(mywebapp, true);
        //setWidth("50%");
    }

    public ResultsPanel(MyWebApp mywebapp, boolean displaySearchForm) {
        super(mywebapp, displaySearchForm);
        this.addStyleName("ResultsPanel");

        //sortingPanel.getElement().setId("sortingPanel");


        //resultsViewLabel.addClickHandler(showResultsOnMapHandler);
        resultsViewLabel.setStyleName("sortingLabel");
        resultsViewLabel.addStyleName("linky");
        sortingLabel.setStyleName("sortingLabel");
        sortingLabel.addStyleName("linky");


        toggleKiloMilesLabel.setStyleName("sortingLabel");
        toggleKiloMilesLabel.addStyleName("linky");
        //toggleKiloMilesLabel.addClickHandler(toggleKiloMilesHandler);

        if (mywebapp.isShowMeters()) {
            toggleKiloMilesLabel.setText(SHOW_MILES);
        } else {
            toggleKiloMilesLabel.setText(SHOW_KM);
        }

    }

    ClickHandler tagsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            // Object sender = event.getSource();
            History.newItem(MyWebApp.TAGS);
            TagsPanel tagsPanel = new TagsPanel(mywebapp, queryResponse);
            mywebapp.swapCenter(tagsPanel);
        }
    };
    ClickHandler sortsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.SORTS);
            SortsPanel sortsPanel = new SortsPanel(mywebapp);
            mywebapp.swapCenter(sortsPanel);
        }
    };
    protected QueryResponse queryResponse = null;

    public void displaySorts(ULPanel ul) {
        if (queryResponse == null) return;
        if (queryResponse.getNumFound() == 0) return;
        ListItem tagsli = new ListItem();
        //removed because this broken layout autg 27
        //tagsli.setStyleName("clearing");
        //FlowPanel hp = new FlowPanel();

        Label sortsLabel = new Label("Sorting");
        sortsLabel.addClickHandler(sortsHandler);
        sortsLabel.addStyleName("linky");
       // hp.add(sortsLabel);
        tagsli.add(sortsLabel);
        ul.add(tagsli);
        ul.setStyleName("sorts");
    }

    public void displayFacets(ULPanel ul) {
        if (queryResponse == null) return;
        if (queryResponse.getFacetFields().size() > 0) {
            int count = queryResponse.getFacetFields().get(0).getFacetCounts().size();
            if (count > 0) {
                ListItem tagsli = new ListItem();
                // tagsli.setStyleName("clearing");
                Label tagsLabel = new Label("Tags (" + count + ")");
                tagsLabel.addClickHandler(tagsHandler);
                tagsLabel.addStyleName("linky");
                tagsli.add(tagsLabel);
                ul.add(tagsli);
            }
        }
    }

    public void displayQueryResponse(QueryResponse queryResponse, String label, int mode) {
        if (queryResponse.getResults().isEmpty()) {
            return;
        }
        if (label != null) {
            Label mylabel = new Label(label);
            mylabel.setStyleName("h1");
            add(mylabel);
        }
        try {
            ULPanel facetsUL = new ULPanel();
            facetsUL.setStyleName("facets");
            add(facetsUL);
            displayFacets(facetsUL);
            ULPanel ul = new ULPanel();
            ul.setStyleName("results");
            add(ul);
            for (SolrDocument solrDocument : queryResponse.getResults()) {
                LocationResult lr = new LocationResult();
                lr.setSolrDocument(solrDocument);
                addResult(ul, lr, mode);
            }
            GWT.log("adding paginationPanel");
            PaginationPanel paginationPanel = new PaginationPanel(
                    previousResultsHandler, nextResultsHandler,
                    searchParameters, queryResponse.getNumFound());
            add(paginationPanel);
        } catch (Exception e) {
            debug("Error on resultspanel " + e.getMessage());
            mywebapp.printStackTrace2(e);
        }
    }

    protected void addResult(ULPanel ul, LocationResult locationResult, int mode) {
        if (mode == 1) {
            addHomePageResult(ul, locationResult);
        } else if (mode == 2) {



          //  addSpotDetailPageResult(ul, locationResult);
        }
    }

    public void performSearch() {
        performSearch(null);
    }

    public void performSearch(final AsyncCallback messageCallback) {
        GWT.log("performSearch");
        final DataOperationDialog gettingResultsDialog = new DataOperationDialog("Getting results");
        gettingResultsDialog.show();
        gettingResultsDialog.center();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.search(searchParameters, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                caught.printStackTrace();
                // GWT.log("search onFailure");
                // gettingResultsDialog.printStackTrace();
                gettingResultsDialog.hide();
                //getMessagePanel().displayError("Search Failure: " + caught.getMessage());
                if (displayResultsCallback != null) {
                    displayResultsCallback.onFailure(caught);
                }
            }

            public void onSuccess(Object result) {
                GWT.log("search onSuccess");
                GWT.log("result=" + result.toString());
                gettingResultsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                displayResultsCallback.onSuccess(mobileResponse);
                if (messageCallback != null) {
                    messageCallback.onSuccess(null);
                   // messageCallback = null;
                }
            }
        });
    }






    public MobileResponse getMobileResponse() {
        return this.mobileResponse;
    }
    /*
   we may be returning a contestholder in the mobileresponse, we
   use this to set the image
    */
    protected void processIfContest(MobileResponse mobileResponse) {
        GWT.log("processIfContest");
        ContestHolder contestHolder = mobileResponse.getContestHolder();
        if (contestHolder == null) {
            return;
        }
        //addImageContent(bigImageDisplayPanel, ch, "320x320");
        ImageScaleHolder imageScaleHolder = getImageScaleHolder(contestHolder.getContentHolder(), "130x130");
        if (imageScaleHolder == null) {
          //  this.setImageResources(mywebapp.resources.contests(), mywebapp.resources.contestsMobile());
        } else {
            GWT.log("url is " + imageScaleHolder.getWebServerAssetHolder().getUrl());
            Image image = new Image();
            image.setUrl(imageScaleHolder.getWebServerAssetHolder().getUrl());
            this.setImage(image);
        }
        //since we are not calling swapCenter, we need to do this here
        GWT.log("The title is " + contestHolder.getName());
        mywebapp.getTopMenuPanel().setTitleBar(contestHolder.getName());
    }

    protected void processIfDrivers() {
        if (!getTitle().equals("Driver Reports")) {
            return;
        }
        HTML htmlPanel = new HTML();
        String html = MyHtmlResources.INSTANCE.getDriver().getText();
        htmlPanel.setHTML(html);
        htmlPanel.setStyleName("aboutdrivers");
        addFieldset(htmlPanel, "","na1",this);

        Hyperlink reportDriver = new Hyperlink("Report Driver", MyWebApp.MARK_PLATE);
        reportDriver.addStyleName("whiteButton");
        add(reportDriver);
    }



    //private FlowPanel sortingPanel = new FlowPanel();

    public void buildMap() {
        resultsViewLabel.setText(LIST_VIEW);

        // Create a base icon for all of our markers that specifies the
        // shadow, icon dimensions, etc.
        baseIcon = Icon.getDefaultIcon();
        baseIcon.setShadowURL("http://www.google.com/mapfiles/shadow50.png");
        baseIcon.setIconSize(Size.newInstance(20, 34));
        baseIcon.setShadowSize(Size.newInstance(37, 34));
        baseIcon.setIconAnchor(Point.newInstance(9, 34));
        baseIcon.setInfoWindowAnchor(Point.newInstance(9, 2));
        Location location = mywebapp.getCurrentLocation();
        LatLng currentLatLng = LatLng.newInstance(location.getLatitude(), location.getLongitude());
        this.mapWidget = new MapWidget(currentLatLng, 19);
        //15 is scaled out too much
        for (LocationResult locationResult : mobileResponse.getLocationResults()) {
            GWT.log("adding location result " + locationResult.getLatitude() + "," + locationResult.getLongitude());
            LatLng latLng = LatLng.newInstance(locationResult.getLatitude(), locationResult.getLongitude());
            Marker marker = createMarker(latLng, locationResult);
            mapWidget.addOverlay(marker);
        }
        mapWidget.setSize("100%", "600px");
        mapWidget.setUIToDefault();
        MapUIOptions opts = mapWidget.getDefaultUI();
        opts.setDoubleClick(false);
        mapWidget.setUI(opts);
        add(mapWidget);
    }
    //http://code.google.com/p/gwt-google-apis/source/browse/trunk/maps/samples/hellomaps/src/com/google/gwt/maps/sample/hellomaps/client/IconClassDemo.java?r=1875

    //this creates the map bubble
    private Marker createMarker(LatLng point, final LocationResult locationResult) {
        GWT.log("createMarker");
        Icon icon = Icon.newInstance(baseIcon);
        icon.setImageURL("http://www.google.com/mapfiles/marker.png");
        if (locationResult.getGooglePlace() != null) {
            icon.setImageURL(locationResult.getGooglePlace().getIcon());
        }
        MarkerOptions options = MarkerOptions.newInstance();
        options.setIcon(icon);
        String title = locationResult.getName();
        options.setTitle(title);
        final Marker marker = new Marker(point, options);
        final MarkerMouseOverHandler h = new MarkerMouseOverHandler() {
            public void onMouseOver(MarkerMouseOverEvent event) {
                InfoWindow info = mapWidget.getInfoWindow();


                //window will not go less than 128 because this is size of image inside it



                //FlowPanel vp = new FlowPanel();
                ComplexPanel vp = getLocationResultPanel(locationResult);
//                vp.setWidth("500px");
              //  vp.setHeight("68px");
                vp.setStyleName("mapresult");
                InfoWindowContent inc = new InfoWindowContent(vp);

             //   inc.setMaxWidth(500);

                //inc.getOptions().
                lastContent = vp;
                lastOpenedInfoWindow = info;
                lastMarker = marker;
                info.open(event.getSender(), inc);
                //info.open();
            }
        };
        marker.addMarkerMouseOverHandler(h);
        return marker;
    }

    private Icon baseIcon;


    //we need to keep this around so we can add the results to a list, or a map
    private MobileResponse mobileResponse = null;

    public void commonInit(MobileResponse mobileResponse) {
        addLeaveMarkAtTopForm();
        FlowPanel fp = new FlowPanel();
        fp.setWidth("100%");
        fp.setStyleName("topbuttonsmark");
        fp.addStyleName("clearing");
        fp.add(sortingLabel);
        fp.add(resultsViewLabel);
        fp.add(toggleKiloMilesLabel);
        //we do not use this for mobile
        if ( MyWebApp.isDesktop()) {
           // add(fp);
        }

        mywebapp.updateTagCloud(mobileResponse.getSearchQueryResponse());
        processIfContest(mobileResponse);
        processIfDrivers();
        //wierd, but this is where the facets are kept
        this.queryResponse = mobileResponse.getSearchQueryResponse();
        // only display if results.
        if (mobileResponse.getLocationResults().size() > 0) {
            if (resultsLabel != null) {
                Label mylabel = new Label(resultsLabel);
                mylabel.setStyleName("h1");
                add(mylabel);
            }
        }
        //this is the kind of wierd thing where we will display
        //the facets that we have
        //response.getSearchQueryResponse()
        if (MyWebApp.isDesktop()) {
            //sortingPanel.setStyleName("sortingPanel");
            //add(sortingPanel);
            mywebapp.getTagListBox().clear();
            mywebapp.getTagListBox().addItem("Filter by...", "");
            for (FacetField facetField : queryResponse.getFacetFields()) {
                // GWT.log("looping 1");
                for (FacetCount facetCount : facetField.getFacetCounts()) {
                    mywebapp.getTagListBox().addItem(facetCount.getName() + " (" + facetCount.getValueCount() + ")", facetCount.getName());
                }
            }
            //create a link that will show down down or
        } else {
            ULPanel facetsSortingUL = new ULPanel();
            facetsSortingUL.setStyleName("facets");
            add(facetsSortingUL);
            displayFacets(facetsSortingUL);
            displaySorts(facetsSortingUL);
        }
    }

    String resultsLabel = null;

    public void displayLocationResults(MobileResponse mobileResponse, String resultsLabel, int mode) {
        this.resultsLabel = resultsLabel;
        this.mobileResponse = mobileResponse;
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (LocationResult locationResult : mobileResponse.getLocationResults()) {
            if (locationResult.getLocation() != null) {
                addLocation(ul, locationResult,
                        locationHandler, true);
            } else if (locationResult.getSolrDocument() != null) {
                String type = locationResult.getSolrDocument().getFirstString("type_s");
                if (type.equalsIgnoreCase("user")) {
                    addUserResult(ul, locationResult);
                } else {
                    addResult(ul, locationResult, mode);
                }
                //if favorites, add a link to remove
            }
            handleResult(ul, locationResult);
        }

    }

    public void resetSearchParameters() {
        this.searchParameters = new SearchParameters();
    }

    protected void handleResult(ULPanel ul, LocationResult locationResult) {
    }


}

