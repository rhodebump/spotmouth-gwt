package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SaveSpotRequest;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/24/13
 * Time: 8:11 PM
 * This is a form that is an abbreviated form for creating a spot, once spot created, we direct to leave mark form.
 */
public class CreateSpotMarkPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Mark Spot";
    }

    protected void doSave() {
        GWT.log("doSave");
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setSpotHolder(spotHolder);
        spotHolder.setName(nameTextBox.getValue());
        spotHolder.setAddressLine1(address1TextField.getValue());
        spotHolder.setCity(citySuggestBox.getValue());
        spotHolder.setState(stateTextBox.getValue());
        spotHolder.setZip(zipcodeTextField.getValue());
        spotHolder.setSpotType(1);
        GWT.log("doSave 1");
        saveSpotRequest.setAuthToken(mywebapp.getAuthToken());
        GWT.log("doSave 2");
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSpot(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
               // postDialog.hide();
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                //postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    GWT.log("success, go to MyWebApp.LEAVE_SPOT_MARK now");
                    SpotHolder spotHolder1 = mobileResponse.getSpotHolder();
                    History.newItem(MyWebApp.LEAVE_SPOT_MARK + spotHolder1.getId());
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected ClickHandler cancelHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("cancelHandler2 onClick");
            History.newItem(MyWebApp.MARK_SPOT);
        }
    };
    private SpotHolder spotHolder = null;

    public CreateSpotMarkPanel(MyWebApp mywebapp) {
        super(mywebapp);
        Button saveButton = new Button();
        saveButton.addClickHandler(saveHandler);
        Button cancelButton = new Button();
        cancelButton.addClickHandler(cancelHandler2);
        this.spotHolder = new SpotHolder();
        // factualIdTextField = new TextField();
        nameTextBox = new TextField();
        address1TextField = new TextField();
        countryTextBox = getCountrySuggestBox(spotHolder.getCountryCode());
        stateTextBox = getStateSuggestBox(spotHolder.getState());
        citySuggestBox = getCitySuggestBox(spotHolder.getCity());
        initZipCodeTextBox(spotHolder.getZip());
        CreateSpotMarkComposite createSpotMarkComposite = new CreateSpotMarkComposite(nameTextBox, countryTextBox, stateTextBox, citySuggestBox, zipcodeTextField, address1TextField, saveButton, cancelButton);
        formPanel.setWidget(createSpotMarkComposite);
        add(formPanel);
    }

//    public ClickHandler saveHandler2 = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//          GWT.log("saveHandler.onClick");
//            doSave();
//        }
//    };


    public void toggleFirst() {
    }
}
