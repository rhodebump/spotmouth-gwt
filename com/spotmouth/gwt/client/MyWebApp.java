package com.spotmouth.gwt.client;

import com.google.gwt.core.client.*;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.*;
import com.spotmouth.gwt.client.coupon.CouponForm;
import com.spotmouth.gwt.client.directory.DirectoryCountriesPanel;
import com.spotmouth.gwt.client.directory.DirectoryPostalCodesPanel;
import com.spotmouth.gwt.client.directory.DirectoryStatesPanel;
import com.spotmouth.gwt.client.event.EventForm;
import com.spotmouth.gwt.client.chat.ChatsPanel;
import com.spotmouth.gwt.client.chat.*;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.contest.ContestsPanel;
import com.spotmouth.gwt.client.contest.ManageContestPanel;
import com.spotmouth.gwt.client.contest.ViewContestPanel;
import com.spotmouth.gwt.client.coupon.Coupons8Panel;
import com.spotmouth.gwt.client.friends.Invited;
import com.spotmouth.gwt.client.friends.ManageFriendPanel;
import com.spotmouth.gwt.client.friends.ManageFriendsPanel;
import com.spotmouth.gwt.client.group.GroupPanel;
import com.spotmouth.gwt.client.group.ManageGroupPanel;
import com.spotmouth.gwt.client.instagram.Data;
import com.allen_sauer.gwt.log.client.Log;
import com.google.api.gwt.oauth2.client.Auth;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwtphonegap.client.*;
import com.googlecode.gwtphonegap.client.geolocation.*;
import com.googlecode.mgwt.ui.client.MGWT;
import com.phonegap.gwt.device.client.Device;
import com.phonegap.gwt.fbconnect.client.FBConnect;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.IntroPanel;
import com.spotmouth.gwt.client.icons.SpotImageResource;
import com.spotmouth.gwt.client.layout.Page;
import com.spotmouth.gwt.client.layout.MapPage;
import com.spotmouth.gwt.client.location.SetLocationManuallyPanel;
import com.spotmouth.gwt.client.menu.ApplicationMenuPanel;
import com.spotmouth.gwt.client.messaging.MessagingPanel;
import com.spotmouth.gwt.client.messaging.NotificationsPanel;
import com.spotmouth.gwt.client.om.LocationOverlay;
import com.spotmouth.gwt.client.profile.ProfileSettingsPanel;
import com.spotmouth.gwt.client.profile.ViewProfilePanel;
import com.spotmouth.gwt.client.rpc.ApiService;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import com.spotmouth.gwt.client.search.SearchForm;
import com.spotmouth.gwt.client.product.ManageProductsPanel;
import com.spotmouth.gwt.client.spot.CreateSpotMarkPanel;
import com.spotmouth.gwt.client.spot.ManageSpotPanel;
import com.spotmouth.gwt.client.usergroups.ManageUserGroupPanel;
import com.spotmouth.gwt.client.usergroups.ViewUserGroupsPanel;
import gdurelle.tagcloud.client.tags.TagCloud;
import gdurelle.tagcloud.client.tags.WordTag;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyWebApp implements EntryPoint {

    private ClickHandler searchHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {

            performSearch();
        }
    };



    public ClickHandler showResultsOnMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("showResultsOnMapHandler " + toggleMapMode.getValue());
            if (toggleMapMode.getValue()) {
                History.newItem(MyWebApp.RESULTS_MAP);
            } else {
                History.newItem(MyWebApp.HOME);
            }
        }
    };
    public ClickHandler toggleKiloMilesHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (toggleMilesCheckBox.getValue()) {
                setShowMeters(false);
            } else {
                setShowMeters(true);
            }
            MobileResponse mobileResponse = getResultsPanel().getMobileResponse();
            setResultsPanel(null);
            getResultsPanel().showResults(mobileResponse);
        }
    };
    private ClickHandler markSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.MARK_SPOT);
        }
    };
    String mkey = "ABQIAAAAM9fymNs8HkMYUXmwm3vULhTX2nrgTAm8U93bq6ygOEgbwPz7jhTJxACRU8efAGZygVMYGL3AL77D1w";
    String prodkey = "ABQIAAAAM9fymNs8HkMYUXmwm3vULhS0gOFYbtEhJwe4Bl3kThyi4Bl3kxQgw1BpRoxN_xXBZ5WOlmGGWf_yUA";
    String devkey = "ABQIAAAAM9fymNs8HkMYUXmwm3vULhS-Lxx7NiGUwfjIB8UxIofDVocgYRSqughddUxwyzJQHGi7LPalDi6hNw";

    public String getGoogleMapKey() {
        Exception e = new Exception();
        GWT.log("getGoogleMapKey", e);
        if (getHost().startsWith("http://dev")) {
            return devkey;
        } else if (getHost().startsWith("http://m.")) {
            return mkey;
        } else {
            return prodkey;
        }
    }

    public AsyncCallback locationUpdateCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not update your location");
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your location has been updated.");
        }
    };

    public static native void trackEvent(String category, String action, String label) /*-{
        $wnd._gaq.push(['_trackEvent', category, action, label]);
    }-*/;

    public static native void trackEvent(String category, String action, String label, int intArg) /*-{
        $wnd._gaq.push(['_trackEvent', category, action, label, intArg]);
    }-*/;

    public static native void trackPageview(String url) /*-{
        $wnd._gaq.push(['_trackPageview', url]);
    }-*/;

    public static native void showPopup() /*-{
        $doc.getElementById('popup').style.zIndex = "9999999";
        $doc.getElementById('popup').style.opacity = "1";
    }-*/;

    public static native void hidePopup() /*-{
        $doc.getElementById('popup').style.opacity = "0";
        $doc.getElementById('popup').style.zIndex = "-1";
    }-*/;

    ChangeHandler changeSortHandler = new ChangeHandler() {
        public void onChange(ChangeEvent changeEvent) {
            int itemSelected = sortingListBox.getSelectedIndex();
            String sortKey = sortingListBox.getValue(itemSelected);
            if (sortKey != null) {
                getResultsPanel().getSearchParameters().setSortKey(sortKey);
                getResultsPanel().performSearch();
            }
        }
    };
    ChangeHandler clickTagHandler = new ChangeHandler() {
        public void onChange(ChangeEvent changeEvent) {
            int itemSelected = tagListBox.getSelectedIndex();
            String facet = tagListBox.getValue(itemSelected);
            if (facet != null) {
                getResultsPanel().getSearchParameters().getTags().clear();
                TagHolder tagHolder = new TagHolder();
                tagHolder.setName(facet);
                getResultsPanel().getSearchParameters().getTags().add(tagHolder);
                //need to go back to first page of results
                getResultsPanel().getSearchParameters().setOffset(0);
                getResultsPanel().performSearch();
            }
        }
    };

    public MyWebApp() {
        this.locationPanel.setStyleName("locationPanel");
        //locationTextBox.setStyleName("locationTextBox");
        if (MyWebApp.isDesktop()) {
            searchBoxPanel = new HorizontalPanel();
        } else {
            searchBoxPanel = new VerticalPanel();
        }
        Image profilePicImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
        profilePicPanel.setWidget(profilePicImage);
        //<button class="btn_blue m_mark">Mark Spot</button>
        markSpotButton.setStyleName("btn_blue");
        markSpotButton.addStyleName("m_mark");
        markSpotButton.addClickHandler(markSpotHandler);
        sortingListBox.addItem("Sort by...", "");
        sortingListBox.addItem("Most Recently Marked or Updated", "updatedate_dt");
        sortingListBox.addItem("User", "username_s");
        sortingListBox.addItem("Total Ratings", "totalratings_d");
        sortingListBox.addItem("Average Rating", "averagerating_d");
        sortingListBox.addItem("Total # Marks", "markcount_i");
        sortingListBox.addItem("Distance", "geodist()");
        sortingListBox.addChangeHandler(changeSortHandler);
        tagListBox.addChangeHandler(clickTagHandler);
    }

    Button markSpotButton = new Button("Mark Spot");
    List<String> logList = new ArrayList<String>();
//
//    public boolean isAdmin() {
//        if (getAuthenticatedUser() == null) {
//            return false;
//        } else {
//            return getAuthenticatedUser().isAdmin();
//        }
//    }

    private Logger log = Logger.getLogger(getClass().getName());

    public TopMenuPanel getTopMenuPanel() {
        return topMenuPanel;
    }

    MessagePanel messagePanel = new MessagePanel();
    TopMenuPanel topMenuPanel = new TopMenuPanel(this);

    public SpotImageResource getResources() {
        return resources;
    }

    public static SpotImageResource resources = GWT.create(SpotImageResource.class);

    public MobileResponse getContests() {
        return contests;
    }

    private MobileResponse contests = null;
    private MobileResponse messages = null;
    // holder for friends and groups
    private MobileResponse friendsAndGroups = null;
    private MobileResponse favorites = null;
    private MobileResponse myBiz = null;
    private List<ItemHolder> notifications = new ArrayList<ItemHolder>();

    public List<ItemHolder> getNotifications() {
        return notifications;
    }

    public MobileResponse getMessages() {
        return messages;
    }

    public void setMessages(MobileResponse messages) {
        this.messages = messages;
    }

    public MobileResponse getFavorities() {
        return favorites;
    }

    public MobileResponse getMyBiz() {
        return myBiz;
    }

    public void setFavorites(MobileResponse favorites) {
        this.favorites = favorites;
    }

    public MobileResponse getFriendsAndGroups() {
        return friendsAndGroups;
    }

    public void setFriendsAndGroups(MobileResponse friendsAndGroups) {
        this.friendsAndGroups = friendsAndGroups;
    }

    //DataOperationDialog friendsDataDialog = null;
    DataOperationDialog messagesDataDialog = null;

    public void fetchMessages(final AsyncCallback callback) {
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setAuthToken(this.getAuthToken());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.getmessages(messageRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("fetchMessages onFailure");
                messagesDataDialog.hide();
                if (callback == null) {
                    getMessagePanel().displayError(caught.getMessage());
                } else {
                    callback.onFailure(caught);
                }
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.messages = mobileResponse;
                messagesDataDialog.hide();
                if (mobileResponse.getNotAuthenticatedException()) {
                    //not authenticated, need to login
                    MyWebApp.this.setAuthenticatedUser(null);
                }
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }
        });
    }

    private static String LAST_LOCATION = "LAST_LOCATION";

    private void fetchInitData() {
        SearchRequest searchRequest = new SearchRequest();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.initData(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                log("fetchInitData.onFailure");
                verifyDisplay();
                //History.fireCurrentHistoryState();
                getMessagePanel().displayError("Initialization Error:", caught);
            }

            public void onSuccess(Object result) {
                log("fetchInitData success");
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.countryHolders = mobileResponse.getCountryHolders();
                MyWebApp.this.stateProvinceHolders = mobileResponse.getStateProvinceHolders();
                MyWebApp.this.productHolders = mobileResponse.getProductHolders();
                MyWebApp.this.manufacturerHolders = mobileResponse.getManufacturerHolders();

                //
                // let's get the locationKeys
                if (isLocalStorageSupported()) {
                    Storage localStorage = Storage.getLocalStorageIfSupported();
                    log("localStorage is not null");
                    String lastLocationKey = localStorage.getItem(LAST_LOCATION);
                    Map<String, Location> locationMap = getLocationsFromLocalStorage();
                    Location location = locationMap.get(lastLocationKey);
                    if (location == null) {
                        MyWebApp.this.setCurrentLocation(mobileResponse.getGeocodedLocation());
                    } else {
                        MyWebApp.this.setCurrentLocation(location);
                    }
                } else {
                    log("fetchInitData onSuccess setCurrentLocation");
                    MyWebApp.this.setCurrentLocation(mobileResponse.getGeocodedLocation());
                }
                String initToken = History.getToken();
                if (initToken == null || initToken.length() == 0) {
                    toggleHome();
                } else {
                    History.fireCurrentHistoryState();
                }
            }
        });
    }

    public List<ProductHolder> getProductHolders() {
        return productHolders;
    }

    public void fetchMyBiz(final AsyncCallback callback) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setAuthToken(this.getAuthToken());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchMyBiz(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                GWT.log("fetchMyBiz onSuccess");
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.myBiz = mobileResponse;
                if (mobileResponse.getNotAuthenticatedException()) {
                    MyWebApp.this.setAuthenticatedUser(null);
                }
                if (callback != null) {
                    if (mobileResponse.getStatus() == 1) {
                        callback.onSuccess(null);
                    }
                }
            }
        });
    }

    public void fetchFavorites(final AsyncCallback callback) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setAuthToken(this.getAuthToken());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchFavorites(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("fetchFavorites onFailure");
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.favorites = mobileResponse;
                if (mobileResponse.getNotAuthenticatedException()) {
                    //not authenticated, need to login
                    MyWebApp.this.setAuthenticatedUser(null);
                }
                if (callback != null) {
                    if (mobileResponse.getStatus() == 1) {
                        callback.onSuccess(null);
                    }
                }
            }
        });
    }

    public void fetchFriendsAndGroups(final AsyncCallback callback) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setAuthToken(this.getAuthToken());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchFriendsAndGroups(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("fetchFriendsAndGroups onFailure");
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.friendsAndGroups = mobileResponse;
                if (mobileResponse.getNotAuthenticatedException()) {
                    //not authenticated, need to login
                    MyWebApp.this.setAuthenticatedUser(null);
                }
                if (callback != null) {
                    if (mobileResponse.getStatus() == 1) {
                        callback.onSuccess(null);
                    }
                }
            }
        });
    }

    // private boolean doToggle = false;
    public FBConnect fbConnect = null;
    public static boolean DEVELOPMENT = true;
    // public static boolean DEVELOPMENT = false;
    // this is only used when running locally, we use childbrowser base
    private static String HOST_URL = "http://www.spotmouth.com";

    // this would work for production deployment, but how about devmode?
    public static String getHost() {
        if (isMobileDevice()) {
            return HOST_URL;
        } else {
            // http://127.0.0.1:8888/MyWebApp.html?gwt.codesvr=127.0.0.1:9997
            StringBuffer sb = new StringBuffer();
            sb.append("http://" + Window.Location.getHostName());
            if (!Window.Location.getPort().equals("")) {
                sb.append(":" + Window.Location.getPort());
            }
            return sb.toString();
        }
    }

    public static boolean isSmallFormat() {
        //  if (mobileSmallFormat) return true;
        //if (true) return false;
        if (isDesktop()) {
            return false;
        }
        if (MGWT.getOsDetection().isTablet()) {
            return false;
        }
        if (MGWT.getOsDetection().isPhone()) {
            return true;
        }
        return true;
    }

    public static boolean isFormSupported() {
        if (isDesktop()) {
            return true;
        }
        if (isMobileDevice()) {
            return false;
        }
        if (getHost().startsWith("http://m.")) {
            return false;
        }
        if (getHost().startsWith("http://www.")) {
            return true;
        }
        //dev
        return false;
    }

    /*
    bug here for /dev in production
    http://www.spotmouth.com/dev
     */
    public String getHostUrl() {
        String path = Window.Location.getPath();
        //Window.Location.
        GWT.log("path=" + path);
        if (getHost().startsWith("http://m.")) {
            return getHost() + path + "api";
        } else if (getHost().startsWith("http://www.")) {
            return getHost() + path + "api";
        } else {
            return getHost() + path + "api";
        }
    }

    public static boolean isMobileDevice() {
        //if (true) return true;
        if (GWT.getModuleBaseURL().startsWith("file:")) {
            return true;
        } else {
            return false;
        }
    }

    public void reverseGeocode(final AsyncCallback callback) {
        log("reverseGeocode");
        GeocodeRequest geocodeRequest = new GeocodeRequest();
        Location location = new Location();
        location.setLatitude(currentLocation.getLatitude());
        location.setLongitude(currentLocation.getLongitude());
        geocodeRequest.setLocation(location);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.reverseGeocode(geocodeRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("reverseGeocode onFailure");
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                log("reverseGeocode onSuccess");
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    Location gelocation = mobileResponse.getGeocodedLocation();
                    gelocation.setLongitude(currentLocation.getLongitude());
                    gelocation.setLatitude(currentLocation.getLatitude());
                    GWT.log("reverseGeocode onSuccess " + gelocation.toString());
                    setCurrentLocation2(gelocation);
                    //instead of calling the aboe
                    //getMessagePanel().clear();
                    log("reverseGeocode.onSuccess calling callback");
                    if (callback != null)
                        callback.onSuccess(null);
                } else {
                    log("reverseGeocode bad status code");
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void pushLocalStorage(String key, String value) {
        if (!Storage.isLocalStorageSupported()) {
            return;
        }
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage != null) {
            localStorage.setItem(key, value);
        }
    }

    // should be called anytime we set the location
    // this will push our long/lat and device uid to the server
    public void pushDeviceLocationToServer() {
        log("pushDeviceLocationToServer");
        DeviceLocation deviceLocation = new DeviceLocation();
        deviceLocation.setLatitude(currentLocation.getLatitude());
        deviceLocation.setLongitude(currentLocation.getLongitude());
        if (this.getAuthenticatedUser() != null) {
            deviceLocation.setAuthToken(this.getAuthToken());
        }
        // add device uid
        Device device = Device.getDevice();
        if (device == null) {
            // Logger.warn("pushDeviceLocationToServer device is null");
            //if we do not have a device id, no need to proceed
            //let's generate a deviceuid and store in local storage
            String deviceuid = null;
            //is the deviceuid in local storage
            Storage localStorage = Storage.getLocalStorageIfSupported();
            // let's get the locationKeys
            if (localStorage != null) {
                deviceuid = localStorage.getItem("deviceuid");
            }
            if (deviceuid == null) {
                deviceuid = UUID.uuid(8, 16);
                //localStorage.setItem("deviceuid", deviceuid);
                pushLocalStorage("deviceuid", deviceuid);
            }
            //Log.warn("pushDeviceLocationToServer deviceuid=" + deviceuid);
            deviceLocation.setDeviceuid(deviceuid);
        } else {
            //Log.warn("pushDeviceLocationToServer device=" + device.getUUID());
            deviceLocation.setDeviceuid(device.getUUID());
        }
        ApiServiceAsync myService = getApiServiceAsync();
        myService.pushDeviceLocationToServer(deviceLocation, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                log("pushDeviceLocationToServer onFailure");
                //log.log(Level.SEVERE,caught);
                //caught.printStackTrace();
                getMessagePanel().clear();
                getMessagePanel().displayMessage("Failed to push location information to spotmouth.  Notifications may not work.");
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                //nothing we need to do;)
            }
        });
    }

    //this calls a waiting servlet, and if it returns, and there are notifications, bring them front and foward
    //we only do this in webapp, if mobile device, we use native notification systems such as apns
    protected void startNotificationPolling() {
        Device device = Device.getDevice();
        NotificationPollRequest npr = new NotificationPollRequest();
        if (device != null) {
            //sb.append("&deviceuid=" + device.getUUID());
            npr.setDeviceuid(device.getUUID());
        } else {
            //my android emulator
            //sb.append("&deviceuid=5284047f-4ffb-3e04-824a-2fd1d1f0cd62");
            npr.setDeviceuid("5284047f-4ffb-3e04-824a-2fd1d1f0cd62");
        }
        ApiServiceAsync myService = getApiServiceAsync();
        myService.getnotifications(npr, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("getnotifications onFailure");
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (!mobileResponse.getNotifications().isEmpty()) {
                    MyWebApp.this.notifications.addAll(mobileResponse.getNotifications());
                    toggleNotifications(false);
                }
                startNotificationPolling();
            }
        });
    }

    public static String BIZ_OWNER = "biz-owner/";
    public static String CONTEST_DETAIL = "!contestdetail/";
    public static String SPOT_DETAIL = "!spotdetail/";
    public static String SORTS = "sorts/";
    public static String TAGS = "tags/";
    public static String VIEW_INVITED = "view_invited/";
    public static String MANAGE_FRIENDS = "manage-friends/";
    public static String SET_LOCATION_FROM_DEVICE = "set-location-from-device/";
    public static String VIEW_USER_GROUPS = "view-user-groups/";
    public static String MANAGE_PRODUCTS = "manage-products/";

    public static String CREATE_EVENT = "create-event/";
    public static String CREATE_COUPON = "create-coupon/";





    public static String GROUP = "!group/";
    //called
//    private void spotdetail(String historyToken) {
//        String spotid = historyToken.replaceAll(SPOT_DETAIL, "");
//        Long selectedSpotId = new Long(spotid);
//        showSpot(selectedSpotId);
//    }

    public void toggleSpotDetail(Long spotId) {
//        if (newItem) {
//            String token = SPOT_DETAIL + spotId;
//            History.newItem(token);
//            return;
//        }
        showSpot(spotId);
    }

    //implement on serverside
    private boolean haveAccess(SpotHolder spotHolder) {
        //SpotHolder spotHolder = mobileResponse.getSpotHolder();
        if (spotHolder.isLockedForEdit()) {
            //it's locked
            UserHolder userHolder = getAuthenticatedUser();
            for (UserHolder editor : spotHolder.getEditors()) {
                if (editor.getId().equals(userHolder.getId())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private void spotAuthRequest() {
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setAuthToken(getAuthToken());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.spotAuthRequest(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                //loginCallback.onFailure(caught);
            }

            public void onSuccess(Object result) {
                //loginCallback.onSuccess(result);
            }
        });
    }


    AsyncCallback manageSpotCallback = new AsyncCallback() {
       public void onFailure(Throwable throwable) {
       }

       public void onSuccess(Object response) {
           SpotHolder spotHolder = (SpotHolder) response;
           ManageSpotPanel manageSpotPanel = new ManageSpotPanel(MyWebApp.this, spotHolder);
           swapCenter(manageSpotPanel);
       }
   };



    private void toggleManageSpot(final Long spotId) {


        getSpot(spotId, manageSpotCallback);


    }

    public void toggleCountry(Long countryId) {
        fetchStates(countryId);
    }

    public void togglePostalCode(Long postalCodeId) {
        // String historyToken = History.getToken();
        //Long postalCodeId = new Long(id);
        //given the postal code, need all the spots
        //
        //do a search
        getMessagePanel().clear();
        getResultsPanel().resetSearchParameters();
        getResultsPanel().getSearchParameters().setPostalCodeId(postalCodeId);
        getResultsPanel().getSearchParameters().setMax(1000);
        getResultsPanel().getSearchParameters().setSpots(true);
        getMessagePanel().clear();
        getResultsPanel().performSearch();
        //DirectoryPostalCodePanel dpp = new DirectoryPostalCodePanel(this, postalCodeId);
        //swapCenter(dpp);
    }

    public void toggleState(boolean newItem, Long stateId) {
        if (newItem) {
            String token = STATE + stateId;
            History.newItem(token);
            return;
        }
        fetchPostalCodes(stateId);
    }

    private void fetchPostalCodes(Long stateId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setStateId(stateId);
        final DataOperationDialog spotDetailDialog = new DataOperationDialog(
                "Retrieving postalcodes.");
        spotDetailDialog.show();
        spotDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchPostalCodes(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                getMessagePanel().clear();
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    // displayResults(mobileResponse);
                    DirectoryPostalCodesPanel dpc = new DirectoryPostalCodesPanel(MyWebApp.this, mobileResponse);
                    swapCenter(dpc);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void fetchStates(Long countryId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setCountryId(countryId);
        final DataOperationDialog spotDetailDialog = new DataOperationDialog(
                "Retrieving states.");
        spotDetailDialog.show();
        spotDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchStates(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                getMessagePanel().clear();
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    DirectoryStatesPanel directoryStatesPanel = new DirectoryStatesPanel(MyWebApp.this, mobileResponse);
                    swapCenter(directoryStatesPanel);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public static String MANAGE_CHAT = "manage-chat/";
    public static String JOIN_CHAT = "join-chat/";
    public static String CHAT_DETAIL = "!chat-detail/";
    public static String ITEM_DETAIL = "!itemdetail/";
    //toggleChatDetail

    private void toggleChatDetail(Long itemId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setItemId(itemId);
        final DataOperationDialog itemDetailDialog = new DataOperationDialog(
                "Retrieving chat.");
        itemDetailDialog.show();
        itemDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.itemdetail(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                itemDetailDialog.hide();
                getMessagePanel().clear();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                itemDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ViewChatPanel spd = new ViewChatPanel(MyWebApp.this, mobileResponse.getItemHolder());
                    swapCenter(spd);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
    //joinChat

    private void joinChat(Long itemId) {
        ChatPanel chatsPanel = new ChatPanel(this, itemId.toString());
        swapCenter(chatsPanel);
    }

    private void manageChat(Long itemId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setItemId(itemId);
        final DataOperationDialog itemDetailDialog = new DataOperationDialog(
                "Retrieving chat.");
        itemDetailDialog.show();
        itemDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.itemdetail(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                itemDetailDialog.hide();
                getMessagePanel().clear();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                itemDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ManageChatPanel spd = new ManageChatPanel(MyWebApp.this, mobileResponse.getItemHolder());
                    swapCenter(spd);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    /*
   called to push photo to contest
    */
    public void doSaveImage(String name, String data) {
        getMessagePanel().clear();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.uploadFile(name, data, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleItemDetail(Long itemId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setItemId(itemId);
        final DataOperationDialog itemDetailDialog = new DataOperationDialog(
                "Retrieving item.");
        itemDetailDialog.show();
        itemDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.itemdetail(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                itemDetailDialog.hide();
                getMessagePanel().clear();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                itemDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ItemDetailPanel spd = new ItemDetailPanel(MyWebApp.this, mobileResponse);
                    swapCenter(spd);
                } else {
                    verifyDisplay();
                    //can we remove this from our navigation?
                    //History.
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private VerticalPanel locationPanel = new VerticalPanel();

    public void refreshLocation() {
        //Log.warn("refreshLocation");
        getPhoneGapGps();
    }

    GeolocationWatcher watcher = null;

    public void getPhoneGapGps() {
        if (phoneGap == null) {
            //we probably are in IE, let's give an error message
            getMessagePanel().clear();
            getMessagePanel().displayError("We are not able to obtain location from device.");
            return;
        }
        //only one watcher
        if (this.watcher != null) return;
        if (!this.autoGps) return;
        GeolocationOptions options = new GeolocationOptions();
        //msec
        //doesn't seem to have an effect
        options.setFrequency(1000);
        this.watcher = phoneGap.getGeolocation().watchPosition(options, new GeolocationCallback() {
            public void onSuccess(Position position) {
                log("watchPosition onSuccess");
                Location location = new Location();
                log("lat=" + position.getCoordinates().getLatitude());
                log("lng=" + position.getCoordinates().getLongitude());
                location.setLatitude(position.getCoordinates().getLatitude());
                location.setLongitude(position.getCoordinates().getLongitude());
                location.setGeocoded(false);
                // i think the setAccuracy is not working
                // let's try setAccuracyVertical...
                // let's not do this when running locally
                setCurrentLocation(location);
                pushDeviceLocationToServer();
                phoneGap.getGeolocation().clearWatch(watcher);
                watcher = null;
                if (geolocationCallback != null) {
                    geolocationCallback.onSuccess(null);
                    setGeolocationCallback(null);
                }
                doTimer();
            }

            public void onFailure(PositionError error) {
                log("watchPosition onFailure");
                phoneGap.getGeolocation().clearWatch(watcher);
                watcher = null;
                getMessagePanel().clear();
                getMessagePanel().displayError("We were not able to obtain your location.");
                getMessagePanel().displayError(error.getMessage());
                if (geolocationCallback != null) {
                    geolocationCallback.onFailure(null);
                    setGeolocationCallback(null);
                }
                doTimer();
            }
        });
    }

    public void setAutoGps(boolean autoGps) {
        this.autoGps = autoGps;
    }

    //instead of creating a callback, we will just use this variable
    //to indicate if
    private AsyncCallback geolocationCallback = null;

    public void setGeolocationCallback(AsyncCallback geolocationCallback) {
        this.geolocationCallback = geolocationCallback;
    }

    private boolean autoGps = true;

    private void doTimer() {
        //if we do
        Timer t = new Timer() {
            public void run() {
                getPhoneGapGps();
            }
        };
        // Schedule the timer to run once in 5 seconds.
        t.schedule(5 * 1000);
    }

    //for IE, we don't have access to the local storage so, this will be our local variable we keep
    Map<String, Location> sessionAddressesMap = new HashMap<String, Location>();



    //this can throw errors if cookies are disabled
    public boolean isLocalStorageSupported() {
        try {
            return Storage.isLocalStorageSupported();
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, Location> getLocationsFromLocalStorage() {
        //log("getLocationsFromLocalStorage enter");
        if (!isLocalStorageSupported()) {
            // log("storage is not supported, returning empty list");
            return sessionAddressesMap;
        }
        Map<String, Location> list = new HashMap<String, Location>();
        Storage localStorage = Storage.getLocalStorageIfSupported();
        // let's get the locationKeys
        String locationKeysStr = localStorage.getItem(key);
        if (locationKeysStr == null) {
            //log("getLocationsFromLocalStorage exit a");
            return list;
        }
        JSONValue val = JSONParser.parseLenient(locationKeysStr);
        // log("locationKeysStr=" + locationKeysStr);
        JSONArray locationKeys = val.isArray();
        for (int i = 0; i < locationKeys.size(); i++) {
            String key = locationKeys.get(i).isString().stringValue();
            //   log("key=" + key);
            String locationString = localStorage.getItem(key);
            //log("locationString=" + locationString);
            if (locationString != null) {
                LocationOverlay locationOverlay = LocationOverlay.fromJson(locationString);
                if (locationOverlay != null) {
                    Location location = new Location();
                    location.setGeocoded(locationOverlay.getGeocoded());
                    location.setLatitude(locationOverlay.getLatitude());
                    location.setLongitude(locationOverlay.getLongitude());
                    location.setAddress1(locationOverlay.getStreetAddress1());
                    location.setCity(locationOverlay.getCity());
                    location.setState(locationOverlay.getState());
                    location.setZipcode(locationOverlay.getZipcode());
                    location.setCountryCode(locationOverlay.getCountryCode());
                    list.put(location.getKey(), location);
                }
            }
        }
        // log("getLocationsFromLocalStorage exit");
        return list;
    }

    private String key = "locationkey22xy";

    public void saveLocationToLocalStorage() {
        if (!isLocalStorageSupported()) {
            sessionAddressesMap.put(currentLocation.getKey(), currentLocation);
            return;
        }
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage == null) {
            return;
        }
        Map<String, Location> list = getLocationsFromLocalStorage();
        //before we add the new location, not just the location key, but compare the full address also
        //we don't want dup addresses
        //things can have the same address, but slightly unique adddresses too
        //log("saveLocationToLocalStorage 2");
        Map<String, Location> addressMap = new HashMap<String, Location>();
        for (Location ll : list.values()) {
            addressMap.put(ll.getFullAddress(), ll);
        }
        Location loc = addressMap.get(currentLocation.getFullAddress());
        if (loc != null) {
            //since this is a more recent copy, let's remove the older one with this address and remove it
            list.remove(loc.getKey());
            localStorage.removeItem(loc.getKey());
        }
        list.put(currentLocation.getKey(), currentLocation);
        this.setLocationManuallyPanel = null;
        LocationOverlay locationOverlay = (LocationOverlay) LocationOverlay.createObject();
        locationOverlay.setGeocoded(currentLocation.getGeocoded());
        locationOverlay.setStreetAddress1(currentLocation.getAddress1());
        locationOverlay.setLatitude(currentLocation.getLatitude());
        locationOverlay.setLongitude(currentLocation.getLongitude());
        locationOverlay.setCity(currentLocation.getCity());
        locationOverlay.setState(currentLocation.getState());
        locationOverlay.setZipcode(currentLocation.getZipcode());
        //GWT.log("setting countrycode " + currentLocation.getCountryCode());
        locationOverlay.setCountryCode(currentLocation.getCountryCode());
        locationOverlay.setLastUsedDate(currentLocation.getLastUsedDate());
        String json = new JSONObject(locationOverlay).toString();
        //remove the old key, since it appears we can have dups
        // localStorage.setItem(currentLocation.getKey(), json);
        pushLocalStorage(currentLocation.getKey(), json);
        //localStorage.setItem(LAST_LOCATION, currentLocation.getKey());
        pushLocalStorage(LAST_LOCATION, currentLocation.getKey());
        saveLocationKeys(list);
    }

    static final Comparator<Location> SENIORITY_ORDER =
            new Comparator<Location>() {
                public int compare(Location e1, Location e2) {
                    return e2.getLastUsedDate().compareTo(e1.getLastUsedDate());
                }
            };

    private void saveLocationKeys(Map<String, Location> locations) {
        if (!isLocalStorageSupported()) {
            return;
        }
        //can we sort by date used?
        //Map<String, Location> locations = getLocationsFromLocalStorage();
        //  log("saveLocationKeys begin");
        if (locations == null) {
            //log("Locations is null");
        }
        Storage localStorage = Storage.getLocalStorageIfSupported();
        //need to trim the list, but get the oldest out, not the newest
        List<Location> locationList = new ArrayList<Location>();
        List<Location> allLocations = new ArrayList<Location>();
        allLocations.addAll(locations.values());
        Collections.sort(allLocations, SENIORITY_ORDER);
        for (Location location : allLocations) {
            if (locationList.size() > 9) {
                localStorage.removeItem(location.getKey());
            } else {
                locationList.add(location);
            }
        }
        //log("saveLocationKeys 2");
        JsArrayString array = JavaScriptObject.createArray().cast();
        for (Location location : locationList) {
            //  log("adding key " + location.getKey());
            array.push(location.getKey());
        }
        //log("saveLocationKeys 3");
        String locationKeys = new JSONArray(array).toString();
        //log("locationKeys=" + locationKeys);
        //localStorage.setItem(key, locationKeys);
        pushLocalStorage(key, locationKeys);
        // log("saveLocationKeys exit");
    }

    ChangeHandler changeHandler = new ChangeHandler() {
        public void onChange(ChangeEvent changeEvent) {
            // GWT.log("onchange");
            int itemSelected = locationsListBox.getSelectedIndex();
            String locationKey = locationsListBox.getValue(itemSelected);
            if (locationKey.equals(UPDATE_LOCATION_FROM_DEVICE)) {
                setLocationFromDevice();
                return;
            } else if (locationKey.equals(UPDATE_LOCATION_FROM_MAP)) {
                History.newItem(MAP);
                return;
            } else if (locationKey.equals(UPDATE_LOCATION_MANUALLY)) {
                // toggleSetLocationManually();
                History.newItem(MyWebApp.SET_LOCATION);
                return;
            }
            //  GWT.log("locationKey=" + locationKey);
            Map<String, Location> locationMap = getLocationsFromLocalStorage();
            Location location = locationMap.get(locationKey);
            if (location != null) {
                setCurrentLocation(location);
                toggleHome();
                //we may be on home right now, we need to refresh the page
                History.fireCurrentHistoryState();
            }
        }
    };

    Map<Widget, String> locationPickMap = new HashMap<Widget, String>();
    ClickHandler clickLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("clickLocationHandler");
            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//
//
//            }
            Widget widget = (Widget) sender;
            String locationKey = locationPickMap.get(widget);
            //nt itemSelected = locationsListBox.getSelectedIndex();
            //String locationKey = locationsListBox.getValue(itemSelected);
            if (locationKey.equals(UPDATE_LOCATION_FROM_DEVICE)) {
                setLocationFromDevice();
                return;
            } else if (locationKey.equals(UPDATE_LOCATION_FROM_MAP)) {
                History.newItem(MAP);
                return;
            } else if (locationKey.equals(UPDATE_LOCATION_MANUALLY)) {
                // toggleSetLocationManually();
                History.newItem(MyWebApp.SET_LOCATION);
                return;
            }
            //  GWT.log("locationKey=" + locationKey);
            Map<String, Location> locationMap = getLocationsFromLocalStorage();
            Location location = locationMap.get(locationKey);
            if (location != null) {
                setCurrentLocation(location);
                toggleHome();
                //we may be on home right now, we need to refresh the page
                History.fireCurrentHistoryState();
            }
        }
    };
    private Location currentLocation = null;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private boolean showMeters = true;

    public boolean isShowMeters() {
        return showMeters;
    }

    public void setShowMeters(boolean showMeters) {
        this.showMeters = showMeters;
    }

    // this is set on a successful login and will store username/password
    private LoginForm loginForm = null;

    public LoginForm getLoginForm() {
        return loginForm;
    }

    public void setLoginForm(LoginForm loginForm) {
        this.loginForm = loginForm;
    }

    private UserHolder userHolder = null;

    public void setAuthenticatedUser(UserHolder userHolder) {
        this.userHolder = userHolder;
        Element element = DOM.getElementById("usmenu");
        Element loginElement = DOM.getElementById("login-menu");
        if (userHolder == null) {
            this.setMessages(null);
            this.setFacebookUser(false);
            element.addClassName("hideme");
            loginElement.removeClassName("hideme");
            //need to hide the user stuff menu
            //need to add "hideme" to usmenu
        } else {
            //this is a login
            page.setUsername(userHolder.getUsername());
            loginElement.addClassName("hideme");
            element.removeClassName("hideme");
            Image profilePicImage = SpotBasePanel.getImage(userHolder.getContentHolder(), "320x320");
            if (profilePicImage == null) {
                profilePicImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
            }
            profilePicPanel.setWidget(profilePicImage);
            //profilePicPanel.add(profilePicImage);
            //need to remove "hideme" from usmenu
        }
        //need to refresh this to reflect login status
        this.setApplicationMenuPanel(null);
    }

    public UserHolder getAuthenticatedUser() {
        return userHolder;
    }

    private boolean facebookUser = false;

    public boolean isFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(boolean facebookUser) {
        this.facebookUser = facebookUser;
    }

    private boolean twitterUser = false;

    public void setTwitterUser(boolean twitterUser) {
        this.twitterUser = twitterUser;
    }

    public void setCurrentLocation(Location clocation) {
        //if the currentLocation is null, let's make sure we skip
        //the next elseif so we get to the setCurrentLocation2 method
        if (currentLocation == null) {
            //so when the reverse geocod
            log("currentLocation is null");
        } else if (clocation.getKey().equals(currentLocation.getKey())) {
            if (clocation.getGeocoded()) {
                // log("setCurrentLocation geocoded is true, returning");
                return;
            }
        }
        setCurrentLocation2(clocation);
    }

    ListBox locationsListBox = new ListBox();
    private static String UPDATE_LOCATION_FROM_DEVICE = "UPDATE_LOCATION_FROM_DEVICE";
    private static String UPDATE_LOCATION_FROM_MAP = "UPDATE_LOCATION_FROM_MAP";
    private static String UPDATE_LOCATION_MANUALLY = "UPDATE_LOCATION_MANUALLY";

    //called by the reverse geocoder
    private void setCurrentLocation2(Location clocation) {
        log("setCurrentLocation2");
        if (clocation == null) return;
        log("setting location to " + clocation.toString());
        DateTimeFormat fmt = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
        // prints Monday, December 17, 2007 in the default locale
        String lastUsedDate = fmt.format(new Date());
        clocation.setLastUsedDate(lastUsedDate);
        this.currentLocation = clocation;
        saveLocationToLocalStorage();
        rebuildLocationListBox();
        if (!currentLocation.getGeocoded()) {
            reverseGeocode(null);
        }
        //this will rebuild the map panel
        initMapPanel();
    }

    public void toggleBackToSearchResults() {
        //what if we went direct to detail page, there won't be results to go back to, so
        //in this case let's go to home page

        //the search results panel may be dirty, so if so, let's refresh


        if (resultsPanel == null) {
            //let's go home
            toggleHome();
            return;
        }  else if (resultsPanel.isDirty()){

            toggleSearch(null);


        }
        swapCenter(getResultsPanel());
        //if we are in map mode, let' indicate this so that we can toggle between these two modes
        if (toggleMapMode.getValue()) {
            History.newItem(MyWebApp.RESULTS_MAP, false);
        } else {
            History.newItem(MyWebApp.HOME, false);
        }
    }

    ClickHandler backToResultsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //swapCenter(spotBasePanel,true);
            toggleBackToSearchResults();
        }
    };
    ClickHandler clickLocationSearch = new ClickHandler() {
        public void onClick(ClickEvent event) {
            performSearch();
        }
    };
//    public ClickHandler setLocationFromDeviceHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            setLocationFromDevice();
//        }
//    };

    private void addToUL(String text, String key) {
        ListItem listItem = new ListItem();
        InlineLabel label1 = new InlineLabel(text);
        //Label label1 = new Label(text);
        locationPickMap.put(label1, key);
        label1.addClickHandler(clickLocationHandler);
        listItem.add(label1);
        previousLocationsULPanel.add(listItem);
    }

    public void rebuildLocationUL() {
        previousLocationsULPanel.clear();
        Map<String, Location> list = getLocationsFromLocalStorage();
        addToUL("Update location from device", UPDATE_LOCATION_FROM_DEVICE);
        Collection<Location> locations = list.values();
        for (Location location : locations) {
            addToUL(location.getFullAddress(), location.getKey());
        }
        //locationsListBox.setSelectedIndex(selectedIndex);
        addToUL("Update location from device", UPDATE_LOCATION_FROM_DEVICE);
        addToUL("Update location using map", UPDATE_LOCATION_FROM_MAP);
        addToUL("Update location manually", UPDATE_LOCATION_MANUALLY);
    }

    public void rebuildLocationListBox() {
        rebuildLocationUL();
        locationPanel.clear();
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("100%");
        locationPanel.add(hp);
        this.locationsListBox.clear();
        //this.locationsListBox = new ListBox();
        this.locationsListBox.setStyleName("locationSelectBox");
        Map<String, Location> list = getLocationsFromLocalStorage();
        locationsListBox.addItem("Update location from device", UPDATE_LOCATION_FROM_DEVICE);
        Collection<Location> locations = list.values();
        int i = 1;
        int selectedIndex = 0;
        // log("setCurrentLocation2 location loop with " + locations.size() + " locations");
        for (Location location : locations) {
            // log("location key=" + location.getKey() + " comparing to " + currentLocation.getKey());
            locationsListBox.addItem(location.getFullAddress(), location.getKey());
            if (currentLocation != null) {
                if (currentLocation.getKey().equals(location.getKey())) {
                    selectedIndex = i;
                }
            }
            i++;
        }
        locationsListBox.setSelectedIndex(selectedIndex);
        locationsListBox.addItem("Update location from device", UPDATE_LOCATION_FROM_DEVICE);
        locationsListBox.addItem("Update location using map", UPDATE_LOCATION_FROM_MAP);
        locationsListBox.addItem("Update location manually", UPDATE_LOCATION_MANUALLY);
        locationsListBox.addChangeHandler(changeHandler);
        Label currentLocationLabel = new Label("Current Location:");
        currentLocationLabel.setStyleName("currentLocation");
        Hyperlink notHereLabel = new Hyperlink("My Spots", MyWebApp.SET_LOCATION);
        notHereLabel.setStyleName("notHere");
        notHereLabel.addStyleName("whiteButton");
        //notHereLabel.addClickHandler(setLocationManuallyHandler);
        locationPanel.add(notHereLabel);
        //12 try at something
        // hp.add(locationTextBox);
        final String findx = "Near (Address, Neighborhood, City, State or Zip)";
        if (getCurrentLocation() == null) {
            locationTextBox.setValue(findx);
        } else {
            locationTextBox.setValue(getCurrentLocation().getFullAddress());
        }
        locationTextBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (locationTextBox.getValue().isEmpty()) {
                    if (getCurrentLocation() == null) {
                        locationTextBox.setValue(findx);
                    } else {
                        locationTextBox.setValue(getCurrentLocation().getFullAddress());
                    }
                }
            }
        });
        locationTextBox.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                if (locationTextBox.getValue().equals(findx)) {
                    locationTextBox.setValue("");
                } else if (getCurrentLocation() != null) {
                    if (locationTextBox.getValue().equals(getCurrentLocation().getFullAddress())) {
                        locationTextBox.setValue("");
                    }
                }
            }
        });
        locationTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSearch();
                }
            }
        });
        //add a search button
        Label searchLabel = new Label("Search");
        searchLabel.addClickHandler(clickLocationSearch);
        searchLabel.setStyleName("button");
        hp.add(searchLabel);
    }

    private void performLocationSearch(String locationSearchString) {
        //String locationSearchString = locationTextBox.getValue();
        //need to reverse geocode search and set location
        final DataOperationDialog searchingDialog = new DataOperationDialog("Searching for your spot...");
        GeocodeRequest geocodeRequest = new GeocodeRequest();
        // geocodeRequest.setGeocodeInput(locationTextBox.getValue());
        geocodeRequest.setGeocodeInput(locationSearchString);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.geocode2(geocodeRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                searchingDialog.hide();
                getMessagePanel().displayError("Could not set location", caught);
            }

            public void onSuccess(Object result) {
                searchingDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    GWT.log("geocode2 onSuccess");
                    //is there more than one??
                    GWT.log("mobileResponse.getLocations()" + mobileResponse.getLocations().size());
                    Location location = mobileResponse.getLocations().get(0);
                    setCurrentLocation(location);
                    log("toggleHome");
                    //toggleHome(locationUpdateCallback);
                    //getMessagePanel().clear();
                    MyWebApp.this.homeCallback = locationUpdateCallback;
                    //need to do search
                    //showHome();
                    performKeywordSearch();
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    //we need to do a search, we may call
    //performLocationSearch if location is changed, or just performKeyword search if it hasn't changed
    private void performSearch() {
        getMessagePanel().clear();
        if (getCurrentLocation() == null) {
            performLocationSearch(locationTextBox.getValue());
        } else if (locationTextBox.getValue().equals(getCurrentLocation().getFullAddress())) {
            //since the location here matches our location, no need
            performKeywordSearch();
        } else {
            performLocationSearch(locationTextBox.getValue());
        }
    }

    public static String SEARCH_RESULTS = "searchresults/";

    private void performKeywordSearch() {
        //need to change the URL so we can go back to search form
        History.newItem(SEARCH_RESULTS);
        getMessagePanel().clear();
        getResultsPanel().resetSearchParameters();
        getResultsPanel().getSearchParameters().setSpots(true);
        getResultsPanel().getSearchParameters().setItemType("locationmark");
        addCurrentLocation();
        getResultsPanel().getSearchParameters().setKeywords(keywordsTextBox.getValue());
        getMessagePanel().clear();
        getResultsPanel().performSearch();
    }

    protected TextBox locationTextBox = new TextBox();


//    //need to reset the results panel and add this style
//    public ResultsPanel getResultsPanel(String styleName) {
//        this.resultsPanel = null;
//        this.resultsPanel = getResultsPanel();
//        resultsPanel.addStyleName(styleName);
//        return this.resultsPanel;
//    }

    public void setResultsPanel(ResultsPanel resultsPanel) {
        this.resultsPanel = resultsPanel;
    }

//    public ResultsPanel getResultsPanel() {
//        return getResultsPanel(false);
//    }

    public ResultsPanel getResultsPanel() {
        if (resultsPanel == null) {
            resultsPanel = new ResultsPanel(this, false);
        }
        return resultsPanel;
    }



    NotificationsPanel notificationsPanel = null;
    MessagingPanel messagingPanel = null;
    SimplePanel topPanel = new SimplePanel();
    //SimplePanel footerPanel = new SimplePanel();
    ResultsPanel resultsPanel = null;
    // only one panel can be center
    VerticalPanel centerPanel = new VerticalPanel();
    MarkSpotPanel markSpotPanel = new MarkSpotPanel(this);
    MarkSpotTypePanel markSpotTypePanel = null;
    //this list just stores the titles of the page
    //i think it will be more memory efficient than holding
    //references to panels we never will reuse
    //LinkedList<String> backList = new LinkedList<String>();
    // private boolean backPushed = false;

    public void toggleBack() {
        GWT.log("toggleBack");
        // if (backList.isEmpty()) return;
        //   // this.backPushed = true;
        GWT.log("calling histry back");
        History.back();
        GWT.log("done histry back");
    }

    // set this panel if we need to login but not logged in
    // we will go here later
    public ComplexPanel callbackPanel = null;
    private SpotBasePanel currentSpotBasePanel = null;

    private boolean checkValidToToggle() {
        log("checkValidToToggle 1");
        if (currentSpotBasePanel != null) {
            if (!currentSpotBasePanel.isValidToToggle()) {
                log("checkValidToToggle false 2");
                return false;
            }
        }
        log("checkValidToToggle true 3");
        return true;
    }

    private void addBackToResults(SpotBasePanel spotBasePanel, ComplexPanel parentPanel) {
        if (MyWebApp.isDesktop()) {
            return;
        }
        if (!spotBasePanel.showBackToResults()) {
            return;
        }
        if (resultsPanel == null) {
            return;
        }
        if (currentSpotBasePanel instanceof ResultsPanel) {
            //don't want to show if this is results page!
            return;
        }
        Label backToResults = new Label("<< Back To Results");
        backToResults.addClickHandler(backToResultsHandler);
        backToResults.setStyleName("whiteButton");
        parentPanel.add(backToResults);
    }

    SimplePanel simplePanel = new SimplePanel();
    //for some reason, if we are not connected to network, we cannot use the form panel
    //  private boolean goodInit = false;
    private int previousWindowScrollPosition = 0;
    private VerticalPanel vp = new VerticalPanel();

    public boolean swapCenter(final SpotBasePanel spotBasePanel) {
        GWT.log("swapCenter");
        //store position of where we scrolled so we can go back to this same position
        if (this.currentSpotBasePanel instanceof ResultsPanel) {
            this.previousWindowScrollPosition = com.google.gwt.user.client.Window.getScrollTop();
        }
        if (!checkValidToToggle()) {
            return false;
        }
        //okay, we are on the map page, and we click on detail page, we need to fix the page here
        //or we could be on detail page, and go back to results page, we should be on map page (same as we left)
        if (toggleMapMode.getValue()) {
            //we are in map mode
            if (spotBasePanel instanceof ResultsPanel) {
                doMapPage();
            } else {
                //detail page, need to fix page
                doDefaultPage();
            }
        } else {
            doDefaultPage();
        }
        this.currentSpotBasePanel = spotBasePanel;
        this.topPanel.clear();
        this.topPanel.add(getTopContents(spotBasePanel));
        spotBasePanel.getTopPanelHolder().setWidget(this.topPanel);
        this.vp = new VerticalPanel();
        vp.setWidth("100%");
        if (!isDesktop()) {
            vp.add(this.topPanel);
        }
        addBackToResults(spotBasePanel, vp);
        if (isFormSupported()) {
            vp.add(spotBasePanel.getFormPanel());
            simplePanel.setWidget(vp);
            if (!isDesktop()) {
                RootPanel.get().clear();
                RootPanel.get().add(simplePanel);
            }
        } else {
            vp.add(spotBasePanel);
            simplePanel.setWidget(vp);
        }
        addBackToResults(spotBasePanel, vp);
        if (isSmallFormat()) {
            simplePanel.addStyleName("smallFormat");
        }
        getMessagePanel().clear();
        if (spotBasePanel instanceof SpotMouthPanel) {
            SpotMouthPanel pnl = (SpotMouthPanel) spotBasePanel;
            String title = pnl.getTitle();
            GWT.log("setting title to " + title);
            topMenuPanel.setTitleBar(title);
            pnl.toggleFirst();
            String pageTitle = pnl.getPageTitle();
            if (Document.get() != null) {
                Document.get().setTitle(pageTitle);
            }
            String pageDescription = pnl.getPageDescription();
            updatePageDescription(pageDescription);
        }
        spotBasePanel.addedToDom();
        //if it is a results panel, let's show the results controls
        Element results_controls = DOM.getElementById("results_controls");
        if (results_controls != null) {
            if (spotBasePanel instanceof ResultsPanel) {
                results_controls.removeClassName("hideme");
            } else {
                results_controls.addClassName("hideme");
            }
        }
        //this will set the id of the higher level per request by dmitriy
        this.page.setId2(spotBasePanel.getBodyClassId());
        GWT.log("swapCenter exit");
        com.google.gwt.user.client.Window.scrollTo(0, 0);
        if (homeCallback != null) {
            homeCallback.onSuccess(null);
            this.homeCallback = null;
        }
        return true;
    }
    //    public void fixPage(int desiredPageMode) {
    //        if (desiredPageMode != currentPageMode) {
    //            if (desiredPageMode == 1) {
    //                doDefaultPage();
    //            } else if (desiredPageMode == 2) {
    //                doMapPage();
    //            }
    //        }
    //    }

    private void updatePageDescription(String newDescription) {
        if (newDescription == null) {
            return;
        }
        NodeList<Element> tags = Document.get().getElementsByTagName("meta");
        for (int i = 0; i < tags.getLength(); i++) {
            MetaElement metaTag = ((MetaElement) tags.getItem(i));
            if (metaTag.getName().toLowerCase().equals("description")) {
                metaTag.setContent(newDescription);
            }
        }
    }



    public void toggleOptions() {
        OptionsPanel optionsPanel = new OptionsPanel(this);
        swapCenter(optionsPanel);
    }

    //public static String SPOTS_ON_MAP = "maps-on-map/";
    public static String CONSOLE = "console/";

    private void toggleConsole() {
        ConsolePanel consolePanel = new ConsolePanel(this);
        consolePanel.init();
        swapCenter(consolePanel);
        this.topMenuPanel.setTitleBar("Console");
    }

    ApplicationMenuPanel applicationMenuPanel = null;
    //i am going to reuse this panel, since the app seems slow if i build this up each click

    public void setApplicationMenuPanel(ApplicationMenuPanel applicationMenuPanel) {
        this.applicationMenuPanel = applicationMenuPanel;
    }

    public void toggleManageGroup(GroupHolder groupHolder, final SpotHolder spotHolder) {
        // GroupHolder groupHolder = clickMap.get(b);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setAuthToken(getAuthToken());
        groupRequest.setGroupHolder(groupHolder);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.editGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                GroupHolder groupHolder = mobileResponse.getGroupHolder();
                if (mobileResponse.getStatus() == 1) {
                    ManageGroupPanel mgp = new ManageGroupPanel(MyWebApp.this, groupHolder, spotHolder);
                    swapCenter(mgp);
                    if (!getAuthenticatedUser().isGoldenMember()) {
                        //let's add the "hideme" to this id
                        Element optin = DOM.getElementById("opt-in-required-div");
                        optin.addClassName("hideme");
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleMenu() {
        this.toggleMenu(null);
    }

    public void toggleMenu(AsyncCallback logoutMessageCallback) {
//        if (newItem) {
//            String token = "menu/";
//            History.newItem(token);
//            return;
//        }
        if (!checkValidToToggle()) {
            return;
        }
        //if (!isMobileDevice()) {
        // }
        if (applicationMenuPanel == null) {
            GWT.log("applicationMenuPanel is null, creating...");
            this.applicationMenuPanel = new ApplicationMenuPanel(this);
        }
        swapCenter(applicationMenuPanel);
        if (logoutMessageCallback != null) {
            logoutMessageCallback.onSuccess(null);
        }
    }

    private IntroPanel introPanel = new IntroPanel(this);
    public static String HIDE_INTRO = "HIDE_INTRO";

    //return true if we show a splash message
    private boolean displaySplash() {
        log("displaySplash");
        if (!isLocalStorageSupported()) {
            return false;
        }
        //only do splash for mobile
        if (MyWebApp.isDesktop()) {
            return false;
        }
        log("displaySplash 1");
        Storage localStorage = Storage.getLocalStorageIfSupported();
        if (localStorage == null) {
            return false;
        }
        log("displaySplash 2");
        String hideintro = localStorage.getItem(HIDE_INTRO);
        log("displaySplash 3");
        if (hideintro == null) {
            log("displaySplash 4");
            swapCenter(introPanel);
            return true;
        } else if (hideintro.equals("true")) {
            log("displaySplash 4");
            return false;
        } else {
            log("displaySplash 5");
            swapCenter(introPanel);
            return true;
        }
    }

    public void toggleSettings(boolean newItem) {
        if (newItem) {
            History.newItem(SETTINGS);
            return;
        }
        SettingsPanel settingsPanel = new SettingsPanel(this);
        swapCenter(settingsPanel);
    }

    public void toggleContact() {
//        if (newItem) {
//            History.newItem(CONTACT);
//            return;
//        }
        ContactPanel contactPanel = new ContactPanel(this);
        swapCenter(contactPanel);
    }

    private ApiServiceAsync apiServiceAsync = null;
    private boolean status = true;
    private boolean xfbml = true;
    private boolean cookie = true;

    public void prepareService(ServiceDefTarget service, final String moduleUrl, String relativeServiceUrl) {
        service.setServiceEntryPoint(moduleUrl + relativeServiceUrl);
        service.setRpcRequestBuilder(new RpcRequestBuilder() {
            @Override
            protected void doFinish(RequestBuilder rb) {
                super.doFinish(rb);
                rb.setHeader(MODULE_BASE_HEADER, moduleUrl);
            }
        });
    }

    //http://blog.daniel-kurka.de/2012/04/gwt-rpc-with-phonegap-revisited.html
    public ApiServiceAsync getApiServiceAsync() {
        if (apiServiceAsync == null) {
            apiServiceAsync = (ApiServiceAsync) GWT.create(ApiService.class);
            ServiceDefTarget endpoint = (ServiceDefTarget) apiServiceAsync;
            // Note the URL where the RPC service is located!
            if (isMobileDevice()) {
                prepareService(endpoint, "http://www.spotmouth.com/v1/gwt/mywebapp/", "rpc");
            } else {
                String moduleRelativeURL = GWT.getModuleBaseURL() + "rpc";
                endpoint.setServiceEntryPoint(moduleRelativeURL);
                //prepareService(endpoint, "http://www.spotmouth.com/gwt/mywebapp/", "rpc");
            }
        }
        return apiServiceAsync;
    }

    public List<CountryHolder> getCountryHolders() {
        return countryHolders;
    }

    public List<ManufacturerHolder> getManufacturerHolders() {
        return manufacturerHolders;
    }

    private List<ManufacturerHolder> manufacturerHolders = new ArrayList<ManufacturerHolder>();
    private List<ProductHolder> productHolders = new ArrayList<ProductHolder>();
    private List<CountryHolder> countryHolders = new ArrayList<CountryHolder>();

    public List<StateProvinceHolder> getStateProvinceHolders() {
        return stateProvinceHolders;
    }
    //  private static boolean mobileSmallFormat = true;

    public static boolean isDesktop() {
        //if (true) return false;
        if (getHost().startsWith("http://m.")) {
            return false;
        }
        return MGWT.getOsDetection().isDesktop();
        //return false;
    }

    private boolean robot = false;

    public boolean isRobot() {
        return robot;
    }

    private List<StateProvinceHolder> stateProvinceHolders = new ArrayList<StateProvinceHolder>();

    public void runApp() {

        //common css for tag and upload controls
        //DesktopResource.INSTANCE.upload().ensureInjected();
        log("runApp");
        if (!isDesktop()) {
            RootPanel.get().add(simplePanel);
        }
        // if we always do this, local or not, we can maybe logout!
        if (isMobileDevice()) {
            fbConnect = FBConnect.getFBConnect();
            // since we cannot seem to permanently delete cookies, let's nuke
            // the session cookies whenever we start
            //fbConnect.logout(APPID);
        } else {
            //recaptchaWidget = new RecaptchaWidget("6LeoPwYAAAAAAEgl-99fWvVvzRQObu5UoTPoQtg1");
            //fbCore.init(MyWebApp.APPID, status, cookie, xfbml);
            log("doing fbCoreLogout");
            //fbCore.logout(new LogoutCallback());
        }
        centerPanel.setWidth("100%");
        String initToken = History.getToken();
        GWT.log("initToken=" + initToken);
        initHistorySupport();
        VerticalPanel vp = new VerticalPanel();
        vp.add(new Label("Loading..."));
        Image splashImage = new Image(MyWebApp.resources.spotmouthSplash());
        vp.add(splashImage);
        simplePanel.add(vp);
        fetchInitData();
        checkForExistingLoginSession();
//
        if (initToken == null || initToken.length() == 0) {
            //want to show results/not a splash at this point
            // displaySplash();
            // toggleHome();
        }
    }
    //become a member does not require a spotmouth account!  it
    //just allows users to communicate to this email address

    private void manageMember(final String historyToken) {
        String invitationCode = historyToken.replaceAll("managemember/invitationCode=", "");
        GWT.log("invitationCode=" + invitationCode);
        ApiServiceAsync myService = getApiServiceAsync();
        //FriendRequest friendRequest = new FriendRequest();
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setInvitationCode(invitationCode);
        // memberRequest.setAuthToken(this.getAuthToken());
        myService.getMemberInvitation(memberRequest, new AsyncCallback() {
            DataOperationDialog dialog = new DataOperationDialog(
                    "Retrieving member invitation.");

            public void onFailure(Throwable caught) {
                dialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                dialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    MemberAcceptForm maf = new MemberAcceptForm(MyWebApp.this, mobileResponse);
                    swapCenter(maf);
                } else {
                    verifyDisplay();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
                // maf.getMessagePanel().displayMessage("You have been logged in, please change your password to something you can remember.");
            }
        });
    }

    //managefriend NO longer requireds to be login, it's just a confirmation that the
    //user can communicate to that friend with that email address
    private void manageFriend(final String historyToken) {
        String invitationCode = historyToken.replaceAll("managefriend/invitationCode=", "");
        GWT.log("invitationCode=" + invitationCode);
        ApiServiceAsync myService = getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setEmailInvitationCode(invitationCode);
        //friendRequest.setAuthToken(this.getAuthToken());
        myService.getFriendInvitation(friendRequest, new AsyncCallback() {
            DataOperationDialog dialog = new DataOperationDialog(
                    "Retrieving invitation.");

            public void onFailure(Throwable caught) {
                dialog.hide();
                verifyDisplay();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                dialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    FriendAcceptForm faf = new FriendAcceptForm(MyWebApp.this, mobileResponse);
                    swapCenter(faf);
                } else {
                    verifyDisplay();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void setLoginMobileResponse(MobileResponse loginResponse) {
        setAuthenticatedUser(loginResponse.getUserHolder());
        setAuthToken(loginResponse.getAuthToken());
        //loginResponse has friends and groups data
        setFriendsAndGroups(loginResponse);
    }

    private void doPasswordReset(String historyToken) {
        String loginToken = historyToken.replaceAll("passwordreset/loginToken=", "");
        ApiServiceAsync myService = getApiServiceAsync();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginToken(loginToken);
        myService.confirmLoginByToken(loginRequest, new AsyncCallback() {
            DataOperationDialog dialog = new DataOperationDialog(
                    "Confirming password reset token.");

            public void onFailure(Throwable caught) {
                dialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                dialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    setLoginMobileResponse(mobileResponse);
                    //MyWebApp.this.setAuthenticatedUser(mobileResponse.getUserHolder());
                    PasswordChangeForm upf = new PasswordChangeForm(MyWebApp.this);
                    swapCenter(upf);
                    getMessagePanel().displayMessage("You have been logged in using a temporary login token.");
                    getMessagePanel().displayMessage("Please enter a new password and hit the save button.");
                } else {
                    //let's bring up some screen
                    verifyDisplay();
                    getMessagePanel().displayError("We could not login to your account with the provided token");
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void doConfirmRegistration(String historyToken) {
        //registration/loginToken=${user.loginToken}
        String loginToken = historyToken.replaceAll("registration/loginToken=", "");
        GWT.log("loginToken=" + loginToken);
        final DataOperationDialog dialog = new DataOperationDialog(
                "Confirming registration.");
        ApiServiceAsync myService = getApiServiceAsync();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginToken(loginToken);
        myService.confirmLoginByToken(loginRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                dialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                dialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    setLoginMobileResponse(mobileResponse);
                    //MyWebApp.this.setAuthenticatedUser(mobileResponse.getUserHolder());
                    RegistrationConfirmPanel upf = new RegistrationConfirmPanel(MyWebApp.this);
                    swapCenter(upf);
                    getMessagePanel().displayMessage("Your account has been validated.");
                    getMessagePanel().displayMessage("Now enter a password and press save.");
                } else {
                    //let's bring up some screen
                    verifyDisplay();
                    getMessagePanel().displayError("We could not validate your account with the provided token");
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public static String REGISTER = "register";

    public void toggleRegister(boolean newItem) {
        if (newItem) {
            History.newItem(REGISTER);
            return;
        }
        RegisterForm registerForm = new RegisterForm(this);
        swapCenter(registerForm);
    }

    AsyncCallback saveSpotCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            MobileResponse mobileResponse = (MobileResponse) response;
            if (mobileResponse.getStatus() == 1) {
                //this will update our url;)
                History.newItem(MyWebApp.SPOT_DETAIL + mobileResponse.getSpotHolder().getId(), false);
                SpotDetailPanel spotDetailPanel = new SpotDetailPanel(MyWebApp.this, mobileResponse);
                swapCenter(spotDetailPanel);
            } else {
                verifyDisplay();
                getMessagePanel().clear();
                getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
            }
        }
    };

    //we clicked on a factual location, but for ui consistency, we need to
    //show the spot detail screen
    //let's save this as a spot
    public void showSpot(LocationResult locationResult) {
        Location location = locationResult.getLocation();
        //History.newItem(MyWebApp.IGNORE + location.getFactualId());
        saveLocationAsSpot(locationResult, "",saveSpotCallback);
    }

    //    public void markFactualLocation(LocationResult locationResult) {
//        saveLocationAsSpot(locationResult, markSpotCallback);
//    }
    //sometimes we need to change the url, but not do anything with it
   // public static String IGNORE = "ignore/";

    public static void convert(LocationResult locationResult, SpotHolder spotHolder) {
        Location location = locationResult.getLocation();
        spotHolder.setAddressLine1(location.getAddress1());
        spotHolder.setCity(location.getCity());
        spotHolder.setState(location.getState());
        spotHolder.setZip(location.getZipcode());
        spotHolder.setFactualId(location.getFactualId());
        spotHolder.setWoeid(location.getWoeid());
        spotHolder.setFourSquareId(location.getFourSquareId());
        spotHolder.setFactualCategories(location.getFactualCategories());
        //what about categories??
        spotHolder.setLatitude(location.getLatitude());
        spotHolder.setLongitude(location.getLongitude());
        spotHolder.setCountryCode(location.getCountryCode());
        spotHolder.setName(location.getName());
        spotHolder.setVoicephone(location.getVoicephone());
        spotHolder.setGeocodeInput(location.getAddress1() + ", " + location.getCity() + ", " + location.getState() + " " + location.getZipcode());
        if (locationResult.getYelpHolder() != null) {
            spotHolder.setYelpId(locationResult.getYelpHolder().getId());
        }
        if (!locationResult.getInstagrams().isEmpty()) {
            Data data = locationResult.getInstagrams().get(0);
            GWT.log("setting spotHolder.setInstagramId to" + data.getLocation().getId());
            spotHolder.setInstagramId(data.getLocation().getId());
        }
        //if (locationResult.get)
        if (location.getSpotType() != null) {
            spotHolder.setSpotType(location.getSpotType());
        } else {
            spotHolder.setSpotType(1);
        }
    }

    public void saveLocationAsSpot(LocationResult locationResult, String spotDescription,final AsyncCallback callback) {
        GWT.log("showSpot factual");
        SpotHolder spotHolder = new SpotHolder();
        convert(locationResult, spotHolder);
        spotHolder.setDescription(spotDescription);
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setIgnoreUploads(true);
        saveSpotRequest.setSpotHolder(spotHolder);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.saveSpot(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(Object result) {
                // spotDetailDialog.hide();
                callback.onSuccess(result);
            }
        });
    }

    private void showSpot(Long spotId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setSpotId(spotId);
        final DataOperationDialog spotDetailDialog = new DataOperationDialog(
                "Retrieving spot.");
        spotDetailDialog.show();
        spotDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.spotdetail(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                getMessagePanel().clear();
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    SpotDetailPanel spotDetailPanel = new SpotDetailPanel(MyWebApp.this, mobileResponse);
                    swapCenter(spotDetailPanel);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void verifyDisplay() {
        if (this.currentSpotBasePanel == null) {
            GWT.log("toggleMenul");
            toggleMenu();
        } else {
            GWT.log("currentSpotBasePanel is not null");
        }
    }

    public void toggleResetPasswordRequest(boolean newItem) {
        if (newItem) {
            History.newItem(RESET_PASSWORD_REQUEST);
            return;
        }
        PasswordResetForm passwordResetForm = new PasswordResetForm(this);
        swapCenter(passwordResetForm);
    }

    private String RESET_PASSWORD_REQUEST = "resetpasswordrequest";

    private void initHistorySupport() {
        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> event) {
                //by default, we want the default page.  we will only
                //go to the map page by a call
                //fixPage(1);
                String historyToken = event.getValue();
                //do not want to do metrics
                // if (!isMobileDevice()) {
                if (isDesktop()) {
                    //don't want to do page views for robots
                    if (!isRobot()) {
                        trackPageview(historyToken);
                    }
                }
                //try to process as given, but if we cannot process
                //the provided token, we try a couple of different things
                if (!process(historyToken)) {
                    //try one more time
                    if (!historyToken.startsWith("!")) {
                        historyToken = "!" + historyToken;
                        if (process(historyToken)) {
                            return;
                        }
                    }
                    if (!historyToken.endsWith("/")) {
                        historyToken = historyToken + "/";
                        if (process(historyToken)) {
                            return;
                        }
                    }
                }
            }
        });
    }

    public AsyncCallback showMap = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not show map");
        }

        public void onSuccess(Object response) {
            doMapPage();
            getResultsPanel().buildMap();
        }
    };

    private void showResultsMap() {
        //we may not have results, if we do, resuse them
        if (getResultsPanel().getMobileResponse() == null) {
            getResultsPanel();
            getResultsPanel().resetSearchParameters();
            addCurrentLocation();
            getResultsPanel().getSearchParameters().setSpots(true);
            getResultsPanel().getSearchParameters().setMarks(true);
            getResultsPanel().setSearchTitle("Spots");
            getResultsPanel().getSearchParameters().setItemType("locationmark");
            getResultsPanel().clear();
            getResultsPanel().setTitle("Home");
            getResultsPanel().performSearch(null);
        } else {
            showMap.onSuccess(null);
        }
    }

    private boolean process(String historyToken) {
        boolean newItem = false;
        GWT.log("addValueChangeHandler historyToken=" + historyToken);
        if (historyToken.startsWith("registration")) {
            doConfirmRegistration(historyToken);
        } else if (historyToken.startsWith(REGISTER)) {
            toggleRegister(newItem);
        } else if (historyToken.startsWith("passwordreset")) {
            doPasswordReset(historyToken);
        } else if (historyToken.startsWith("managemember")) {
            manageMember(historyToken);
        } else if (historyToken.startsWith("managefriend")) {
            manageFriend(historyToken);
        } else if (historyToken.startsWith(SPOT_DETAIL)) {
            String id = historyToken.replaceAll(SPOT_DETAIL, "");
            Long spotId = new Long(id);
            toggleSpotDetail(spotId);
        } else if (historyToken.startsWith(MANAGE_SPOT)) {
            String id = historyToken.replaceAll(MANAGE_SPOT, "");
            Long spotId = new Long(id);
            toggleManageSpot(spotId);
        } else if (historyToken.startsWith(GROUP)) {
            String id = historyToken.replaceAll(GROUP, "");
            Long groupId = new Long(id);
            toggleGroup(groupId);
        } else if (historyToken.startsWith(VIEW_USER_GROUPS)) {
            toggleViewUserGroups();
        } else if (historyToken.startsWith(SET_LOCATION_FROM_DEVICE)) {
            setLocationFromDevice();
        } else if (historyToken.startsWith(VIEW_INVITED)) {
            viewInvited();
        } else if (historyToken.startsWith(NEW_USER_GROUP)) {
            addNewUserGroup();
        } else if (historyToken.startsWith(NEW_FRIEND)) {
            addFriend();
        } else if (historyToken.startsWith(MANAGE_FRIENDS)) {
            toggleManageFriends();
        } else if (historyToken.startsWith(MARK_LOCATION)) {
            toggleMarkLocation();
        } else if (historyToken.startsWith(ITEM_DETAIL)) {
            String id = historyToken.replaceAll(ITEM_DETAIL, "");
            Long itemId = new Long(id);
            toggleItemDetail(itemId);
        } else if (historyToken.startsWith(CHAT_DETAIL)) {
            String id = historyToken.replaceAll(CHAT_DETAIL, "");
            Long itemId = new Long(id);
            toggleChatDetail(itemId);
        } else if (historyToken.startsWith(MANAGE_CHAT)) {
            String id = historyToken.replaceAll(MANAGE_CHAT, "");
            Long itemId = new Long(id);
            manageChat(itemId);
        } else if (historyToken.startsWith(JOIN_CHAT)) {
            String id = historyToken.replaceAll(JOIN_CHAT, "");
            Long itemId = new Long(id);
            joinChat(itemId);
        } else if (historyToken.startsWith(HOME)) {
            showHome();
        } else if (historyToken.startsWith(RESULTS_MAP)) {
            this.toggleMapMode.setValue(true);
            showResultsMap();
        } else if (historyToken.startsWith(MENU)) {
            toggleMenu();
        } else if (historyToken.startsWith("report-driver")) {
            toggleMarkPlate();
        } else if (historyToken.startsWith(POLICY)) {
            togglePolicy();
        } else if (historyToken.startsWith(DRIVERS)) {
            toggleDrivers();
        } else if (historyToken.startsWith(CONTESTS)) {
            toggleContests();
        } else if (historyToken.startsWith(LOGIN)) {
            toggleLogin();
        } else if (historyToken.startsWith(MARK_SPOT)) {
            toggleMarkSpot();
        } else if (historyToken.startsWith(MARK_PLATE)) {
            toggleMarkPlate();
        } else if (historyToken.startsWith(SET_LOCATION)) {
            toggleSetLocationManually();
        } else if (historyToken.startsWith(AROUND_LOCATION)) {
            String location = historyToken.replaceAll(AROUND_LOCATION, "");
            //this will set the location
            toggleAroundLocation(location);
        } else if (historyToken.startsWith(ABOUT)) {
            toggleAbout();
        } else if (historyToken.startsWith(FEATURES)) {
            toggleFeatures();
        } else if (historyToken.startsWith(COUPONS)) {
            toggleCoupons();
        } else if (historyToken.startsWith(ABOUT_US)) {
            toggleAboutUS();
        } else if (historyToken.startsWith(DIRECTORY)) {
            toggleDirectory();
        } else if (historyToken.startsWith(CONSOLE)) {
            toggleConsole();
        } else if (historyToken.startsWith(LOGOUT)) {
            toggleLogout(newItem);
        } else if (historyToken.startsWith(LODGING)) {
            toggleLodging();
        } else if (historyToken.startsWith(SEARCH)) {
            toggleSearch();
        } else if (historyToken.startsWith(MAP)) {
            toggleMap();
        } else if (historyToken.startsWith(ADD_CONTEST)) {
            toggleAddContest();
        } else if (historyToken.startsWith(CREATE_CHAT)) {
            toggleCreateChat();
        }else if (historyToken.startsWith(CREATE_SPOT)) {
            toggleCreateSpot();
        } else if (historyToken.startsWith(MANAGE_FRIENDS)) {
            toggleManageFriends();
        } else if (historyToken.startsWith(STATE)) {
            String id = historyToken.replaceAll(STATE, "");
            Long stateId = new Long(id);
            toggleState(newItem, stateId);
        } else if (historyToken.startsWith(ACCOUNT_SETTINGS)) {
            toggleAccountSettings(newItem);
        } else if (historyToken.startsWith(PROFILE_SETTINGS)) {
            toggleProfileSettings();
        } else if (historyToken.startsWith(MOST_RECENTLY_VOTED)) {
            toggleMostRecentlyVoted(newItem);
        } else if (historyToken.startsWith(DINING)) {
            toggleDining();
        } else if (historyToken.startsWith(CHATS)) {
            toggleChats();
        } else if (historyToken.startsWith(FUN)) {
            toggleFun();
        } else if (historyToken.startsWith(VIEW_USER_PROFILE)) {
            String id = historyToken.replaceAll(VIEW_USER_PROFILE, "");
            Long userId = new Long(id);
            toggleViewUserProfile(userId);
        } else if (historyToken.startsWith(MANAGE_PRODUCTS)) {
            String id = historyToken.replaceAll(MANAGE_PRODUCTS, "");
            Long userId = new Long(id);
            toggleManageProducts(userId);
        } else if (historyToken.startsWith(CREATE_EVENT)) {
            String id = historyToken.replaceAll(CREATE_EVENT, "");
            Long userId = new Long(id);
            createEvent(userId);


        } else if (historyToken.startsWith(CREATE_COUPON)) {
            String id = historyToken.replaceAll(CREATE_COUPON, "");
            Long userId = new Long(id);
            createCoupon(userId);




        } else if (historyToken.startsWith(NOTIFICATIONS)) {
            toggleNotifications(newItem);
        } else if (historyToken.startsWith(POSTALCODE)) {
            String id = historyToken.replaceAll(POSTALCODE, "");
            Long postalCodeId = new Long(id);
            togglePostalCode(postalCodeId);
        } else if (historyToken.startsWith(MANAGE_SPOT_FRIEND)) {
            String id = historyToken.replaceAll(MANAGE_SPOT_FRIEND, "");
            Long spotId = new Long(id);
            toggleManageSpotFriend(spotId);
        } else if (historyToken.startsWith(ADD_MY_BIZ)) {
            String id = historyToken.replaceAll(ADD_MY_BIZ, "");
            Long spotId = new Long(id);
            toggleAddMyBiz(spotId);
        } else if (historyToken.startsWith(ADD_SPOT_FRIEND)) {
            String id = historyToken.replaceAll(ADD_SPOT_FRIEND, "");
            Long spotId = new Long(id);
            toggleAddSpotFriend(spotId);
        } else if (historyToken.startsWith(DRINKING)) {
            toggleDrinking();
        } else if (historyToken.startsWith(SETTINGS)) {
            toggleSettings(newItem);
        } else if (historyToken.startsWith(CONTACT)) {
            toggleContact();
        } else if (historyToken.startsWith(CONTEST_DETAIL)) {
            String id = historyToken.replaceAll(CONTEST_DETAIL, "");
            Long contestId = new Long(id);
            toggleContestDetail(contestId);
        } else if (historyToken.startsWith(LEAVE_SPOT_MARK)) {
            String id = historyToken.replaceAll(LEAVE_SPOT_MARK, "");
            Long spotId = new Long(id);
            toggleLeaveMarkForSpot(spotId);
        } else if (historyToken.startsWith(TAG_FILTER)) {
            String tagName = historyToken.replaceAll(TAG_FILTER, "");
            //Long spotId = new Long(id);
            toggleTagClick(tagName);
        } else if (historyToken.startsWith(MANAGE_CONTEST)) {
            String id = historyToken.replaceAll(MANAGE_CONTEST, "");
            Long contestId = new Long(id);
            toggleManageContest(contestId);
        } else if (historyToken.startsWith(RESET_PASSWORD_REQUEST)) {
            toggleResetPasswordRequest(newItem);
        } else if (historyToken.startsWith(MESSAGING)) {
            toggleMessaging();
        } else if (historyToken.startsWith(BIZ_OWNER)) {
            toggleBizOwner(null);
        } else if (historyToken.startsWith(SECRET_KEY)) {
            toggleSecretKey();
        } else if (historyToken.startsWith(FAVORITES)) {
            toggleFavorites(null);
        } else if (historyToken.startsWith(COUNTRY)) {
            String id = historyToken.replaceAll(COUNTRY, "");
            Long countryId = new Long(id);
            toggleCountry(countryId);
        } else if (historyToken.startsWith(CONTEST_TOTAL_VOTES)) {
            String contestId = historyToken.replaceAll(CONTEST_TOTAL_VOTES, "");
            Long id = new Long(contestId);
            String sortKey = "contestTotalVotes_" + contestId + "_l";
            toggleContests(sortKey, id);
        } else if (historyToken.startsWith(CONTEST_AVERAGE_VOTES)) {
            String contestId = historyToken.replaceAll(CONTEST_AVERAGE_VOTES, "");
            Long id = new Long(contestId);
            String sortKey = "contestAverageVote_" + contestId + "_d";
            toggleContests(sortKey, id);
        } else if (historyToken.startsWith(SPOT_GROUPS)) {
            String spotId = historyToken.replaceAll(SPOT_GROUPS, "");
            Long id = new Long(spotId);
            toggleSpotGroups(null, id);
        } else if (historyToken.startsWith("ignore")) {
            //do nothing
        } else {
            //toggleHome();
            // Window.alert("Unknown historyToken " + historyToken);
            return false;
        }
        return true;
    }

    //     toggleManageSpotFriend
    public static String MANAGE_SPOT_FRIEND = "manage_spot_friend/";
    public static String MANAGE_SPOT = "manage_spot/";
    public static String ADD_MY_BIZ = "add-my-biz/";
    public static String ADD_SPOT_FRIEND = "add_spot_friend/";
    public static String MANAGE_CONTEST = "managecontest/";
    public static String TAG_FILTER = "!tag/";
    public static String FACTUAL_TAG = "!ftag/";
    public static String MESSAGING = "!messaging/";
    public static String CONTACT = "!contact/";
    public static String SETTINGS = "!settings/";
    public static String FAVORITES = "!favorites/";
    public static String DRIVERS = "!driver_reports/";
    public static String CONTESTS = "!contests/";
    public static String LOGIN = "!login";
    public static String CONTEST_TOTAL_VOTES = "!contest-total-votes/";
    public static String CONTEST_AVERAGE_VOTES = "!contest-average-votes/";
    public static String MARK_SPOT = "!mark-spot/";
    public static String MARK_PLATE = "!mark-plate/";
    public static String DIRECTORY = "!directory/";
    public static String COUNTRY = "!country/";
    public static String STATE = "!state/";
    public static String POSTALCODE = "!postalcode/";
    public static String SET_LOCATION = "!setlocation/";
    public static String ADD_CONTEST = "!add-contest";
    public static String CREATE_CHAT = "!create-chat";

    public static String CREATE_SPOT = "!create-spot";



    public static String LOGOUT = "logout";
    public static String SEARCH = "!search/";
    public static String FACEBOOK_APPID = "106261966064641";
    //public static final Auth AUTH = Auth.get();
    private Auth auth = null;
    protected TextBox keywordsTextBox = new TextBox();

    public Auth getAuth() {
        return auth;
    }

    private void initAuth() {

        try {
            if (!Storage.isLocalStorageSupported()) {
                return;
            }
        } catch (Exception e) {
            log("isLocalStorageSupported throws error , probably shut down");
            return;

        }

        try {


            Auth.export();
        } catch (Exception e) {
            log("Auth export failure, probably ie");
            return;
        }
        try {
            auth = Auth.get();
        } catch (Exception e) {
            log("Auth get failure, probably ie");
            return;
        }
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {


        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(Throwable throwable) {
                GWT.log("Uncaught Exception", throwable);
                log.log(Level.SEVERE, "uncaught exception", throwable);
                ContactRequest contactRequest = new ContactRequest();
                contactRequest.setSubject("Error Report");
                String text = "Uncaught exception: ";
                while (throwable != null) {
                    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                    text += throwable.toString() + "\n";
                    for (int i = 0; i < stackTraceElements.length; i++) {
                        text += "    at " + stackTraceElements[i] + "\n";
                    }
                    throwable = throwable.getCause();
                    if (throwable != null) {
                        text += "Caused by: ";
                    }
                }
                for (String message : logList) {
                    text += message + "\n";
                }
                contactRequest.setMessage(text);
                ApiServiceAsync myService = getApiServiceAsync();
                myService.contact(contactRequest, new AsyncCallback() {
                    public void onFailure(Throwable caught) {
                        log.log(Level.SEVERE, "onUncaughtException onFailure:" + caught.toString());
                    }

                    public void onSuccess(Object result) {
                        log.log(Level.SEVERE, "onUncaughtException onSuccess:");
                    }
                });
            }
        });

        this.robot = Window.Location.getQueryString().indexOf("robot=true") >= 0;


        //if we do have cookies, let's fail?

        initAuth();
        //phonegap uses the following to init, which will error out in IE
        //$doc.addEventListener($intern_1704, $entry(f), false);
        if (isDesktop()) {
            toggleMilesCheckBox.addClickHandler(toggleKiloMilesHandler);
            toggleMapMode.addClickHandler(showResultsOnMapHandler);
            initSearchBox();
            initMapPanel();
            doDefaultPage();
            runApp();
            return;
        }
        try {
            initSearchBox();
            phoneGap = GWT.create(PhoneGap.class);
            phoneGap.addHandler(new PhoneGapAvailableHandler() {
                @Override
                public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
                    runApp();
                }
            });
            phoneGap.addHandler(new PhoneGapTimeoutHandler() {
                @Override
                public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
                    Window.alert("can not load phonegap");
                }
            });
            log("initializePhoneGap ");
            phoneGap.initializePhoneGap();
        } catch (Exception e) {
            this.phoneGap = null;
            runApp();
        }
    }

    //we use this to track if we have mappage or page being displayed
    //0=none,1=page,2=map
    private int pageModeDisplayed = 0;
    private ListBox tagListBox = new ListBox();

    public ListBox getTagListBox() {
        return tagListBox;
    }

    private ListBox sortingListBox = new ListBox();

    public void doMapPage() {


        if (pageModeDisplayed == 2) return;

        //need to do a loadMapsApi at least once
        if (Maps.isLoaded()) {
            doMap();
        } else {

            Maps.loadMapsApi(getGoogleMapKey(), "2", false, new Runnable() {
                       public void run() {
                           doMap();
                       }
                   });

        }



    }
    //private boolean mapInit = false;

    private void doMap() {
        MapPage page = new MapPage(simplePanel, popularULPanel, latestULPanel, messagePanel, tagCloudPanel, searchBoxPanel, mapPanel, toggleMilesCheckBox, toggleMapMode, markSpotButton, tagListBox, sortingListBox);
        RootPanel.get().clear();
        RootPanel.get().add(page);
        this.pageModeDisplayed = 2;

    }
    SimpleCheckBox toggleMilesCheckBox = new SimpleCheckBox();
    SimpleCheckBox toggleMapMode = new SimpleCheckBox();

    public SimpleCheckBox getToggleMapMode() {
        return toggleMapMode;
    }

    private void doDefaultPage() {
        if (pageModeDisplayed == 1) return;
        Button searchButton = new Button();

        searchButton.addClickHandler(searchHandler);
        this.page = new Page(simplePanel, messagePanel, searchBoxPanel, keywordsTextBox, locationTextBox, profilePicPanel, previousLocationsULPanel, toggleMilesCheckBox, toggleMapMode, markSpotButton, tagListBox, sortingListBox,searchButton);
        RootPanel.get().clear();
        RootPanel.get().add(page);
        this.pageModeDisplayed = 1;
    }

    private Page page = null;
    ULPanel previousLocationsULPanel = new ULPanel();
    SimplePanel profilePicPanel = new SimplePanel();
    ComplexPanel searchBoxPanel = null;

    private void initSearchBox() {
        if (MyWebApp.isDesktop()) {
            initSearchBoxDesktop();
        } else {
            initSearchBoxMobile();
        }
    }


    private void initSearchBoxDesktop() {
        rebuildLocationListBox();
        //final String k2 = "Search for (e.g. chinese, jim's, kabob)";
        keywordsTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSearch();
                }
            }
        });
        keywordsTextBox.setStyleName("keywordsTextBox");

    }



    final String k2 = "Search for (e.g. chinese, jim's, kabob)";

    private void initSearchBoxMobile() {
        rebuildLocationListBox();
        keywordsTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSearch();
                }
            }
        });
        keywordsTextBox.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (keywordsTextBox.getValue().isEmpty()) {
                    keywordsTextBox.setValue(k2);
                }
            }
        });
        keywordsTextBox.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                if (keywordsTextBox.getValue().equals(k2)) {
                    keywordsTextBox.setValue("");
                }
            }
        });
        // keywordsTextBox.setWidth("90%");
        keywordsTextBox.setStyleName("keywordsTextBox");
        keywordsTextBox.setValue(k2);
        //TabPanel tp1 = new TabPanel();
        searchBoxPanel.add(keywordsTextBox);
//        tp1.add(keywordsTextBox, k2);
//        tp1.selectTab(0);
        //searchBoxPanel.setWidth("100%");
//        searchBoxPanel.add(tp1);
//        TabPanel tp = new TabPanel();
        //  tp.add(locationTextBox, "Near (Address, Neighborhood, City, State or Zip)");
        // tp.add(locationsListBox, "Previous Locations");
        //   tp.selectTab(0);
        searchBoxPanel.add(locationsListBox);
        Label searchLabel = new Label("Search");
        searchLabel.addClickHandler(clickLocationSearch);
        searchLabel.setStyleName("whiteButton");
        searchBoxPanel.add(searchLabel);
    }

    private PhoneGap phoneGap = null;

    public void updateTagCloud(QueryResponse queryResponse) {
        if (true) return;
        //we only have tagcloud in desktop app
        if (!isDesktop()) return;
        this.tagCloudPanel.clear();
        this.tagCloud = new TagCloud();
        this.tagCloudPanel.add(tagCloud);
        processTagCloud(queryResponse, tagCloud);
    }

    private void processTagCloud(QueryResponse queryResponse, TagCloud tagCloud) {
        for (FacetField facetField : queryResponse.getFacetFields()) {
            // GWT.log("looping 1");
            for (FacetCount facetCount : facetField.getFacetCounts()) {
                for (int i = 0; i < facetCount.getValueCount(); i++) {
                    String link = "#" + MyWebApp.TAG_FILTER + facetCount.getName();
                    WordTag wordTag = new WordTag(facetCount.getName(), link);
                    tagCloud.addWord(wordTag);
                }
            }
        }
    }

    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public void printStackTrace(Object[] stackTrace) {
        for (Object line : stackTrace) {
            GWT.log(line.toString());
        }
    }

    public void printStackTrace2(Exception exception) {
        Object[] stackTrace = exception.getStackTrace();
        printStackTrace(stackTrace);
    }

    private void toggleChats() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setChats(true);
        addCurrentLocation(searchParameters);
        ApiServiceAsync myService = getApiServiceAsync();
        final DataOperationDialog gettingResultsDialog = new DataOperationDialog("Getting results");
        gettingResultsDialog.show();
        gettingResultsDialog.center();
        myService.searchChats(searchParameters, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                gettingResultsDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                gettingResultsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ChatsPanel chatsPanel = new ChatsPanel(MyWebApp.this, mobileResponse.getSearchQueryResponse());
                    swapCenter(chatsPanel);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void toggleDining() {
        getResultsPanel().resetSearchParameters();
        resultsPanel.getSearchParameters().setDining(true);
        resultsPanel.getSearchParameters().setSpots(true);
        addCurrentLocation();
        getResultsPanel().setTitle("Dining");
        resultsPanel.setImageResources(resources.dining(), resources.diningMobile());
        resultsPanel.performSearch();
    }

    public static String CHATS = "!chats/";
    public static String DINING = "!dining/";
    public static String FUN = "!fun/";


    public void toggleFun() {
        getResultsPanel().resetSearchParameters();
        resultsPanel.getSearchParameters().setFun(true);
        resultsPanel.getSearchParameters().setSpots(true);
        addCurrentLocation();
        getResultsPanel().setTitle("Fun");
        resultsPanel.setImageResources(resources.fun(), resources.funMobile());
        resultsPanel.performSearch();
    }

    //searchform uses data for manufactureres car, so let's init later
    //SearchForm searchForm = new SearchForm(this);
    public static String LODGING = "!lodging/";

    private void toggleLodging() {
        getResultsPanel().resetSearchParameters();
        resultsPanel.getSearchParameters().setLodging(true);
        resultsPanel.getSearchParameters().setSpots(true);
        addCurrentLocation();
        resultsPanel.setTitle("Lodging");
        resultsPanel.setImageResources(resources.lodging(), resources.lodgingMobile());
        resultsPanel.performSearch();
    }

    private void initSetLocationManuallyPanel() {
        if (this.setLocationManuallyPanel == null) {
            this.setLocationManuallyPanel = new SetLocationManuallyPanel(this);
        }
    }

    private void toggleAroundLocation(String location) {
        performLocationSearch(location);
    }

    private void toggleSetLocationManually() {
        initSetLocationManuallyPanel();
        swapCenter(setLocationManuallyPanel);
        getMessagePanel().displayMessage(SetLocationManuallyPanel.WHERE_ARE_YOU);
        //getMessagePanel().displayMessage("Please use the form below to set your location.  The HELP button can give you more assistance.");
    }

    public void addCurrentLocation() {
        addCurrentLocation(getResultsPanel().getSearchParameters());
    }

    public void addCurrentLocation(SearchParameters searchParameters) {
        if (this.getCurrentLocation() != null) {
            searchParameters.setLatitude(getCurrentLocation().getLatitude());
            searchParameters.setLongitude(getCurrentLocation().getLongitude());
        }
    }

    SetLocationManuallyPanel setLocationManuallyPanel = null;
    HomePanel homePanel = new HomePanel(this);

    public void toggleHome(AsyncCallback homeCallback) {
        this.homeCallback = homeCallback;
        History.newItem(HOME);
    }

    public void toggleHome() {
        GWT.log("toggleHome");
        toggleHome(null);
    }

    private void toggleTagClick(String tagName) {
        getResultsPanel().getSearchParameters().getTags().clear();
        TagHolder tagHolder = new TagHolder();
        tagHolder.setName(tagName);
        getResultsPanel().getSearchParameters().getTags().add(tagHolder);
        //need to go back to first page of results
        getResultsPanel().getSearchParameters().setOffset(0);
        getResultsPanel().performSearch();
    }

    public void showHome() {
        GWT.log("showHome");
        if (displaySplash()) {
            return;
        }
        if (!checkValidToToggle()) {
            return;
        }
        getResultsPanel();
        getResultsPanel().resetSearchParameters();
        addCurrentLocation();
        getResultsPanel().getSearchParameters().setSpots(true);
        getResultsPanel().getSearchParameters().setMarks(true);
        getResultsPanel().setSearchTitle("Spots");
        getResultsPanel().getSearchParameters().setItemType("locationmark");
        getResultsPanel().clear();
        getResultsPanel().setTitle("Home");
        GWT.log("performSearch");
        getResultsPanel().performSearch(homeCallback);
        this.homeCallback = null;
        if (getHost().startsWith("http://m.")) {
            getResultsPanel().add(homePanel);
        }
        if (!isSmallFormat()) {
            HomePanel homePanel2 = new HomePanel(this, true);
            getResultsPanel().add(homePanel2);
        }
    }

    private static String NOTIFICATIONS = "notifications";

    //notifications can be device based so let's remove the
    //requirement to login
    public void toggleNotifications(boolean newItem) {
        if (newItem) {
            History.newItem(NOTIFICATIONS);
            return;
        }
        //no need to check if notifications are available, we are always trying to fetch them
        MyWebApp.this.notificationsPanel = new NotificationsPanel(MyWebApp.this);
        swapCenter(notificationsPanel);
    }

    private ULPanel popularULPanel = null;
    private ULPanel latestULPanel = null;



    Panel tagCloudPanel = null;
    TagCloud tagCloud = null;
    FlowPanel mapPanel = new FlowPanel();

    private void initMapPanel() {
        //trying to speed up and not do unncessary things
        if (true) return;

        if (!isDesktop()) {
            return;
        }
        //let's not do google map init if it's a robot
        if (robot) {
            return;
        }
        Maps.loadMapsApi(getGoogleMapKey(), "2", false, new Runnable() {
            public void run() {
                buildMap(mapPanel);
            }
        });
    }

    private void buildMap(FlowPanel simplePanel) {
        mapPanel.clear();
        MapWidget map = null;
        Location location = getCurrentLocation();
        if (location == null) {
            map = new MapWidget();
        } else {
            LatLng latLng = LatLng.newInstance(location.getLatitude(), location.getLongitude());
            map = new MapWidget(latLng, 15);
            //reverseGeocode(latLng, null);
            Marker marker = new Marker(latLng);
            map.addOverlay(marker);
        }
        //map.setSize("100%", "500px");
        map.setHeight("200px");
        map.setUIToDefault();
        MapUIOptions opts = map.getDefaultUI();
        opts.setDoubleClick(false);
        map.setUI(opts);
        map.addMapClickHandler(new MapClickHandler() {
            public void onClick(MapClickEvent e) {
                Overlay overlay = e.getOverlay();
                LatLng point = e.getLatLng();
                if (overlay != null && overlay instanceof Marker) {
                    // no point if we click on marker
                }
            }
        });
        simplePanel.add(map);
    }




    private void populateResults(MobileResponse mobileResponse, ULPanel ulPanel) {
        GWT.log("populateResults");
        for (LocationResult locationResult : mobileResponse.getLocationResults()) {
            ListItem listItem = new ListItem();
            SolrDocument solrDocument = locationResult.getSolrDocument();
            String miniPath = solrDocument.getFirstString("latest_mark_thumbnail_57x57_url_s");
            if (miniPath == null) {
                miniPath = solrDocument.getFirstString("image_thumbnail_57x57_url_s");
            }
            Image image = null;
            if (miniPath == null) {
                image = new Image(MyWebApp.resources.spotImageMini());
            } else {
                image = new Image();
                image.setUrl(miniPath);
            }
            image.setStyleName("tab-thumb");
            image.addStyleName("wp-post-image");
            listItem.add(image);
            FlowPanel info = new FlowPanel();
            info.setStyleName("info");
            listItem.add(info);
            String spot_label_s = solrDocument.getFirstString("spot_label_s");
            Anchor anchor = new Anchor("@" + spot_label_s);
            Long spotId = solrDocument.getFirstLong("spotid_l");
            anchor.setHref("#" + MyWebApp.SPOT_DETAIL + spotId);
            info.add(anchor);
            GWT.log(solrDocument.toString());
            String createdAt = solrDocument.getFirstString("updatedate_s");
            //we have a lot of old docs with this not in the index
            if (createdAt != null) {
                InlineLabel meta = new InlineLabel("At " + createdAt);
                meta.setStyleName("meta");
                info.add(meta);
            }
            addClear(listItem);
            ulPanel.add(listItem);
        }
    }

    private void addClear(Panel flowPanel) {
        FlowPanel clear = new FlowPanel();
        clear.setStyleName("clear");
        flowPanel.add(clear);
    }

    public Panel getTopContents(SpotBasePanel spotBasePanel) {
        Image image = spotBasePanel.getImage();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setStyleName("topContents");
        verticalPanel.addStyleName("panel");
        verticalPanel.setWidth("100%");
        verticalPanel.add(topMenuPanel);
        HorizontalPanel hp = new HorizontalPanel();
        verticalPanel.add(hp);
        hp.setWidth("100%");
        hp.add(image);
        hp.setCellWidth(image, "1%");
        VerticalPanel verticalPanel2 = new VerticalPanel();
        verticalPanel2.setWidth("100%");
        if (!isDesktop()) {
            //ANDROID
            if (spotBasePanel.displayLocationForm()) {
                //searchBoxPanel
                verticalPanel.add(searchBoxPanel);
            }
        }
        hp.add(verticalPanel2);
        hp.setCellWidth(verticalPanel2, "100%");
        //dont' add message panel here if we are on desktop layout
        if (!MyWebApp.isDesktop()) {
            verticalPanel2.add(messagePanel);
        }
        return verticalPanel;
    }

    //this is what the "messaging" menu calls.
    public void toggleMessaging() {
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                GWT.log("toggleMessaging callback");
                toggleMessaging();
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        if (!isFriendsDataAvailable(loginCallback)) {
            return;
        }
        if (!isMessagesDataAvailable(loginCallback)) {
            return;
        }
        MyWebApp.this.messagingPanel = new MessagingPanel(MyWebApp.this);
        swapCenter(messagingPanel);
    }

    public MessagingPanel getMessagingPanel() {
        return messagingPanel;
    }

    public static List<GroupHolder> getGroups(List<GroupHolder> groupHolders) {
        List<GroupHolder> groupList = new ArrayList<GroupHolder>();
        for (GroupHolder groupHolder : groupHolders) {
            groupList.add(groupHolder);
        }
        return groupList;
    }

    private boolean isMessagesDataAvailable(AsyncCallback mycallback) {
        GWT.log("messagesDataAvailable");
        if (this.getMessages() == null) {
            fetchMessages(mycallback);
            messagesDataDialog = new DataOperationDialog("Getting Messages");
            return false;
        } else {
            return true;
        }
    }

    public void fetchContests(final AsyncCallback callback) {
        GWT.log("fetchContests");
        ContestRequest contestRequest = new ContestRequest();
        contestRequest.setAuthToken(this.getAuthToken());
        if (this.getCurrentLocation() != null) {
            contestRequest.getSearchParameters().setLatitude(getCurrentLocation().getLatitude());
            contestRequest.getSearchParameters().setLongitude(getCurrentLocation().getLongitude());
            contestRequest.getSearchParameters().setRadius(50.0);
        }
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchContests(contestRequest, new AsyncCallback() {
            DataOperationDialog fetchingDialog = new DataOperationDialog(
                    "Fetching contests");

            public void onFailure(Throwable caught) {
                fetchingDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                fetchingDialog.hide();
                GWT.log("fetchContests onSuccess ");
                MobileResponse mobileResponse = (MobileResponse) result;
                MyWebApp.this.contests = mobileResponse;
                if (callback != null) {
                    GWT.log("fetchContests onSuccess callback is not null");
                    GWT.log("status= " + mobileResponse.getStatus());
                    if (mobileResponse.getStatus() == 1) {
                        callback.onSuccess(null);
                    }
                } else {
                    GWT.log("fetchContests onSuccess callback is null");
                }
            }
        });
    }

    private void toggleManageContest(final Long contestId) {
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                MyWebApp.this.toggleManageContest(contestId);
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        AsyncCallback callback2 = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                //getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                MobileResponse mobileResponse = (MobileResponse) response;
                if (mobileResponse.getStatus() == 0) {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                } else {
                    ContestHolder contestHolder = mobileResponse.getContestHolder();
                    if (!haveAccess(contestHolder)) {
                        getMessagePanel().displayMessage("You do not have access to this contest.");
                        getMessagePanel().displayMessage("A message has been sent to the contest owner to allow them to grant you access.");
                        getMessagePanel().displayMessage("If you do not receive a response within 1-2 days, you can use the contact form to contact spotmouth.");
                        contestAccessContact(contestHolder);
                    } else {
                        ManageContestPanel mcp = new ManageContestPanel(MyWebApp.this, contestHolder);
                        swapCenter(mcp);
                    }
                }
            }
        };
        fetchContest(callback2, contestId);
    }

    private void contestAccessContact(ContestHolder contestHolder) {
        //call api and contact contest holder to grant access
        ContestRequest contestRequest = new ContestRequest();
        contestRequest.setAuthToken(getAuthToken());
        contestRequest.getContestHolder().setId(contestHolder.getId());
        ApiServiceAsync myService = getApiServiceAsync();
        myService.contestAuthRequest(contestRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                //loginCallback.onFailure(caught);
            }

            public void onSuccess(Object result) {
                //loginCallback.onSuccess(result);
            }
        });
    }

    private boolean haveAccess(ContestHolder contestHolder) {
        UserHolder userHolder = getAuthenticatedUser();
        for (UserHolder editor : contestHolder.getEditors()) {
            if (editor.getId().equals(userHolder.getId())) {
                return true;
            }
        }
        return false;
    }

    private void fetchContest(final AsyncCallback loginCallback, Long contestId) {
        ContestRequest contestRequest = new ContestRequest();
        contestRequest.setAuthToken(getAuthToken());
        contestRequest.getContestHolder().setId(contestId);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchContest(contestRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                // loginCallback.onFailure(caught);
                //getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
            }

            public void onSuccess(Object result) {
                loginCallback.onSuccess(result);
            }
        });
    }

    public static String SECRET_KEY = "!secretkey/";

    public void toggleSecretKey() {
//        if (newItem) {
//            History.newItem(SECRET_KEY);
//            return;
//        }
        this.getMessagePanel().clear();
        // need to clear out keyword search if one was done
        getResultsPanel().resetSearchParameters();
        SecretKeyForm secretKeyForm = new SecretKeyForm(this);
        swapCenter(secretKeyForm);
    }

    public static String RESULTS_MAP = "!results-map/";
    public static String HOME = "!home/";
    public static String MENU = "!menu/";
    private static String POLICY = "!policy/";

    private void togglePolicy() {

        this.getMessagePanel().clear();
        // need to clear out keyword search if one was done
        UGRPanel aboutPanel = new UGRPanel(this);
        swapCenter(aboutPanel);
    }

    public static String AROUND_LOCATION = "around-location/";
    public static String ABOUT_US = "!about-us/";
    public static String ABOUT = "!about/";
    public static String FEATURES = "!features/";
    public static String COUPONS = "!coupons/";

    private void toggleAboutUS() {
        this.getMessagePanel().clear();
        String html = MyHtmlResources.INSTANCE.getAboutUSHtml().getText();
        AboutPanel aboutPanel = new AboutPanel(this, html);
        swapCenter(aboutPanel);
    }

    private void toggleAbout() {
        this.getMessagePanel().clear();
        // need to clear out keyword search if one was done
        String html = MyHtmlResources.INSTANCE.getAboutHtml().getText();
        AboutPanel aboutPanel = new AboutPanel(this, html);
        swapCenter(aboutPanel);
    }

    private void toggleFeatures() {
        this.getMessagePanel().clear();
        // need to clear out keyword search if one was done
        String html = MyHtmlResources.INSTANCE.getFeatures().getText();
        //FeaturesPanel featuresPanel = new FeaturesPanel();

            AboutPanel aboutPanel = new AboutPanel(this, html);
        swapCenter(aboutPanel);
    }

    public void toggleLeaveMark(ItemHolder itemHolder) {
        LeaveMarkForm leaveMarkForm = new LeaveMarkForm(this, null, false, itemHolder);
        swapCenter(leaveMarkForm);
    }

    //we are on the spot detail page, and want to leave a mark
    //we don't want to go get the spot again from back end
    //but we need to update the URL
    public void toggleLeaveMark(SpotHolder spotHolder) {
        GWT.log("toggleLeaveMark with spotholder");
        //History.newItem(MyWebApp.LEAVE_SPOT_MARK + spotHolder.getId());
        //History.fireCurrentHistoryState();
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.setSpotHolder(spotHolder);
        LeaveMarkForm leaveMarkForm = new LeaveMarkForm(this, null, false, itemHolder);
        swapCenter(leaveMarkForm);
    }
    public static String LISTING_INFO = "listinginfo/";



    public static String LEAVE_SPOT_MARK = "!leavemarkspot/";
    /*
   *   Wierd. We want the contest to be bookmarkable, currently, we just need the solritem
   *
   * */

    public void toggleContestDetail(Long contestId) {
        getMessagePanel().clear();
        SearchRequest searchRequest = new SearchRequest();
        SearchParameters searchParameters = searchRequest.getSearchParameters();
        searchParameters.setGeospatialOff(true);
        searchParameters.setContestId(contestId);
        searchParameters.setContests(true);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.search(searchParameters, new AsyncCallback() {
            DataOperationDialog contestDialog = new DataOperationDialog(
                    "Retrieving contest.");

            public void onFailure(Throwable caught) {
                contestDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                contestDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                LocationResult locationResult = mobileResponse.getLocationResults().get(0);
                SolrDocument solrDocument = locationResult.getSolrDocument();
                ViewContestPanel viewContestPanel = new ViewContestPanel(MyWebApp.this, solrDocument);
                swapCenter(viewContestPanel);
            }
        });
    }

    private void toggleLeaveMarkForSpot(Long selectedSpotId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setSpotId(selectedSpotId);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.spotdetail(searchRequest, new AsyncCallback() {
            DataOperationDialog spotDetailDialog = new DataOperationDialog(
                    "Retrieving spot detail data.");

            public void onFailure(Throwable caught) {
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getSpotHolder() == null) {
                    GWT.log("spotholder is null from spotdetail");
                }
                toggleLeaveMark(mobileResponse.getSpotHolder());
            }
        });
    }

    public static String VIEW_USER_PROFILE = "!userprofile/";




    private void createCoupon(final Long spotId) {

        getSpot(spotId, createCouponCallback);
    }



    AsyncCallback createCouponCallback = new AsyncCallback() {
       public void onFailure(Throwable throwable) {
       }

       public void onSuccess(Object response) {
           SpotHolder spotHolder = (SpotHolder) response;
           ItemHolder itemHolder = new ItemHolder();
           CouponForm eventForm = new CouponForm(MyWebApp.this, spotHolder,itemHolder);
           swapCenter(eventForm);
       }
   };



     AsyncCallback createEventCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
        }

        public void onSuccess(Object response) {
            SpotHolder spotHolder = (SpotHolder) response;
            ItemHolder itemHolder = new ItemHolder();

            EventForm eventForm = new EventForm(MyWebApp.this, spotHolder,itemHolder);
            swapCenter(eventForm);
        }
    };


    private void createEvent(final Long spotId) {

        getSpot(spotId, createEventCallback);
    }


    private void getSpot(final Long spotId,final AsyncCallback gotSpotCallback){
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                GWT.log("toggleMessaging callback");
                //toggleManageSpot(spotId);
                getSpot(spotId,gotSpotCallback);
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setSpotId(spotId);
        final DataOperationDialog spotDetailDialog = new DataOperationDialog(
                "Retrieving spot.");
        spotDetailDialog.show();
        spotDetailDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        myService.spotdetail(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                getMessagePanel().clear();
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (!haveAccess(mobileResponse.getSpotHolder())) {
                    getMessagePanel().displayMessage("You do not have access to this spot.  It is locked.");
                    getMessagePanel().displayMessage("A message has been sent to the spot owner to allow them to grant you access.");
                    getMessagePanel().displayMessage("If you do not receive a response within 1-2 days, you can use the contact form to contact spotmouth.");
                    spotAuthRequest();
                    return;
                } else {
                }
                if (mobileResponse.getStatus() == 1) {
                    gotSpotCallback.onSuccess(mobileResponse.getSpotHolder());
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });

    }

    AsyncCallback manageProductsCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
        }

        public void onSuccess(Object response) {
            SpotHolder spotHolder = (SpotHolder) response;
            ManageProductsPanel manageProductsPanel = new ManageProductsPanel(MyWebApp.this, spotHolder);
            swapCenter(manageProductsPanel);
        }
    };


    private void toggleManageProducts(final Long spotId) {
        getSpot(spotId, manageProductsCallback);
    }

    public void toggleViewUserProfile(Long userId) {
        getMessagePanel().clear();
        SearchRequest searchRequest = new SearchRequest();
        SearchParameters searchParameters = searchRequest.getSearchParameters();
        searchParameters.setUserId(userId);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.viewprofile(searchRequest, new AsyncCallback() {
            DataOperationDialog userProfileDialog = new DataOperationDialog(
                    "Retrieving user profile.");

            public void onFailure(Throwable caught) {
                userProfileDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                userProfileDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ViewProfilePanel vpp = new ViewProfilePanel(MyWebApp.this,
                            mobileResponse);
                    swapCenter(vpp);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public static String MAP = "map";
    SpotMap spotMap = null;

    private void toggleMap() {
        this.spotMap = new SpotMap(this, currentLocation);
        swapCenter(spotMap);
    }

    public void toggleBizOwner(final AsyncCallback messageCallback) {
        AsyncCallback backCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleBizOwner(messageCallback);
            }
        };
        if (!isLoggedIn(backCallback)) {
            return;
        }
        if (!isMyBizAvailable(backCallback)) {
            return;
        }
        if (this.myBiz.getLocationResults().size() == 1) {
            //let's just go to tha one spot
            LocationResult locationResult = this.myBiz.getLocationResults().get(0);
            SolrDocument solrDocument = locationResult.getSolrDocument();
            Long spotId = solrDocument.getFirstLong("spotid_l");
            History.newItem(MyWebApp.MANAGE_SPOT + spotId);
            return;
        }
        BusinessOwnerPanel businessOwnerPanel = new BusinessOwnerPanel(this);
        swapCenter(businessOwnerPanel);
        if (messageCallback != null) {
            messageCallback.onSuccess(null);
        }
    }

    private void toggleCoupons() {
        ApiServiceAsync myService = getApiServiceAsync();
        Location location = getCurrentLocation();
        final DataOperationDialog couponDialog = new DataOperationDialog("Getting coupons...");
        myService.findCoupons(location, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                couponDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                couponDialog.hide();
                MobileResponse couponResponse = (MobileResponse) result;
                if (couponResponse.getStatus() == 1) {
                    Coupons8Panel coupons8Panel = new Coupons8Panel(MyWebApp.this, couponResponse);
                    swapCenter(coupons8Panel);
                } else {
                    verifyDisplay();
                    getMessagePanel().displayErrors(couponResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleFavorites(final AsyncCallback messageCallback) {
        AsyncCallback backCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                GWT.log("toggleMessaging callback");
                toggleFavorites(messageCallback);
            }
        };
        if (!isLoggedIn(backCallback)) {
            return;
        }
        if (!isFavoritiesAvailable(backCallback)) {
            return;
        }
        FavoritesPanel favoritesPanel = new FavoritesPanel(this);
        swapCenter(favoritesPanel);
        if (messageCallback != null) {
            messageCallback.onSuccess(null);
        }
    }

    private void addNewUserGroup() {
        AsyncCallback backCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                addNewUserGroup();
            }
        };
        if (!isLoggedIn(backCallback)) {
            return;
        }
        UserGroupHolder groupHolder = new UserGroupHolder();
        ManageUserGroupPanel mgp = new ManageUserGroupPanel(this, groupHolder);
        swapCenter(mgp);
    }

    private static String MOST_RECENTLY_VOTED = "most-recently-voted";

    public void toggleMostRecentlyVoted(boolean newItem) {
        if (newItem) {
            History.newItem(MOST_RECENTLY_VOTED);
            return;
        }
        getResultsPanel().clear();
        resultsPanel.resetSearchParameters();
        addCurrentLocation();
        //    Date mostRecentVoteDate = null;
        //resultsPanel.getSearchParameters().setSpots(true);
        resultsPanel.getSearchParameters().setVotedUpon(true);
        resultsPanel.getSearchParameters().setSpots(true);
        resultsPanel.getSearchParameters().setLicensePlate(true);
        resultsPanel.getSearchParameters().setMarks(true);
        resultsPanel.getSearchParameters().setUsers(true);
        resultsPanel.getSearchParameters().setSortKey("mostRecentVoteDate_dt");
        resultsPanel.getSearchParameters().setSortOrder("desc");
        resultsPanel.getSearchParameters().setExcludeFactual(true);
        resultsPanel.setTitle("Most Recently Voted");
       // getResultsPanel().setImageResources(resources.contests(), resources.contestsMobile());
        resultsPanel.performSearch();
    }

    private void addFriend() {
        FriendHolder friendHolder = new FriendHolder();
        friendHolder.setEmailInviteAccepted(false);
        friendHolder.setEmailInviteSent(false);
        ManageFriendPanel mgp = new ManageFriendPanel(this, friendHolder, null);
        swapCenter(mgp);
    }

    private void viewInvited() {
        Invited mgp = new Invited(this);
        swapCenter(mgp);
    }

    public void toggleContests() {
        //do not need to login to view all contests, just to add/edit
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                ContestsPanel mcp = new ContestsPanel(MyWebApp.this);
                swapCenter(mcp);
            }
        };
        this.fetchContests(loginCallback);
    }

    SearchForm searchForm = null;

    public void toggleSearch() {
        if (searchForm == null) {
            //we are doing a lazy init so manufacturers are retrieved
            searchForm = new SearchForm(this);
        }
        swapCenter(searchForm);
    }

    public void toggleMarkSpot() {
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleMarkSpot();
            }
        };
        if (!isLocationSet(callback)) {
            return;
        }
        markSpotTypePanel = new MarkSpotTypePanel(this);
        swapCenter(markSpotTypePanel);
    }

    public void toggleMarkPlate() {

        //need to have currentLocation set

        PlateSearchPanel plateSearchPanel = new PlateSearchPanel(this);
        //markSpotPanel.activate(true);
        swapCenter(plateSearchPanel);
    }

    public void toggleDirectory() {
        DirectoryCountriesPanel directoryPanel = new DirectoryCountriesPanel(this);
        swapCenter(directoryPanel);
    }

    private void toggleLogin() {
        loginForm = new LoginForm(this);
        if (isDesktop()) {
            vp.add(loginForm);
        } else {
            swapCenter(loginForm);
        }
    }

    AsyncCallback logoutMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Logged out");
        }
    };

    public void toggleLogout(boolean newItem) {
        if (newItem) {
            History.newItem(LOGOUT);
            return;
        }
        log("setAuthenticatedUser");
        setAuthenticatedUser(null);
        setFacebookUser(false);
        setFriendsAndGroups(null);
        toggleMenu(logoutMessageCallback);
        //this will remove all the cookies, since we don't use any
        //we should be cool
        java.util.Collection<String> cookies = Cookies.getCookieNames();
        java.util.Iterator<String> vals = cookies.iterator();
        log("clearing cookies");
        while (vals.hasNext()) {
            String val = vals.next();
            log("removing cookie val=" + val);
            Cookies.removeCookie(val);
        }

        //http://stackoverflow.com/questions/8049467/remove-cookie-issue
        Cookies.removeCookie("spotmouth_session_id", "/");

        // String spotmouth_session_id = Cookies.getCookie("spotmouth_session_id");

        if (isFacebookUser()) {
            log("mywebapp.fbConnect.logout");
            if (fbConnect != null) {
                //mywebapp.fbConnect.logout(MyWebApp.APPID);
                fbConnect.logout(MyWebApp.FACEBOOK_APPID);
            }
        } else {
            log("mywebapp.isFacebookUser is false");
        }
    }

    private void toggleDrivers() {
        getResultsPanel().clear();
        getResultsPanel().resetSearchParameters();
        addCurrentLocation();
        getResultsPanel().getSearchParameters().setLicensePlate(true);
        getResultsPanel().getSearchParameters().setSpots(true);
        getResultsPanel().setTitle("Driver Reports");
        getResultsPanel().setImageResources(resources.drivers(), resources.driversMobile());
        getResultsPanel().performSearch();
    }

    public String getUrl(String href) {
        if (href.startsWith("http://")) {
            return href;
        } else {
            if (href.startsWith("/")) {
                return getHost() + href;
            } else {
                return getHost() + "/" + href;
            }
        }
    }

    private void toggleContests(String sortKey, Long contestId) {
        getResultsPanel().clear();
        resultsPanel.resetSearchParameters();
        addCurrentLocation();
        getResultsPanel().getSearchParameters().setLicensePlate(true);
        resultsPanel.setTitle("Contests");
        resultsPanel.getSearchParameters().setMarks(true);
        resultsPanel.getSearchParameters().setSpots(true);
        resultsPanel.getSearchParameters().setUsers(true);
        resultsPanel.getSearchParameters().setSortKey(sortKey);
        resultsPanel.getSearchParameters().setSortOrder("desc");
        resultsPanel.getSearchParameters().setExcludeFactual(true);
        resultsPanel.getSearchParameters().setContestId(contestId);
        resultsPanel.performSearch();
    }

    public static String MARK_LOCATION = "!marklocation/";

    public void toggleMarkLocation() {
        AsyncCallback callback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleMarkLocation();
            }
        };
        if (!isLocationSet(callback)) {
            return;
        }
        Location location = getCurrentLocation();
        ItemHolder itemHolder = new ItemHolder();
        MarkLocationForm leaveMarkForm = new MarkLocationForm(this, location, true, itemHolder);
        swapCenter(leaveMarkForm);
    }

    public static String SPOT_GROUPS = "spotgroups/";

    public void toggleSpotGroups(Long spotId) {
        String token = SPOT_GROUPS + spotId;
        History.newItem(token);
    }

    public static String DRINKING = "drinking/";

    public void toggleDrinking() {
        getResultsPanel().resetSearchParameters();
        resultsPanel.getSearchParameters().setDrinking(true);
        resultsPanel.getSearchParameters().setSpots(true);
        addCurrentLocation();
        getResultsPanel().setTitle("Drinking");
        resultsPanel.setImageResources(resources.drinking(), resources.drinkingMobile());
        resultsPanel.performSearch();
    }

    /*
    we call this when we want to re-execute the existing search
     */
    public void toggleSearch(final AsyncCallback messageCallback) {
        History.newItem(MyWebApp.SEARCH_RESULTS,false);
        getResultsPanel().performSearch(messageCallback);
    }

    /*
   there are 2 toggleSpotGroups, one is called when it is deleted and will not update the menu
    */

    public void toggleSpotGroups(final AsyncCallback messageCallback, Long selectedSpotId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setSpotId(selectedSpotId);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.spotdetail(searchRequest, new AsyncCallback() {
            DataOperationDialog spotDetailDialog = new DataOperationDialog(
                    "Retrieving group information.");

            public void onFailure(Throwable caught) {
                spotDetailDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                spotDetailDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                ViewGroupsPanel mfp = new ViewGroupsPanel(MyWebApp.this, mobileResponse);
                swapCenter(mfp);
                if (messageCallback != null) {
                    messageCallback.onSuccess(null);
                }
            }
        });
    }

    private void toggleViewUserGroups() {
        GWT.log("toggleViewUserGroups");
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleViewUserGroups();
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        if (!isFriendsDataAvailable(loginCallback)) {
            return;
        }
        ViewUserGroupsPanel mfp = new ViewUserGroupsPanel(MyWebApp.this, MyWebApp.this.getFriendsAndGroups());
        swapCenter(mfp);
        if (homeCallback != null) {
            homeCallback.onSuccess(null);
            this.homeCallback = null;
        }
    }

    private boolean isFavoritiesAvailable(AsyncCallback loginCallback) {
        GWT.log("isFavoritiesAvailable");
        if (this.getFavorities() == null) {
            this.fetchFavorites(loginCallback);
            return false;
        } else {
            return true;
        }
    }

    private boolean isMyBizAvailable(AsyncCallback loginCallback) {
        if (this.getMyBiz() == null) {
            this.fetchMyBiz(loginCallback);
            return false;
        } else {
            return true;
        }
    }

    private boolean isFriendsDataAvailable(AsyncCallback loginCallback) {
        GWT.log("isFriendsGroupDataAvailable");
        if (this.getFriendsAndGroups() == null) {
            this.fetchFriendsAndGroups(loginCallback);
            return false;
        } else {
            return true;
        }
    }

    /*
    when app is restarted, let's try to restore the login
     */
    private void checkForExistingLoginSession() {
        String spotmouth_session_id = Cookies.getCookie("spotmouth_session_id");
        if (spotmouth_session_id == null) {
            //no login already
            return;
        }

        ApiServiceAsync myService = getApiServiceAsync();


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLoginToken(spotmouth_session_id);
        myService.confirmLoginByCookieValue(loginRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError("Login init error: " + caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    setLoginMobileResponse(mobileResponse);
                }
            }
        });
    }
    public boolean isLoggedIn(final AsyncCallback loginCallback) {
        GWT.log("isLoggedIn");
        if (getAuthenticatedUser() == null) {
//            //do we have a cookie that contains a loginid?
//            String spotmouth_session_id = Cookies.getCookie("spotmouth_session_id");
//            if (spotmouth_session_id != null) {
//                ApiServiceAsync myService = getApiServiceAsync();
//                LoginRequest loginRequest = new LoginRequest();
//                loginRequest.setLoginToken(spotmouth_session_id);
//                myService.confirmLoginByCookieValue(loginRequest, new AsyncCallback() {
//                    public void onFailure(Throwable caught) {
//                        getMessagePanel().displayError("Login init error: " + caught.getMessage());
//                    }
//
//                    public void onSuccess(Object result) {
//                        MobileResponse mobileResponse = (MobileResponse) result;
//                        if (mobileResponse.getStatus() == 1) {
//                            setLoginMobileResponse(mobileResponse);
//                            loginCallback.onSuccess(null);
//                        } else {
//                            //getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
//                            //let's login
                           // showLogin(loginCallback);
//                        }
//                    }
//                });
//                return false;
//            }
            showLogin(loginCallback);
            return false;
        } else {
            return true;
        }
    }

    private void showLogin(AsyncCallback loginCallback) {
        loginForm = new LoginForm(this);
        //we want the login form to popup, but not lose current place in
        //app which swapCenter will do, let's add this to structure
        verifyDisplay();
        vp.add(loginForm);
        //swapCenter(loginForm);
        GWT.log("isLoggedIn is false");
        loginForm.setCallback(loginCallback);
        getMessagePanel().clear();
        getMessagePanel().displayMessage("What you want you to do requires you to login now.");
    }

    public static String NEW_USER_GROUP = "new-user-group";
    public static String NEW_FRIEND = "new-friend";
    private static String PROFILE_SETTINGS = "profile-settings";
    //private static String USER_PROFILE_FORM = "user-profile-form";
    private static String ACCOUNT_SETTINGS = "account-settings";

    public void toggleProfileSettings() {
        //temp
//        AsyncCallback backCallback = new AsyncCallback() {
//            public void onFailure(Throwable throwable) {
//                Window.alert(throwable.getMessage());
//            }
//
//            public void onSuccess(Object response) {
//                toggleUserProfileForm(false);
//            }
//        };
//        if (!isLoggedIn(backCallback)) {
//            return;
//        }
        ProfileSettingsPanel upf = new ProfileSettingsPanel(this);
        swapCenter(upf);
    }

    public void toggleAccountSettings(boolean newItem) {
        if (newItem) {
            History.newItem(ACCOUNT_SETTINGS);
            return;
        }
//        AsyncCallback backCallback = new AsyncCallback() {
//            public void onFailure(Throwable throwable) {
//                Window.alert(throwable.getMessage());
//            }
//
//            public void onSuccess(Object response) {
//                toggleUserProfileForm(false);
//            }
//        };
//        if (!isLoggedIn(backCallback)) {
//            return;
//        }
        AccountSettingsPanel upf = new AccountSettingsPanel(this);
        swapCenter(upf);
    }

    private void toggleManageFriends() {
        AsyncCallback backCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleManageFriends();
            }
        };
        if (!isLoggedIn(backCallback)) {
            return;
        }
        if (!isFriendsDataAvailable(backCallback)) {
            return;
        }
        ManageFriendsPanel mfp = new ManageFriendsPanel(MyWebApp.this);
        swapCenter(mfp);
        if (homeCallback != null) {
            homeCallback.onSuccess(null);
            this.homeCallback = null;
        }
    }
    private void toggleCreateSpot() {

        CreateSpotMarkPanel createSpotMarkPanel = new CreateSpotMarkPanel(this);

        swapCenter(createSpotMarkPanel);

    }

    private void toggleCreateChat() {
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.setStartDate(new Date());
        Date endDate = new Date();
        CalendarUtil.addDaysToDate(endDate, 1);
        itemHolder.setEndDate(endDate);
        //creating a new contest, let's use our location to init things
        if (getCurrentLocation() != null) {
//            chatHolder.setAddressLine1(getCurrentLocation().getAddress1());
//            chatHolder.setCity(getCurrentLocation().getCity());
//            chatHolder.setState(getCurrentLocation().getState());
//            chatHolder.setZip(getCurrentLocation().getZipcode());
//            chatHolder.setCountryCode(getCurrentLocation().getCountryCode());
        }
        itemHolder.setStartDate(new Date());
        ManageChatPanel mcp = new ManageChatPanel(this, itemHolder);
        //mcp.setTitle("Add Contest");
        swapCenter(mcp);
    }

    public void toggleAddContest() {
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleAddContest();
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        ContestHolder contestHolder = new ContestHolder();
        contestHolder.setIconStyle(1);
        contestHolder.setNumberOfStars(3);
        //creating a new contest, let's use our location to init things
        if (getCurrentLocation() != null) {
            contestHolder.setAddressLine1(getCurrentLocation().getAddress1());
            contestHolder.setCity(getCurrentLocation().getCity());
            contestHolder.setState(getCurrentLocation().getState());
            contestHolder.setZip(getCurrentLocation().getZipcode());
            contestHolder.setCountryCode(getCurrentLocation().getCountryCode());
            contestHolder.setStartDate(new Date());
        }
        ManageContestPanel mcp = new ManageContestPanel(this, contestHolder);
        //mcp.setTitle("Add Contest");
        swapCenter(mcp);
    }

    private boolean isLocationSet(AsyncCallback callback) {
        if (this.getCurrentLocation() == null) {
            GWT.log("current location is null, going to set locally manually");
            //this.callback = toggleMarkSpotCallback;
            // we need to set a location before leaving a mark
            this.initSetLocationManuallyPanel();
            this.setLocationManuallyPanel.setCallback(callback);
            this.toggleSetLocationManually();
            getMessagePanel().displayMessage("Before marking a spot, we need to know your location.");
            return false;
        }
        return true;
    }

    public void log(String message) {
        log.warning(message);
        logList.add(message);
        GWT.log(message);
        Log.warn(message);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private String authToken = null;
    AsyncCallback deviceHomeCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not set your location by your device");
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your location has been set by your device");
        }
    };

    public void setHomeCallback(AsyncCallback homeCallback) {
        this.homeCallback = homeCallback;
    }

    private AsyncCallback homeCallback = null;
    AsyncCallback deviceGeolocationCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not set your location by your device");
        }

        public void onSuccess(Object response) {
            //MyWebApp.this.homeCallback = deviceHomeCallback;
            toggleHome(deviceHomeCallback);
        }
    };

    private void setLocationFromDevice() {
        //if (phoneGap.)
        setAutoGps(true);
        //need to notify user when the location has been set
        setGeolocationCallback(deviceGeolocationCallback);
        refreshLocation();
    }

    private void toggleManageSpotFriend(final Long spotId) {
        //let's be logged in!
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                toggleManageSpotFriend(spotId);
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setAuthToken(getAuthToken());
        friendRequest.setSpotHolder(new SpotHolder());
        friendRequest.getSpotHolder().setId(spotId);
        ApiServiceAsync myService = getApiServiceAsync();
        myService.fetchFriend(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError("Fetch Friend Error: " + caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    SpotHolder spotHolder = mobileResponse.getSpotHolder();
                    FriendHolder friendHolder = mobileResponse.getFriendHolder();
                    SpotFriendPanel spotFriendPanel = new SpotFriendPanel(MyWebApp.this, friendHolder, spotHolder);
                    swapCenter(spotFriendPanel);
                } else {
                    //getMessagePanel().displayError("Fetch Friend Error: " + caught.getMessage());
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void toggleAddSpotFriend(final Long spotId) {
        //let's be logged in!
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                //if we hit this callback, we don't want to stay on the login screen
                toggleMenu();
                toggleAddSpotFriend(spotId);
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        ApiServiceAsync myService = getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSpotHolder(new SpotHolder());
        friendRequest.getSpotHolder().setId(spotId);
        friendRequest.setAuthToken(getAuthToken());
        myService.followSpot(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Added to favorites");
                    setFavorites(null);
                    //mywebapp.toggleFavorites(followingMessages);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void toggleAddMyBiz(final Long spotId) {
        //let's be logged in!
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                //if we hit this callback, we don't want to stay on the login screen
                toggleMenu();
                toggleAddMyBiz(spotId);
            }
        };
        if (!isLoggedIn(loginCallback)) {
            return;
        }
        ApiServiceAsync myService = getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSpotHolder(new SpotHolder());
        friendRequest.getSpotHolder().setId(spotId);
        friendRequest.setAuthToken(getAuthToken());
        myService.addToMyBiz(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Added to My Biz's");
                    MyWebApp.this.myBiz = null;
                    //mywebapp.toggleFavorites(followingMessages);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleGroup(final Long groupId) {
        //we need to be logged in
        AsyncCallback loginCallback = new AsyncCallback() {
            public void onFailure(Throwable throwable) {
                getMessagePanel().displayError(throwable.getMessage());
            }

            public void onSuccess(Object response) {
                //  toggleGroup(groupHolder);
                fetchGroup(groupId);
            }
        };
        if (isLoggedIn(loginCallback)) {
            loginCallback.onSuccess(null);
        }
    }

    private void fetchGroup(Long groupId) {
        final DataOperationDialog groupDialog = new DataOperationDialog(
                "Retrieving group.");
        groupDialog.show();
        groupDialog.center();
        ApiServiceAsync myService = getApiServiceAsync();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setAuthToken(getAuthToken());
        groupRequest.getGroupHolder().setId(groupId);
        myService.fetchGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                verifyDisplay();
                getMessagePanel().clear();
                groupDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                groupDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //GWT.log("showing GroupPanel ");
                    GroupPanel groupPanel = new GroupPanel(MyWebApp.this, mobileResponse.getGroupHolder(), mobileResponse.getSpotHolder());
                    swapCenter(groupPanel);
                } else {
                    verifyDisplay();
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
