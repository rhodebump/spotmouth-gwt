package com.spotmouth.gwt.client.spot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
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

    public boolean showBackToResults() {
        return false;
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

    CheckBox lodgingCheckBox = null;
    CheckBox diningCheckBox = null;
    CheckBox funCheckBox = null;
    CheckBox drinkingCheckBox = null;
    CheckBox shareImageCheckBox = null;

    protected void doSave() {
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setSpotHolder(spotHolder);
        spotHolder.setName(nameTextBox.getValue());
        spotHolder.setAddressLine1(address1TextBox.getValue());
        spotHolder.setCity(citySuggestBox.getValue());
        spotHolder.setState(stateTextBox.getValue());
        spotHolder.setZip(zipcodeTextBox.getValue());
        spotHolder.setEmail(emailTextBox.getValue());
        spotHolder.setWebsite(websiteTextBox.getValue());
        spotHolder.setHours(hoursTextArea.getValue());
        spotHolder.setDescription(contentTextArea.getValue());
        spotHolder.setLodging(lodgingCheckBox.getValue());
        spotHolder.setDining(diningCheckBox.getValue());
        spotHolder.setFun(funCheckBox.getValue());
        spotHolder.setDrinking(drinkingCheckBox.getValue());
        spotHolder.setShareImage(shareImageCheckBox.getValue());
        spotHolder.setVoicephone(voiceTextBox.getValue());
        spotHolder.setContentsToRemove(contentsToRemove);
        spotHolder.setFactualId(factualIdTextBox.getValue());
        if (!isEmpty(woeIdTextBox)) {
            int woeid = Integer.parseInt(woeIdTextBox.getValue());
            spotHolder.setWoeid(woeid);
        }

        spotHolder.setYelpId(yelpIdTextBox.getValue());




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

    public PlaceForm(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        this.addStyleName("PlaceForm");
        nameTextBox = addTextBox("Name", "name", spotHolder.getName());
        address1TextBox = addTextBox("Address Line #1", "addressLine1",
                spotHolder.getAddressLine1());
        citySuggestBox = addCity(spotHolder.getCity(),this);
        //stateTextBox = addTextBox("State", "state", spotHolder.getState());
        stateTextBox = addState(spotHolder.getState(),this);
        zipcodeTextBox = addTextBox("Zip", "zip", spotHolder.getZip());
        emailTextBox = addTextBox("Email", "email", "");
        Label label2 = new Label();
        label2.setText("Email address can be saved, but will never be displayed.  This is to protect email addresses from being spammed.");
        add(label2);
        websiteTextBox = addTextBox("Website", "website", spotHolder
                .getWebsite());


        voiceTextBox= addTextBox("Voice Phone", "voice", spotHolder.getVoicephone());
        addTagHolderForm(spotHolder.getTagHolders());
        lodgingCheckBox = addCheckbox2("Lodging", "lodging", spotHolder.getLodging(), mywebapp.getResources().lodging());
        diningCheckBox = addCheckbox2("Dining", "dining", spotHolder.getDining(), mywebapp.getResources().dining());
        funCheckBox = addCheckbox2("Fun", "fun", spotHolder.getFun(), mywebapp.getResources().fun());
        drinkingCheckBox = addCheckbox2("Drinking", "drinking", spotHolder.getDrinking(), mywebapp.getResources().drinking());
        shareImageCheckBox = addCheckbox2("Share Image", "shareImage", spotHolder.getShareImage(), null);
        contentTextArea = addTextArea("Description", "description", spotHolder.getDescription(), false);
        hoursTextArea = addTextArea("Hours", "hours", spotHolder.getHours(),
                false);



        addTagHolderForm(spotHolder.getTagHolders());


        factualIdTextBox= addTextBox("Factual ID", "facId", spotHolder.getFactualId());
        woeIdTextBox = addTextBox("WOEID", "woeid", new Integer(spotHolder.getWoeid()).toString());
        yelpIdTextBox = addTextBox("Yelp ID", "yelpId", spotHolder.getYelpId());

//        addMediaFields("Upload images OR video for spot");
//        add(contentsPanel);
//        addContentHolder(spotHolder.getContentHolder(), true, true);
        add(saveButton());


        add(syncFactual());

        add(cancelButton());

    }


    protected TextBox woeIdTextBox = null;
    protected TextBox yelpIdTextBox = null;


    private void initForm(SpotHolder spotHolder) {
        nameTextBox.setValue(spotHolder.getName());
        address1TextBox.setValue(spotHolder.getAddressLine1());
        citySuggestBox.setValue(spotHolder.getCity());
        stateTextBox.setValue(spotHolder.getState());

        zipcodeTextBox.setValue(spotHolder.getZip());

        websiteTextBox.setValue( spotHolder
                .getWebsite());


        voiceTextBox.setValue(spotHolder.getVoicephone());
        //addTagHolderForm(spotHolder.getTagHolders());
        lodgingCheckBox.setValue( spotHolder.getLodging());
        diningCheckBox.setValue(spotHolder.getDining());

        funCheckBox.setValue(spotHolder.getFun());
        drinkingCheckBox.setValue( spotHolder.getDrinking());
        shareImageCheckBox .setValue(spotHolder.getShareImage());
        contentTextArea.setValue(spotHolder.getDescription());
        hoursTextArea.setValue(spotHolder.getHours());


        factualIdTextBox.setValue(spotHolder.getFactualId());

        woeIdTextBox.setValue( new Integer(spotHolder.getWoeid()).toString());

        yelpIdTextBox.setValue(spotHolder.getYelpId());





    }

    TextArea hoursTextArea = new TextArea();



    public void toggleFirst() {
        nameTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;
    }
}
