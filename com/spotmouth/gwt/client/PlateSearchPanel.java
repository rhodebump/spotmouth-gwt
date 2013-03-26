package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DropPanel;

import java.util.ArrayList;
import java.util.List;

//this is used for places and plates
public class PlateSearchPanel extends MarkSpotPanel implements SpotMouthPanel {
    private ImageResource imageResource = MyWebApp.resources.plate();

    public ImageResource getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return "Mark Plate";
    }

    public TextBox keywordsTextBox = new TextBox();

    //Button plateSearchButton = new Button();
    public PlateSearchPanel() {
    }

    public Button plateSearchButton = new Button("Submit");

    public PlateSearchPanel(MyWebApp mywebapp) {
        super(mywebapp);
        if (MyWebApp.isDesktop()) {
            plateSearchButton.addClickHandler(plateSearchHandler);
            this.licensePlate = true;
            initColorListBox("");
            initManufacturersListBox(null);
            stateTextBox = initState("");
            initVehicleType("");
            MarkData markData = new MarkData();
            markData.saySomethingTextArea = contentTextArea;
            markData.spotDescriptionTextArea = spotDescriptionTextArea;
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            markData.tagSearchTextBox = tagSearchTextBox;
            Button leaveMarkButton = new Button();
            leaveMarkButton.addClickHandler(saveHandler);
            MultiUploader multiUploader = new MultiUploader();
            this.defaultUploader =  multiUploader;
            //FlowPanel panelImages3 = new FlowPanel();
            multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler3);
            //IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages3);
            // multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            ULPanel pickSpotListBox = getPickSpotULPanel();
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
            Button shareOnFacebookButton = getFacebookButton(markData);
            Button addTagButton = new Button();
            //addTagAnchor.getElement().setId("addTag");
            addTagButton.addClickHandler(addTagHandler);
            DropPanel dropPanel = getDropPanel();
            PlateSearchComposite plateSearchComposite = new PlateSearchComposite(colorsListBox, plateNameTextBox, keywordsTextBox, manufacturersListBox, stateTextBox,
                    vehicleTypeListBox, plateSearchButton, markData.tagSearchTextBox, markData.secretKeyTextBox, markData.saySomethingTextArea,
                    selectedTagsPanel, leaveMarkButton, multiUploader, panelImages, pickSpotListBox, mywebapp, shareOnFacebookButton, addTagButton, suggestionsPanel, markData.spotDescriptionTextArea,dropPanel);
            plateSearchComposite.tab1.setValue(true);
            add(plateSearchComposite);
            return;
        }
//        this.licensePlate = true;
//        String helpText = "While you can always add a new plate, it's best for everyone to try to see if the plate you want to mark is already in spotmouth.  Try putting in keywords that may describe the vehicle (e.g. red ford blond driver";
//
//
//        addFieldset("Plate Search",helpText);
//        plateNameTextBox = addTextBox("Plate: (You can put in wild cards if you can't remember the whole plate, i.e. x1c*)", "plateNameTextBox", "");
//        keywordsTextBox = addTextBox("Keywords", "keywords", "");
//        //addState2("");
//        stateTextBox = addState("", this);
//        addColorListBox("");
//        addManufacturersListBox(null);
//        addVehicleType("");
//        add(plateSearchButton());
//        add(cancelButton());
    }

    protected Label plateSearchButton() {
        Label btn = new Label("Search");
        btn.addClickHandler(plateSearchHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    public void performPlateSearch() {
        getMessagePanel().clear();
        if (isEmpty(plateNameTextBox)) {
            getMessagePanel().displayError("Plate is required.");
        }
        if (isEmpty(stateTextBox.getTextBox())) {
            getMessagePanel().displayError("State is required.");
        }
        if (isEmpty(colorsListBox)) {
            getMessagePanel().displayError("Color is required.");
        } else {
           // getMessagePanel().displayError("Color is " + colorsListBox.getSelectedIndex());
        }
        if (isEmpty(manufacturersListBox)) {
            getMessagePanel().displayError("Manufacturer is required.");
        }
        if (isEmpty(vehicleTypeListBox)) {
            getMessagePanel().displayError("Vehicle Type is required.");
        }
        if (getMessagePanel().isHaveMessages()) {
            return;
        }
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setLicensePlate(true);
        searchParameters.setSpots(true);
        searchParameters.setPlateName(plateNameTextBox.getValue());
        searchParameters.setKeywords(keywordsTextBox.getValue());
        searchParameters.setState(stateTextBox.getValue());
        {
            String val = getValue(colorsListBox);
            searchParameters.setColor(val);
        }
        Long manufacturerId = getManufacturerId();
        searchParameters.setManufacturerId(manufacturerId);
        {
            String val = getValue(vehicleTypeListBox);
            searchParameters.setVehicleType(val);
        }
        searchParameters.setMax(20);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.search(searchParameters, new AsyncCallback() {
            DataOperationDialog fetchLocalSpotsDialog = new DataOperationDialog("Searching Plates.");

            public void onFailure(Throwable caught) {
                fetchLocalSpotsDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                fetchLocalSpotsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //if no plates were found, let's just go to the new plate form
                    if (mobileResponse.getLocationResults().isEmpty()) {
                        savePlateSearchAsSpot();
                    } else {
                        displayLicensePlates(mobileResponse.getLocationResults(), 1);
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void savePlateSearchAsSpot() {
        GWT.log("showSpot factual");
        SpotHolder spotHolder = new SpotHolder();
        spotHolder.setName(plateNameTextBox.getValue());
        spotHolder.setState(stateTextBox.getValue());
        String color = getValue(colorsListBox);
        spotHolder.setColor(color);
        Long manufacturerId = getManufacturerId();
        spotHolder.getManufacturerHolder().setId(manufacturerId);
        String vehicleType = getValue(vehicleTypeListBox);
        spotHolder.setVehicleType(vehicleType);
        spotHolder.setDescription(keywordsTextBox.getValue());
        spotHolder.setSpotType(2);
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setIgnoreUploads(true);
        saveSpotRequest.setSpotHolder(spotHolder);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSpot(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                // callback.onFailure(caught);
                getMessagePanel().displayError("Could not save plate:" + caught.toString());
            }

            public void onSuccess(Object result) {
                // callback.onSuccess(result);
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    SpotHolder spotHolder1 = mobileResponse.getSpotHolder();
                    String token = MyWebApp.LEAVE_SPOT_MARK + spotHolder1.getId();
                    History.newItem(token);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    ClickHandler plateSearchHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            performPlateSearch();
        }
    };

    @Override
    public void toggleFirst() {
        if (plateNameTextBox != null) {
            plateNameTextBox.setFocus(true);
        }
    }


}
