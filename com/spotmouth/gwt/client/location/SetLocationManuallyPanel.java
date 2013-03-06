package com.spotmouth.gwt.client.location;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.GeocodeRequest;
import com.spotmouth.gwt.client.dto.Location;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SetLocationManuallyPanel extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.locationManualMobile();
        } else {
            return MyWebApp.resources.locationmanual();
        }
    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getSetLocation();
    }

    public static String WHERE_ARE_YOU = "Where are you?  We need to know your location before we can show you stuff.";
    ClickHandler selectLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Label) {
                Label b = (Label) sender;
                Location location = locationMap.get(b);
                if (location != null) {
                    mywebapp.setCurrentLocation(location);
                    mywebapp.toggleHome(mywebapp.locationUpdateCallback);
                }
            }
        }
    };
    ClickHandler hidePreviousLocationsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            com.google.gwt.user.client.Window.scrollTo(0, 0);
            previousLocationsPanel.clear();
            previousLocationsPanel.add(showPreviousLocationsButton());
        }
    };
    ClickHandler displayPreviousLocationsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            com.google.gwt.user.client.Window.scrollTo(0, 0);
            CaptionPanel captionPanel = getPreviousLocationsPanel();
            previousLocationsPanel.clear();
            previousLocationsPanel.add(hidePreviousLocationsButton());
            previousLocationsPanel.add(captionPanel);
        }
    };
    private VerticalPanel previousLocationsPanel = new VerticalPanel();
    private VerticalPanel addressFormPanel = new VerticalPanel();

    private CaptionPanel getPreviousLocationsPanel() {
        Map<String, Location> locationMap1 = mywebapp.getLocationsFromLocalStorage();
        Collection<Location> locations = locationMap1.values();
        //List<Location> locations = mywebapp.getLocationsFromLocalStorage();
        CaptionPanel captionPanel = new CaptionPanel("Previous Locations");
        if (!locations.isEmpty()) {
            add(captionPanel);
        }
        ULPanel ul = new ULPanel();
        ul.setStyleName("previouslocations");
        captionPanel.add(ul);
        for (Location location : locations) {
            // while (iterator.hasNext()) {
            FlowPanel hp = new FlowPanel();
            ListItem li = new ListItem();
            li.setStyleName("clearing");
            li.add(hp);
            ul.add(li);
            Label locationLabel = new Label(location.getFullAddress());
            locationLabel.addClickHandler(selectLocationHandler);
            locationMap.put(locationLabel, location);
            locationLabel.setTitle("Use this location");
            locationLabel.addStyleName("linky");
            ImageResource deleteImageIR = MyWebApp.resources.deleteX();
            Image deleteImage = new Image(deleteImageIR);
            deleteImage.addClickHandler(removeLocationHandler);
            deleteImage.setStyleName("linky");
            deleteImage.setTitle("Remove Location");
            locationMap.put(deleteImage, location);
            hp.add(locationLabel);
            hp.add(deleteImage);
        }
        return captionPanel;
    }

    ClickHandler resizeMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mapWidget.checkResize();
        }
    };
    ClickHandler removeLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("selected location");
            // get the location
            Object sender = event.getSource();
            Widget b = (Widget) sender;
            Location location = locationMap.get(b);
            if (location != null) {
                //Map<String,Location> mylocations = mywebapp.getLocationsFromLocalStorage();
                GWT.log("removing key " + location.getKey() + " from map");
                Storage localStorage = Storage.getLocalStorageIfSupported();
                localStorage.removeItem(location.getKey());
                //mylocations.remove(location.getKey());
                //mywebapp.saveLocationKeys(mylocations);
                mywebapp.saveLocationToLocalStorage();
                //rebuild the location dropdown
                mywebapp.rebuildLocationListBox();
                //mywebapp.toggleSetLocationManually();
                getMessagePanel().displayMessage("Removed " + location.getFullAddress());
                //this will rebuild the list of locations that should be displayed
                displayPreviousLocationsHandler.onClick(null);
            }
        }
    };


    public String getTitle() {
        return "Set Location";
    }


    Map<Widget, Location> locationMap = new HashMap<Widget, Location>();

    public SetLocationManuallyPanel() {
    }

    private ULPanel getPreviousLocations() {
        ULPanel ulPanel = new ULPanel();
        ulPanel.setStyleName("sl_previous_list");
        Map<String, Location> locationMap1 = mywebapp.getLocationsFromLocalStorage();
        Collection<Location> locations = locationMap1.values();
        for (Location location : locations) {
            ListItem li = new ListItem();
            ulPanel.add(li);
            InlineLabel address = new InlineLabel(location.getFullAddress());
            li.add(address);
            address.addClickHandler(selectLocationHandler);
            locationMap.put(address, location);
            InlineLabel removeLocation = new InlineLabel("x");
            removeLocation.addClickHandler(removeLocationHandler);
            removeLocation.setStyleName("kill_loc");
            removeLocation.setTitle("Remove Location");
            locationMap.put(removeLocation, location);
            li.add(removeLocation);
        }
        return ulPanel;
    }

    public SetLocationManuallyPanel(final MyWebApp mywebapp) {
        super(mywebapp, false);
        if (MyWebApp.isDesktop()) {
            final ULPanel previousLocations = getPreviousLocations();
            countryTextBox = getCountrySuggestBox("");
            stateTextBox = getStateSuggestBox("");
            citySuggestBox = getCitySuggestBox("");
            initZipCodeTextBox();
            initAddress1TextBox();
            final Button updateButton = new Button();
            //updateButton.setStyleName("btn_blue");
            updateButton.addClickHandler(setLocationHandler);
            final Button fromDeviceButton = new Button();
            fromDeviceButton.addClickHandler(setLocationFromDeviceHandler);
            //hold the map Panel
            final SimplePanel mapPanel = new SimplePanel();
            mapPanel.setWidth("100%");
            final SimpleRadioButton mapRadioButton = new SimpleRadioButton("sl_switch");
            //   mapRadioButton.setStyleName("sl_switch");
            mapRadioButton.getElement().setId("sl_use_map");
            mapRadioButton.addClickHandler(resizeMapHandler);
            if (Maps.isLoaded()) {
                geocoder = new Geocoder();
                initMap(mywebapp.getCurrentLocation(), mapPanel);
                SetLocationComposite slc = new SetLocationComposite(previousLocations, countryTextBox, stateTextBox, citySuggestBox, zipcodeTextField, address1TextField,
                        updateButton, fromDeviceButton, mapPanel, mapRadioButton);
                add(slc);
            } else {
                Maps.loadMapsApi(mywebapp.getGoogleMapKey(), "2", false, new Runnable() {
                    public void run() {
                        geocoder = new Geocoder();
                        initMap(mywebapp.getCurrentLocation(), mapPanel);
                        SetLocationComposite slc = new SetLocationComposite(previousLocations, countryTextBox, stateTextBox, citySuggestBox, zipcodeTextField, address1TextField,
                                updateButton, fromDeviceButton, mapPanel, mapRadioButton);
                        add(slc);
                    }
                });
            }
        }
    }

    Label hidePreviousLocationsButton() {
        Label btn = new Label("Hide Previous Locations");
        btn.addClickHandler(hidePreviousLocationsHandler);
        addImageToButton(btn, MyWebApp.resources.mapButton(), MyWebApp.resources.mapButtonMobile());
        fixButton(btn);
        return btn;
    }

    Label showPreviousLocationsButton() {
        Label btn = new Label("Show Previous Locations");
        addImageToButton(btn, MyWebApp.resources.mapButton(), MyWebApp.resources.mapButtonMobile());
        btn.addClickHandler(displayPreviousLocationsHandler);
        fixButton(btn);
        return btn;
    }


    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    ClickHandler setLocationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.getMessagePanel().clear();
            if (!isEmpty(zipcodeTextField)) {
                if (!isNumeric(zipcodeTextField.getValue())) {
                    mywebapp.getMessagePanel().displayError("Zipcode must be numeric");
                    return;
                }
            }
            mywebapp.setAutoGps(false);
            GeocodeRequest geocodeRequest = new GeocodeRequest();
            Location location = new Location();
            geocodeRequest.setLocation(location);
            location.setAddress1(address1TextField.getValue());
            location.setCity(citySuggestBox.getValue());
            location.setState(stateTextBox.getValue());
            //need to be number only for zipcode
            location.setZipcode(zipcodeTextField.getValue());
            ApiServiceAsync myService = mywebapp.getApiServiceAsync();
            myService.geocode(geocodeRequest, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    getMessagePanel().displayError("Could not set location", caught);
                }

                public void onSuccess(Object result) {
                    MobileResponse mobileResponse = (MobileResponse) result;
                    if (mobileResponse.getStatus() == 1) {
                        //we need to turn off the auto gps since we have set this
                        mywebapp.setCurrentLocation(mobileResponse
                                .getGeocodedLocation());
                        mywebapp.toggleHome(mywebapp.locationUpdateCallback);
                        mywebapp.getMessagePanel().clear();
                    } else {
                        getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                    }
                }
            });
        }
    };

    public void toggleFirst() {
    }




}
