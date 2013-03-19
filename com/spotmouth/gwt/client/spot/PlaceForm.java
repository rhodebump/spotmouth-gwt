package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import  com.spotmouth.gwt.client.common.TextField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

public class PlaceForm extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Place";
    }
    public String getPageTitle() {
        return getTitle();
    }


    ClickHandler syncFactualHandler = new ClickHandler() {
        public void onClick(ClickEvent clickEvent) {
            SearchParameters searchParameters = new SearchParameters();
            searchParameters.setFactualId(spotHolder.getFactualId());


            ApiServiceAsync myService = mywebapp.getApiServiceAsync();

            myService.searchFactual(searchParameters, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    getMessagePanel().displayError(caught.getMessage());
                }

                public void onSuccess(Object result) {
                    MobileResponse mobileResponse = (MobileResponse) result;
                    if (mobileResponse.getStatus() == 1) {
                        LocationResult locationResult = new LocationResult();

                        Location location = mobileResponse.getLocations().get(0);
                        locationResult.setLocation(location);
                        MyWebApp.convert(locationResult,spotHolder);
                        initForm(spotHolder);
                        getMessagePanel().displayMessage("Spot has been populated with factual information");

                    } else {
                        getMessagePanel().clear();
                        getMessagePanel().displayErrors(mobileResponse.getErrorMessages());

                    }
                }
            });




        }
    };


    Label syncFactual() {
        Label btn = new Label("Factual Sync");
        btn.addClickHandler(syncFactualHandler);
        fixButton(btn);
        return btn;
    }



    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getPlaceForm();
    }

    SimpleCheckBox lodgingCheckBox = new SimpleCheckBox();
    SimpleCheckBox diningCheckBox= new SimpleCheckBox();
    SimpleCheckBox funCheckBox = new SimpleCheckBox();
    SimpleCheckBox drinkingCheckBox = new SimpleCheckBox();
    CheckBox shareImageCheckBox = new CheckBox();

    protected void doSave() {
        GWT.log("doSave");
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setSpotHolder(spotHolder);
        spotHolder.setName(nameTextBox.getValue());
        spotHolder.setAddressLine1(address1TextField.getValue());
        spotHolder.setCity(citySuggestBox.getValue());
        spotHolder.setState(stateTextBox.getValue());
        spotHolder.setZip(zipcodeTextField.getValue());
        spotHolder.setEmail(emailTextField.getValue());
        spotHolder.setWebsite(websiteTextField.getValue());
        spotHolder.setHours(hoursTextArea.getValue());

        spotHolder.setDescription(contentTextArea.getValue());
        GWT.log("Set spotholder description to "  + spotHolder.getDescription());


        spotHolder.setLodging(lodgingCheckBox.getValue());
        spotHolder.setDining(diningCheckBox.getValue());
        spotHolder.setFun(funCheckBox.getValue());
        spotHolder.setDrinking(drinkingCheckBox.getValue());
        spotHolder.setShareImage(shareImageCheckBox.getValue());
        spotHolder.setVoicephone(voicePhoneTextField.getValue());
        spotHolder.setContentsToRemove(contentsToRemove);
        spotHolder.setFactualId(factualIdTextField.getValue());
        if (!isEmpty(woeIdTextField)) {
            int woeid = Integer.parseInt(woeIdTextField.getValue());
            spotHolder.setWoeid(woeid);
        }

        spotHolder.setYelpId(yelpIdTextField.getValue());




        saveSpotRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSpot(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //successfulSave();
                    //mywebapp.toggleSpotDetail();
                    //the spot is returned, so let's use that spot object to
                    //SpotHolder msh =mobileResponse.getSpotHolder();
                    ManageSpotPanel msp = new ManageSpotPanel( mywebapp,  mobileResponse.getSpotHolder());
                    mywebapp.swapCenter(msp);

                    mywebapp.getMessagePanel().displayMessage("Spot save was successful");
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private SpotHolder spotHolder = null;

    protected ClickHandler cancelHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.MANAGE_SPOT + spotHolder.getId());
        }
    };


    public PlaceForm(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        if (MyWebApp.isDesktop()) {


            Button saveButton = new Button();
            saveButton.addClickHandler(saveHandler);

            Button cancelButton = new Button();
            cancelButton.addClickHandler(cancelHandler2);
            SuggestBox tagSearchTextBox = getTagSuggestBox(spotHolder.getTagHolders());
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            factualIdTextField = new TextField();
            nameTextBox = new TextField();
            address1TextField = new TextField();
            countryTextBox = getCountrySuggestBox(spotHolder.getCountryCode());
            stateTextBox = getStateSuggestBox(spotHolder.getState());
            citySuggestBox = getCitySuggestBox(spotHolder.getCity());
            emailTextField = new TextField();
            initZipCodeTextBox();
            initForm(spotHolder);

            SpotFormComposite sfc = new SpotFormComposite(cancelButton, nameTextBox, voicePhoneTextField, websiteTextField,  emailTextField,
                                          contentTextArea, saveButton,
                                          lodgingCheckBox, funCheckBox, drinkingCheckBox, diningCheckBox,
                                          countryTextBox,  stateTextBox,  citySuggestBox,  zipcodeTextField,  address1TextField,
                                          tagSearchTextBox,
                                                                         selectedTagsPanel, suggestionsPanel,
                                                                         factualIdTextField, woeIdTextField, yelpIdTextField);
            add(sfc);

            return;
        }

        this.addStyleName("PlaceForm");
        nameTextBox = addTextBox("Name", "name", spotHolder.getName());
        address1TextField = addTextBox("Address Line #1", "addressLine1",
                spotHolder.getAddressLine1());
        citySuggestBox = addCity(spotHolder.getCity(),this);
        //stateTextBox = addTextBox("State", "state", spotHolder.getState());
        stateTextBox = addState(spotHolder.getState(),this);
        zipcodeTextField = addTextBox("Zip", "zip", spotHolder.getZip());
        emailTextField = addTextBox("Email", "email", "");
        Label label2 = new Label();
        label2.setText("Email address can be saved, but will never be displayed.  This is to protect email addresses from being spammed.");
        add(label2);
        websiteTextField = addTextBox("Website", "website", spotHolder
                .getWebsite());


        voicePhoneTextField= addTextBox("Voice Phone", "voice", spotHolder.getVoicephone());
        //addTagHolderForm(spotHolder.getTagHolders());
//        lodgingCheckBox = addCheckbox2("Lodging", "lodging", spotHolder.getLodging(), mywebapp.getResources().lodging());
//        diningCheckBox = addCheckbox2("Dining", "dining", spotHolder.getDining(), mywebapp.getResources().dining());
//        funCheckBox = addCheckbox2("Fun", "fun", spotHolder.getFun(), mywebapp.getResources().fun());
//        drinkingCheckBox = addCheckbox2("Drinking", "drinking", spotHolder.getDrinking(), mywebapp.getResources().drinking());
        shareImageCheckBox = addCheckbox2("Share Image", "shareImage", spotHolder.getShareImage(), null);
        contentTextArea = addTextArea("Description", "description", spotHolder.getDescription(), false);
        hoursTextArea = addTextArea("Hours", "hours", spotHolder.getHours(),
                false);



        //addTagHolderForm(spotHolder.getTagHolders());


        factualIdTextField= addTextBox("Factual ID", "facId", spotHolder.getFactualId());
        woeIdTextField = addTextBox("WOEID", "woeid", new Integer(spotHolder.getWoeid()).toString());
        yelpIdTextField = addTextBox("Yelp ID", "yelpId", spotHolder.getYelpId());

//        addMediaFields("Upload images OR video for spot");
//        add(contentsPanel);
//        addContentHolder(spotHolder.getContentHolder(), true, true);
        add(saveButton());


        add(syncFactual());

        add(cancelButton());

    }


    protected TextField woeIdTextField  = new TextField();
    protected TextField yelpIdTextField  = new TextField();


    private void initForm(SpotHolder spotHolder) {
        nameTextBox.setValue(spotHolder.getName());
        address1TextField.setValue(spotHolder.getAddressLine1());
        citySuggestBox.setValue(spotHolder.getCity());
        stateTextBox.setValue(spotHolder.getState());

        zipcodeTextField.setValue(spotHolder.getZip());

        websiteTextField.setValue( spotHolder
                .getWebsite());


        voicePhoneTextField.setValue(spotHolder.getVoicephone());
        //addTagHolderForm(spotHolder.getTagHolders());
        lodgingCheckBox.setValue( spotHolder.getLodging());
        diningCheckBox.setValue(spotHolder.getDining());

        funCheckBox.setValue(spotHolder.getFun());
        drinkingCheckBox.setValue( spotHolder.getDrinking());
        shareImageCheckBox .setValue(spotHolder.getShareImage());
        contentTextArea.setValue(spotHolder.getDescription());
        hoursTextArea.setValue(spotHolder.getHours());


        factualIdTextField.setValue(spotHolder.getFactualId());

        woeIdTextField.setValue(new Integer(spotHolder.getWoeid()).toString());

        yelpIdTextField.setValue(spotHolder.getYelpId());





    }

    TextArea hoursTextArea = new TextArea();



    public void toggleFirst() {
        nameTextBox.setFocus(true);
    }


}
