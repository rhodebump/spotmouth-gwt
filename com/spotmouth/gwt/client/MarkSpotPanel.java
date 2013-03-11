package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

import java.util.List;

//this is used for places and plates
public class MarkSpotPanel extends SpotBasePanel implements SpotMouthPanel {
    private ImageResource imageResource = MyWebApp.resources.place();

    public ImageResource getImageResource() {
        return imageResource;
    }



    public MarkSpotPanel() {
    }
    public TextBox plateNameTextBox = new TextBox();
    public boolean isRootPanel() {
        return false;
    }

    public String getTitle() {
        return "Mark Address";
    }


    public MarkSpotPanel(MyWebApp mywebapp) {
        super(mywebapp, false);
            pickSpotPanel.addStyleName("ResultsPanel");
            pickSpotPanel.setWidth("100%");
    }

    protected boolean licensePlate = false;

    public void activate(boolean licensePlate) {
        clear();
        this.licensePlate = licensePlate;
        doFetchSpots();
        add(pickSpotPanel);
    }

    private DataOperationDialog fetchLocalSpotsDialog = null;

    public void doFetchSpots() {
        GWT.log("doFetchSpots");
        fetchLocalSpotsDialog = new DataOperationDialog("Fetching spots.");
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLatitude(mywebapp.getCurrentLocation().getLatitude());
        searchParameters.setLongitude(mywebapp.getCurrentLocation().getLongitude());

        searchParameters.setLicensePlate(licensePlate);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.spotsbylocation(searchParameters, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                fetchLocalSpotsDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                fetchLocalSpotsDialog.hide();
                if (mobileResponse.getStatus() == 1) {
                    displayLocationResults(mobileResponse);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected ClickHandler getNewSpotHandler() {
        ClickHandler newSpotHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                setupNewForm();
            }
        };
        return newSpotHandler;
    }

    protected void setupNewForm() {
        GWT.log("setupNewForm");
        GWT.log("creating a new spot");
        SpotHolder spotHolder = new SpotHolder();
        //TOD0, Set info based upon current location
        if (mywebapp.getCurrentLocation().getGeocoded()) {
            spotHolder.setAddressLine1(mywebapp.getCurrentLocation().getAddress1());
            spotHolder.setCity(mywebapp.getCurrentLocation().getCity());
            spotHolder.setState(mywebapp.getCurrentLocation().getState());
            spotHolder.setZip(mywebapp.getCurrentLocation().getZipcode());
        }
        spotHolder.setLockedForEdit(false);
        if (licensePlate) {
            spotHolder.setSpotType(2);
            //let's pass the search stuff we specified
            spotHolder.setName(plateNameTextBox.getValue());
            spotHolder.setState(stateTextBox.getValue());
            {
                String val = getValue(colorsListBox);
                spotHolder.setColor(val);
            }
            {
                String val = getValue(manufacturersListBox);
                if (val != null) {
                    Long id = new Long(val);
                    // searchParameters.setManufacturerId(id);
                    spotHolder.getManufacturerHolder().setId(id);
                }
            }
            {
                String val = getValue(vehicleTypeListBox);
                spotHolder.setVehicleType(val);
            }


        } else {
            spotHolder.setSpotType(1);
        }
        GWT.log("setting getSpotType=" + spotHolder.getSpotType());
        ItemHolder itemHolder = new ItemHolder();
        itemHolder.setSpotHolder(spotHolder);
        LeaveMarkForm leaveMarkForm = new LeaveMarkForm(mywebapp, null, false, itemHolder);
        mywebapp.swapCenter(leaveMarkForm);
    }

    //display spots to pick from, these are spots that are not license plates
    protected void displayLocationResults(MobileResponse mobileResponse) {
        clear();
        pickSpotPanel.clear();
        add(pickSpotPanel);
        try {
            mywebapp.getMessagePanel().displayMessage("Please select the spot that you wish to mark");
            ULPanel ul = new ULPanel();
            ul.setStyleName("results");
            pickSpotPanel.add(ul);
            addSpotNotHere(ul);
            GWT.log("there are xx " + mobileResponse.getLocationResults().size() + " results ");
            for (LocationResult locationResult : mobileResponse.getLocationResults()) {
                GWT.log("locationResults count "
                        + mobileResponse.getLocationResults().size());
                if (locationResult.getLocation() != null) {
                    GWT.log("it is location");
                    addLocation(ul, locationResult,pickLocationHandler,false);
                } else if (locationResult.getSolrDocument() != null) {
                    GWT.log("it is result");
                    addResult2(ul, locationResult );
                } else {
                    GWT.log("not a location or result");
                }
            }
            // we need to add a link to create a new empty spot, in case we
            // don't have
            // the spot
            addSpotNotHere(ul);
        } catch (Exception e) {
            GWT.log("error in displaySpots", e);
            mywebapp.printStackTrace2(e);
        }
    }

    //mobileResponse
    //.getSpotsByLocationQueryResponse()
    //called to display results for picking a spot/plate
    protected void displayLicensePlates(List<LocationResult> locationResultList, int mode) {
        clear();
        pickSpotPanel.clear();
        add(pickSpotPanel);
        try {
            getMessagePanel().displayMessage("Please select the spot that you wish to mark");
            ULPanel ul = new ULPanel();
            ul.setStyleName("results");
            pickSpotPanel.add(ul);
            addSpotNotHere(ul);
            for (LocationResult locationResult : locationResultList) {
                if (locationResult.getLocation() != null) {
                    addLocation(ul, locationResult,pickLocationHandler,false);
                } else if (locationResult.getSolrDocument() != null) {
                    GWT.log("it is result");
                    addResult2(ul, locationResult );
                }
            }
            //we need to add a link to create a new empty spot, in case we don't have
            //the spot
            addSpotNotHere(ul);
        } catch (Exception e) {
            GWT.log("error in displaySpots", e);
            mywebapp.printStackTrace2(e);
        }
    }

    public void addSpotNotHere(ULPanel ulPanel) {
        ListItem linewspot = new ListItem();
        linewspot.addStyleName("spotnothere");
        ulPanel.add(linewspot);
        Label lbl = new Label("My Spot is not listed here.");
        lbl.addClickHandler(getNewSpotHandler());
        lbl.setStyleName("linky");
        linewspot.add(lbl);
    }

    VerticalPanel pickSpotPanel = new VerticalPanel();

    public void toggleFirst() {
    }


}
