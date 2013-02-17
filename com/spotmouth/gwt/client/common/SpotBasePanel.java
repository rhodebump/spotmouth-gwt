package com.spotmouth.gwt.client.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.safehtml.shared.*;
import com.google.gwt.i18n.client.NumberFormat;
import com.bramosystems.oss.player.core.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.maps.client.*;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle.MultiWordSuggestion;
import com.phonegap.gwt.camera.client.Camera;
import com.phonegap.gwt.camera.client.CameraCallback;
import com.phonegap.gwt.capture.client.Capture;
import com.phonegap.gwt.capture.client.Capture.CaptureCallback;
import com.phonegap.gwt.capture.client.Capture.CaptureError;
import com.phonegap.gwt.capture.client.Capture.CaptureOptions;
import com.phonegap.gwt.capture.client.Capture.MediaFile;
import com.phonegap.gwt.console.client.Logger;
import com.phonegap.gwt.filetransfer.client.FileTransfer;
import com.spotmouth.gwt.client.*;
import com.spotmouth.gwt.client.contest.ContestsPanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.mark.*;
import com.spotmouth.gwt.client.group.ManageGroupPanel;
import com.spotmouth.gwt.client.instagram.Data;
import com.spotmouth.gwt.client.help.HelpPanel;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.icons.SpotImageResource;
import com.spotmouth.gwt.client.messaging.ViewMessagePanel;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import com.spotmouth.gwt.client.search.SearchForm;
import com.spotmouth.gwt.client.product.ProductInstallPanel;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import org.cobogw.gwt.user.client.ui.Rating;

import java.util.*;
//import de.kurka.phonegap.showcase.client.
//http://code.google.com/p/gwt-eye-candy/wiki/ButtoningUp
/*
* Facebook login is a little tricky
* We need to invoke a childbrowser that will login to facebook and return to us the accesstoken
*
* I send the accesstoken to spotmouth that will then go to facebook and get the userid
*/

//BIG, used to extend verticalpanel
public abstract class SpotBasePanel extends FlowPanel {


    protected SimplePanel imageUploaderImagePanel = new SimplePanel();


    protected IUploader.OnFinishUploaderHandler onFinishUploaderHandler2 = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                new PreloadedImage(uploader.fileUrl(), showImage2);
                // The server sends useful information to the client by default
                IUploader.UploadedInfo info = uploader.getServerInfo();
                System.out.println("File name " + info.name);
                System.out.println("File content-type " + info.ctype);
                System.out.println("File size " + info.size);
                // You can send any customized message and parse it
                System.out.println("Server message " + info.message);
                //okay, we don't have a final "URL" for this image, and we need one to be able to insert into
                //a wysiwyg editor
                //let's call the server and sweep through the session files and convert these to content
                //saveSessionContents();
            }
        }
    };
    // Attach an image to the pictures viewer
    private PreloadedImage.OnLoadPreloadedImageHandler showImage2 = new PreloadedImage.OnLoadPreloadedImageHandler() {
        public void onLoad(PreloadedImage image) {
            imageUploaderImagePanel.setWidget(image);
        }
    };


                       //used by map
    protected Geocoder geocoder;


    protected MapWidget mapWidget = null;



    //mapPanel could be null;
    protected void initMap(Location location,SimplePanel mapPanel) {
        GWT.log("initMap");
        if (location == null) {
            mapWidget = new MapWidget();
        } else {
            LatLng latLng = LatLng.newInstance(location.getLatitude(), location.getLongitude());
            mapWidget = new MapWidget(latLng, 15);
            reverseGeocode(latLng, null);
            Marker marker = new Marker(latLng);
            mapWidget.addOverlay(marker);
        }
        mapWidget.setSize("840px", "400px");
        mapWidget.setUIToDefault();
        MapUIOptions opts = mapWidget.getDefaultUI();
        opts.setDoubleClick(false);
        mapWidget.setUI(opts);
        mapWidget.addMapClickHandler(new MapClickHandler() {
            public void onClick(MapClickEvent e) {
                Overlay overlay = e.getOverlay();
                LatLng point = e.getLatLng();
                if (overlay != null && overlay instanceof Marker) {
                    // no point if we click on marker
                } else {
                    mywebapp.setAutoGps(false);
                    //this is a callback
                    reverseGeocode(point, afterGeocodeCallback);
                }
            }
        });


        if (mapPanel != null) {
            mapPanel.setWidget(mapWidget);
        }

    }

    AsyncCallback afterGeocodeCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
           mywebapp.verifyDisplay();
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            mywebapp.toggleHome();
        }
    };


    /*
   the calllback we pass here happens after we do our reverse geocoding
    */
    private void reverseGeocode(final LatLng latLng, final AsyncCallback callback) {
        GWT.log("reverseGeocode");
        geocoder.getLocations(latLng, new LocationCallback() {
            public void onFailure(int statusCode) {
                getMessagePanel().displayError("Failed to geocode position " + latLng.toString()
                        + ". Status: " + statusCode + " "
                        + StatusCodes.getName(statusCode));
                if (callback != null) {
                    callback.onFailure(null);
                }
                //but we can still set the location
            }

            public void onSuccess(JsArray<Placemark> locations) {
                GWT.log("reverseGeocode onSuccess " + locations.length());
                // for (int i = 0; i < locations.length(); ++i) {
                //just need to get the very first location!
                Placemark placemark = locations.get(0);
                Location location = new Location();
                location.setLatitude(placemark.getPoint().getLatitude());
                location.setLongitude(placemark.getPoint().getLongitude());
                GWT.log("address is " + placemark.getStreet());
                location.setAddress1(placemark.getStreet());
                location.setCity(placemark.getCity());
                location.setState(placemark.getState());
                location.setZipcode(placemark.getPostalCode());
                location.setCountryCode(placemark.getCountry());
                location.setGeocoded(true);
                //does not fetch data, but will update the top of the app
               mywebapp.setCurrentLocation(location);
                if (callback != null) {
                    callback.onSuccess(null);
                }
                //we just reverse geocoded, not that we clicked on map
                //  }
            }
        });
    }





    protected void initZipCodeTextBox() {
        zipcodeTextField = new TextField();
       // zipcodeTextBox.getElement().setAttribute("placeholder", "Zip Code");
       // zipcodeTextBox.setMaxLength(6);

    }

    protected void initAddress1TextBox() {
        address1TextField = new TextField();
        address1TextField.getElement().setAttribute("placeholder", "Address");


    }



    protected Button getFacebookButton(MarkData markData) {
        Button shareOnFacebookButton = new Button();
        shareOnFacebookButton.addClickHandler(saveHandlerFacebook);
        widgetMarkDataMap.put(shareOnFacebookButton,markData);
        if (! mywebapp.isFacebookUser()) {
            //shareOnFacebookButton.setVisible(false);
            //shareOnFacebookButton.setStyleName("hideme");
            //need to do directly on this element

            hideElement(shareOnFacebookButton.getElement());
        }
        return shareOnFacebookButton;
    }






    protected void showElement(Element element) {
        element.removeAttribute("style");
    }


    protected void hideElement(Element element) {
        element.setAttribute("style","display: none !important;");
    }

    AsyncCallback deleteMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Friend deleted.");
        }
    };



    protected void deleteFriend(FriendHolder friendHolder) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFriendHolder(friendHolder);
        friendRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteFriend(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.getMessagePanel().clear();
                    mywebapp.setFriendsAndGroups(null);
                    mywebapp.setHomeCallback(deleteMessageCallback);
                    History.newItem(MyWebApp.MANAGE_FRIENDS);
                    //mywebapp.toggleManageFriends(deleteMessageCallback);
                } else {
                    // do it again...
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected ClickHandler backToResultsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleBackToSearchResults();
        }
    };

    public Anchor getBackAnchor() {
        Anchor backToSearchResultsAnchor = new Anchor();
        backToSearchResultsAnchor.setTitle("Back to Search Results");
        backToSearchResultsAnchor.addClickHandler(backToResultsHandler);
        backToSearchResultsAnchor.getElement().setAttribute("id", "goback");
        return backToSearchResultsAnchor;
    }

    AsyncCallback saveLocationAsSpotGoToLeaveMarkCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            MobileResponse mobileResponse = (MobileResponse) response;
            if (mobileResponse.getStatus() == 1) {
                String targetHistoryToken = MyWebApp.LEAVE_SPOT_MARK + mobileResponse.getSpotHolder().getId();
                History.newItem(targetHistoryToken);
            } else {
                mywebapp.verifyDisplay();
                getMessagePanel().clear();
                getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
            }
        }
    };
    protected ClickHandler createSpotFromLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget widget = (Widget) event.getSource();
            LocationResult locationResult = pickLocationMap.get(widget);
            mywebapp.saveLocationAsSpot(locationResult, saveLocationAsSpotGoToLeaveMarkCallback);
        }
    };
    Map<Widget, LocationResult> pickLocationMap = new HashMap<Widget, LocationResult>();

    protected ULPanel getPickSpotULPanel() {
        final ULPanel ulPanel = new ULPanel();
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLatitude(mywebapp.getCurrentLocation().getLatitude());
        searchParameters.setLongitude(mywebapp.getCurrentLocation().getLongitude());
        searchParameters.setLicensePlate(false);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.search(searchParameters, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayMessage("Error:" + caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                Anchor newSpotAnchor = new Anchor("My Spot is not listed here.");
                ulPanel.add(newSpotAnchor);
                for (LocationResult locationResult : mobileResponse.getLocationResults()) {
                    ListItem listItem = new ListItem();
                    Anchor anchor = new Anchor(locationResult.getLabel());
                    listItem.add(anchor);
                    ulPanel.add(listItem);
                    if (locationResult.getSolrDocument() == null) {
                        pickLocationMap.put(anchor, locationResult);
                        anchor.addClickHandler(createSpotFromLocationHandler);
                    } else {
                        Long spotId = locationResult.getSolrDocument().getFirstLong("spotid_l");
                        String targetHistoryToken = "#" + MyWebApp.LEAVE_SPOT_MARK + spotId;
                        anchor.setHref(targetHistoryToken);
                    }
                }
                Anchor newSpotAnchor2 = new Anchor("My Spot is not listed here.");
                ulPanel.add(newSpotAnchor2);
            }
        });
        return ulPanel;
    }

    /*
    added so we can add a special id at the top level for the "mark_address" for dmitriy
     */
    public String getBodyClassId() {
        return "spotmouth_default_id";
    }

    public void addedToDom() {
        //do nothing but override
    }

    protected void toggleAddGroup(SpotHolder spotHolder) {
        GWT.log("toggleAddGroup");
        GroupHolder groupHolder = new GroupHolder();
        groupHolder.setVisibleToMembers(false);
        groupHolder.setManageByMembers(false);
        //who will own this group, a person or a spot??
        ManageGroupPanel mgp = new ManageGroupPanel(mywebapp, groupHolder, spotHolder);
        mywebapp.swapCenter(mgp);
        if (!mywebapp.getAuthenticatedUser().isGoldenMember()) {
            //let's add the "hideme" to this id
            Element optin = DOM.getElementById("opt-in-required-div");
            optin.addClassName("hideme");
        }
    }

    protected Map<Label, GroupHolder> groupMap = new HashMap<Label, GroupHolder>();
    protected ClickHandler selectGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Label) {
                Label b = (Label) sender;
                GroupHolder groupHolder = groupMap.get(b);
                if (groupHolder != null) {
                    History.newItem(MyWebApp.GROUP + groupHolder.getId());
                    History.fireCurrentHistoryState();
                }
            }
        }
    };
    //protected TextBox emailAddressTextBox = new TextBox();
    protected TextBox smsPhoneNumberTextBox = new TextBox();
    protected TextBox oldPasswordTextBox = new TextBox();
    protected TextBox newPasswordTextBox = new TextBox();

    protected void saveAccountSettings() {
        getMessagePanel().clear();
        GWT.log("saveAccountSettings");
        UserRequest userRequest = new UserRequest();
        userRequest.setUserHolder(mywebapp.getAuthenticatedUser());
        userRequest.setAuthToken(mywebapp.getAuthToken());
        UserHolder userHolder = userRequest.getUserHolder();
        userHolder.setEmailAddress(emailTextField.getValue());
        userHolder.setSmsPhoneNumber(smsPhoneNumberTextBox.getValue());
        userRequest.setOldPassword(oldPasswordTextBox.getValue());
        userRequest.setNewPassword(newPasswordTextBox.getValue());
        //should we check if valid??
        //don't know old password, and everything is optional, so no!
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        final DataOperationDialog saveAccountSettingsDialog = new DataOperationDialog("Saving account settings");
        myService.saveAccountSettings(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                saveAccountSettingsDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                saveAccountSettingsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //UserHolder uh = mobileResponse.getUserHolder();
                    mywebapp.setAuthenticatedUser(mobileResponse.getUserHolder());
                    //we should exit the account settings page, but where to go??
                    mywebapp.toggleAccountSettings(true);
                    getMessagePanel().displayMessage("Your account have been saved.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected boolean havePicture(MobileResponse mobileResponse) {
        if (mobileResponse.getUserHolder().getContentHolder() == null) {
            return false;
        } else if (mobileResponse.getUserHolder().getContentHolder().getContentHolders().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static void addRequired(TextBox textBox) {
        DOM.setElementAttribute(textBox.getElement(), "required", "required");
    }

    public boolean displayLocationForm() {
        if (MyWebApp.isSmallFormat()) {
            return false;
        } else {
            return true;
        }
    }

    //override if you do not want this to be displayed
    public boolean showBackToResults() {
        return true;
    }

    protected void addSpotLink(SpotHolder spotHolder) {
        Hyperlink spotNameButton = getSpotLink("Go to this spot", spotHolder.getId());
        spotNameButton.setStyleName("whiteButton");
        add(spotNameButton);
    }

    protected void addToMyBiz(SpotHolder spotHolder) {
        String addLink = MyWebApp.ADD_MY_BIZ + spotHolder.getId();
        Hyperlink addToFavoritesLabel = new Hyperlink("This is my biz", addLink);
        //addToFavoritesLabel.addClickHandler(followSpotHandler);
        fixButton(addToFavoritesLabel);
        add(addToFavoritesLabel);
    }



    protected void addToFavorites(SpotHolder spotHolder) {
        String addLink = MyWebApp.ADD_SPOT_FRIEND + spotHolder.getId();
        Hyperlink addToFavoritesLabel = new Hyperlink("Add to Favorites", addLink);
        fixButton(addToFavoritesLabel);
        add(addToFavoritesLabel);
    }

    protected void addClear(FlowPanel flowPanel) {
        FlowPanel clear = new FlowPanel();
        clear.setStyleName("clear");
        flowPanel.add(clear);
    }

    protected FlowPanel mainPanel = null;

    //need to override
    public FlowPanel getMainPanel() {
        return null;
    }

    Map<Widget, SmallAdvancedForm> hideAdvancedMap = new HashMap<Widget, SmallAdvancedForm>();
    Map<Widget, MarkData> showAdvancedMap = new HashMap<Widget, MarkData>();
    ClickHandler showAdvancedWriteHerePanelHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            Button label = (Button) sender;
            moveAdvancedPanel(label);
        }
    };
    private MarkData previousMarkData = null;
    ClickHandler replyHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            //need to display the advanced panel for this spot/write on
            //source is a label
            Widget clickLabel = (Widget) sender;
            //user has clicked to reply to a post
            MarkData replyData = replyMap.get(clickLabel);
            //we will get the markdata object that is associated with this
            //new edit, and edit it to have our replyItemId
            doReply(replyData);
            //no way to keep hitting after we reply
            clickLabel.setVisible(false);
            if (previousMarkData != null) {
                GWT.log("have previous mark data to hide");
                previousMarkData.expandData.replyGoesHere.clear();
            } else {
                GWT.log("no previous markdate");
            }
            previousMarkData = replyData;
        }
    };
    MarkData previousEdit = null;
    ClickHandler clickTagHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                TagHolder tagHolder = tagHolderClickMap.get(b);
                if (tagHolder != null) {
                    //change url, but not do anything
                    History.newItem(MyWebApp.FACTUAL_TAG);
                    mywebapp.getResultsPanel().getSearchParameters().getTags().clear();
                    mywebapp.getResultsPanel().getSearchParameters().getTags().add(tagHolder);
                    //need to go back to first page of results
                    mywebapp.getResultsPanel().getSearchParameters().setOffset(0);
                    mywebapp.getResultsPanel().performSearch();
                }
            }
        }
    };
    Map<Widget, TagHolder> tagHolderClickMap = new HashMap<Widget, TagHolder>();

    protected void addGroupHeader(GroupHolder groupHolder) {
        HTML description = new HTML(groupHolder.getDescription());
        VerticalPanel vp = new VerticalPanel();
        Label groupNameLabel = new Label(groupHolder.getName());
        groupNameLabel.setStyleName("h1");
        vp.add(groupNameLabel);
        vp.add(description);
        Fieldset fs = new Fieldset();
        fs.add(vp);
        add(fs);
    }

    public ClickHandler setLocationFromDeviceHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //   mywebapp.setLocationFromDevice();
            History.newItem(MyWebApp.SET_LOCATION_FROM_DEVICE);
        }
    };
    ClickHandler logoutHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleLogout(true);
        }
    };

    public FlowPanel getTopPanel() {
        FlowPanel topPanel = new FlowPanel();
        topPanel.setStyleName("menugrouping");
        topPanel.addStyleName("clearing");
        if (mywebapp.getAuthenticatedUser() == null) {
            addImageIcon(MyWebApp.LOGIN, new LoginForm(), topPanel);
        } else {
            addImageIcon(logoutHandler, "Logout", MyWebApp.resources.logout(), MyWebApp.resources.logoutMobile(), topPanel, null);
        }
        //  addImageIcon("Dining",MyWebApp.DINING,  mywebapp.resources.dining(), mywebapp.resources.diningMobile(), linksPanel, null);
        //String markSpotToken = MyWebApp.MARK_SPOT
        addImageIcon(MyWebApp.MARK_SPOT, new MarkSpotTypePanel(), topPanel);
        addImageIcon(MyWebApp.SEARCH, new SearchForm(), topPanel);
        addImageIcon(MyWebApp.CONTESTS, new ContestsPanel(), topPanel);
        addImageIcon("Drivers", MyWebApp.DRIVERS, mywebapp.resources.drivers(), mywebapp.resources.driversMobile(), topPanel, null);
        return topPanel;
    }

    ;

    public static Image addImageToButton(Widget buttonLabel, ImageResource bigImage, ImageResource smallImage) {
        Image image = null;
        if (MyWebApp.isSmallFormat()) {
            image = new Image(smallImage);
        } else {
            image = new Image(bigImage);
        }
        buttonLabel.getElement().appendChild(image.getElement());
        return image;
    }

    //need to override
    public void addContentHolderHandler(ContentHolder contentHolder) {
    }

    protected void addHeader(String header) {
        Label myLabel = new Label(header);
        myLabel.setStyleName("h1");
        add(myLabel);
    }

    protected void validateDateRange() {
        checkRequired(startDatePicker.getTextBox(), "Start Date is required");
        checkRequired(endDatePicker.getTextBox(), "End Date is required");
        if (getItemHolder().getStartDate() == null) {
            return;
        } else if (getItemHolder().getEndDate() == null) {
            return;
        }
        if (getItemHolder().getStartDate().after(getItemHolder().getEndDate())) {
            getMessagePanel().displayError("Start Date must come before End Date.");
        }
        //end date must be before current date
        Date currentDate = new Date();
        if (getItemHolder().getEndDate().before(currentDate)) {
            getMessagePanel().displayError("The end date must not occur in the past.");
        }
    }

    Map<Long, Rating> ratingMap = new HashMap<Long, Rating>();

    protected Rating getRating(SolrDocument solrDocument, Long voteForId) {
        Rating rating = null;
        String numberOfStars_i = solrDocument.getFirstString("numberofstars_i");
        Integer maxRating = new Integer(numberOfStars_i);
        int initRating = 0;
        Integer iconStyle = solrDocument.getFirstInteger("iconstyle_i");
        Long contestId = solrDocument.getFirstLong("contestid_l");
        if (iconStyle == 1) {
            rating = new Rating(0, maxRating);
        } else if (iconStyle == 2) {
            ImageResource imageResource = mywebapp.getResources().thumbsUp16();
            Image image = new Image(imageResource);
            ImageResource imageResource2 = mywebapp.getResources().thumbsUpDeselected16();
            Image deselectedImage = new Image(imageResource2);
            Image hoverImg = new Image(imageResource);
            rating = new Rating(initRating, maxRating, Rating.RTL, image.getUrl(), deselectedImage.getUrl(), hoverImg.getUrl(), 16, 16);
        } else if (iconStyle == 3) {
            ImageResource imageResource = mywebapp.getResources().thumbsDown16();
            Image image = new Image(imageResource);
            ImageResource imageResource2 = mywebapp.getResources().thumbsUpDeselected16();
            Image deselectedImage = new Image(imageResource2);
            Image hoverImg = new Image(imageResource);
            rating = new Rating(initRating, maxRating, Rating.RTL, image.getUrl(), deselectedImage.getUrl(), hoverImg.getUrl(), 16, 16);
        } else {
            getMessagePanel().displayError("Invalid icon style:" + iconStyle);
        }
        ratingMap.put(contestId, rating);
        doVoting(rating, voteForId);
        return rating;
    }

    private void doVoting(final Rating rating, final Long voteForId) {
        rating.addValueChangeHandler(new ValueChangeHandler() {
            public void onValueChange(ValueChangeEvent event) {
                // lbl2.setText("you selected: " + event.getValue());
                // lbl1.setText("");
                rating.setReadOnly(true);
                getMessagePanel().clear();
                saveVote(voteForId);
            }
        });
    }

    protected void saveVote(Long voteForId) {
        //we need to be logged in to vote
        if (mywebapp.getAuthenticatedUser() == null) {
            getMessagePanel().displayMessage("Please login before voting.");
            return;
        }
        ContestRequest contestRequest = new ContestRequest();
        GWT.log("authToken=" + mywebapp.getAuthToken());
        contestRequest.setAuthToken(mywebapp.getAuthToken());
        //we don't init the check boxes when it's location only
        for (Long contestId : ratingMap.keySet()) {
            Rating rating = ratingMap.get(contestId);
            //only register if there was a vote
            Integer val = rating.getValue();
            if (val > 0) {
                ContestEntryHolder ceh = new ContestEntryHolder();
                ceh.setContestId(contestId);
                ceh.setRating(val);
                ceh.setVoteForId(voteForId);
                contestRequest.getContestEntryHolders().add(ceh);
            }
        }
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.vote(contestRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                //postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Thanks for voting");
                } else {
                    //postDialog.hide();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected void displayContests(QueryResponse contestQueryResponse, boolean showRating, Long voteForId) {
        if (contestQueryResponse.getResults().isEmpty()) {
            return;
        }
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        ul.addStyleName("contestresults");
        add(ul);
        for (SolrDocument solrDocument : contestQueryResponse.getResults()) {
            FlowPanel hp = new FlowPanel();
            hp.setWidth("100%");
            HorizontalPanel hp2 = new HorizontalPanel();
            hp2.setWidth("100%");
            hp2.add(hp);
            //HorizontalPanel hp = new HorizontalPanel();
            //hp.setStyleName("resultholder");
            FlowPanel middleTable = new FlowPanel();
            middleTable.setStyleName("middletable");
            ListItem li = new ListItem();
            //li.setStyleName("clearing");
            li.add(hp2);
            ul.add(li);
            String name = solrDocument.getFirstString("name");
            Long contestId = solrDocument.getFirstLong("contestid_l");
            String token = MyWebApp.CONTEST_DETAIL + contestId;
            Hyperlink nameLabel = new Hyperlink(name, token);
            nameLabel.setStyleName("nameLabel");
            // addContestLink(nameLabel, solrDocument);
            // nameLabel.addClickHandler(viewContest);
            String imgUrl = solrDocument.getFirstString("image_thumbnail_130x130_url_s");
            if (imgUrl != null) {
                String fullUrl = mywebapp.getUrl(imgUrl);
                Image image = new Image(fullUrl);
                image.addStyleName("imgholder");
                Hyperlink imageLink = new Hyperlink("", token);
                imageLink.getElement().appendChild(image.getElement());
                hp.add(imageLink);
                //addContestLink(image, solrDocument);
                //image.addClickHandler(viewContest);
            }
            middleTable.add(nameLabel);
            String desc = solrDocument.getFirstString("description_s");
            Hyperlink descLabel = new Hyperlink(desc, token);
            middleTable.add(descLabel);
            descLabel.setStyleName("descLabel");
            addViewVotingResults(middleTable, solrDocument);
            hp.add(middleTable);
            if (showRating) {
                Rating rating = getRating(solrDocument, voteForId);
                hp.add(rating);
            }
        }
    }

    protected Map<Widget, ProductHolder> productClickMap = new HashMap<Widget, ProductHolder>();
    protected Map<Widget, ProductInstallHolder> productInstallClickMap = new HashMap<Widget, ProductInstallHolder>();
    protected ClickHandler pickProductInstallHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                ProductInstallHolder productInstallHolder = productInstallClickMap.get(b);
                ProductInstallPanel pih = new ProductInstallPanel(mywebapp, productInstallHolder);
                mywebapp.swapCenter(pih);
            }
        }
    };
//    ClickHandler mostVotesHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            GWT.log("mostVotesHandler onClick");
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                Widget b = (Widget) sender;
//                SolrDocument solrDocument = contestsClickMap.get(b);
//                Long contestId = solrDocument.getFirstLong("contestid_l");
//                mywebapp.toggleContestTotalVotes(contestId);
//
//
//            }
//        }
//    };
//    ClickHandler viewAverageVoteHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            GWT.log("viewAverageVoteHandler onClick");
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                Widget b = (Widget) sender;
//                SolrDocument solrDocument = contestsClickMap.get(b);
//                Long contestId = solrDocument.getFirstLong("contestid_l");
//                //String sortKey = "contestAverageVote_" + contestId + "_d";
//                mywebapp.toggleContestAverageVotes(contestId);
//            }
//        }
//    };

    protected void addViewVotingResults(Panel panel, SolrDocument solrDocument) {
        Long contestId = solrDocument.getFirstLong("contestid_l");
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("viewVoteLinks");
        //Label highestLabel = new Label("View the Winners");
        String token = MyWebApp.CONTEST_AVERAGE_VOTES + contestId;
        Hyperlink highestLabel = new Hyperlink("View the Winners", token);
        Image starImage = new Image(mywebapp.resources.star());
        highestLabel.getElement().appendChild(starImage.getElement());
        //highestLabel.addClickHandler(viewAverageVoteHandler);
        //contestsClickMap.put(highestLabel, solrDocument);
        flowPanel.add(highestLabel);
        String token2 = MyWebApp.CONTEST_TOTAL_VOTES + contestId;
        Hyperlink mostVotesLabel = new Hyperlink("View Who Has The Most Votes", token2);
        Image starImage2 = new Image(mywebapp.resources.star());
        mostVotesLabel.getElement().appendChild(starImage2.getElement());
        flowPanel.add(mostVotesLabel);
        //flowPanel.add(panel);
        panel.add(flowPanel);
    }
    //   protected Map<Widget, SolrDocument> contestsClickMap = new HashMap<Widget, SolrDocument>();
//
//    protected void addContestLink(Widget widget, SolrDocument solrDocument) {
//        widget.addStyleName("linky");
//        contestsClickMap.put(widget, solrDocument);
//    }
//    public ClickHandler viewContest = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                Widget b = (Widget) sender;
//                SolrDocument solrDocument = contestsClickMap.get(b);
//                ViewContestPanel viewContestPanel = new ViewContestPanel(mywebapp, solrDocument);
//                mywebapp.swapCenter(viewContestPanel);
//            }
//        }
//    };

    protected void addButtons() {
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("80%");
        hp.setStyleName("messageButtons");
        hp.add(sendButton());
        hp.add(saveButton());
        hp.add(discardButton());
        add(hp);
    }

    Label sendButton() {
        Label btn = new Label("Send");
        btn.addClickHandler(sendHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    public void sendMessage() {
        getItemHolder().setSend(true);
        saveIt();
    }

    Label discardButton() {
        Label btn = new Label("Discard");
        btn.addClickHandler(cancelHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler sendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            // need to set the hidden send variable to true
            //sendHidden.setValue("true");
            //uploadMediaFiles();
            //formPanel.submit();
            getItemHolder().setSend(true);
            saveHandler.onClick(event);
        }
    };
    private ItemHolder itemHolder2 = null;

    public void showAvailableUserGroups(List<UserGroupHolder> allGroupHolders, List<UserGroupHolder> selectedGroupHolders, ClickHandler addGroupHandler) {
        GWT.log("showFriends");
        // GWT.log("there are friends " + friendHolders.size());
        //List<UserGroupHolder> groupsList = new ArrayList<UserGroupHolder>();
        List<UserGroupHolder> filteredGroups = new ArrayList<UserGroupHolder>();
        for (UserGroupHolder ugh : allGroupHolders) {
            if (!inList(selectedGroupHolders, ugh)) {
                filteredGroups.add(ugh);
            }
        }
        // groupsList.removeAll(selectedGroupHolders);
        // need to filter to just accepted friends
//        for (UserGroupHolder groupHolder : groupsList) {
//            filteredGroups.add(groupHolder);
//        }
        for (UserGroupHolder groupHolder : filteredGroups) {
            Label label = new Label(groupHolder.getName());
            label.setStyleName("notgwt");
            availableFriendsAndGroupsPanel.add(label);
            ImageResource addImageIR = MyWebApp.resources.add();
            Image addImage = new Image(addImageIR);
            availableFriendsAndGroupsPanel.add(addImage);
            addImage.setTitle("Add Group");
            addImage.addClickHandler(addGroupHandler);
            clickMapUserGroupHolder.put(addImage, groupHolder);
        }
    }

    public void showAvailableGroups(List<GroupHolder> allGroupHolders, List<GroupHolder> selectedGroupHolders, ClickHandler addGroupHandler) {
        GWT.log("showFriends");
        // GWT.log("there are friends " + friendHolders.size());
        List<GroupHolder> groupsList = new ArrayList(allGroupHolders);
        List<GroupHolder> filteredGroups = new ArrayList<GroupHolder>();
        groupsList.removeAll(selectedGroupHolders);
        // need to filter to just accepted friends
        for (GroupHolder groupHolder : groupsList) {
            filteredGroups.add(groupHolder);
        }
        for (GroupHolder groupHolder : filteredGroups) {
            Label label = new Label(groupHolder.getName());
            label.setStyleName("notgwt");
            availableFriendsAndGroupsPanel.add(label);
            ImageResource addImageIR = MyWebApp.resources.add();
            Image addImage = new Image(addImageIR);
            availableFriendsAndGroupsPanel.add(addImage);
            addImage.setTitle("Add Group");
            addImage.addClickHandler(addGroupHandler);
            clickMapGroupHolder.put(addImage, groupHolder);
        }
    }

    protected boolean inList(List<FriendHolder> selectedFriendHolders, FriendHolder fh) {
        for (FriendHolder friendHolder : selectedFriendHolders) {
            if (friendHolder.getKey().equals(fh.getKey())) {
                return true;
            }
        }
        return false;
    }

    private boolean inList(List<UserGroupHolder> selectedFriendHolders, UserGroupHolder userGroupHolder) {
        for (UserGroupHolder ugh : selectedFriendHolders) {
            if (ugh.getId().equals(userGroupHolder.getId())) {
                return true;
            }
        }
        return false;
    }

    public ItemHolder getItemHolder() {
        return itemHolder2;
    }

    public void setItemHolder(ItemHolder itemHolder) {
        this.itemHolder2 = itemHolder;
    }

    public void refresh() {
        selectedFriendsAndGroupsPanel.clear();
        clickMapFriendHolder.clear();
        clickMapGroupHolder.clear();
        clickMapUserGroupHolder.clear();
        if (getItemHolder() == null) {
            getMessagePanel().displayError("It's null");
        }
        showSelectedFriends(getItemHolder().getFriendHolders(), removeFriendFromItemHandler);
        showSelectedGroups(getItemHolder().getGroupHolders(), removeGroupFromItemHandler);
        showSelectedUserGroups(getItemHolder().getUserGroupHolders(), removeUserGroupFromItemHandler);
        GWT.log("refreshAvailable");
        availableFriendsAndGroupsPanel.clear();
        showAvailableFriends(mywebapp.getFriendsAndGroups().getFriendHolders(), getItemHolder().getFriendHolders(), addFriendHandler);
        showAvailableGroups(mywebapp.getFriendsAndGroups().getGroupHolders(), getItemHolder().getGroupHolders(), addGroupHandler);
        showAvailableUserGroups(mywebapp.getFriendsAndGroups().getUserGroupHolders(), getItemHolder().getUserGroupHolders(), addUserGroupHandler);
    }

    protected ClickHandler removeFriendFromItemHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget widget = (Widget) sender;
                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
                List<FriendHolder> list = new ArrayList<FriendHolder>();
                list.addAll(getItemHolder().getFriendHolders());
                getItemHolder().getFriendHolders().clear();
                for (FriendHolder fh : getItemHolder().getFriendHolders()) {
                    if (fh.getKey().equals(friendHolder.getKey())) {
                        //this is the one we are removing
                    } else {
                        getItemHolder().getFriendHolders().add(fh);
                    }
                }
            }
            refresh();
        }
    };
    protected ClickHandler addFriendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("addFriendHandler");
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("addFriendHandler1");
                Widget widget = (Widget) sender;
                FriendHolder friendHolder = clickMapFriendHolder.get(widget);
                if (friendHolder == null) {
                    GWT.log("friendHolder is null");
                    return;
                }
                getItemHolder().getFriendHolders().add(friendHolder);
            }
            refresh();
        }
    };
    protected ClickHandler addGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget widget = (Widget) sender;
                GroupHolder groupHolder = clickMapGroupHolder.get(widget);
                getItemHolder().getGroupHolders().add(groupHolder);
            }
            refresh();
        }
    };
    protected ClickHandler addUserGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget widget = (Widget) sender;
                UserGroupHolder groupHolder = clickMapUserGroupHolder.get(widget);
                getItemHolder().getUserGroupHolders().add(groupHolder);
            }
            refresh();
        }
    };
    protected ClickHandler removeUserGroupFromItemHandler = new ClickHandler() {
        public void onClick(ClickEvent clickEvent) {
            getMessagePanel().clear();
            Object sender = clickEvent.getSource();
            if (sender instanceof Widget) {
                Widget widget = (Widget) sender;
                UserGroupHolder groupHolder = clickMapUserGroupHolder.get(widget);
                List<UserGroupHolder> list = new ArrayList<UserGroupHolder>();
                list.addAll(getItemHolder().getUserGroupHolders());
                GWT.log("clearing usergroupholders");
                getItemHolder().getUserGroupHolders().clear();
                for (UserGroupHolder fh : list) {
                    if (fh.getId().equals(groupHolder.getId())) {
                        //this is the one we are removing
                        //so let's not add it
                        GWT.log("found the one we need to skip!!");
                    } else {
                        getItemHolder().getUserGroupHolders().add(fh);
                    }
                }
            }
            refresh();
        }
    };
    protected ClickHandler removeGroupFromItemHandler = new ClickHandler() {
        public void onClick(ClickEvent clickEvent) {
            getMessagePanel().clear();
            Object sender = clickEvent.getSource();
            if (sender instanceof Widget) {
                Widget widget = (Widget) sender;
                GroupHolder groupHolder = clickMapGroupHolder.get(widget);
                List<GroupHolder> list = new ArrayList<GroupHolder>();
                if (getItemHolder() == null) {
                    GWT.log("itemHolder is null");
                }
                list.addAll(getItemHolder().getGroupHolders());
                getItemHolder().getGroupHolders().clear();
                for (GroupHolder fh : list) {
                    if (fh == null) {
                        GWT.log("fh is null");
                    }
                    if (groupHolder == null) {
                        GWT.log("groupHolder is null");
                    }
                    if (fh.getId().equals(groupHolder.getId())) {
                        //this is the one we are removing
                        //so let's not add it
                    } else {
                        getItemHolder().getGroupHolders().add(fh);
                    }
                }
            }
            refresh();
        }
    };

    public void showAvailableFriends(List<FriendHolder> allFriendHolders, List<FriendHolder> selectedFriendHolders, ClickHandler addFriendHandler) {
        GWT.log("showFriends");
        List<FriendHolder> friendsList = new ArrayList<FriendHolder>();
        for (FriendHolder friendHolder : allFriendHolders) {
            if (!inList(selectedFriendHolders, friendHolder)) {
                friendsList.add(friendHolder);
            }
        }
        for (FriendHolder friendHolder : friendsList) {
            Label label = new Label(friendHolder.getLabel());
            label.setStyleName("notgwt");
            availableFriendsAndGroupsPanel.add(label);
            ImageResource addImageIR = MyWebApp.resources.add();
            Image addImage = new Image(addImageIR);
            availableFriendsAndGroupsPanel.add(addImage);
            addImage.setTitle("Add Friend");
            addImage.addStyleName("linky");
            addImage.addClickHandler(addFriendHandler);
            clickMapFriendHolder.put(addImage, friendHolder);
        }
    }

    protected Map<Widget, UserGroupHolder> clickMapUserGroupHolder = new HashMap<Widget, UserGroupHolder>();
    protected Map<Widget, GroupHolder> clickMapGroupHolder = new HashMap<Widget, GroupHolder>();
    protected Map<Widget, FriendHolder> clickMapFriendHolder = new HashMap<Widget, FriendHolder>();
    protected Map<Widget, TagHolder> clickMapTagHolder = new HashMap<Widget, TagHolder>();
    protected FlowPanel selectedFriendsAndGroupsPanel = new FlowPanel();

    {
        selectedFriendsAndGroupsPanel.setStyleName("selectedFriendsAndGroupsPanel");
    }

    protected FlowPanel availableFriendsAndGroupsPanel = new FlowPanel();

    {
        availableFriendsAndGroupsPanel.setStyleName("availableFriendsAndGroupsPanel");
    }

    protected FlowPanel selectedMembersPanel = new FlowPanel();

    public void showSelectedUserGroups(List<UserGroupHolder> selectedGroupHolders, ClickHandler clickHandler) {
        // clickMapGroupHolder.clear();
        for (UserGroupHolder groupHolder : selectedGroupHolders) {
            GWT.log("UserGroupHolder" + groupHolder.getId());
            Label label = new Label(groupHolder.getName());
            selectedFriendsAndGroupsPanel.add(label);
            if (clickHandler != null) {
                ImageResource deleteImageIR = MyWebApp.resources.deleteX();
                Image deleteImage = new Image(deleteImageIR);
                selectedFriendsAndGroupsPanel.add(deleteImage);
                deleteImage.addClickHandler(clickHandler);
                deleteImage.setTitle("Remove Group");
                deleteImage.addStyleName("linky");
                clickMapUserGroupHolder.put(deleteImage, groupHolder);
            }
        }
    }

    public void showSelectedGroups(List<GroupHolder> selectedGroupHolders, ClickHandler clickHandler) {
        //selectedGroupsPanel.clear();
        //clickMapGroupHolder.clear();
        for (GroupHolder groupHolder : selectedGroupHolders) {
            Label label = new Label(groupHolder.getName());
            selectedFriendsAndGroupsPanel.add(label);
            if (clickHandler != null) {
                ImageResource deleteImageIR = MyWebApp.resources.deleteX();
                Image deleteImage = new Image(deleteImageIR);
                selectedFriendsAndGroupsPanel.add(deleteImage);
                deleteImage.addClickHandler(clickHandler);
                deleteImage.setTitle("Remove Group");
                deleteImage.addStyleName("linky");
                clickMapGroupHolder.put(deleteImage, groupHolder);
            }
        }
    }

    //handler could be null, in case we do not do a delete or remove link
    public void showSelectedFriends(List<FriendHolder> selectedFriendHolders, ClickHandler clickHandler) {
        //selectedFriendsPanel.clear();
        // clickMapFriendHolder.clear();
        for (FriendHolder friendHolder : selectedFriendHolders) {
            Label label = new Label(friendHolder.getLabel());
            label.setStyleName("notgwt");
            selectedFriendsAndGroupsPanel.add(label);
            if (clickHandler != null) {
                ImageResource deleteImageIR = MyWebApp.resources.deleteX();
                Image deleteImage = new Image(deleteImageIR);
                selectedFriendsAndGroupsPanel.add(deleteImage);
                deleteImage.addClickHandler(clickHandler);
                deleteImage.setTitle("Remove Friend");
                clickMapFriendHolder.put(deleteImage, friendHolder);
            }
        }
    }

    protected void addImageIcon(String token, SpotBasePanel spotBasePanel, FlowPanel flowPanel) {
        addImageIcon(token, spotBasePanel.getTitle(), spotBasePanel.getImage(), flowPanel, spotBasePanel.getTitle());
    }

    //text is the anchor text, and token is the href
    protected void addImageIcon(String text, String token, ImageResource bigImage, ImageResource mobileImage, FlowPanel flowPanel, String tooltip) {
        if (MyWebApp.isSmallFormat()) {
            Image image = new Image(mobileImage);
            addImageIcon(token, text, image, flowPanel, tooltip);
        } else {
            Image image = new Image(bigImage);
            addImageIcon(token, text, image, flowPanel, tooltip);
        }
    }

    protected void addImageIcon(ClickHandler clickHandler, SpotBasePanel spotBasePanel, FlowPanel flowPanel) {
        addImageIcon(clickHandler, spotBasePanel.getTitle(), spotBasePanel.getImage(), flowPanel, spotBasePanel.getTitle());
    }

    protected void addImageIcon(ClickHandler clickHandler, String text, ImageResource bigImage, ImageResource mobileImage, FlowPanel flowPanel, String tooltip) {
        if (MyWebApp.isSmallFormat()) {
            Image image = new Image(mobileImage);
            addImageIcon(clickHandler, text, image, flowPanel, tooltip);
        } else {
            Image image = new Image(bigImage);
            addImageIcon(clickHandler, text, image, flowPanel, tooltip);
        }
    }

    protected void addImageIcon(ClickHandler clickHandler, String text, Image bigImage, FlowPanel flowPanel, String tooltip) {
        if (tooltip == null) {
            tooltip = text;
        }
        ImageIcon imageIcon = new ImageIcon(bigImage, text, clickHandler, tooltip);
        if (MyWebApp.isSmallFormat()) {
            imageIcon.addStyleName("SmallIcon");
        } else {
            imageIcon.addStyleName("BigIcon");
        }
        imageIcon.addStyleName("menuimage");
        //int row, int column,
        flowPanel.add(imageIcon);
    }

    protected void addImageIcon(String token, String text, Image bigImage, FlowPanel flowPanel, String tooltip) {
        if (tooltip == null) {
            tooltip = text;
        }
        ImageIcon imageIcon = new ImageIcon(bigImage, text, token, tooltip);
        if (MyWebApp.isSmallFormat()) {
            imageIcon.addStyleName("SmallIcon");
        } else {
            imageIcon.addStyleName("BigIcon");
        }
        imageIcon.addStyleName("menuimage");
        //int row, int column,
        flowPanel.add(imageIcon);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        if (image == null) {
            return new Image(getImageResource());
        }
        return image;
    }

    private ImageResource imageResource = MyWebApp.resources.spot_image_placeholder130x130();
    private ImageResource imageResourceMobile = MyWebApp.resources.spot_image_placeholder57x57();

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return imageResourceMobile;
        } else {
            return imageResource;
        }
    }
//    public void setImageResourceMobile(ImageResource imageResourceMobile) {
//        this.imageResourceMobile = imageResourceMobile;
//    }

    public void setImageResources(ImageResource imageResource, ImageResource imageResourceMobile) {
        this.imageResource = imageResource;
        this.imageResourceMobile = imageResourceMobile;
    }

    private TextResource helpTextResource = HelpResources.INSTANCE.getHelp();

    public TextResource getHelpTextResource() {
        return helpTextResource;
    }

    protected void checkRequired(SuggestBox textBox, String message) {
        //mywebapp.log("checkRequired message=" + message);
        if (textBox == null) return;
        if (textBox.getValue() == null || textBox.getValue().length() == 0) {
            getMessagePanel().displayError(message);
        }
    }

    protected void checkRequired(TextBoxBase textBox, String message) {
        //mywebapp.log("checkRequired message=" + message);
        if (textBox == null) return;
        if (textBox.getValue() == null || textBox.getValue().length() == 0) {
            getMessagePanel().displayError(message);
        }
    }


    protected boolean isNotEmpty(TextBoxBase textBox) {
        return ! isEmpty(textBox.getValue());
    }

    protected boolean isEmpty(TextBoxBase textBox) {
        return isEmpty(textBox.getValue());
    }

    protected boolean isEmpty(ListBox listBox) {
        if (listBox.getSelectedIndex() == -1) {
            return true;
        }
        return false;
    }


    protected boolean isEmpty(String val) {
        if (val == null || val.length() == 0) {
            return true;
        } else if (val.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    protected SimpleCheckBox geoFilterCheckbox = null;
    protected SimpleCheckBox contestFilterCheckbox = null;
    protected SimpleCheckBox plateFilterCheckbox = null;
    protected SimpleCheckBox markFilterCheckbox = null;
    protected SimpleCheckBox spotFilterCheckbox = null;
    ClickHandler removeTagHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            GWT.log("removeTagHandler onClick");
            if (sender instanceof Widget) {
                Widget widget = (Widget) sender;
                SuggestBox suggestBox = widgetTagHoldersMap2.get(widget);
                TagHolder tagHolder = clickMapTagHolder.get(widget);
                List<TagHolder> tagHolders = widgetTagHoldersMap.get(suggestBox);
                tagHolders.remove(tagHolder);
                resetTagBox(suggestBox);
            }
        }
    };

    protected MessagePanel getMessagePanel() {
        return mywebapp.getMessagePanel();
    }

    protected void addGroupsHeader(SpotHolder spotHolder) {
        if (spotHolder != null) {
            Label label = new Label("Groups @" + spotHolder.getName());
            label.setStyleName("h1");
            add(label);
        }
    }

    private void resetTagBox(SuggestBox suggestBox) {
        GWT.log("resetTagBox");
        FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(suggestBox);
        selectedTagsPanel.clear();
        Element tagStatusElement = DOM.getElementById("tag-status");
        Element seltagsElement = DOM.getElementById("seltags");
        List<TagHolder> tagHolders = widgetTagHoldersMap.get(suggestBox);
        //seltagsElement should only be visible if there are tags
        if (tagHolders == null) {
            GWT.log("tagHolders is null");
            if (seltagsElement != null) {
                seltagsElement.addClassName("hideme");
            }
            if (tagStatusElement != null) {
                tagStatusElement.removeClassName("hideme");
            }
        } else if (tagHolders.isEmpty()) {
            GWT.log("tagHolders is empty");
            if (tagStatusElement != null) {
                tagStatusElement.removeClassName("hideme");
            }
            if (seltagsElement != null) {
                seltagsElement.addClassName("hideme");
            }
        } else {
            GWT.log("adding tags");
            if (tagStatusElement != null) {
                tagStatusElement.addClassName("hideme");
            }
            if (seltagsElement != null) {
                seltagsElement.removeClassName("hideme");
            }
            for (TagHolder tagHolder : tagHolders) {
                InlineLabel tagLabel = new InlineLabel(tagHolder.getName());
                tagLabel.setStyleName("ma_sel_tag");
                selectedTagsPanel.add(tagLabel);
                InlineLabel deleteInlineLabel = new InlineLabel("x");
                deleteInlineLabel.setStyleName("kill-tag");
                deleteInlineLabel.addClickHandler(removeTagHandler);
                selectedTagsPanel.add(deleteInlineLabel);
                clickMapTagHolder.put(deleteInlineLabel, tagHolder);
                widgetTagHoldersMap2.put(deleteInlineLabel,suggestBox);
            }
        }
        //todo, check out if we should ever have a null tagHolders
        if (tagHolders != null) {
            //once we get to five tags, shut down ui
            if (tagHolders.size() >= 5) {
                hideElement(suggestBox.getElement());
            } else {
                suggestBox.getElement().removeAttribute("style");
            }
        }



    }

    protected Label searchButton() {
        Label btn = new Label("Search");
        btn.addClickHandler(searchHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    protected ClickHandler searchHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            performSearch();
        }
    };

    public void performSearch() {
        //need to change the URL so we can go back to search form
        History.newItem(MyWebApp.SEARCH_RESULTS);
        getMessagePanel().clear();
        if (keywordsTextBox.getValue().isEmpty()) {
            getMessagePanel().displayMessage("Search term is required.");
            return;
        }
        mywebapp.getResultsPanel().resetSearchParameters();
        //keyword search does not restrict to spots
        mywebapp.addCurrentLocation();
        if (markFilterCheckbox != null) {
            mywebapp.getResultsPanel().getSearchParameters().setLicensePlate(markFilterCheckbox.getValue());
        }
        if (contestFilterCheckbox != null) {
            mywebapp.getResultsPanel().getSearchParameters().setContests(contestFilterCheckbox.getValue());
        }
        if (geoFilterCheckbox != null) {
            mywebapp.getResultsPanel().getSearchParameters().setGeospatialOff(!geoFilterCheckbox.getValue());
        }
        if (plateFilterCheckbox != null) {
            mywebapp.getResultsPanel().getSearchParameters().setLicensePlate(plateFilterCheckbox.getValue());
        }
        if (spotFilterCheckbox != null) {
            mywebapp.getResultsPanel().getSearchParameters().setSpots(spotFilterCheckbox.getValue());
        }
        String color = getValue(colorsListBox);
        mywebapp.getResultsPanel().getSearchParameters().setColor(color);
        String manufacturer = getValue(manufacturersListBox);
        if (manufacturer != null) {
            Long id = new Long(manufacturer);
            mywebapp.getResultsPanel().getSearchParameters().setManufacturerId(id);
        }
        String vehicleType = getValue(vehicleTypeListBox);
        mywebapp.getResultsPanel().getSearchParameters().setVehicleType(vehicleType);
//        for (TagHolder tagHolder : tagHolders) {
//            mywebapp.getResultsPanel().getSearchParameters().getTags().add(tagHolder);
//        }
        mywebapp.getResultsPanel().getSearchParameters().setKeywords(keywordsTextBox.getValue());
        mywebapp.getMessagePanel().clear();
        mywebapp.getResultsPanel().performSearch();
        mywebapp.getTopMenuPanel().setTitleBar("Search");
        mywebapp.getResultsPanel().setImageResources(resources.search(), resources.searchMobile());
    }

    protected TextField keywordsTextBox = new TextField();

    /*
    the following addHelp and addTopPanel are used to add
    the help and toppanel to each base class
    sometimes, we will clear the panel and reuse it, if we
    do this, we need to restore the panel and that is why
    we use the class vars
     */
    protected void addHelp() {
        if (!displayHelp) return;
        HelpPanel helpPanel = new HelpPanel(mywebapp, this);
        add(helpPanel);
    }

    protected void addTopPanel() {
//        if (MyWebApp.isDesktop()) {
//            return;
//        }
        if (!displayTopPanel) return;
        if (MyWebApp.isDesktop()) {
            //our desktop already has tons of links in the ui
            return;
        }
        if (!MyWebApp.isSmallFormat()) {
            add(getTopPanel());
        }
    }

    protected void addSearchFormOrig() {
        //FlexTable hp  = new FlexTable();
        //FlowPanel flowPanel = new FlowPanel();
        //flowPanel.setStyleName("topsearch");
        Fieldset fs = new Fieldset();
        fs.addStyleName("globalsearch");
        keywordsTextBox = new TextField();
        final String text = "Find People, Places, and Stuff";
        keywordsTextBox.setText(text);
        //hp.getFlexCellFormatter().setColSpan(0, 1, 2);
        //hp.setWidget(0, 1, keywordsTextBox);
        FocusHandler focusHandler = new FocusHandler() {
            public void onFocus(FocusEvent event) {
                if (keywordsTextBox.getValue().equals(text)) {
                    keywordsTextBox.setText("");
                }
            }
        };
        // Listen for keyboard events in the input box.
        keywordsTextBox.addKeyPressHandler(new KeyPressHandler() {
            public void onKeyPress(KeyPressEvent event) {
                if (event.getCharCode() == KeyCodes.KEY_ENTER) {
                    searchHandler.onClick(null);
                }
            }
        });
        keywordsTextBox.addFocusHandler(focusHandler);
        Label btn = new Label("Search");
        //btn.setStyleName("button");
        fixButton(btn);
        // btn.addClickHandler(cancelHandler);
        ImageResource ir = null;
        if (MyWebApp.isSmallFormat()) {
            ir = MyWebApp.resources.searchButtonSmall();
        } else {
            ir = MyWebApp.resources.searchButton();
        }
        Image img = new Image(ir);
        btn.getElement().appendChild(img.getElement());
        btn.addClickHandler(searchHandler);
        fs.add(keywordsTextBox);
        fs.add(btn);
        if (!MyWebApp.isSmallFormat()) {
            //let's not put a search form
            add(fs);
        }
        addHelp();
    }
    //public SuggestBox tagSearchTextBox = null;
    //http://thinkbelievedo.com/gwt-suggestbox-with-dto-or-pojos

    public class CitySuggestOracle extends SuggestOracle {
        public boolean isDisplayStringHTML() {
            return false;
        }

        public void requestSuggestions(SuggestOracle.Request req, SuggestOracle.Callback callback) {
            //IQuoteService.Util.getInstance().getQuote(req, new TagSuggestCallback(req, callback));
            //TagSuggestCallback tagSuggestCallback = new TagSuggestCallback(req, callback);
            fetchPostalCodes(req, callback);
        }

        private void fetchPostalCodes(final SuggestOracle.Request req, final SuggestOracle.Callback callback) {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setDistinctCitiesOnly(true);
            searchRequest.getSearchParameters().setState(stateTextBox.getText());
            searchRequest.setQuery(req.getQuery());
            ApiServiceAsync myService = mywebapp.getApiServiceAsync();
            myService.searchPostalCodes(searchRequest, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    mywebapp.getMessagePanel().displayError(caught.getMessage());
                }

                public void onSuccess(Object result) {
                    MobileResponse mobileResponse = (MobileResponse) result;
                    if (mobileResponse.getStatus() == 1) {
                        List<MultiWordSuggestion> list = new ArrayList<MultiWordSuggestion>();
                        for (PostalCodeHolder pch : mobileResponse.getPostalCodeHolders()) {
                            MultiWordSuggestion mws = new MultiWordSuggestion(pch.getCityName(), pch.getCityName());
                            list.add(mws);
                        }
                        Response resp = new Response(list);
                        callback.onSuggestionsReady(req, resp);
                    } else {
                        mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                    }
                }
            });
        }
    }

    public class TagSuggestOracle extends SuggestOracle {
        public boolean isDisplayStringHTML() {
            return false;
        }

        public void requestSuggestions(SuggestOracle.Request req, SuggestOracle.Callback callback) {
            //IQuoteService.Util.getInstance().getQuote(req, new TagSuggestCallback(req, callback));
            //TagSuggestCallback tagSuggestCallback = new TagSuggestCallback(req, callback);
            fetchTags(req, callback);
        }

        private void fetchTags(final SuggestOracle.Request req, final SuggestOracle.Callback callback) {
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.setQuery(req.getQuery());
            ApiServiceAsync myService = mywebapp.getApiServiceAsync();
            myService.searchtags(searchRequest, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    mywebapp.getMessagePanel().displayError(caught.getMessage());
                }

                public void onSuccess(Object result) {
                    MobileResponse mobileResponse = (MobileResponse) result;
                    if (mobileResponse.getStatus() == 1) {
                        List<MultiWordSuggestion> list = new ArrayList<MultiWordSuggestion>();
                        for (TagHolder tagHolder : mobileResponse.getTagHolders()) {
                            MultiWordSuggestion mws = new MultiWordSuggestion(tagHolder.getName(), tagHolder.getName());
                            list.add(mws);
                        }
                        Response resp = new Response(list);
                        callback.onSuggestionsReady(req, resp);
                    } else {
                        mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                    }
                }
            });
        }
    }

    //this form should display selected tags with remove option for the tag
    //and a tag search to add to the tags list
    //http://thinkbelievedo.com/gwt-suggestbox-with-dto-or-pojos
    protected void addTagHolderForm(List<TagHolder> tagHolders) {
        SuggestBox suggestBox = initTagHolderForm(tagHolders);
        //let's add a "link"
        Label addTagLabel = new Label("Add Tag");
        addTagLabel.setStyleName("button");
        addTagLabel.addClickHandler(addTagHandler);
        FlowPanel fp = new FlowPanel();
        fp.setWidth("100%");
        //fp.add(tagSearchTextBox);
        //FlowPanel fp2 = new FlowPanel();
        //fp.add(addTagLabel);
        // fp.add(fp2);
        fp.add(addTagLabel);
        //add(tagSearchTextBox);
        addFieldset(fp, "Tag Search", "tagsearch");
        FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(suggestBox);
        addFieldset(selectedTagsPanel, "Selected Tags", "tag");
    }

    protected SuggestBox getTagSuggestBox(List<TagHolder> tagHolders) {
        if (tagHolders == null) {
            tagHolders = new ArrayList<TagHolder>();
        }
        SuggestBox suggestBox = initTagHolderForm(tagHolders);
        return suggestBox;
    }

    protected SuggestBox initTagHolderForm(List<TagHolder> tagHolders) {
        TagSuggestOracle oracle = new TagSuggestOracle();
        FlowPanel suggestionsPanel = new FlowPanel();

        SuggestBox tagSearchTextBox = new SuggestBox(oracle, new TextBox(),new CustomSuggestionDisplay(suggestionsPanel));
        //autocomplete="off"
        tagSearchTextBox.getElement().setPropertyString("autocomplete","off");
        tagSearchTextBox.getElement().setAttribute("placeholder", "Start typing");


        tagSearchTextBox.addSelectionHandler(tagSelectionHandler);
        FlowPanel selectedTagsPanel = new FlowPanel();
        selectedTagsPanel.setStyleName("ma_tags");
        widgetSelectedTagsPanelMap.put(tagSearchTextBox, selectedTagsPanel);

        widgetSelectedTagsPanelMap2.put(tagSearchTextBox, suggestionsPanel);




        //why are we creating a new object of tagHolders here?
        //tagHolders = new ArrayList<TagHolder>();
        widgetTagHoldersMap.put(tagSearchTextBox, tagHolders);
        resetTagBox(tagSearchTextBox);
        return tagSearchTextBox;
    }

    protected SuggestBox getCitySuggestBox(String cityName) {
        CitySuggestOracle oracle = new CitySuggestOracle();
        /*
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("Cat");
        oracle.add("Dog");
        oracle.add("Horse");
        oracle.add("Canary");
        */
        //let's add a "link"
        SuggestBox cityTextBox = new SuggestBox(oracle);
        //add(tagSearchTextBox);
        if ((cityName != null) && (cityName.length() > 0)) {
            cityTextBox.setText(cityName);
        } else if (mywebapp.getCurrentLocation() != null) {
            cityTextBox.setText(mywebapp.getCurrentLocation().getCity());
        } else {
            // cityTextBox.setText("US");
        }

        cityTextBox.getTextBox().addFocusHandler(focusHandler);

        return cityTextBox;
    }

    protected SuggestBox addCity(String cityName, Panel panel) {
        SuggestBox citySuggestBox = getCitySuggestBox(cityName);
        addFieldset(citySuggestBox, "City", "city", panel);
        return citySuggestBox;
    }

    protected SuggestBox addState(String state, Panel panel) {
        SuggestBox stateSuggestBox = initState(state);
        addFieldset(stateSuggestBox, "State", "stateSearch", panel);
        return stateSuggestBox;
    }

    protected SuggestBox getStateSuggestBox(String state) {
        SuggestBox stateSuggestBox = initState(state);
        return stateSuggestBox;
    }

    protected SuggestBox initState(String state) {
        GWT.log("addState");
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        for (StateProvinceHolder stateProvinceHolder : mywebapp.getStateProvinceHolders()) {
            oracle.add(stateProvinceHolder.getShortName());
            // oracle.add
        }
        SuggestBox stateSuggestBox = new SuggestBox(oracle);
        stateSuggestBox.getTextBox().addFocusHandler(focusHandler);
        if ((state != null) && (state.length() > 0)) {
            stateSuggestBox.setText(state);
        } else if (mywebapp.getCurrentLocation() != null) {
            stateSuggestBox.setText(mywebapp.getCurrentLocation().getState());
        } else {
            //stateSuggestBox.setText("US");
        }
        stateSuggestBox.getTextBox().addFocusHandler(focusHandler);
        return stateSuggestBox;
    }

    FocusHandler focusHandler = new FocusHandler() {
        public void onFocus(FocusEvent event) {
            Object obj = event.getSource();
          //  if (obj instanceof  SuggestBox) {
                //SuggestBox sb = (SuggestBox) obj;
            TextBox textBox = (TextBox) obj;
            textBox.selectAll();
           // }
        }
    };

    protected SuggestBox getCountrySuggestBox(String country) {
        GWT.log("getCountrySuggestBox " + country);
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        for (CountryHolder countryHolder : mywebapp.getCountryHolders()) {
            GWT.log("adding country " + countryHolder.getShortName());
            oracle.add(countryHolder.getShortName());
        }
        SuggestBox countrySuggestBox = new SuggestBox(oracle);
        countrySuggestBox.getTextBox().addFocusHandler(focusHandler);
        if ((country != null) && (country.length() > 0)) {
            countrySuggestBox.setText(country);
     //   } else if (mywebapp.getCurrentLocation() != null) {
     //       GWT.log("mywebapp.getCurrentLocation().getCountryCode()=" + mywebapp.getCurrentLocation().getCountryCode());
     //       countrySuggestBox.setText(mywebapp.getCurrentLocation().getCountryCode());
        } else {
            // GWT.log("mywebapp.getCurrentLocation().getCountryCode()=" + mywebapp.getCurrentLocation().getCountryCode());
            //countrySuggestBox.setText("US");
            countrySuggestBox.setText("Select Country");
        }
        return countrySuggestBox;
    }

    protected SuggestBox addCountry(String country, Panel panel) {
        SuggestBox suggestBox = getCountrySuggestBox(country);
        addFieldset(suggestBox, "Country", "countrySearch", panel);
        return suggestBox;
    }

    //public FlowPanel selectedTagsPanel = new FlowPanel();
    protected AsyncCallback callback = null;

    public void setCallback(AsyncCallback callback) {
        this.callback = callback;
    }
    //spotreadonly

    protected TextField addTextBoxNoMessing(String labelText, String name, String value) {
        TextField textbox = addTextBox(labelText, name, value, false);
        makeNoMessing(textbox);
        return textbox;
    }

    protected void makeNoMessing(TextBox textBox) {
        textBox.getElement().setAttribute("autocapitalize", "off");
        textBox.getElement().setAttribute("autocorrect", "off");
        textBox.getElement().setAttribute("autocomplete", "off");
    }

    protected TextField addTextBox(String labelText, String name, String value) {
        return addTextBox(labelText, name, value, false);
    }

    protected TextField addTextBox(String labelText, String name, String value, boolean spotreadonly) {
        return addTextBox(labelText, name, value, spotreadonly, this);
    }

    protected TextField addTextBox(String labelText, String name, String value, boolean spotreadonly, Panel panel) {
        TextField textBox = new TextField();
        textBox.setName(name);
        textBox.setValue(value);
        textBox.setReadOnly(spotreadonly);
        addFieldset(textBox, labelText, name, panel);
        return textBox;
    }

    protected void doPlate(SpotHolder spotHolder, boolean spotreadonly) {
        if (spotreadonly) {
            addSpotHeader(spotHolder);
            //let's add label for manufacturer
            addFieldset("Color", spotHolder.getColor());
            ManufacturerHolder manufacturerHolder = spotHolder.getManufacturerHolder();
            if (manufacturerHolder != null) {
                addFieldset("Manufacturer", manufacturerHolder.getName());
            }
            //color
            //vehicleType
            addFieldset("Vehicle Type", spotHolder.getVehicleType());
        } else {
            // name
            nameTextBox = addTextBox("Plate Number *", "name", spotHolder.getName(), spotreadonly);
            // cityTextBox = addTextBox("City", "city", spotHolder.getCity(), spotreadonly);
            citySuggestBox = addCity(spotHolder.getCity(), this);
            stateTextBox = addState(spotHolder.getState(), this);
            // stateTextBox.setMaxLength(2);
            addColorListBox(spotHolder.getColor());
            addManufacturersListBox(spotHolder.getManufacturerHolder());
            addVehicleType(spotHolder.getVehicleType());
        }
    }

    private List<String> getVehicleTypes() {
        List<String> list = new ArrayList<String>();
        list.add("Car");
        list.add("Truck");
        list.add("SUV");
        list.add("Van");
        list.add("Motorcycle");
        list.add("Other");
        return list;
    }

    protected void initVehicleType(String vehicleType) {
        List<String> types = getVehicleTypes();
        vehicleTypeListBox.addItem("Please choose...", "");
        for (String c : types) {
            vehicleTypeListBox.addItem(c);
        }
        int found = types.indexOf(vehicleType);
        if (found != -1) {
            vehicleTypeListBox.setSelectedIndex(found + 1);
        }
    }

    protected void addVehicleType(String vehicleType) {
        initVehicleType(vehicleType);
        addFieldset(vehicleTypeListBox, "Vehicle Type", "vType");
    }

    protected void initManufacturersListBox(ManufacturerHolder mh) {
        //this.manufacturersListBox = new ListBox();
        manufacturersListBox.addItem("Please choose...", "");
        int index = 1;
        for (ManufacturerHolder manuHolder : mywebapp.getManufacturerHolders()) {
            manufacturersListBox.addItem(manuHolder.getName(), manuHolder.getId().toString());
            if ((mh != null) && (mh.getId() != null)) {
                if (mh.getId().equals(manuHolder.getId())) {
                    manufacturersListBox.setSelectedIndex(index);
                }
            }
            index++;
        }
    }

    protected void addManufacturersListBox(ManufacturerHolder mh) {
        initManufacturersListBox(mh);
        addFieldset(manufacturersListBox, "Manufacturer", "manu");
    }

    private List<String> getColorList() {
        List<String> colorList = new ArrayList<String>();
        //colorList.add("Please choose...","");
        colorList.add("White");
        colorList.add("Black");
        colorList.add("Silver");
        colorList.add("Grey");
        colorList.add("Red");
        colorList.add("Blue");
        colorList.add("Brown/Beige");
        colorList.add("Yellow/Gold");
        colorList.add("Green");
        colorList.add("Other");
        return colorList;
    }

    protected void initColorListBox(String color) {
        List<String> colorList = getColorList();
        colorsListBox.addItem("Please choose...", "");
        for (String c : colorList) {
            colorsListBox.addItem(c);
        }
        int found = colorList.indexOf(color);
        if (found != -1) {
            colorsListBox.setSelectedIndex(found + 1);
        }
    }

    protected void addColorListBox(String color) {
        initColorListBox(color);
        addFieldset(colorsListBox, "Color", "color");
    }

    public ListBox vehicleTypeListBox = new ListBox();
    public ListBox manufacturersListBox = new ListBox();
    public ListBox colorsListBox = new ListBox();

    protected void addSpotHeader(SpotHolder spotHolder) {
        if (spotHolder == null) {
            return;
        }
        Label addressLabel = new Label(spotHolder.getGeocodeInput());
        addFieldset(addressLabel, spotHolder.getName(), "x");
        if (!isEmpty(spotHolder.getVoicephone())) {
            Fieldset fieldset = new Fieldset();
            addPhone(spotHolder.getVoicephone(), fieldset);
            add(fieldset);
        }
    }

    protected void doPlace(SpotHolder spotHolder, boolean spotreadonly) {
        if (spotreadonly) {
            addSpotHeader(spotHolder);
        } else {
            nameTextBox = addTextBox("Name", "name", spotHolder.getName(), spotreadonly);
            address1TextField = addTextBox("Address Line #1", "addressLine1", spotHolder.getAddressLine1(), spotreadonly);
            citySuggestBox = addCity(spotHolder.getCity(), this);
            stateTextBox = addState(spotHolder.getState(), this);
            //addTextBox("State", "state", spotHolder.getState(), spotreadonly);
            zipcodeTextField = addTextBox("Postal Code", "zip", spotHolder.getZip(), spotreadonly);
        }
    }

    protected void addFieldset(String prompt, String value) {
        if (value == null) {
            return;
        }
        HTML label = new HTML();
        label.setHTML(value);
        addFieldset(label, prompt, "na", this);
    }

    protected Fieldset addFieldset(Widget widget, String labelText, String name) {
        return addFieldset(widget, labelText, name, this);
    }

    protected Fieldset addFieldset(Widget widget, String labelText, String name, Panel addToPanel) {
        DOM.setElementAttribute(widget.getElement(), "id", name);
        //textBox.setStylePrimaryName(name);
        Fieldset fs = new Fieldset();
        HTML label = new HTML();
        label.addStyleName("gwtl");
        label.setHTML("<label for='" + name + "'>" + labelText + "</label>");
        fs.add(label);
        fs.add(widget);
        addToPanel.add(fs);
        return fs;
    }

    protected ClickHandler addTagHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            SuggestBox tagSearchTextBox = (SuggestBox) event.getSource();
            String tagName = tagSearchTextBox.getValue();
            GWT.log("addTagHandler=" + tagName);

            List<TagHolder> tagHolders = widgetTagHoldersMap.get(tagSearchTextBox);

            if ((tagName != null) && (tagName.length() > 0)) {
                TagHolder tagHolder = new TagHolder(tagName);
                tagHolders.add(tagHolder);
                resetTagBox(tagSearchTextBox);
            }
        }
    };
    //widget will be suggest box or anchor
    protected Map<Widget, List<TagHolder>> widgetTagHoldersMap = new HashMap<Widget, List<TagHolder>>();


   protected Map<Widget, SuggestBox> widgetTagHoldersMap2 = new HashMap<Widget, SuggestBox>();


    protected Map<Widget, FlowPanel> widgetSelectedTagsPanelMap2 = new HashMap<Widget, FlowPanel>();


    protected Map<Widget, FlowPanel> widgetSelectedTagsPanelMap = new HashMap<Widget, FlowPanel>();
    protected SelectionHandler tagSelectionHandler = new SelectionHandler() {
        public void onSelection(SelectionEvent selectionEvent) {
            MultiWordSuggestion mws = (MultiWordSuggestion) selectionEvent.getSelectedItem();
            SuggestBox suggestBox = (SuggestBox) selectionEvent.getSource();
            TagHolder tagHolder = new TagHolder(mws.getDisplayString());
            //GWT.log("adding tag " + tagHolder.getName());
            List<TagHolder> tagHolders = widgetTagHoldersMap.get(suggestBox);

            tagHolders.add(tagHolder);
            resetTagBox(suggestBox);
            //want to remove what the user has typed in
            suggestBox.setValue("");
            //selectedTagsListBox.addItem(mws.getDisplayString());
//            if (hideTagsOnFive()) {
//                suggestBox.setVisible(false);
//            }


        }
    };
    AsyncCallback saveSpotCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            postDialog.hide();
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            postDialog.hide();
            MobileResponse mobileResponse = (MobileResponse) response;
            //fetchLocalSpotsDialog.hide();
            if (mobileResponse.getStatus() == 1) {
                ItemHolder itemHolder = new ItemHolder();
                itemHolder.setSpotHolder(mobileResponse.getSpotHolder());
                LeaveMarkForm leaveMarkForm = new LeaveMarkForm(mywebapp, null, false, itemHolder);
                mywebapp.swapCenter(leaveMarkForm);
            } else {
                mywebapp.verifyDisplay();
                getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
            }
        }
    };
    //we need to create a spot out of this before we send this on it's way
    protected ClickHandler pickLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("pickLocationHandler");
                Widget widget = (Widget) sender;
                LocationResult locationResult = clickMapLocation.get(widget);
                postDialog = new DataOperationDialog("Fetching...");
                postDialog.show();
                mywebapp.saveLocationAsSpot(locationResult, saveSpotCallback);
            }
        }
    };
    protected Map<Widget, LocationResult> clickMapLocation = new HashMap<Widget, LocationResult>();
    public SpotImageResource resources = GWT.create(SpotImageResource.class);

    public static void add(StringBuffer sb, String delimiter, String value) {
        if (value != null) {
            if (sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(value);
        }
    }

    //We do not need to crawl this stuff, so we will use a handler
    protected void addLocation(ULPanel ul, LocationResult locationResult, ClickHandler handler, boolean showCategories) {
        ListItem li = new ListItem();
        ComplexPanel vp = getLocationPanel(locationResult, handler, showCategories);
        li.add(vp);
        ul.add(li);
    }

    //protected Map<TextArea, MarkData> autoGrowTextAreaLocationResultMap = new HashMap<TextArea, MarkData>();
    /*
                   <form class="comment-form">
                   <img src="" class="com-ava"/>
                   <textarea rows="1" placeholder="Leave your comment..."></textarea><input class="search-send" type="submit"><br>
                   <a class="adv">show advanced</a><a class="adv">hide</a>
               </form>
    */
    protected Map<Widget, MarkData> widgetMarkDataMap = new HashMap<Widget, MarkData>();
    public ClickHandler saveHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("saveHandler2");
            Widget widget = (Widget) event.getSource();
            MarkData markData = widgetMarkDataMap.get(widget);
            saveIt(markData);
        }
    };
    public ClickHandler saveHandlerFacebook = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("saveHandlerFacebook");
            Widget widget = (Widget) event.getSource();
            MarkData markData = widgetMarkDataMap.get(widget);
            markData.shareOnFacebookCheckbox.setValue(true);
            saveIt(markData);
        }
    };
    public ClickHandler expandHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget widget = (Widget) event.getSource();
            ExpandData expandData = expandLocationResultMap.get(widget);
            Anchor anchor = (Anchor) widget;
            GWT.log("expandHandler onClick");
            if (anchor.getText().equals(EXPAND)) {
                GWT.log("expandHandler onClick 1");
                //we want to expand something now, but
                //we can only have one expanded section now.  need to close last one
                //opps, what if we are trying to expand it again, that is, this was the last expanded puppy??
                //bug
                //fix but by just moving this to before we do the expand location
                if (lastExpandAnchor != null) {
                    lastExpandAnchor.setText(EXPAND);
                    ExpandData lastExpandData = expandLocationResultMap.get(lastExpandAnchor);
                    lastExpandData.resultPanel.clear();
                }
                expandLocation(expandData);
                anchor.setText("Hide");
                lastExpandAnchor = anchor;
            } else {
                GWT.log("expandHandler onClick 2");
                anchor.setText(EXPAND);
                expandData.resultPanel.clear();
            }
        }
    };
    private static String EXPAND = "Expand";
    private Anchor lastExpandAnchor = null;

    public void doReply(MarkData replyToMarkData) {
        Long userId = null;
        Image userImage = null;
        String userPath = null;
        if (mywebapp.getAuthenticatedUser() != null) {
            ImageScaleHolder imageScaleHolder = getImageScaleHolder(mywebapp.getAuthenticatedUser().getContentHolder(), "57x57");
            if (imageScaleHolder != null) {
                userPath = imageScaleHolder.getWebServerAssetHolder().getUrl();
            }
        }
        if (userPath == null) {
            userImage = new Image(MyWebApp.resources.anon57x57());
        } else {
            String fullUrl = mywebapp.getUrl(userPath);
            userImage = new Image(fullUrl);
        }
        userImage.setStyleName("com-ava");
        if (userId != null) {
            String userHistoryToken = MyWebApp.VIEW_USER_PROFILE + userId;
            String userHistoryToken2 = "#" + userHistoryToken;
        }
        TextArea saySomethingTextArea = new TextArea();
        MarkData markData = new MarkData();
        markData.expandData = replyToMarkData.expandData;
        markData.replyItemHolder = replyToMarkData.itemHolder;
        markData.saySomethingTextArea = saySomethingTextArea;
        initAutoGroup(saySomethingTextArea, markData);
        Button leaveMarkButton = new Button("Leave Mark");
        leaveMarkButton.addClickHandler(saveHandler2);
        widgetMarkDataMap.put(leaveMarkButton, markData);
        Button showAdvancedButton = new Button(SHOW_ADVANCED);
        showAdvancedMap.put(showAdvancedButton, markData);
        showAdvancedButton.addClickHandler(showAdvancedWriteHerePanelHandler);
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("ex_mark");
        flowPanel.add(userImage);
        flowPanel.add(saySomethingTextArea);
        flowPanel.add(leaveMarkButton);
        //flowPanel.add(showAdvancedButton);
        FormPanel myFormPanel = new FormPanel();
        myFormPanel.setWidget(flowPanel);
        setupRootPanelForm(myFormPanel, markData);
        replyToMarkData.expandData.replyGoesHere.add(myFormPanel);
        /*
        shareOnFacebookButton.addClickHandler(saveHandlerFacebook);
        widgetMarkDataMap.put(shareOnFacebookButton, markData);

        ULPanel latestMarksPanel = new ULPanel();
        ResultComponent resultComponent = new ResultComponent(saySomethingTextArea, leaveMarkButton, userImage, latestMarksPanel, showAdvancedButton,shareOnFacebookButton);
        FormPanel myFormPanel = new FormPanel();
        myFormPanel.setWidget(resultComponent);
        setupRootPanelForm(myFormPanel, markData);
        replyToMarkData.expandData.replyGoesHere.add(myFormPanel);

        replyToMarkData.expandData.resultPanel = replyToMarkData.expandData.replyGoesHere;
        */
    }

    public MarkData expandLocation(ExpandData expandData) {
        //  GWT.log("expandLocation reply=" + reply);
        Long userId = null;
        Image userImage = null;
        String userPath = null;
        if (mywebapp.getAuthenticatedUser() != null) {
            ImageScaleHolder imageScaleHolder = getImageScaleHolder(mywebapp.getAuthenticatedUser().getContentHolder(), "57x57");
            if (imageScaleHolder != null) {
                userPath = imageScaleHolder.getWebServerAssetHolder().getUrl();
            }
        }
        if (userPath == null) {
            userImage = new Image(MyWebApp.resources.anon57x57());
        } else {
            String fullUrl = mywebapp.getUrl(userPath);
            userImage = new Image(fullUrl);
        }
        userImage.setStyleName("com-ava");
        if (userId != null) {
            String userHistoryToken = MyWebApp.VIEW_USER_PROFILE + userId;
            String userHistoryToken2 = "#" + userHistoryToken;
        }
        TextArea saySomethingTextArea = new TextArea();
        MarkData markData = new MarkData();
        markData.expandData = expandData;
        markData.saySomethingTextArea = saySomethingTextArea;
        initAutoGroup(saySomethingTextArea, markData);
        Button leaveMarkButton = new Button("Leave Mark");
        leaveMarkButton.addClickHandler(saveHandler2);
        widgetMarkDataMap.put(leaveMarkButton, markData);
        Button showAdvancedButton = new Button(SHOW_ADVANCED);
        showAdvancedMap.put(showAdvancedButton, markData);
        showAdvancedButton.addClickHandler(showAdvancedWriteHerePanelHandler);
//        Button shareOnFacebookButton = new Button("Share On facebook");
//        shareOnFacebookButton.addClickHandler(saveHandlerFacebook);
//        widgetMarkDataMap.put(shareOnFacebookButton, markData);


        Button shareOnFacebookButton =   getFacebookButton(markData);

        ULPanel latestMarksPanel = getLatestMarksPanel(expandData);
        ResultComponent resultComponent = new ResultComponent(saySomethingTextArea, leaveMarkButton, userImage, latestMarksPanel, showAdvancedButton, shareOnFacebookButton);
        FormPanel myFormPanel = new FormPanel();
        myFormPanel.setWidget(resultComponent);
        setupRootPanelForm(myFormPanel, markData);
        expandData.resultPanel.add(myFormPanel);
        return markData;
    }

    private Anchor getResultHeader(String spotName) {
        H2 spotLabel = new H2();
        spotLabel.setText(spotName);
        Anchor anchor = new Anchor();
        anchor.getElement().appendChild(spotLabel.getElement());
        anchor.setStyleName("result-header");
        //spotLabel.addStyleName("linky");
        return anchor;
    }

    /*
    <div id="result">
    	<img src="ava.png" class="avatar"/>
    	<input class="check" type="checkbox">
    	<div class="center-block">
    		<a href="#" class="result-header"><h2>@Vishal Adma, 4300 Brenner Dr, Kansas City, KS 66104</h2></a>
    		<div class="tags"><a href="#">ks</a><a href="#">66104</a><a href="#">kansas city</a></div>
    			<form class="comment-form">
    				<img src="" class="com-ava"/>
    				<textarea rows="1" placeholder="Leave your comment..."></textarea><input class="search-send" type="submit"><br>
    				<a class="adv">show advanced</a><a class="adv">hide</a>
    			</form>
    		</div>
    		<a href="#" class="distanse"><h1>560</h1><p>meters avay</p></a>
    	<div class="search-adv">
    	<form class="adv-form">
    			<label class="add-photo" title="add photo"><input type="file"></label>
    			<label class="add-photo" title="add photo"><input type="file"></label>
    			<label class="add-photo" title="add photo"><input type="file"></label>
    			<textarea rows="1" placeholder="Secret Key"></textarea>
    			<span><input type="submit">
    			<p>Max file size - 5mb</p></span>
    		</form>
    	</div>
    </div>
    */
    protected ComplexPanel getLocationPanel(LocationResult locationResult, ClickHandler handler, boolean showCategories) {
        Location location = locationResult.getLocation();
        FlowPanel result = new FlowPanel();
        result.setStyleName("result");
        Image image = new Image(getSpotImage());
        //image.setHeight("68px");
        image.setStyleName("avatar");
        clickMapLocation.put(image, locationResult);
        image.addClickHandler(handler);
        //addImageOrig(image, result, "avatar");
        result.add(image);
        FlowPanel centerBlock = new FlowPanel();
        centerBlock.setStyleName("center-block");
        result.add(centerBlock);
        //	<a href="#" class="result-header"><h2>@Vishal Adma, 4300 Brenner Dr, Kansas City, KS 66104</h2></a>
        if (location.getName() != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("@");
            add(sb, "", location.getName());
            add(sb, ", ", location.getAddress1());
            add(sb, ", ", location.getCity());
            add(sb, ", ", location.getState());
            add(sb, " ", location.getZipcode());
            Anchor resultHeader = getResultHeader(sb.toString());
            resultHeader.addClickHandler(handler);
            clickMapLocation.put(resultHeader, locationResult);
            centerBlock.add(resultHeader);
        }
        FlowPanel categoriesPanel = addCategories(location, showCategories);
        centerBlock.add(categoriesPanel);
        //<a href="#" class="distanse"><h1>560</h1><p>meters avay</p></a>
        //centerBlock
        // asdf
        //Label distanceLabel = getDistanceLabel(locationResult);
        //<h1>560</h1><p>meters avay</p>
        Anchor distanceAnchor = getDistanceAnchor(locationResult);
        result.add(distanceAnchor);
        addOurBuddies(centerBlock, locationResult);
        //FlowPanel resultPanel,LocationResult locationResult
        addExpand(result, locationResult, centerBlock);
        return result;
    }

    private Map<Widget, ExpandData> expandLocationResultMap = new HashMap<Widget, ExpandData>();

    public static class ExpandData {
        public LocationResult locationResult = null;
        public FlowPanel resultPanel = null;
        public FlowPanel replyGoesHere = null;
    }

    private void addExpand(FlowPanel resultPanel, LocationResult locationResult, FlowPanel centerBlock) {
        if (mywebapp.getToggleMapMode().getValue()) {
            return;
        }
        Anchor expandAnchor = new Anchor("Expand");
        expandAnchor.setStyleName("expand");
        expandAnchor.addClickHandler(expandHandler);
        ExpandData expandData = new ExpandData();
        //the following fp can be hidden/deleted/whatever if we need to un-expand
        FlowPanel fp = new FlowPanel();
        resultPanel.add(fp);
        expandData.resultPanel = fp;
        expandData.locationResult = locationResult;
        expandLocationResultMap.put(expandAnchor, expandData);
        centerBlock.add(expandAnchor);
    }

    private Anchor getDistanceAnchor(LocationResult locationResult) {
        double displayDistance = 0.0D;
        String safe = null;
        Double ddistance = new Double(locationResult.getDistance());
        if (mywebapp.isShowMeters()) {
            displayDistance = locationResult.getDistance() / 1000.0;
            if (displayDistance > 1.0D) {
                String val = NumberFormat.getFormat("####.#").format(displayDistance);
                safe = "<h1>" + val + "</h1><p>km away</p>";
            } else {
                int dd = ddistance.intValue();
                safe = "<h1>" + dd + "</h1><p>meters away</p>";
            }
        } else {
            double displayDistanceInMiles = locationResult.getDistance() * METERS_TO_MILES;
            double distanceInYards = locationResult.getDistance() * YARD;
            int dd = new Double(distanceInYards).intValue();
            //int dd = ddistance.intValue();
            if (dd > 999) {
                String val = NumberFormat.getFormat("####.#").format(displayDistanceInMiles);
                safe = "<h1>" + val + "</h1><p>miles away</p>";
            } else {
                safe = "<h1>" + dd + "</h1><p>yards away</p>";
            }
        }
        SafeHtml safeHtml = SafeHtmlUtils.fromTrustedString(safe);
        Anchor anchor = new Anchor(safeHtml);
        anchor.setStyleName("distance");
        return anchor;
    }

    protected FlowPanel addCategories(Location location, boolean showCategories) {
        GWT.log("addCategories " + location.getFactualCategories() + " showCategories " + showCategories);
        FlowPanel categoriesPanel = new FlowPanel();
        categoriesPanel.addStyleName("tags");
        //we do not want to show categories when we are showing spots that we are picking from
        if (showCategories) {
            if ((location.getFactualCategories() != null) && (location.getFactualCategories().length() > 0)) {
                String[] cats = location.getFactualCategories().split(">");
                for (String cat : cats) {
                    //        //<div class="tags"><a href="#">ks</a><a href="#">66104</a><a href="#">kansas city</a></div>
                    String trimCat = cat.trim();
                    TagHolder tagHolder = new TagHolder(trimCat);
                    Anchor catLabel = new Anchor(trimCat);
                    //catLabel.addStyleName("linky");
                    catLabel.addClickHandler(clickTagHandler2);
                    tagHolderClickMap.put(catLabel, tagHolder);
                    categoriesPanel.add(catLabel);
                }
            }
        }
        return categoriesPanel;
    }

    /*
        protected ComplexPanel getLocationPanelOriginal(LocationResult locationResult, ClickHandler handler, boolean showCategories) {
            Location location = locationResult.getLocation();
            VerticalPanel vp = new VerticalPanel();
            vp.setStyleName("resultHolderTable");
            HorizontalPanel hp = new HorizontalPanel();
            vp.setWidth("100%");
            vp.add(hp);
            Image image = new Image(getSpotImage());
            image.addStyleName("linky");
            clickMapLocation.put(image, locationResult);
            image.addClickHandler(handler);
            Label distanceLabel = getDistanceLabel(locationResult);
            clickMapLocation.put(distanceLabel, locationResult);
            distanceLabel.addClickHandler(handler);
            Label metersLabel = new Label("meters away");
            clickMapLocation.put(metersLabel, locationResult);
            metersLabel.addClickHandler(handler);
            metersLabel.addStyleName("linky");
            ComplexPanel distancePanel = getDistancePanel(distanceLabel, metersLabel, locationResult);
            addImageOrig(image, hp, "spotimage");
            hp.setCellWidth(image, "1%");
            VerticalPanel middleTable = new VerticalPanel();
            middleTable.setStyleName("middletable");
            if (location.getName() != null) {
                //GWT.log("processing " + location.getName());
                StringBuffer sb = new StringBuffer();
                sb.append("@");
                add(sb, "", location.getName());
                add(sb, ", ", location.getAddress1());
                add(sb, ", ", location.getCity());
                add(sb, ", ", location.getState());
                add(sb, " ", location.getZipcode());
                Label spotLabel = new Label(sb.toString());
                spotLabel.addClickHandler(handler);
                spotLabel.setStyleName("spotLabel");
                spotLabel.addStyleName("linky");
                clickMapLocation.put(spotLabel, locationResult);
                middleTable.add(spotLabel);
            }
            middleTable.add(addCategories(location, showCategories));
            addPhone(location.getVoicephone(), middleTable);
            // middleTable.add(categoriesPanel);
    //        Label firstMarkLabel = getLabel("Be the first to mark this spot!", handler, locationResult);
    //        firstMarkLabel.addStyleName("befirst");
            //middleTable.add(firstMarkLabel);
            //lets' add the twitter trends
            if (!MyWebApp.isSmallFormat()) {
                addOurBuddies(middleTable, locationResult);
            }
            //if it is small format, we do not add middletable to the middle, but go down one to give more space
            if (MyWebApp.isDesktop()) {
                //ouch
                // vp.add(middleTable);
                hp.add(middleTable);
                hp.setCellWidth(middleTable, "100%");
                hp.add(distancePanel);
            } else {
                hp.add(middleTable);
                hp.setCellWidth(middleTable, "100%");
                // hp.add(distancePanel);
                //hp.setCellWidth(distancePanel, "300");
            }
            //we do not add a 2nd image if it's a mobile device.  not enough space!
            if ((alex) && (!MyWebApp.isSmallFormat())) {
                Image latestMarkImage = new Image(getSpotImage());
                latestMarkImage.addStyleName("linky");
                clickMapLocation.put(latestMarkImage, locationResult);
                latestMarkImage.addClickHandler(handler);
                addImageOrig(latestMarkImage, hp, "spotimage");
                latestMarkImage.addStyleName("latestMarkImage");
                hp.setCellWidth(latestMarkImage, "1%");
            }
            expandLocation(locationResult, middleTable,false);
            return vp;
        }

    */
    protected Label leaveMarkButton() {
        Label leaveMarkButton = new Label("Leave Mark");
        leaveMarkButton.addClickHandler(saveHandler);
        fixButton(leaveMarkButton);
        addImageToButton(leaveMarkButton, MyWebApp.resources.saveButton(), MyWebApp.resources.saveButtonMobile());
        return leaveMarkButton;
    }

    protected void addPhone(String phone, ComplexPanel complexPanel) {
        //let's show the phone
        //<a href="callto:0123456789">call me</a>         will work for desktop, (/^callto:/, "tel:
        if (phone == null) {
            return;
        }
        Anchor phoneAnchor = getPhone(phone);
        complexPanel.add(phoneAnchor);
    }

    protected Anchor getPhone(String phone) {
        if (MyWebApp.isDesktop()) {
            Anchor anchor = new Anchor(phone, "callto:" + phone);
            return anchor;
        } else {
            Anchor anchor = new Anchor(phone, "tel:" + phone);
            return anchor;
        }
    }

    protected void initAutoGroup(TextArea saySomethingTextArea, MarkData markData) {
        saySomethingTextArea.setValue(WRITE_SOMETHING);
        saySomethingTextArea.addBlurHandler(saySomethingBlur);
        saySomethingTextArea.addFocusHandler(saySomethingFocus);
        saySomethingTextArea.addKeyDownHandler(saySomethingKeyDownHandler);
        //saySomethingTextArea.setValue(WRITE_SOMETHING);
        markData.saySomethingTextArea = saySomethingTextArea;
        widgetMarkDataMap.put(saySomethingTextArea, markData);
    }

    protected Marker lastMarker = null;
    protected InfoWindow lastOpenedInfoWindow = null;
    //wierd, but we are going to store the last piece of content that we put on an info window
    //because the action to do advanced has a reference to this, and adds it in
    protected ComplexPanel lastContent = null;

    private void moveAdvancedPanel(Button label) {
        GWT.log("moveAdvancedPanel ");
        if (lastOpenedInfoWindow != null) {
            lastOpenedInfoWindow.close();
        }
        if (label.getText().equals(SHOW_ADVANCED)) {
            MarkData markData = showAdvancedMap.get(label);
            TextArea saySomethingTextArea = new TextArea();
            //we are using markData1 because we are creating new form, not using the old non-advanced form
            MarkData advancedMarkData = new MarkData();
            advancedMarkData.expandData = markData.expandData;
            initAutoGroup(saySomethingTextArea, advancedMarkData);
            label.setText("Hide Advanced");
            Button leaveMarkButton = new Button();
            leaveMarkButton.addClickHandler(saveHandler2);
            widgetMarkDataMap.put(leaveMarkButton, advancedMarkData);
            MultiUploader multiUploader = new MultiUploader();
            FlowPanel panelImages = new FlowPanel();
            IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages);
            multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            markData.tagSearchTextBox = tagSearchTextBox;
//            TextBox secretKeyTextBox = new TextBox();
//            markData.secretKeyTextBox = secretKeyTextBox;
            //SimpleCheckBox shareOnFacebookCheckbox = new SimpleCheckBox();
            //markData.shareOnFacebookCheckbox = shareOnFacebookCheckbox;
            Anchor addTagAnchor = new Anchor("Add");
            addTagAnchor.getElement().setId("addTag");
            addTagAnchor.addClickHandler(addTagHandler);
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            //MultiUploader multiUploader = new MultiUploader();
            //FlowPanel panelImages = new FlowPanel();
            //IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages);
            //multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            SmallAdvancedForm smallAdvancedForm = new SmallAdvancedForm(selectedTagsPanel, tagSearchTextBox, markData.secretKeyTextBox, multiUploader, panelImages,suggestionsPanel);
            //MarkComposite markComposite = new MarkComposite(leaveMarkButton, advancedMarkData.saySomethingTextArea, multiUploader, panelImages, mywebapp,tagSearchTextBox,selectedTagsPanel,addTagAnchor);
            hideAdvancedMap.put(label, smallAdvancedForm);
            FormPanel myform = new FormPanel();
            setupRootPanelForm(myform, advancedMarkData);
            myform.setWidget(smallAdvancedForm);
            advancedMarkData.expandData.resultPanel.add(myform);
            //markComposite.hideTitle();
        } else {
            //hide advanced
            SmallAdvancedForm markComposite = hideAdvancedMap.get(label);
            markComposite.removeFromParent();
            label.setText(SHOW_ADVANCED);
        }
        if (lastOpenedInfoWindow != null) {
            InfoWindow info =mapWidget.getInfoWindow();
            InfoWindowContent inc = new InfoWindowContent(lastContent);
            lastOpenedInfoWindow = info;
            info.open(lastMarker, inc);
        }
    }

    //


    protected void addLeaveMarkAtTopForm() {
        Long userId = null;
        Image userImage = null;
        String userPath = null;
        HorizontalPanel hp = new HorizontalPanel();
        if (mywebapp.getAuthenticatedUser() != null) {
            ImageScaleHolder imageScaleHolder = getImageScaleHolder(mywebapp.getAuthenticatedUser().getContentHolder(), "57x57");
            if (imageScaleHolder != null) {
                userPath = imageScaleHolder.getWebServerAssetHolder().getUrl();
            }
        }
        if (userPath == null) {
            userImage = new Image(MyWebApp.resources.anon57x57());
        } else {
            String fullUrl = mywebapp.getUrl(userPath);
            userImage = new Image(fullUrl);
        }
        if (userId != null) {
            String userHistoryToken = MyWebApp.VIEW_USER_PROFILE + userId;
            String userHistoryToken2 = "#" + userHistoryToken;
            Anchor userAnchor = new Anchor("", userHistoryToken2);
            userAnchor.getElement().appendChild(userImage.getElement());
            hp.add(userAnchor);
        } else {
            hp.add(userImage);
        }
        //AutoGrowTextArea saySomethingTextArea = new AutoGrowTextArea();
        VerticalPanel topFormPanel = new VerticalPanel();
        topFormPanel.setStyleName("topFormPanel");
        topFormPanel.setWidth("100%");
        add(hp);
        topFormPanel.add(hp);
        Label advancedLabel = new Label(SHOW_ADVANCED);
        //MarkData markData = new MarkData();
        // markData.saySomethingTextArea = saySomethingTextArea;
        //  markData.formHolder = hp;
        //advancedMap.put(advancedLabel, markData);
        //labelParentMap.put(advancedLabel,middleTable);
        //advancedLabel.addStyleName("linky");
        //advancedLabel.addClickHandler(showAdvancedWriteHerePanelHandler);
        topFormPanel.add(advancedLabel);
        // markData.wherePanel = topFormPanel;
        //labelParentMap.put(advancedLabel,topFormPanel);
        // add(statusLocationSpotListBox);
        imagePanel.setWidth("100%");
        imagePanel.clear();
        imagePanel.setVisible(false);
        addMediaFields("Upload images OR video", imagePanel);
        imagePanel.add(contentsPanel);
        if (mywebapp.isFacebookUser()) {
          //  addCheckbox(shareOnFacebookCheckbox, null, imagePanel);
        }
        secretKeyTextBox = addTextBox("Secret Key", "secretKey", "", false, imagePanel);
        topFormPanel.add(imagePanel);
        Label leaveMarkButton = new Label("Leave Mark");
        leaveMarkButton.addClickHandler(saveHandler);
        leaveMarkButton.setStyleName("button");
        leaveMarkButton.addStyleName("leavermarkbutton");
        imagePanel.add(leaveMarkButton);
        //topFormPanel.add(fp);
    }

    public static String SHOW_SORTING = "Show Sorting/Tags";
    protected Label sortingLabel = new Label(SHOW_SORTING);
    //show
    public static String SHOW_MILES = "Show miles";
    public static String SHOW_KM = "Show kilometers";
    protected Label toggleKiloMilesLabel = new Label();
    protected Label resultsViewLabel = new Label(MAP_VIEW);
    public static String MAP_VIEW = "Map View";
    public static String LIST_VIEW = "List View";
    protected VerticalPanel imagePanel = new VerticalPanel();
    public static String SHOW_ADVANCED = "Show Advanced";
    public static String WRITE_SOMETHING = "Write something here...";

    private void addOurBuddies(Panel middleTable, LocationResult locationResult) {
        if (mywebapp.getToggleMapMode().getValue()) {
            return;
        }
        addTwitter(middleTable, locationResult);
        addFourSquare(middleTable, locationResult);
        addYelpSearchResults(middleTable, locationResult.getYelpHolder());
        addGooglePlaces(middleTable, locationResult);
        addInstagramResultsPage(middleTable, locationResult.getInstagrams());
        addYahooUpcoming(middleTable, locationResult.getEvents());
    }

    boolean alex = false;

    protected ImageResource getSpotImage() {
        //150px × 120px for theme
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.spot_image_placeholder57x57();
        } else {
            return MyWebApp.resources.spot_image_placeholder130x130();
        }
    }
//    private Label getLabel(String labeltext, ClickHandler handler, LocationResult locationResult) {
//        Label lbl = new Label(labeltext);
//        clickMapLocation.put(lbl, locationResult);
//        lbl.addClickHandler(handler);
//        lbl.addStyleName("linky");
//        return lbl;
//    }

    public static Image addImageContent(Panel panel, ContentHolder contentHolder, String attrName) {
        if (contentHolder == null) {
            GWT.log("contentHolder is null");
            return null;
        }
        Image image = getImage(contentHolder, attrName);
        if (image == null) {
            return null;
        }
        image.setStyleName("mainImg");
        panel.add(image);
        return image;
    }

    public static Image getImage(ContentHolder contentHolder, String attrName) {
        ImageScaleHolder imageScaleHolder = getImageScaleHolder(contentHolder, attrName);
        if (imageScaleHolder == null) {
            GWT.log("imageScaleHolder is null, returning");
            return null;
        } else {
            GWT.log("imageScaleHolder is NOT null");
        }
        GWT.log("url is " + imageScaleHolder.getWebServerAssetHolder().getUrl());
        Image image = new Image();
        image.setUrl(imageScaleHolder.getWebServerAssetHolder().getUrl());
        return image;
    }

    public static ImageScaleHolder getImageScaleHolder(ContentHolder contentHolder, String attrName) {
        if (contentHolder == null) {
            GWT.log("getImageScaleHolder contentHolder is null");
            return null;
        }
        GWT.log("getImageScaleHolder seeking  in " + attrName + ",contentHolder=" + contentHolder.getFileName() + "for " + contentHolder.getId());
        GWT.log("This getImageScaleHolder " + contentHolder.getAttributeName());
        GWT.log(" contentHolder.getImageScaleHolders().length()=" + contentHolder.getImageScaleHolders().size());
        for (ImageScaleHolder imageScaleHolder : contentHolder.getImageScaleHolders()) {
            GWT.log("imageScaleHolder.getAttributeName()=  evaluating " + imageScaleHolder.getAttributeName());
            if (imageScaleHolder.getAttributeName().equals(attrName)) {
                GWT.log("Found a match for " + attrName + " returning...");
                return imageScaleHolder;
            } else {
                GWT.log("NOT  a match for " + attrName);
            }
        }
        for (ContentHolder childContentHolder : contentHolder.getContentHolders()) {
            GWT.log("childContentHolder " + childContentHolder.getAttributeName());
            GWT.log("Calling getImageScaleHolder " + attrName);
            ImageScaleHolder ish = getImageScaleHolder(childContentHolder, attrName);
            if (ish != null) {
                GWT.log("returning imagescaleholder in getImageScaleHolder");
                return ish;
            }
        }
        return null;
    }

    protected void fixButton(Widget label) {
        //label.setWidth("100%");
        label.setStyleName("whiteButton");
    }

    protected boolean isUploading() {
        if (defaultUploader == null) return false;
        if (defaultUploader.getStatus() == Status.INPROGRESS) {
            mywebapp.getMessagePanel().displayMessage("Please wait until files are done uploading.");
            return true;
        } else if (defaultUploader.getStatus() == Status.QUEUED) {
            mywebapp.getMessagePanel().displayMessage("Please wait until files are done uploading.");
            return true;
        }
        return false;
    }

    //need to override
    protected boolean isValid() {
        return true;
    }

    //this needs to be overridden if you want to utilize, it will call in toggle...
    public boolean isValidToToggle() {
        return true;
    }

    protected void saveIt() {
        // let's do the file uploads for the media files
        //temp, let's see if we do not clear
        getMessagePanel().clear();
        //com.google.gwt.user.client.Window.scrollTo(0, 0);
        if (isUploading()) {
            return;
        }
        if (!isValid()) {
            return;
        }
        postDialog = new DataOperationDialog("Saving...");
        postDialog.show();
        //we need to upload all the files first so they are in the session
        uploadMediaFiles();
        if (isUploadSupported()) {
            mywebapp.log("formPanel.submit in saveIt");
            formPanel.submit();
        } else {
            mywebapp.log("calling doSave");
            //no file upload, so let's kick off this
            doSave();
        }
    }

    public ClickHandler saveHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.log("saveit");
            saveIt();
        }
    };
    public BlurHandler saySomethingBlur = new BlurHandler() {
        public void onBlur(BlurEvent blurEvent) {
            //To change body of implemented methods use File | Settings | File Templates.
            TextArea textArea = (TextArea) blurEvent.getSource();
            if (isEmpty(textArea)) {
                textArea.setValue(WRITE_SOMETHING);
            }
        }
    };
    public FocusHandler saySomethingFocus = new FocusHandler() {
        public void onFocus(FocusEvent event) {
            TextArea textArea = (TextArea) event.getSource();
            if (textArea.getValue().equals(WRITE_SOMETHING)) {
                textArea.setValue("");
            }
        }
    };
    //    public BlurHandler saySomethingBlur2 = new BlurHandler() {
//        public void onBlur(BlurEvent blurEvent) {
//            //To change body of implemented methods use File | Settings | File Templates.
//            TextArea textArea = (TextArea) blurEvent.getSource();
//            if (textArea.getValue().isEmpty()) {
//                textArea.setValue(WRITE_SOMETHING2);
//            }
//        }
//    };
//    public FocusHandler saySomethingFocus2 = new FocusHandler() {
//        public void onFocus(FocusEvent event) {
//            TextArea textArea = (TextArea) event.getSource();
//            if (textArea.getValue().equals(WRITE_SOMETHING2)) {
//                textArea.setValue("");
//            }
//        }
//    };
    //protected AutoGrowTextArea selectedAutoGrowTextArea = null;
    public KeyDownHandler saySomethingKeyDownHandler = new KeyDownHandler() {
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() != KeyCodes.KEY_ENTER) {
                return;
            }
            //need to get the markData that this is part of
            TextArea selectedAutoGrowTextArea = (TextArea) event.getSource();
            MarkData markData = widgetMarkDataMap.get(selectedAutoGrowTextArea);
            //saveStatus();
            saveIt(markData);
        }
    };

    protected void saveIt(final MarkData markData) {
        GWT.log("saveit markdata style");
        // let's do the file uploads for the media files
        //temp, let's see if we do not clear
        getMessagePanel().clear();
        //com.google.gwt.user.client.Window.scrollTo(0, 0);
        if (isUploading()) {
            return;
        }
        if (!isValid(markData)) {
            GWT.log("not valid returning");
            return;
//        } else {
//            GWT.log(" valid  not returning");
        }
        postDialog = new DataOperationDialog("Saving...");
        postDialog.show();
        //we need to upload all the files first so they are in the session
        uploadMediaFiles();
        if (isUploadSupported()) {
            GWT.log("formPanel.submit  saveIt(final MarkData markData)");
            //formPanel.submit();
            markData.formPanel.submit();
        } else {
            GWT.log("calling doSave");
            //no file upload, so let's kick off this
            doSave(markData);
        }
    }

    //    //override
//    protected void doSave(MarkData markData) {
//        GWT.log("override me mMarkData =======");
//    }
//
//
//    @Override
    protected void doSave(MarkData markData) {
        saveStatus(markData);
    }

    protected boolean isValid(MarkData markData) {
        GWT.log("isValid called");
        //Write something on this spot...
        TextArea selectedAutoGrowTextArea = markData.saySomethingTextArea;
        if (selectedAutoGrowTextArea.getValue().equals(WRITE_SOMETHING)) {
            GWT.log("Write something should error out");
            //checkRequired(selectedAutoGrowTextArea, "To submit a mark, you need to say something.");
            getMessagePanel().displayError("To submit a mark, you need to say something.");
        }
//        if (selectedAutoGrowTextArea.getValue().equals(WRITE_SOMETHING2)) {
//            checkRequired(selectedAutoGrowTextArea, "To submit a mark, you need to say something.");
//        }
        checkRequired(selectedAutoGrowTextArea, "To submit a mark, you need to say something.");
        return (!getMessagePanel().isHaveMessages());
    }

    //protected CheckBox shareOnFacebookCheckbox = new CheckBox("Share on facebook");
    public TextBox secretKeyTextBox = new TextBox();

    //you need to override this
    protected void doSave() {
        GWT.log("Please override");
    }

    public Button saveButton() {
        Button btn = new Button("Save");
        btn.addClickHandler(saveHandler);
        btn.setStyleName("btn_blue");
        // fixButton(btn);
        //addImageToButton(btn, MyWebApp.resources.saveButton(), MyWebApp.resources.saveButtonMobile());
        return btn;
    }

    public Button cancelButton(String label) {
        Button btn = new Button(label);
        btn.addClickHandler(cancelHandler);
        //addImageToButton(btn, MyWebApp.resources.cancelButton(), MyWebApp.resources.cancelButtonMobile());
        //fixButton(btn);
        btn.setStyleName("btn_blue");
        return btn;
    }

    public Button cancelButton() {
        return cancelButton("Cancel");
    }

    protected DataOperationDialog postDialog = null;

    protected Image getMarkPhoto(SolrDocument solrDocument) {
        String imageKey = null;
        String spotKey = null;
        if (MyWebApp.isSmallFormat()) {
            imageKey = "latest_mark_thumbnail_57x57_url_s";
            spotKey = "image_thumbnail_57x57_url_s";
        } else {
            imageKey = "latest_mark_thumbnail_130x130_url_s";
            spotKey = "image_thumbnail_130x130_url_s";
        }
        String val = solrDocument.getFirstString(imageKey);
        Image image = null;
        if (val != null) {
            image = new Image();
            image.setUrl(val);
        } else {
            image = new Image();
            if (MyWebApp.isSmallFormat()) {
                image.setResource(resources.spot_image_placeholder57x57());
            } else {
                image.setResource(resources.spot_image_placeholder130x130());
            }
        }
        return image;
    }

    protected void addMarkPhoto2(SolrDocument solrDocument, String targetHistoryToken, ComplexPanel hp) {
        String imageKey = null;
        String spotKey = null;
        if (MyWebApp.isSmallFormat()) {
            imageKey = "latest_mark_thumbnail_57x57_url_s";
            spotKey = "image_thumbnail_57x57_url_s";
        } else {
            imageKey = "latest_mark_thumbnail_130x130_url_s";
            spotKey = "image_thumbnail_130x130_url_s";
        }
        String val = solrDocument.getFirstString(imageKey);
        if (val != null) {
            Image image = addImage2(solrDocument, hp, imageKey
                    , null, null, targetHistoryToken);
            image.setStyleName("avatar");
            //setColumnWidth(image,hp);
        } else {
            Image image = addImage2(solrDocument, hp, spotKey,
                    resources.spot_image_placeholder130x130(), resources.spot_image_placeholder57x57(), targetHistoryToken);
            image.setStyleName("avatar");
        }
    }

    protected void addMarkPhoto(SolrDocument solrDocument, String targetHistoryToken, HorizontalPanel hp) {
        String imageKey = null;
        String spotKey = null;
        if (MyWebApp.isSmallFormat()) {
            imageKey = "latest_mark_thumbnail_57x57_url_s";
            spotKey = "image_thumbnail_57x57_url_s";
        } else {
            imageKey = "latest_mark_thumbnail_130x130_url_s";
            spotKey = "image_thumbnail_130x130_url_s";
        }
        String val = solrDocument.getFirstString(imageKey);
        if (val != null) {
            Image image = addImage(solrDocument, hp, imageKey
                    , null, null, "spotimage", targetHistoryToken);
            setColumnWidth(image, hp);
        } else {
            Image image = addImage(solrDocument, hp, spotKey,
                    resources.spot_image_placeholder130x130(), resources.spot_image_placeholder57x57(), "spotimage", targetHistoryToken);
            setColumnWidth(image, hp);
        }
    }

    private boolean isSpot(SolrDocument solrDocument) {
        String type_s = solrDocument.getFirstString("type_s");
        boolean isItem = false;
        boolean isSpot = false;
        if (type_s != null) {
            String type = type_s.toString();
            if (type.equals("GeoRepoItem")) {
                return false;
            } else if (type.equals("Spot")) {
                return true;
            }
        }
        return false;
    }

    protected Hyperlink getSpotLink(String text, Long spotId) {
        String token = MyWebApp.SPOT_DETAIL + spotId;
        Hyperlink hyperlink = new Hyperlink(text, token);
        return hyperlink;
    }

    protected Image addImage(SolrDocument result, Panel hp, String fieldName,
                             Hyperlink hyperlink, ImageResource imageResourceBig, ImageResource imageResourceMobile, String styleName) {
        String val = result.getFirstString(fieldName);
        Image image = null;
        if (val != null) {
            image = addImageOrig(val.toString(), hp, styleName);
        } else if (imageResource != null) {
            ImageResource imageResource = null;
            if (MyWebApp.isSmallFormat()) {
                imageResource = imageResourceMobile;
            } else {
                imageResource = imageResourceBig;
            }
            image = new Image(imageResource);
            //hp.add(image);
            addImageOrig(image, hp, styleName, hyperlink);
        }
        return image;
    }

    //here we will display a solr result that users can pick to leave a mark upon
    //mode 1 is what we want, so removing
    protected void addResult2(ULPanel ul, LocationResult locationResult) {
        // vertical panel, 2 rows into each li
        // one label on top row
        // hp goes into 2nd row
        SolrDocument solrDocument = locationResult.getSolrDocument();
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp = new HorizontalPanel();
        Long spotId = spotId = solrDocument.getFirstLong("spotid_l");
        //all links for this will go to mark a spot
        String targetHistoryToken = MyWebApp.LEAVE_SPOT_MARK + spotId;
        Long itemId = null;
        itemId = solrDocument.getFirstLong("georepoitemid_l");
        if (itemId == null) {
            itemId = solrDocument.getFirstLong("latest_mark_georepoitemid_l");
        }
        vp.add(hp);
        ListItem li = new ListItem();
        li.add(vp);
        ul.add(li);
        addMarkPhoto(solrDocument, targetHistoryToken, hp);
        VerticalPanel middleTable = new VerticalPanel();
        hp.add(middleTable);
        hp.setCellWidth(middleTable, "100%");
        middleTable.setStyleName("middletable");
        //this will add the first row to the middle table
        //mode is is a results page, whereas 2 is marks for a spot
        String spot_label_s = solrDocument.getFirstString("spot_label_s");
        if (spot_label_s != null) {
            String sl = spot_label_s.toString();
            Hyperlink spotLabel = new Hyperlink("@" + sl, targetHistoryToken);
            spotLabel.setStyleName("spotLabel");
            middleTable.add(spotLabel);
        }
        Anchor distanceLabel = getDistanceHyperlink(locationResult, targetHistoryToken);
        Hyperlink metersLabel = new Hyperlink("meters away", targetHistoryToken);
        ComplexPanel distancePanel = getDistancePanel(distanceLabel, metersLabel, locationResult);
        Hyperlink latest_mark_escapedjavascriptsnippet_s = addHtml2(solrDocument, middleTable, "latest_mark_escapedjavascriptsnippet_s", targetHistoryToken);
        if (latest_mark_escapedjavascriptsnippet_s != null) {
            latest_mark_escapedjavascriptsnippet_s.addStyleName("latestMarkEscapedJavascriptSnippet");
        }
        ComplexPanel categoriesPanel = addCategories(solrDocument);
        middleTable.add(categoriesPanel);
        if (itemId == null) {
            Hyperlink label2 = new Hyperlink("Be the first to mark this spot!", targetHistoryToken);
            label2.addStyleName("linky");
            label2.addStyleName("befirst");
            //middleTable.add(label2);
        }
        String snippet = solrDocument.getFirstString("spot_geoRepoItemescapedjavascriptsnippet_s");
        if (snippet != null) {
            Hyperlink html2 = new Hyperlink(snippet, targetHistoryToken);
            html2.addStyleName("spotGeoRepoItemEscapedJavascriptSnippet");
        }
        Hyperlink html2 = addHtml2(solrDocument, middleTable, "spot_geoRepoItemescapedjavascriptsnippet_s", targetHistoryToken);
        if (html2 != null) {
            html2.addStyleName("spotGeoRepoItemEscapedJavascriptSnippet");
        }
        //2nd image
        //spot
        if (!MyWebApp.isSmallFormat()) {
            if ((alex)) {
                Hyperlink userHyperLink = new Hyperlink();
                userHyperLink.setTargetHistoryToken(targetHistoryToken);
                Image image = addImage(solrDocument, hp, "image_thumbnail_130x130_url_s",
                        userHyperLink, resources.spot_image_placeholder130x130(), resources.spot_image_placeholder57x57(), "spotimage");
                setColumnWidth(image, hp);
            }
        }
        hp.add(distancePanel);
        setColumnWidth(distancePanel, hp);
    }

    private void addFourSquare(Panel middleTable, LocationResult locationResult) {
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("foursquare");
        if (locationResult.getStats() != null) {
            Image image = new Image(MyWebApp.resources.fourSquare());
            flowPanel.add(image);
            InlineLabel checkins = new InlineLabel("" + locationResult.getStats().getCheckinsCount() + " Checkins");
            flowPanel.add(checkins);
            InlineLabel users = new InlineLabel(locationResult.getStats().getUsersCount() + " Users");
            flowPanel.add(users);
        }
        //show the specials?
        if (locationResult.getCompleteSpecialHolders() != null) {
            for (CompleteSpecialHolder csh : locationResult.getCompleteSpecialHolders()) {
                Label title = new Label(csh.getTitle());
                flowPanel.add(title);
            }
        }
        middleTable.add(flowPanel);
    }

    private void addTwitter(Panel middleTable, LocationResult locationResult) {
        if (locationResult.getTrends() != null) {
            for (TrendHolder th : locationResult.getTrends()) {
                Anchor anchor = new Anchor(th.getName(), th.getQuery());
                middleTable.add(anchor);
            }
        }
    }

    protected void addYahooUpcoming(Panel middleTable, List<YahooUpcomingHolder> events) {
        if (events == null) return;
        if (events.isEmpty()) return;
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("yahooupcoming");
        Image image = new Image(MyWebApp.resources.yahooUpcoming());
        flowPanel.add(image);
        for (YahooUpcomingHolder holder : events) {
            HorizontalPanel hp = new HorizontalPanel();
            if (holder.getPhotoUrl() != null) {
                Image photo = new Image(holder.getPhotoUrl());
                add(photo);
            }
            VerticalPanel vp = new VerticalPanel();
            Anchor anchor = new Anchor(holder.getName(), holder.getUrl());
            anchor.setTarget("_blank");
            vp.add(anchor);
            vp.add(new Label(holder.getStartDate() + " " + holder.getStartTime()));
            if (holder.getTicketPrice() != null) {
                vp.add(new Label("Price: " + holder.getTicketPrice()));
            }
            hp.add(vp);
            flowPanel.add(hp);
        }
        middleTable.add(flowPanel);
    }

    protected void addInstagramResultsPage(Panel middleTable, List<Data> instagrams) {
        //let's only do 2 thumbnails
        HorizontalPanel hp = new HorizontalPanel();
        middleTable.add(hp);
        int i = 0;
        for (Data data : instagrams) {
            if (i >= 2) return;
            VerticalPanel vp = new VerticalPanel();
            if (data.getImages() != null) {
                Image image = new Image(data.getImages().getThumbnailResolution().getImageUrl());
                vp.add(image);
            }
            if (data.getCaption() != null) {
                vp.add(new Label(data.getCaption().getText()));
            }
            hp.add(vp);
            i++;
        }
    }

    protected void addInstagramDetailPage(Panel middleTable, List<Data> instagrams) {
        //int k = i%j;
        int i = 0;
        HorizontalPanel hp = new HorizontalPanel();
        for (Data data : instagrams) {
            //thumbnails
            VerticalPanel vp = new VerticalPanel();
            if (i % 3 == 0) {
                hp = new HorizontalPanel();
            }
            if (data.getCaption() != null) {
                vp.add(new Label(data.getCaption().getText()));
            }
            if (data.getImages() != null) {
                Image image = new Image(data.getImages().getThumbnailResolution().getImageUrl());
                vp.add(image);
            }
            hp.add(vp);
            middleTable.add(hp);
            i++;
        }
    }

    private void addGooglePlaces(Panel middleTable, LocationResult locationResult) {
        GooglePlace googlePlace = locationResult.getGooglePlace();
        if (googlePlace != null) {
            //not really anything of value unless htere is an event
            if (googlePlace.getEvents().isEmpty()) {
                //return;
            }
            VerticalPanel flowPanel = new VerticalPanel();
            flowPanel.setStyleName("googleplaces");
            Image image = new Image(MyWebApp.resources.googlePowered());
            flowPanel.add(image);
            HorizontalPanel hp = new HorizontalPanel();
            Image iconImage = new Image(googlePlace.getIcon());
            hp.add(iconImage);
            if (googlePlace.getRating() != null) {
                Label rating = new Label("Rated " + googlePlace.getRating());
                hp.add(rating);
            }
            flowPanel.add(hp);
            for (GoogleEvent ge : googlePlace.getEvents()) {
                // HorizontalPanel hp = new HorizontalPanel();
                //Label summary = new Label(ge.getSummary());
                Anchor anchor = new Anchor(ge.getSummary(), ge.getUrl());
                flowPanel.add(anchor);
            }
            middleTable.add(flowPanel);
        }
    }

    public static void addYelpDetails(Panel middleTable, YelpHolder yelpHolder) {
        if (yelpHolder != null) {
            Anchor anchor = new Anchor("", "http://www.yelp.com");
            anchor.setTarget("_blank");
            FlowPanel flowPanel = new FlowPanel();
            flowPanel.setStyleName("yelp_marks");
            //flowPanel.getElement().setId("yelp_marks");
            Image image = new Image(MyWebApp.resources.yelp());
            anchor.getElement().appendChild(image.getElement());
            HorizontalPanel topRow = new HorizontalPanel();
            flowPanel.add(topRow);
            topRow.add(anchor);
            if (yelpHolder.getRatingImageUrlLarge() != null) {
                Image imageUrl = new Image(yelpHolder.getRatingImageUrlLarge());
                imageUrl.setTitle("Overall rating of " + yelpHolder.getRating() + " stars");
                topRow.add(imageUrl);
            }
            topRow.add(new Label("" + yelpHolder.getReviewCount() + " Reviews"));
            Anchor yelpAnchor = new Anchor("", yelpHolder.getUrl());
            if (yelpHolder.getImageUrl() != null) {
                Image imageUrl = new Image(yelpHolder.getImageUrl());
                yelpAnchor.getElement().appendChild(imageUrl.getElement());
            }
            middleTable.add(flowPanel);
        }
    }

    public static void addYelpSearchResults(Panel middleTable, YelpHolder yelpHolder) {
        if (yelpHolder != null) {
            Anchor anchor = new Anchor("", "http://www.yelp.com");
            anchor.setTarget("_blank");
            FlowPanel flowPanel = new FlowPanel();
            flowPanel.setStyleName("yelp");
            Image image = new Image(MyWebApp.resources.yelp());
            anchor.getElement().appendChild(image.getElement());
            HorizontalPanel topRow = new HorizontalPanel();
            flowPanel.add(topRow);
            topRow.add(anchor);
            if (yelpHolder.getRatingImageUrlLarge() != null) {
                Image imageUrl = new Image(yelpHolder.getRatingImageUrlLarge());
                imageUrl.setTitle("Overall rating of " + yelpHolder.getRating() + " stars");
                topRow.add(imageUrl);
            }
            topRow.add(new Label("" + yelpHolder.getReviewCount() + " Reviews"));
            Anchor yelpAnchor = new Anchor("", yelpHolder.getUrl());
            if (yelpHolder.getImageUrl() != null) {
                Image imageUrl = new Image(yelpHolder.getImageUrl());
                yelpAnchor.getElement().appendChild(imageUrl.getElement());
            }
            HorizontalPanel hp = new HorizontalPanel();
            hp.setWidth("100%");
            if (yelpHolder.getSnippetImageUrl() != null) {
                hp.add(new Image(yelpHolder.getSnippetImageUrl()));
            }
            FlowPanel fp = new FlowPanel();
            InlineLabel snippetLabel = new InlineLabel(yelpHolder.getSnippetText());
            fp.add(snippetLabel);
            Anchor anchor2 = new Anchor("Read more...", yelpHolder.getUrl());
            anchor2.setTarget("_blank");
            fp.add(anchor2);
            hp.add(fp);
            hp.add(yelpAnchor);
            flowPanel.add(hp);
            middleTable.add(flowPanel);
        }
    }

    public ClickHandler locationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("location handler");
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                LocationResult locationResult = clickMapLocation.get(b);
                mywebapp.showSpot(locationResult);
            } else {
                GWT.log("not a widget");
            }
        }
    };

    protected ComplexPanel getLocationResultPanel(LocationResult locationResult) {
        if (locationResult.getSolrDocument() == null) {
            return getLocationPanel(locationResult, locationHandler, true);
        } else {
            return getLocationResultPanelSolrNew(locationResult);
        }
    }

    /*
    <div id="result">
        <img src="ava.png" class="avatar"/>
        <input class="check" type="checkbox">
        <div class="center-block">
            <a href="#" class="result-header"><h2>@Vishal Adma, 4300 Brenner Dr, Kansas City, KS 66104</h2></a>
            <div class="tags"><a href="#">ks</a><a href="#">66104</a><a href="#">kansas city</a></div>
                <form class="comment-form">
                    <img src="" class="com-ava"/>
                    <textarea rows="1" placeholder="Leave your comment..."></textarea><input class="search-send" type="submit"><br>
                    <a class="adv">show advanced</a><a class="adv">hide</a>
                </form>
            </div>
            <a href="#" class="distanse"><h1>560</h1><p>meters avay</p></a>
        <div class="search-adv">
        <form class="adv-form">
                <label class="add-photo" title="add photo"><input type="file"></label>
                <label class="add-photo" title="add photo"><input type="file"></label>
                <label class="add-photo" title="add photo"><input type="file"></label>
                <textarea rows="1" placeholder="Secret Key"></textarea>
                <span><input type="submit">
                <p>Max file size - 5mb</p></span>
            </form>
        </div>
    </div>
    */
    protected ComplexPanel getLocationResultPanelSolrNew(LocationResult locationResult) {
        SolrDocument solrDocument = locationResult.getSolrDocument();
        Long spotId = solrDocument.getFirstLong("spotid_l");
        String targetHistoryToken = MyWebApp.SPOT_DETAIL + spotId;
        String targetHistoryToken2 = "#" + targetHistoryToken;
        FlowPanel result = new FlowPanel();
        result.setStyleName("result");
        addMarkPhoto2(solrDocument, targetHistoryToken2, result);
        FlowPanel centerBlock = new FlowPanel();
        centerBlock.setStyleName("center-block");
        result.add(centerBlock);
        //	<a href="#" class="result-header"><h2>@Vishal Adma, 4300 Brenner Dr, Kansas City, KS 66104</h2></a>
        String spot_label_s = solrDocument.getFirstString("spot_label_s");
        if (spot_label_s != null) {
            String sl = "@" + spot_label_s.toString();
            Anchor resultHeader = getResultHeader(sl);
            resultHeader.setHref(targetHistoryToken2);
            centerBlock.add(resultHeader);
        }
        ComplexPanel categoriesPanel = addCategories(solrDocument);
        centerBlock.add(categoriesPanel);
        // FlowPanel categoriesPanel = addCategories( location, showCategories);
        // centerBlock.add(categoriesPanel);
        Anchor distanceAnchor = getDistanceAnchor(locationResult);
        result.add(distanceAnchor);
        addOurBuddies(centerBlock, locationResult);
        addExpand(result, locationResult, centerBlock);
        return result;
    }

    protected ComplexPanel getLocationResultPanelSolrMark(LocationResult locationResult) {
        // vertical panel, 2 rows into each li
        // one label on top row
        // hp goes into 2nd row
        SolrDocument solrDocument = locationResult.getSolrDocument();
        Long itemId = solrDocument.getFirstLong("georepoitemid_l");
        if (itemId == null) {
            itemId = solrDocument.getFirstLong("latest_mark_georepoitemid_l");
        }
        String targetHistoryToken = MyWebApp.ITEM_DETAIL + itemId;
        String targetHistoryToken2 = "#" + targetHistoryToken;
        FlowPanel result = new FlowPanel();
        result.setStyleName("result");
        addMarkPhoto2(solrDocument, targetHistoryToken2, result);
        FlowPanel centerBlock = new FlowPanel();
        centerBlock.setStyleName("center-block");
        result.add(centerBlock);
        //	<a href="#" class="result-header"><h2>@Vishal Adma, 4300 Brenner Dr, Kansas City, KS 66104</h2></a>
        String spot_label_s = solrDocument.getFirstString("location_label_s");
        if (spot_label_s != null) {
            String sl = "@" + spot_label_s.toString();
            Anchor resultHeader = getResultHeader(sl);
            resultHeader.setHref(targetHistoryToken2);
            centerBlock.add(resultHeader);
        }
        ComplexPanel categoriesPanel = addCategories(solrDocument);
        centerBlock.add(categoriesPanel);
        // FlowPanel categoriesPanel = addCategories( location, showCategories);
        // centerBlock.add(categoriesPanel);
        Anchor distanceAnchor = getDistanceAnchor(locationResult);
        result.add(distanceAnchor);
        //addOurBuddies(centerBlock, locationResult);
        addExpand(result, locationResult, centerBlock);
        return result;
        /*

       VerticalPanel vp = new VerticalPanel();
       vp.addStyleName("markHolder");
       HorizontalPanel hp = new HorizontalPanel();
       hp.addStyleName("markHolder");
       // Long spotId = null;
       //spotId = solrDocument.getFirstLong("spotid_l");
       Long itemId = solrDocument.getFirstLong("georepoitemid_l");
       if (itemId == null) {
           itemId = solrDocument.getFirstLong("latest_mark_georepoitemid_l");
       }
       vp.add(hp);
       ListItem li = new ListItem();
       //li.setStyleName("clearing");
       li.add(vp);
       ul.add(li);
       String targetHistoryToken = MyWebApp.ITEM_DETAIL + itemId;
       String targetHistoryToken2 = "#" + targetHistoryToken;
       //need to add mark photo
       addMarkPhoto(solrDocument, targetHistoryToken, hp);
       VerticalPanel middleTable = new VerticalPanel();
       // hp.add(middleTable);
       //hp.setCellWidth(middleTable, "100%");
       middleTable.setStyleName("middletable");
       String location_label_s = solrDocument.getFirstString("location_label_s");
       if (location_label_s != null) {
           String sl = location_label_s.toString();
           Anchor spotLabel = new Anchor("@" + sl, targetHistoryToken2);
           spotLabel.setStyleName("spotLabel");
           middleTable.add(spotLabel);
       }
       ComplexPanel distancePanel = getDistancePanel(locationResult, targetHistoryToken2);
       Hyperlink latest_mark_escapedjavascriptsnippet_s = addHtml2(solrDocument, middleTable, "latest_mark_escapedjavascriptsnippet_s", targetHistoryToken);
       if (latest_mark_escapedjavascriptsnippet_s != null) {
           latest_mark_escapedjavascriptsnippet_s.addStyleName("latestMarkEscapedJavascriptSnippet");
       }
       ComplexPanel catsPanel = addCategories(solrDocument);
       middleTable.add(catsPanel);
       Hyperlink html2 = addHtml2(solrDocument, middleTable, "spot_geoRepoItemescapedjavascriptsnippet_s", targetHistoryToken);
       if (html2 != null) {
           html2.addStyleName("spotGeoRepoItemEscapedJavascriptSnippet");
       }
       //2nd image
       //spot
       if (!MyWebApp.isSmallFormat()) {
           if (alex) {
               Hyperlink userHyperLink = new Hyperlink();
               userHyperLink.setTargetHistoryToken(targetHistoryToken);
               Image image = addImage(solrDocument, hp, "image_thumbnail_130x130_url_s",
                       userHyperLink, resources.spot_image_placeholder130x130(), resources.spot_image_placeholder57x57(), "spotimage");
               setColumnWidth(image, hp);
           }
       }
       //hp.add(distancePanel);
       //setColumnWidth(distancePanel, hp);
       if (MyWebApp.isDesktop()) {
           setColumnWidth(distancePanel, hp);
           hp.add(middleTable);
           hp.add(distancePanel);
           hp.setCellWidth(middleTable, "100%");
       } else {
           setColumnWidth(distancePanel, hp);
           hp.add(middleTable);
           //not enought space
           //hp.add(distancePanel);
           hp.setCellWidth(middleTable, "100%");
       }
        */
        //return result;
    }
    //Map<LocationResult,ULPanel> locationResultLatestMarksPanelMap = new HashMap<LocationResult,ULPanel>();

    protected ULPanel getLatestMarksPanel(ExpandData expandData) {
        LocationResult locationResult = expandData.locationResult;
        GWT.log("getLatestMarksPanel");
        ULPanel ulPanel = new ULPanel();
        ulPanel.setStyleName("ex_comments");
        if (locationResult.getItemHolders() == null) {
            GWT.log("itemholders is null");
            return ulPanel;
        } else {
            GWT.log("itemholders is not null");
        }
        /*
         ...<ul class="ex_comments">
    <li><img class="ex_ava"></img><h4></h4><img class="ex_rate"></img><p></p></li>
  </ul>...

         */
        for (ItemHolder itemHolder : locationResult.getItemHolders()) {
            //height is the postion of the parents, child would be 0, parent would be 1, grandparent would be 2
            //Integer height = new Integer(0);
            GWT.log("getLatestMarksPanel adding a mark");
            addLatestMark(locationResult, ulPanel, itemHolder, 0, null, expandData);
        }
        return ulPanel;
    }

    protected void addLatestMark(LocationResult locationResult, ULPanel middleTable, ItemHolder itemHolder, int parentCount, ItemHolder parentItemHolder, ExpandData expandData) {
        //addParentRefs(locationResult,middleTable,itemHolder);
        addLatestMarkOrig(locationResult, middleTable, itemHolder, parentCount, parentItemHolder, expandData);
        for (ItemHolder child : itemHolder.getChildrenItemHolders()) {
            //height is the postion of the parents, child would be 0, parent would be 1, grandparent would be 2
            //Integer height = new Integer(0);
            addLatestMark(locationResult, middleTable, child, parentCount + 1, itemHolder, expandData);
        }
    }

    protected void addLatestMarkOrig(LocationResult locationResult, ULPanel middleTable, ItemHolder itemHolder, int parentCount, ItemHolder parentItemHolder, ExpandData parentExpandData) {
        String snippet = itemHolder.getSnippet();
        if (snippet == null) {
            return;
        }
        ListItem listItem = new ListItem();
        if (parentCount == 0) {
            listItem.addStyleName("p0");
        } else if (parentCount == 1) {
            listItem.addStyleName("p1");
        } else if (parentCount > 1) {
            listItem.addStyleName("p2");
        }
        Anchor usernameReplyAnchor = null;
        Long userId = null;
        Image userImage = null;
        UserHolder itemUser = itemHolder.getUserHolder();
        if (itemUser != null) {
            userId = itemUser.getId();
            userImage = getImage(itemUser.getContentHolder(), "57x57");
            usernameReplyAnchor = new Anchor("@" + itemUser.getUsername());
        } else {
            usernameReplyAnchor = new Anchor("@anonymous");
        }
        usernameReplyAnchor.addClickHandler(replyHandler);
        usernameReplyAnchor.setStyleName("mark_username");
        if (userImage == null) {
            userImage = new Image(MyWebApp.resources.anon57x57());
        }
        userImage.setStyleName("ex_ava");
        if (userId != null) {
            String userHistoryToken = MyWebApp.VIEW_USER_PROFILE + userId;
            String userHistoryToken2 = "#" + userHistoryToken;
            Anchor userAnchor = new Anchor("", userHistoryToken2);
            userAnchor.getElement().appendChild(userImage.getElement());
            listItem.add(userAnchor);
        } else {
            listItem.add(userImage);
        }
        if (parentItemHolder != null) {
            UserHolder replyUser = parentItemHolder.getUserHolder();
            if (replyUser != null) {
                InlineLabel label = new InlineLabel("RE:  " + replyUser.getUsername());
                label.setStyleName("replyto");
                listItem.add(label);
            } else {
                InlineLabel label = new InlineLabel("RE:  @anonymous");
                label.setStyleName("replyto");
                listItem.add(label);
            }
        }
        //this is a link to reply using the reply
        listItem.add(usernameReplyAnchor);
        Long itemId = itemHolder.getId();
        String targetHistoryToken = MyWebApp.ITEM_DETAIL + itemId;
        String targetHistoryToken2 = "#" + targetHistoryToken;
        //i don't understand why we are using parent user here
        Anchor snippetAnchor = new Anchor(snippet, targetHistoryToken2);
        snippetAnchor.setStyleName("mark_snippet");
        listItem.add(snippetAnchor);
        FlowPanel replyGoesHerePanel = new FlowPanel();
        MarkData replyData = new MarkData();
        replyData.itemHolder = itemHolder;
        ExpandData expandData = new ExpandData();
        expandData.resultPanel = parentExpandData.resultPanel;
        expandData.replyGoesHere = replyGoesHerePanel;
        expandData.locationResult = locationResult;
        replyData.expandData = expandData;
        listItem.add(replyGoesHerePanel);
        replyMap.put(usernameReplyAnchor, replyData);
        ContentHolder itemContentHolder = itemHolder.getContentHolder();
        if (itemContentHolder != null) {
            for (ContentHolder contentHolder : itemContentHolder.getContentHolders()) {
                Image markImage = getImage(contentHolder, "130x130");
                if (markImage != null) {
                    InlineLabel hm_img_thumb = new InlineLabel();
                    hm_img_thumb.setStyleName("hm_img_thumb");
                    listItem.add(hm_img_thumb);
                    markImage.setStyleName("hm_img");
                    InlineLabel hm_img_tt = new InlineLabel();
                    hm_img_tt.setStyleName("hm_img_tt");
                    hm_img_tt.getElement().appendChild(markImage.getElement());
                    hm_img_thumb.getElement().appendChild(hm_img_tt.getElement());
                    Image _img = getImage(contentHolder, "130x130");
                    _img.setStyleName("_img");
                    hm_img_thumb.getElement().appendChild(_img.getElement());
                }
            }
        }
//        if (MyWebApp.isDesktop()) {
//            Image markImage = getImage(itemHolder.getContentHolder(), "130x130");
//            if (markImage != null) {
//                Anchor markImageAnchor = new Anchor();
//                markImageAnchor.setHref(targetHistoryToken2);
//                markImageAnchor.getElement().appendChild(markImage.getElement());
//                listItem.add(markImageAnchor);
//            }
//        }
        middleTable.add(listItem);
    }

    public static class MarkData {
        public SpotHolder spotHolder = null;
        public ExpandData expandData = null;
        public ItemHolder replyItemHolder = null;
        public ItemHolder itemHolder = null;
        public FormPanel formPanel = null;
        public TextArea saySomethingTextArea = null;
        public SuggestBox tagSearchTextBox = null;
        public TextBox secretKeyTextBox = new TextBox();
        public SimpleCheckBox shareOnFacebookCheckbox = new SimpleCheckBox();
        //public List<TagHolder> tagHolders = new ArrayList<TagHolder>();
    }

    Map<Widget, MarkData> replyMap = new HashMap<Widget, MarkData>();

    protected FlowPanel addCategories(SolrDocument solrDocument) {
        FlowPanel categoriesPanel = new FlowPanel();
        categoriesPanel.setStyleName("tags");
        List<String> cats = solrDocument.getStrings("cat");
        //<div class="tags"><a href="#">ks</a><a href="#">66104</a><a href="#">kansas city</a></div>
        if (cats != null) {
            for (String cat : cats) {
                TagHolder tagHolder = new TagHolder(cat);
                Anchor catLabel = new Anchor(cat);
                catLabel.addClickHandler(clickTagHandler2);
                tagHolderClickMap.put(catLabel, tagHolder);
                //catLabel.addStyleName("linky");
                categoriesPanel.add(catLabel);
            }
        } else {
            GWT.log("cat_txt is null");
        }
        return categoriesPanel;
    }

    protected void addHomePageResult(ULPanel ul, LocationResult locationResult) {
        SolrDocument solrDocument = locationResult.getSolrDocument();
        boolean isSpot = isSpot(solrDocument);
        ListItem li = new ListItem();
        if (isSpot) {
            ComplexPanel vp = getLocationResultPanel(locationResult);
            li.add(vp);
            ul.add(li);
        } else {
            // addMarkHomePageResult(ul, locationResult);
            ComplexPanel vp = getLocationResultPanelSolrMark(locationResult);
            li.add(vp);
            ul.add(li);
        }
    }

    //
    protected ComplexPanel getDistancePanel(Widget row1, Widget row2, LocationResult locationResult) {
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("distancePanel");
        vp.add(row1);
        vp.add(row2);
        if (locationResult.getDistance() < 2000) {
            vp.addStyleName("close");
            // return "close";
        } else if (locationResult.getDistance() < 4000) {
            vp.addStyleName("medium");
        } else {
            vp.addStyleName("far");
        }
        return vp;
    }

    private static double METERS_TO_MILES = 0.000621371D;
    //1 meter = 1.09361 yard
    private static double YARD = 1.09361D;

    protected Anchor getDistanceHyperlink(LocationResult locationResult, String target) {
        //0.000621371 mile in 1 meter
        //int distance = ddistance.intValue();
        //conversion to show km or miles
        String val = getDistanceDisplay(locationResult);
        Anchor distanceLabel = new Anchor(val, target);
        distanceLabel.setStyleName("distance");
        return distanceLabel;
    }

    private String getDistanceDisplay(LocationResult locationResult) {
        double displayDistance = 0.0D;
        Double ddistance = new Double(locationResult.getDistance());
        if (mywebapp.isShowMeters()) {
            displayDistance = locationResult.getDistance() / 1000;
        } else {
            //distance is in meters
            displayDistance = locationResult.getDistance() * METERS_TO_MILES;
        }
        String val = NumberFormat.getFormat("000000.0").format(displayDistance);
        if (displayDistance > 1.0D) {
            return val;
        } else {
            return new Integer(ddistance.intValue()).toString();
        }
    }

    protected Label getDistanceLabel(LocationResult locationResult) {
        Double ddistance = new Double(locationResult.getDistance());
        int distance = ddistance.intValue();
        Label distanceLabel = new Label(new Integer(distance).toString());
        distanceLabel.setWidth("100%");
        distanceLabel.setStyleName("distance");
        distanceLabel.addStyleName("linky");
        //walking, biking, else
        return distanceLabel;
    }

    protected void setColumnWidth(Widget image, HorizontalPanel hp) {
        if (image == null) {
            return;
        }
        hp.setCellWidth(image, "1%");
    }

    protected Hyperlink addHtml2(SolrDocument result, Panel hp, String fieldName, String targetHistoryToken) {
        String val = result.getFirstString(fieldName);
        if (val != null) {
            Hyperlink hyperlink = new Hyperlink(val, targetHistoryToken);
            hp.add(hyperlink);
            return hyperlink;
        }
        // hp.add(sp);
        return null;
    }
//    protected ComplexPanel getDistancePanel(LocationResult locationResult, String targetHistoryToken2) {
//        Anchor distanceLabel = getDistanceHyperlink(locationResult, targetHistoryToken2);
//        Anchor metersLabel = new Anchor("alsdkf meters away", targetHistoryToken2);
//        FlowPanel metersLabel2 = new FlowPanel();
//        metersLabel2.setStyleName("gwt-Label");
//        metersLabel2.add(metersLabel);
//        ComplexPanel distancePanel = getDistancePanel(distanceLabel, metersLabel2, locationResult);
//        return distancePanel;
//    }
//
//    protected HTML addHtml(SolrDocument result, Panel hp, String fieldName,
//                           ClickHandler clickHandler, Long theid) {
//        if (clickHandler == null) return null;
//        String val = result.getFirstString(fieldName);
//        if (val != null) {
//            HTML html = new HTML(val.toString());
//            html.addClickHandler(clickHandler);
//            clickMapLong.put(html, theid);
//            html.addStyleName("linky");
//            hp.add(html);
//            return html;
//        }
//        // hp.add(sp);
//        return null;
//    }
    private SimplePanel topPanelHolder = new SimplePanel();

    public SimplePanel getTopPanelHolder() {
        return topPanelHolder;
    }
    // protected HandlerRegistration formHandlerRegistration = null;

    public AsyncCallback getSaveLocationAsSpotCallback(final MarkData markData) {
        AsyncCallback saveLocationAsSpotCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                MobileResponse mobileResponse = (MobileResponse) response;
                if (mobileResponse.getStatus() == 1) {
                    leavemark(mobileResponse.getSpotHolder().getId(), markData);
                } else {
                    mywebapp.verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        };
        return saveLocationAsSpotCallback;
    }

    protected void saveStatus(MarkData markData) {
        postDialog.hide();
        LocationResult locationResult = markData.expandData.locationResult;
        if (markData.spotHolder != null) {
            leavemark(markData.spotHolder.getId(), markData);
        } else if (locationResult == null) {
            //going to be current location
            LocationResult currentLocationResult = new LocationResult();
            Location currentLocation = mywebapp.getCurrentLocation();
            currentLocation.setName("Location");
            currentLocationResult.setLocation(mywebapp.getCurrentLocation());
            //let's set to type of 3, a location
            currentLocationResult.getLocation().setSpotType(3);
            AsyncCallback callback = getSaveLocationAsSpotCallback(markData);
            mywebapp.saveLocationAsSpot(currentLocationResult, callback);
        } else if (locationResult.getLocation() != null) {
            AsyncCallback callback = getSaveLocationAsSpotCallback(markData);
            mywebapp.saveLocationAsSpot(locationResult, callback);
        } else if (locationResult.getSolrDocument() != null) {
            Long spotId = locationResult.getSolrDocument().getFirstLong("spotid_l");
            leavemark(spotId, markData);
        } else {
            throw new RuntimeException("saveStatus error");
        }
    }

    protected void leavemark(Long spotId, final MarkData markData) {
        GWT.log("leavemark");
        final TextArea selectedAutoGrowTextArea = markData.saySomethingTextArea;
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        leaveMarkRequest.setAuthToken(mywebapp.getAuthToken());
        ItemHolder itemHolder = new ItemHolder();
        //set the replyitemid, if it's null, no harm!
        // replyItem.setParentItemHolder(getItemHolder());
        if (markData != null) {
            if (markData.replyItemHolder != null) {
                //ItemHolder parentItem = new ItemHolder();
                //parentItem.setId(markData.replyItemId);
                itemHolder.setParentItemHolder(markData.replyItemHolder);
                GWT.log("markData.replyItemHolder not null");
            } else {
            }
            GWT.log("markData.replyItemHolder is null");
        }
        itemHolder.getSpotHolder().setId(spotId);
        leaveMarkRequest.setItemHolder(itemHolder);
        //need to add the tags
        //List<TagHolder> tagHolders = widgetTagHoldersMap.get(tagSearchTextBox);
        if (markData.tagSearchTextBox != null) {
            GWT.log("adding tag to item");
            List<TagHolder> tagHolders = widgetTagHoldersMap.get(markData.tagSearchTextBox);
            if (tagHolders != null) {
                for (TagHolder th : tagHolders) {
                    GWT.log("adding tag " + th.getName());
                }
                itemHolder.getTagHolders().addAll(tagHolders);


            }

        } else {
            GWT.log("tagSearchTextBox is null");
        }
        itemHolder.setTextData(selectedAutoGrowTextArea.getValue());
        itemHolder.setShareOnFacebook(markData.shareOnFacebookCheckbox.getValue());
        itemHolder.setPassword(markData.secretKeyTextBox.getValue());
        leaveMarkRequest.setAuthToken(mywebapp.getAuthToken());
        postDialog = new DataOperationDialog("Saving...");
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.leavemark(leaveMarkRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Your mark has been saved");
                    if (markData.expandData.resultPanel == null) {
                        //we are doing the full screen
                        ItemHolder itemHolder = mobileResponse.getItemHolder();
                        History.newItem(MyWebApp.ITEM_DETAIL + itemHolder.getId());
                    } else {
                        addMarkData(markData, mobileResponse);
                        markData.expandData.resultPanel.clear();
                        MarkData markData1 = expandLocation(markData.expandData);
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    /*
   this will take markdata form and and add to locationresult
    */
    private void addMarkData(MarkData markData, MobileResponse mobileResponse) {
        //markData.saySomethingTextArea.getValue()
        ItemHolder itemHolder = mobileResponse.getItemHolder();
        GWT.log("adding itemholder " + itemHolder.getId());
        if (markData.replyItemHolder != null) {
            markData.replyItemHolder.getChildrenItemHolders().add(itemHolder);
        } else {
            if (markData.expandData.locationResult.getItemHolders() == null) {
                markData.expandData.locationResult.setItemHolders(new ArrayList<ItemHolder>());
            }
            markData.expandData.locationResult.getItemHolders().add(itemHolder);
        }
    }

    AsyncCallback markSaved = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your mark has been saved");
        }
    };

    protected void setupRootPanelForm(FormPanel myform, final MarkData markData) {
        markData.formPanel = myform;
        GWT.log("setupRootPanelForm markData style");
        myform.setEncoding(FormPanel.ENCODING_MULTIPART);
        myform.setMethod(FormPanel.METHOD_POST);
        myform.setAction(mywebapp.getHostUrl() + "/saveMediaFile");
        myform
                .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        GWT.log(event.getResults());
                        GWT.log("saveMediaFile complete markdata style");
                        if (event.getResults().equals("SUCCESS")) {
                            doSave(markData);
                        } else {
                            postDialog.hide();
                            mywebapp.getMessagePanel().displayError(event.getResults());
                        }
                    }
                });
    }

    protected void setupRootPanelForm() {
        GWT.log("setupRootPanelForm default style");
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setAction(mywebapp.getHostUrl() + "/saveMediaFile");
        formPanel
                .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
                    public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
                        //need to remove
                        //formHandlerRegistration.removeHandler();
                        GWT.log(event.getResults());
                        mywebapp.log("saveMediaFile complete non-markdata style");
                        if (event.getResults().equals("SUCCESS")) {
                            doSave();
                        } else {
                            postDialog.hide();
                            mywebapp.getMessagePanel().displayError(event.getResults());
                        }
                    }
                });
    }

    protected CheckBox addCheckbox2(String label, String formname, boolean value, ImageResource imageResource) {
        CheckBox checkbox = new CheckBox(label);
        Image img = null;
        if (imageResource != null) {
            img = new Image(imageResource);
        }
        checkbox.setName(formname);
        checkbox.setValue(value);
        addCheckbox(checkbox, img, this);
        return checkbox;
    }

    protected void addCheckbox(CheckBox checkbox, Image img, ComplexPanel parentPanel) {
        Fieldset fs = new Fieldset();
        fs.add(checkbox);
        if (img != null) {
            SimplePanel sp = new SimplePanel();
            sp.add(img);
            fs.add(sp);
        }
        parentPanel.add(fs);
    }

    protected TextArea addTextArea(String label, String formname, String formvalue, boolean readonly) {
        TextArea textBox = new TextArea();
        textBox.setName(formname);
        textBox.setValue(formvalue);
        textBox.setReadOnly(readonly);
        addFieldset(textBox, label, formname);
        return textBox;
    }

    protected ClickHandler cancelHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleBack();
        }
    };
    protected Map<Widget, ItemHolder> clickMapItemHolder = new HashMap<Widget, ItemHolder>();

    protected void addItems2(List<ItemHolder> items, String label,
                             ClickHandler messageHandler) {
        addItems(items, label, messageHandler);
    }

    protected void addItems(List<ItemHolder> items, String label,
                            ClickHandler messageHandler) {
        Label mylabel = new Label(label);
        mylabel.setStyleName("h1");
        add(mylabel);
        if (items.isEmpty()) {
            add(new Label("No results to display."));
        }
        final Logger logger = new Logger();
        if (items.isEmpty()) {
            return;
        }
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (ItemHolder itemHolder : items) {
            addResult(ul, itemHolder, messageHandler);
        }
    }

    protected void addResult(ULPanel ul, ItemHolder result,
                             ClickHandler messageHandler) {
        GWT.log("addResult");
        HorizontalPanel hp = new HorizontalPanel();
        ListItem li = new ListItem();
        //li.setStyleName("clearing");
        li.add(hp);
        ul.add(li);
        if (!MyWebApp.isSmallFormat()) {
            if (result.getUserHolder() != null) {
                ContentHolder contentHolder = result.getUserHolder().getContentHolder();
                if (contentHolder != null) {
                    Image userImage = addImageContent(hp, contentHolder, "130x130");
                    hp.setCellWidth(userImage, "1%");
                    userImage.addClickHandler(messageHandler);
                    userImage.addStyleName("linky");
                    clickMapItemHolder.put(userImage, result);
                }
            }
        }
        VerticalPanel middleTable = new VerticalPanel();
        hp.add(middleTable);
        hp.setCellWidth(middleTable, "100%");
        middleTable.setStyleName("middletable");
        Label subject = new Label(result.getTitle());
        clickMapItemHolder.put(subject, result);
        subject.addClickHandler(messageHandler);
        subject.addStyleName("linky");
        middleTable.add(subject);
//        Label createDate = new Label(result.getCreateDateDisplay());
//        middleTable.add(createDate);
        if (result.getContentHolder() != null) {
            Image myimage = addImageContent(hp, result.getContentHolder(), "130x130");
            if (myimage != null) {
                myimage.addClickHandler(messageHandler);
                clickMapItemHolder.put(myimage, result);
                myimage.addStyleName("linky");
                hp.setCellWidth(myimage, "1%");
            }
        }
    }

    private boolean isDeleted(ContentHolder contentHolder) {
        for (ContentHolder ch : contentsToRemove) {
            if (ch.getId().equals(contentHolder.getId())) {
                return true;
            }
        }
        return false;
    }

    ContentHolder parentContentHolder = null;
    FlowPanel bigImageDisplayPanel = new FlowPanel();

    protected void addContentHolder(ContentHolder contentHolder, boolean deleteLinks, boolean bigFirstImage) {
        contentsPanel.clear();
        this.parentContentHolder = contentHolder;
        if (contentHolder == null) {
            GWT.log("contentHolder is null");
            return;
        }
        if (contentHolder.getContentHolders() == null) {
            debug("contentHolder.getContentHolders() is null");
            return;
        }
        //image an image holder for the 320x320 image that will be displayed here when
        //the 130x130 is clicked.
        //let's use the first image to prepolate this 320x320 image
        contentsPanel.add(bigImageDisplayPanel);
        if (contentHolder.getContentHolders() == null) {
            return;
        }
        debug("contentHolder.getContentHolders().length()=" + contentHolder.getContentHolders().size());
        for (ContentHolder ch : contentHolder.getContentHolders()) {
            boolean deleted = isDeleted(ch);
            //http://code.google.com/p/sfeir/source/browse/trunk/html5-slides/src/com/sfeir/gwt/html5/client/slides/Slide22.java?r=800
            if (deleted) {
                //since deleted, do not display
            } else if (ch.getVideo()) {
                debug("it's a video");
                addMedia(contentsPanel, ch);
                addDeleteLink(contentsPanel, ch, deleteLinks);
            } else if (ch.getImage()) {
                debug("it is an image");
                if (bigFirstImage) {
                    bigFirstImage = false;
                    addImageContent(bigImageDisplayPanel, ch, "320x320");
                }
                //if there is ONLY one image, no need to add small image to the big image
                //but we still need a delete link if this is showing delete links
                if ((deleteLinks) || (contentHolder.getContentHolders().size() > 1)) {
                    Image image = addImageContent(contentsPanel, ch, "130x130");
                    image.addClickHandler(showBiggerHandler);
                    mediaMap.put(image, ch);
                    addDeleteLink(contentsPanel, ch, deleteLinks);
                }
            } else if (ch.getAudio()) {
                addMedia(contentsPanel, ch);
                addDeleteLink(contentsPanel, ch, deleteLinks);
            } else {
                addAnchor(contentsPanel, ch);
                addDeleteLink(contentsPanel, ch, deleteLinks);
            }
        }
    }

    protected void debug(String message) {
        //GWT.log(message);
        mywebapp.log(message);
    }

    public String getPageDescription() {
        return null;
    }

    public String getPageTitle() {
        return getTitle();
    }

    //holds all the content, so if we remove one, we can just clear and rebuild this one panel
    protected VerticalPanel contentsPanel = new VerticalPanel();
    ClickHandler deleteHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ContentHolder contentHolder = mediaMap.get(b);
                //remove the content
                debug("adding contentholder to remove list " + contentHolder.getFileName() + ",id=" + contentHolder.getId());
                contentsToRemove.add(contentHolder);
                bigImageDisplayPanel.clear();
                //rebuild panel
                //how do we get the main (parent) contentholder
                addContentHolder(parentContentHolder, true, true);
            }
        }
    };
    //here is where we store the contents that we need to remove
    protected List<ContentHolder> contentsToRemove = new ArrayList<ContentHolder>();
    ClickHandler viewMediaHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //GWT.log("selected location");
            //toggleMap();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ContentHolder contentHolder = mediaMap.get(b);
                displayMedia(contentHolder);
            }
        }
    };
    protected ClickHandler showBiggerHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ContentHolder contentHolder = mediaMap.get(b);
                bigImageDisplayPanel.clear();
                addImageContent(bigImageDisplayPanel, contentHolder, "320x320");
            }
        }
    };
    protected Map<Widget, ContentHolder> mediaMap = new HashMap<Widget, ContentHolder>();
    //clicking on image makes bigImagePanel populate
    //    Map<Image, ContentHolder> biggerImageMap = new HashMap<Image, ContentHolder>();

    private static class MediaPopup extends PopupPanel {
        public MediaPopup() {
            // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
            // If this is set, the panel closes itself automatically when the user
            // clicks outside of it.
            super(true);
            // PopupPanel is a SimplePanel, so you have to set it's widget property to
            // whatever you want its contents to be.
            //setWidget(new Label("Click outside of this popup to close it"));
        }
    }

    private void displayMedia(ContentHolder contentHolder) {
        SimplePanel panel = new SimplePanel();   // create panel to hold the player
        //panel.setWidth("100%");
        double width = Window.getClientWidth() * 0.9;
        Integer iwidth = new Double(width).intValue();
        panel.setWidth("" + iwidth + "px");
        GWT.log("setting width to " + iwidth);
        AbstractMediaPlayer player = null;
        try {
            MediaPopup mediaPopup = new MediaPopup();
            mediaPopup.setWidget(panel);
            // get any player that can playback media
            String url = contentHolder.getWebServerAssetHolder().getUrl();
            // "http://www.example.com/some-funny-video.qt"
            player = PlayerUtil.getPlayer(Plugin.Auto,
                    url,
                    false, "90%", "90%");
            panel.setWidget(player); // add player to panel.
            //
            mediaPopup.show();
        } catch (LoadException e) {
            // catch loading exception and alert user
            mywebapp.getMessagePanel().displayError("An error occured while loading");
        } catch (PluginVersionException e) {
            // catch PluginVersionException, thrown if required plugin version is not found
            panel.setWidget(PlayerUtil.getMissingPluginNotice(e.getPlugin()));
        } catch (PluginNotFoundException e) {
            // catch PluginNotFoundException, thrown if no plugin is not found
            panel.setWidget(PlayerUtil.getMissingPluginNotice(e.getPlugin()));
        }
    }

    private void addMedia(Panel vp, ContentHolder contentHolder) {
        Label label = new Label();
        label.setText("View " + contentHolder.getFileName());
        label.addClickHandler(viewMediaHandler);
        label.setStyleName("whiteButton");
        vp.add(label);
        mediaMap.put(label, contentHolder);
    }

    private void addDeleteLink(Panel vp, ContentHolder contentHolder, boolean deleteLink) {
        if (deleteLink) {
            ImageResource deleteImageIR = MyWebApp.resources.deleteX();
            Image deleteImage = new Image(deleteImageIR);
            deleteImage.addClickHandler(deleteHandler);
            vp.add(deleteImage);
            mediaMap.put(deleteImage, contentHolder);
        }
    }

    private void addAnchor(Panel vp, ContentHolder contentHolder) {
        debug("addAnchor " + contentHolder.getFileName());
        Anchor anchor = new Anchor();
        if (contentHolder.getWebServerAssetHolder() == null) {
            debug("addAnchor contentHolder.getWebServerAssetHolder() is null!!");
            return;
        }
        anchor.setHref(contentHolder.getWebServerAssetHolder().getUrl());
        anchor.setText(contentHolder.getFileName());
        vp.add(anchor);
    }

    /*
    *
    *
    * <video width="320" height="240" controls="controls"> <source
    * src="movie.mp4" type="video/mp4" /> <source src="movie.ogg"
    * type="video/ogg" /> Your browser does not support the video tag. </video>
    * Image image = new Image(); image.setStyleName("mainImg");
    * image.setUrl(spotHolder
    * .getDefaultImageItemHolder().getImageScaleHolder320x320()
    * .getWebServerAssetHolder().getUrl()); add(image);
    */
    // private Hidden spotIdHidden = new Hidden();
    protected FormPanel formPanel = new FormPanel();

    public FormPanel getFormPanel() {
        return formPanel;
    }

    public ClickHandler clickAudioHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            captureAudio();
        }
    };
    public ClickHandler clickVideoHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            captureVideo();
        }
    };
    public ClickHandler clickLibraryHandler = new ClickHandler() {
        /**
         * Fired when the user clicks on the sendButton.
         */
        public void onClick(ClickEvent event) {
            Camera c = Camera.getCamera();
            if (!Camera.isSupported()) {
                GWT.log("Camera not supported!");
                return;
            }
            c.getPictureFromLibrary(new CameraCallback() {
                public void onSuccess(String picture) {
                    GWT.log("picture=" + picture);
                    pictureDataHidden.setValue(picture);
                    image.setUrl("data:image/jpeg;base64," + picture);
                }

                public void onFailure(String error) {
                    GWT.log("getPictureFromCamera" + error);
                }
            });
        }
    };
    public ClickHandler clickCameraHandler = new ClickHandler() {
        /**
         * Fired when the user clicks on the sendButton.
         */
        public void onClick(ClickEvent event) {
            // sendNameToServer();
            Camera c = Camera.getCamera();
            if (!Camera.isSupported()) {
                GWT.log("Camera not supported!");
                return;
            }
            c.getPictureFromCamera(new CameraCallback() {
                public void onSuccess(String picture) {
                    GWT.log("picture=" + picture);
                    pictureDataHidden.setValue(picture);
                    image.setUrl("data:image/jpeg;base64," + picture);
                }

                public void onFailure(String error) {
                    GWT.log("getPictureFromCamera" + error);
                }
            });
        }
    };

    protected void addVideoCapture(ComplexPanel parentPanel) {
        Label label = new Label("Capture Video");
        label.addClickHandler(clickVideoHandler);
        fixButton(label);
        parentPanel.add(label);
    }

    protected void addCameraButton(ComplexPanel parentPanel) {
        // {limit:2});
        Label label = new Label("Take Photo");
        label.addClickHandler(clickCameraHandler);
        fixButton(label);
        if (Camera.isSupported()) {
            parentPanel.add(label);
        }
    }

    protected void addLibraryButton(ComplexPanel parentPanel) {
        // {limit:2});
        Label label = new Label("Image Library");
        label.addClickHandler(clickLibraryHandler);
        fixButton(label);
        if (Camera.isSupported()) {
            parentPanel.add(label);
        }
    }

    protected void addAudioCapture(ComplexPanel parentPanel) {
        // navigator.device.capture.captureAudio(captureSuccess, captureError,
        // {limit:2});
        Label label = new Label("Capture Audio");
        label.addClickHandler(clickAudioHandler);
        fixButton(label);
        // Button btn = new Button("Capture Audio", new MyHandler());
        // btn.setStyleName("whiteButton");
        parentPanel.add(label);
    }

    Map<Button, MediaFile> mediaFileButtonMap = new HashMap<Button, MediaFile>();
    protected VerticalPanel mediaFilesPanel = new VerticalPanel();
    public ClickHandler removeMediaFileHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            List<MediaFile> list = new ArrayList<MediaFile>();
            MediaFile mf = mediaFileButtonMap.get(event.getSource());
            for (MediaFile mediaFile : mediaFiles) {
                if (mf.getFullPath().equals(mediaFile.getFullPath())) {
                } else {
                    list.add(mediaFile);
                }
            }
            mediaFiles.clear();
            mediaFiles.addAll(list);
        }
    };

    protected void uploadMediaFiles() {
        // string url
        GWT.log("uploadMediaFiles");
        String url = mywebapp.getHostUrl() + "/saveMediaFile";
        GWT.log("url=" + url);
        for (MediaFile mediaFile : mediaFiles) {
            GWT.log("mediaFile loop");
            FileTransfer.uploadFile(mediaFile, url);
        }
    }

    protected void viewMessage(ItemHolder itemHolder) {
        ViewMessagePanel viewMessagePanel = new ViewMessagePanel(mywebapp, itemHolder);
        mywebapp.swapCenter(viewMessagePanel);
    }

    protected void addMediaFilesPanel() {
        mediaFilesPanel.clear();
        mediaFileButtonMap.clear();
        for (MediaFile mediaFile : mediaFiles) {
            HorizontalPanel hp = new HorizontalPanel();
            mediaFilesPanel.add(hp);
            hp.add(new Label(mediaFile.getName()));
            Button btn = new Button("Remove");
            btn.setStyleName("whiteButton");
            hp.add(btn);
            mediaFileButtonMap.put(btn, mediaFile);
            btn.addClickHandler(removeMediaFileHandler);
        }
    }

    VerticalPanel fileUploadPanel = new VerticalPanel();
    private int i = 1;

    private void addFileUploadField() {
        //need to have button that will add another file upload
        FileUpload upload = new FileUpload();
        upload.setName("fileUpload" + i);
        fileUploadPanel.add(upload);
        i++;
    }

    class AddFileUpload implements ClickHandler {
        public void onClick(ClickEvent event) {
            addFileUploadField();
        }
    }

    protected boolean isUploadSupported() {
//        GWT.log("MGWT.getOsDetection().isTablet()=" + MGWT.getOsDetection().isTablet());
//        GWT.log("MyWebApp.isMobileDevice()=" + MyWebApp.isMobileDevice());
        if (MyWebApp.isDesktop()) {
            return true;
        } else {
            return false;
        }
//
//
//        if (MyWebApp.isMobileDevice()) {
//            return false;
//        }
//
//        return true;
    }

    protected MultiUploader defaultUploader = null;
    protected FlowPanel panelImages = new FlowPanel();

    protected void addUpload(String prompt, ComplexPanel parentPanel) {
        if (!isUploadSupported()) {
            return;
        }
        VerticalPanel vp = new VerticalPanel();
        panelImages.setStyleName("images_uploaded_preview");
        defaultUploader = new MultiUploader();
        vp.add(defaultUploader);
        Label limitLabel = new Label("There is a 50 mg limit on file uploads");
        vp.add(limitLabel);
        addFieldset(vp, prompt, "multiUploader", parentPanel);
        //add(defaultUploader);
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
    }

    protected void addMediaFields(String prompt) {
        addMediaFields(prompt, this);
    }

    protected void addMediaFields(String prompt, ComplexPanel parentPanel) {
        addUpload(prompt, parentPanel);
        //addFileUpload();
        if (mywebapp.isMobileDevice()) {
            addCameraStuff(parentPanel);
            addAudioCapture(parentPanel);
            addVideoCapture(parentPanel);
            addMediaFilesPanel();
            parentPanel.add(mediaFilesPanel);
        }
    }

    protected IUploader.OnFinishUploaderHandler getOnFinishUploaderHandlerDetails(final FlowPanel panelImages) {
        // Load the image in the document and in the case of success attach it to the viewer
        IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
            public void onFinish(IUploader uploader) {
                if (uploader.getStatus() == Status.SUCCESS) {
                    PreloadedImage preloadedImage = new PreloadedImage(uploader.fileUrl(), showImageOnDetails);
                    // The server sends useful information to the client by default
                    preloadedImagePanelMap.put(preloadedImage, panelImages);
                    UploadedInfo info = uploader.getServerInfo();
                }
            }
        };
        return onFinishUploaderHandler;
    }

    protected IUploader.OnFinishUploaderHandler getOnFinishUploaderHandler(final FlowPanel panelImages) {
        // Load the image in the document and in the case of success attach it to the viewer
        IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
            public void onFinish(IUploader uploader) {
                if (uploader.getStatus() == Status.SUCCESS) {
                    PreloadedImage preloadedImage = new PreloadedImage(uploader.fileUrl(), showImage);
                    // The server sends useful information to the client by default
                    preloadedImagePanelMap.put(preloadedImage, panelImages);
                    UploadedInfo info = uploader.getServerInfo();
                }
            }
        };
        return onFinishUploaderHandler;
    }

    protected IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == Status.SUCCESS) {
                PreloadedImage preloadedImage = new PreloadedImage(uploader.fileUrl(), showImage);
                // The server sends useful information to the client by default
                preloadedImagePanelMap.put(preloadedImage, panelImages);
                UploadedInfo info = uploader.getServerInfo();
            }
        }
    };
    // Attach an image to the pictures viewer
    private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
        public void onLoad(PreloadedImage image) {
            image.setVisible(false);
            FlowPanel panelImages = preloadedImagePanelMap.get(image);
            // panelImages.add(image);
            //need to get the panel we need to add to it
            //image.setWidth("75px");
            // panelImages.add(image);
            // saveSessionContents();
            panelImages.setStyleName("ma_files");
            InlineLabel inlineLabel = new InlineLabel();
            panelImages.add(inlineLabel);
            Element element = DOM.createElement("i");
            inlineLabel.getElement().appendChild(element);
            element.setClassName("md_file_thumb");
            String theStyle = "background: url('" + image.getUrl() + "') no-repeat center center;";
            element.setAttribute("style", theStyle);
            /*
           <div class="ma_files">
               <span>
                   <i class="md_file_thumb" style="backgound: url('_uploaded_image_') no-repeat center center;"></i>
               </span>
           </div>


            */
        }
    };
    private OnLoadPreloadedImageHandler showImageOnDetails = new OnLoadPreloadedImageHandler() {
        public void onLoad(PreloadedImage image) {
            image.setVisible(false);
            FlowPanel panelImages = preloadedImagePanelMap.get(image);
            // panelImages.add(image);
            //need to get the panel we need to add to it
            //image.setWidth("75px");
            // panelImages.add(image);
            // saveSessionContents();
            panelImages.setStyleName("md_files");
            InlineLabel inlineLabel = new InlineLabel();
            panelImages.add(inlineLabel);
            Element element = DOM.createElement("i");
            inlineLabel.getElement().appendChild(element);
            element.setClassName("md_file_thumb");
            String theStyle = "background: url('" + image.getUrl() + "') no-repeat center center;";
            element.setAttribute("style", theStyle);
            /*
           <div class="ma_files">
               <span>
                   <i class="md_file_thumb" style="backgound: url('_uploaded_image_') no-repeat center center;"></i>
               </span>
           </div>


            */
        }
    };
    Map<PreloadedImage, FlowPanel> preloadedImagePanelMap = new HashMap<PreloadedImage, FlowPanel>();

    private void saveSessionContents() {
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        ContentRequest contentRequest = new ContentRequest();
        myService.saveSessionContents(contentRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ContentHolder contentHolder = mobileResponse.getContentHolder();
                    if (parentContentHolder == null) {
                        parentContentHolder = new ContentHolder();
                    }
                    parentContentHolder.getContentHolders().add(contentHolder);
                    addContentHolder(parentContentHolder, true, true);
                    addContentHolderHandler(contentHolder);
                }
            }
        });
    }

    protected List<MediaFile> mediaFiles = new ArrayList<MediaFile>();
    CaptureCallback captureCallback = new CaptureCallback() {
        //@Override
        public void onSuccess(JsArray<MediaFile> mediaFilesarg) {
            for (int i = 0; i < mediaFilesarg.length(); i++) {
                MediaFile mediaFile = mediaFilesarg.get(i);
                mediaFiles.add(mediaFile);
            }
            //we need to redo the display of the mediaFiles, we should have a
            //mediaFilesPanel that we can clear, and rebuild!
            addMediaFilesPanel();
            /*
                *
                * // Upload files to server function uploadFile(mediaFile) { var ft
                * = new FileTransfer(), path = mediaFile.fullPath, name =
                * mediaFile.name;
                *
                * ft.upload(path, "http://my.domain.com/upload.php",
                * function(result) { console.log('Upload success: ' +
                * result.responseCode); console.log(result.bytesSent + ' bytes
                * sent'); }, function(error) { console.log('Error uploading file '
                * + path + ': ' + error.code); }, { fileName: name }); }
                *
                *
                *
                * String lb = "<br/>"; StringBuilder sb = new StringBuilder(); for
                * (int i = 0; i < mediaFiles.length(); i++) { MediaFile mediaFile =
                * mediaFiles.get(i); sb.append("Name:" + mediaFile.getName() + lb);
                * sb.append("FullPath:" + mediaFile.getFullPath() + lb);
                * sb.append("Type:" + mediaFile.getType() + lb);
                * sb.append("LastModifiedDate:" + mediaFile.getLastModifiedDate() +
                * lb); sb.append("size:" + mediaFile.getSize() + lb);
                * sb.append(lb); }
                */
            // text.setHTML("Success: " + lb + sb.toString());
        }

        //@Override
        public void onError(CaptureError error) {
            // text.setHTML("Error: " + error.getCode());
        }
    };

    void captureAudio() {
        Capture.captureAudio(captureCallback, new CaptureOptions().limit(1).duration(
                10));
    }

    void captureImage() {
        Capture.captureImage(captureCallback, new CaptureOptions().limit(1).duration(
                10));
    }

    void captureVideo() {
        Capture.captureVideo(captureCallback, new CaptureOptions().limit(1).duration(
                10));
    }

    public TextArea contentTextArea = new TextArea();
    protected MyWebApp mywebapp = null;
    protected Image image = null;
    protected Hidden pictureDataHidden = new Hidden("encodedFile");
    final Button cameraButton = new Button("Camera");
    final Button libraryButton = new Button("From Library");
    protected Hidden mimeType = null;
    //protected boolean newspot = false;
    protected TextField nameTextBox = null;
    protected TextField address1TextField = null;
    protected SuggestBox citySuggestBox = null;
    protected SuggestBox countryTextBox = null;
    public SuggestBox stateTextBox = null;
    protected TextField zipcodeTextField = null;
    protected TextField emailTextField = null;
    protected TextField websiteTextField = new TextField();
    protected TextField titleTextBox = null;
    protected TextField factualIdTextField = null;
    protected TextField voicePhoneTextField = new TextField();
    protected DateTextField startDatePicker = new DateTextField();
    protected DateTextField endDatePicker = new DateTextField();

    protected void addDateRange() {
        //final Label startDateLabel = new Label("Start Date");
        //add(startDateLabel);
        //add(startDatePicker);
        addFieldset(startDatePicker, "Start Date", "startDate");
        addFieldset(endDatePicker, "End Date", "endDate");
    }

    protected void addCameraStuff(ComplexPanel parentPanel) {
        add(pictureDataHidden);
        // Create a handler for the sendButton and nameField
        addCameraButton(parentPanel);
        addLibraryButton(parentPanel);
        //add(image);
        pictureDataHidden = new Hidden("encodedFile");
        add(pictureDataHidden);
        mimeType = new Hidden("mimeType");
        mimeType.setValue("image/jpeg");
        parentPanel.add(mimeType);
    }

    protected Map<Widget, Long> clickMapLong = new HashMap<Widget, Long>();

    protected Image addImage(SolrDocument result, Panel hp, String fieldName, ImageResource imageResourceBig, ImageResource imageResourceMobile, String stylename, String targetHistoryToken) {
        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setTargetHistoryToken(targetHistoryToken);
        String val = result.getFirstString(fieldName);
        Image image = null;
        if (val != null) {
            image = addImage(val.toString(), hp, stylename, hyperlink);
        } else if (imageResource != null) {
            ImageResource imageResource = null;
            if (MyWebApp.isSmallFormat()) {
                imageResource = imageResourceMobile;
            } else {
                imageResource = imageResourceBig;
            }
            image = new Image(imageResource);
            addImage(image, hp, stylename, hyperlink);
        }
        return image;
    }

    protected Image addImage2(SolrDocument result, Panel hp, String fieldName, ImageResource imageResourceBig, ImageResource imageResourceMobile, String targetHistoryToken) {
        Anchor hyperlink = new Anchor("", targetHistoryToken);
        String val = result.getFirstString(fieldName);
        Image image = null;
        if (val != null) {
            image = addImage2(val.toString(), hp, hyperlink);
        } else if (imageResource != null) {
            ImageResource imageResource = null;
            if (MyWebApp.isSmallFormat()) {
                imageResource = imageResourceMobile;
            } else {
                imageResource = imageResourceBig;
            }
            image = new Image(imageResource);
            addImage2(image, hp, hyperlink);
        }
        return image;
    }

    protected Image addImage2(String href, Panel hp, Anchor hyperlink) {
        if (href == null) {
            return null;
        }
        if (href.equals("")) {
            return null;
        }
        final Image image = new Image();
        image.setUrl(mywebapp.getUrl(href));
        addImage2(image, hp, hyperlink);
        return image;
    }

    protected Image addImage(String href, Panel hp, String stylename, Hyperlink hyperlink) {
        //GWT.log("addImage " + href);
        if (href == null) {
            return null;
        }
        if (href.equals("")) {
            return null;
        }
        final Image image = new Image();
        image.setUrl(mywebapp.getUrl(href));
        addImage(image, hp, stylename, hyperlink);
        return image;
    }

    protected void addImage2(Image image, Panel hp, Anchor anchor) {
        anchor.getElement().appendChild(image.getElement());
        hp.add(anchor);
    }

    protected void addImage(Image image, Panel hp, String stylename, Hyperlink hyperlink) {
        SimplePanel sp = new SimplePanel();
        sp.setStyleName("imgholder");
        if (stylename != null) {
            sp.addStyleName(stylename);
        }
        sp.add(image);
        hyperlink.getElement().appendChild(sp.getElement());
        hp.add(hyperlink);
    }
    //http://www.808.dk/?code-html-5-video

    public SpotBasePanel() {
    }

    public void clear() {
        super.clear();
        addTopPanel();
    }

    protected Long getManufacturerId() {
        String val = getValue(manufacturersListBox);
        if (val == null) {
            return null;
        }
        return new Long(val);
    }

    protected String getValue(ListBox listBox) {
        if (listBox == null) return null;
        //first option is always a "Please select", so we don't want it
        int selectedIndex = listBox.getSelectedIndex();
        GWT.log("selectedIndex=" + selectedIndex);
        if (selectedIndex == 0) {
            return null;
        }
        if (selectedIndex == -1) {
            return null;
        }
        String val = listBox.getValue(selectedIndex);
        return val;
    }

    private boolean displayTopPanel = false;
    private boolean displayHelp = false;

    public SpotBasePanel(MyWebApp mywebapp, boolean displayTopPanel, boolean displayHelp, boolean newStyle) {
        this.displayTopPanel = displayTopPanel;
        this.displayHelp = displayHelp;
        //for our new thing, don't want
        if (!newStyle) {
            this.setStyleName("panel");
            this.addStyleName("SpotBasePanel");
            this.setWidth("100%");
        }
        this.mywebapp = mywebapp;
        if (!MyWebApp.isMobileDevice()) {
            setupRootPanelForm();
            formPanel.setWidget(this);
        }
        if (!MyWebApp.isDesktop()) {
            GWT.log("addinttop panel");
            addTopPanel();
            addHelp();
        }
    }

    public SpotBasePanel(MyWebApp mywebapp, boolean displayTopPanel) {
        this(mywebapp, displayTopPanel, displayTopPanel, false);
    }

    public SpotBasePanel(MyWebApp mywebapp) {
        this(mywebapp, true, true, false);
    }

    protected void add(Panel panel, SpotLabel label, Widget textBox) {
        Fieldset hp = new Fieldset();
        VerticalPanel vp = new VerticalPanel();
        hp.add(vp);
        vp.add(label);
        vp.add(textBox);
        panel.add(vp);
    }

    protected void add(SpotLabel label, Widget textBox) {
        Fieldset hp = new Fieldset();
        VerticalPanel vp = new VerticalPanel();
        hp.add(vp);
        vp.add(label);
        vp.add(textBox);
        add(vp);
    }

    protected void add(SpotLabel label1, SpotLabel label2) {
        Fieldset hp = new Fieldset();
        VerticalPanel vp = new VerticalPanel();
        hp.add(vp);
        vp.add(label1);
        vp.add(label2);
        add(vp);
    }

    protected Image addImageOrig(String href, Panel hp, String stylename) {
        //GWT.log("addImage " + href);
        if (href == null) {
            return null;
        }
        if (href.equals("")) {
            return null;
        }
        final Image image = new Image();
        //image.addStyleName("scaleme");
        image.setUrl(mywebapp.getUrl(href));
        addImageOrig(image, hp, stylename);
        return image;
    }

    protected void addImageOrig(Image image, Panel hp, String stylename) {
        SimplePanel sp = new SimplePanel();
        sp.setStyleName("imgholder");
        if (stylename != null) {
            sp.addStyleName(stylename);
        }
        sp.add(image);
        hp.add(sp);
    }

    protected void addImageOrig(Image image, Panel hp, String styleName, Hyperlink hyperlink) {
        SimplePanel sp = new SimplePanel();
        sp.setStyleName("imgholder");
        if (styleName != null) {
            sp.addStyleName(styleName);
        }
        sp.add(hyperlink);
        hyperlink.getElement().appendChild(image.getElement());
        hp.add(sp);
    }
}
