package com.spotmouth.gwt.client.contest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;
import com.kiouri.sliderbar.client.event.*;
import com.kiouri.sliderbar.client.solution.simplehorizontal.SliderBarSimpleHorizontal;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import org.vectomatic.dnd.DropPanel;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/11/12
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageContestPanel extends SpotBasePanel implements SpotMouthPanel {
    //https://github.com/laaglu/lib-gwt-file-test/blob/master/src/main/java/org/vectomatic/file/client/TestAppMain.java
    // Load the image in the document and in the case of success attach it to the viewer
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler2 = new IUploader.OnFinishUploaderHandler() {
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
            GWT.log("ManageContestPanel, showImage2");
            contestImagePanel.setWidget(image);
        }
    };



    public String getTitle() {
        return "Manage Contest";
    }

    protected void validateDateRange() {
        checkRequired(startDatePicker.getTextBox(), "Start Date is required");
        checkRequired(endDatePicker.getTextBox(), "End Date is required");
        if (contestHolder.getStartDate() == null) {
            return;
        } else if (contestHolder.getEndDate() == null) {
            return;
        }
        if (contestHolder.getStartDate().after(contestHolder.getEndDate())) {
            getMessagePanel().displayError("Start Date must come before End Date.");
        }
        //end date must be before current date
        Date currentDate = new Date();
        if (contestHolder.getEndDate().before(currentDate)) {
            getMessagePanel().displayError("The end date must not occur in the past.");
        }
    }

    protected boolean isValid() {
        checkRequired(nameTextBox, "Name is required");
        checkRequired(contentTextArea, "Description is required");
        populate();
        validateDateRange();
        checkRequired(countryTextBox, "Country is required");
        checkRequired(stateTextBox, "State is required");
        checkRequired(citySuggestBox, "City is required");
        checkRequired(zipcodeTextField, "Zip is required");
        if (contestHolder.getRadius() == null) {
            getMessagePanel().displayMessage("Please select a radius.");
        }
        // checkRequired(radiusTextBox, "Radius is required");
        //radius must be number
        //need to validate that radius is less than 100
        //only require tags if a place
        if (contestHolder.getAppliesTo() == 2) {
            if (contestHolder.getTagHolders().isEmpty()) {
                getMessagePanel().displayMessage("At least one tag is required");
            }
        }
        if (contestHolder.getAppliesTo() == 0) {
            getMessagePanel().displayMessage("Please select what this contest applies to.");
        }
        if (contestHolder.getNumberOfStars() == 0) {
            getMessagePanel().displayMessage("Please specify number of stars.");
        }
        return (!getMessagePanel().isHaveMessages());
    }

    public void toggleFirst() {
        nameTextBox.setFocus(true);
    }



    public ManageContestPanel() {
    }

    private CheckBox geoGlobalCheckbox = new CheckBox("GEO Global");
    private CheckBox shareOnFacebookCheckbox = new CheckBox("Share on facebook");
    protected TextBox numberOfStarsTextBox = new TextBox();
    protected TextBox iconStyleTextBox = new TextBox();
    //protected TextBox address1TextBox = null;
    private ContestHolder contestHolder = null;
    //protected TextBox zipcodeTextBox = null;
    private SimplePanel contestImagePanel = new SimplePanel();
    ListBox appliesToListBox = new ListBox();
    ValueChangeHandler vch = new ValueChangeHandler<String>() {
        public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
            GWT.log("on value change");
            updateUI();
        }
    };

    public void addedToDom() {
        updateUI();
    }

    private void updateUI() {
        GWT.log("updateUI");
        if (isEmpty(countryTextBox.getTextBox())) {
            hideElement(stateTextBox.getElement());
            countryTextBox.getTextBox().setValue("Country");
            countryTextBox.removeStyleName("mc_selected");
        } else {
            showElement(stateTextBox.getElement());
            countryTextBox.addStyleName("mc_selected");
        }
        if (isEmpty(stateTextBox.getTextBox())) {
            hideElement(citySuggestBox.getElement());
            stateTextBox.getTextBox().setValue("State/Province");
            stateTextBox.removeStyleName("mc_selected");
        } else {
            showElement(citySuggestBox.getElement());
            stateTextBox.addStyleName("mc_selected");
        }
        if (isEmpty(citySuggestBox.getTextBox())) {
            citySuggestBox.removeStyleName("mc_selected");
        } else {
            citySuggestBox.addStyleName("mc_selected");
        }
        if (isEmpty(address1TextField)) {
            address1TextField.removeStyleName("mc_selected");
        } else {
            address1TextField.addStyleName("mc_selected");
        }
        if (isEmpty(zipcodeTextField)) {
            zipcodeTextField.removeStyleName("mc_selected");
        } else {
            zipcodeTextField.addStyleName("mc_selected");
        }
        if (isEmpty(nameTextBox)) {
            nameTextBox.removeStyleName("mc_selected");
        } else {
            nameTextBox.addStyleName("mc_selected");
        }
        if (isEmpty(contentTextArea)) {
            contentTextArea.removeStyleName("mc_selected");
        } else {
            contentTextArea.addStyleName("mc_selected");
        }
        if (appliesToListBox.getSelectedIndex() == 0) {
            appliesToListBox.removeStyleName("mc_selected");
        } else {
            appliesToListBox.addStyleName("mc_selected");
        }
        com.google.gwt.user.client.Element mc_address = DOM.getElementById("mc_address");
        com.google.gwt.user.client.Element mc_radius = DOM.getElementById("mc_radius");
        if (mc_address != null) {
            //if all location fields, need to set mc_location to visible
            if (isNotEmpty(countryTextBox.getTextBox()) && isNotEmpty(stateTextBox.getTextBox()) && isNotEmpty(citySuggestBox.getTextBox())) {
                showElement(mc_address);
                showElement(mc_radius);
            } else {
                hideElement(mc_address);
                hideElement(mc_radius);
            }
        }
        //
    }



    private ContestFormComposite cfc = null;

    //TextBox contestName
    public ManageContestPanel(MyWebApp mywebapp, final ContestHolder contestHolder) {
        super(mywebapp, false, true, false);
        this.contestHolder = contestHolder;
        if (MyWebApp.isDesktop()) {
            nameTextBox = new TextField();
            nameTextBox.setTabIndex(1);
           // nameTextBox.getElement().setAttribute("placeholder", "Contest Name");
            nameTextBox.setValue(contestHolder.getName());
            nameTextBox.addValueChangeHandler(vch);
            //   		<textarea class="mc_desc"  onblur="inpValidate(this, this.value)" tabindex="4" rows="4"></textarea>
            contentTextArea = new TextArea();
            contentTextArea.setStyleName("mc_desc");
            contentTextArea.setVisibleLines(4);
            contentTextArea.setTabIndex(4);
            contentTextArea.setValue(contestHolder.getDescription());
            contentTextArea.addValueChangeHandler(vch);
            startDatePicker.setValue(contestHolder.getStartDate());
            endDatePicker.setValue(contestHolder.getEndDate());
            startDatePicker.setTabIndex(2);
            startDatePicker.getElement().setAttribute("placeholder", "Start Date");
            endDatePicker.setTabIndex(3);
            endDatePicker.getElement().setAttribute("placeholder", "End Date");
            countryTextBox = getCountrySuggestBox(contestHolder.getCountryCode());
            countryTextBox.getElement().setId("mc_sel_country");
            countryTextBox.setStyleName("mc_select");
            countryTextBox.setTabIndex(5);
            stateTextBox = getStateSuggestBox(contestHolder.getState());
            stateTextBox.getElement().setId("mc_sel_state");
            stateTextBox.setStyleName("mc_select");
            stateTextBox.setTabIndex(6);
            //only show stateTextBox if there is country set
            countryTextBox.addValueChangeHandler(vch);
            stateTextBox.addValueChangeHandler(vch);
            citySuggestBox = getCitySuggestBox(contestHolder.getCity());
            citySuggestBox.getElement().setId("mc_sel_city");
            citySuggestBox.setStyleName("mc_select");
            citySuggestBox.setTabIndex(7);
            citySuggestBox.addValueChangeHandler(vch);
            //		<input type="text" placeholder="Zip Code" maxlength="6" tabindex="9" onblur="inpValidate(this, this.value);mcShowRadius(this.value);"/>

            initZipCodeTextBox();


            zipcodeTextField.setTabIndex(9);

            zipcodeTextField.setValue(contestHolder.getZip());
            zipcodeTextField.addValueChangeHandler(vch);




            initAddress1TextBox();

            address1TextField.setTabIndex(8);
            address1TextField.addValueChangeHandler(vch);
            address1TextField.setValue(contestHolder.getAddressLine1());

            SuggestBox tagSearchTextBox = getTagSuggestBox(contestHolder.getTagHolders());
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            tagSearchTextBox.getElement().setId("mc_tags_inp");

            tagSearchTextBox.setTabIndex(11);


            appliesToListBox.addItem("This contest applies to...", "0");
            appliesToListBox.setStyleName("mc_select");
            appliesToListBox.addStyleName("mc_ap_sel");
            appliesToListBox.addItem("Users", "1");
            appliesToListBox.addItem("Places", "2");
            appliesToListBox.addItem("Plates", "3");
            appliesToListBox.addItem("Locations", "4");
            appliesToListBox.addItem("Events", "5");
            appliesToListBox.addItem("Coupons", "6");
            appliesToListBox.setSelectedIndex(contestHolder.getAppliesTo());
            appliesToListBox.addChangeHandler(new ChangeHandler() {
                public void onChange(ChangeEvent changeEvent) {
                    updateUI();
                }
            });
            Button saveButton = saveButton();
            SliderBarSimpleHorizontal sliderBarSimpleHorizontal =
                    new SliderBarSimpleHorizontal(30, "430px", true);
            sliderBarSimpleHorizontal
                    .addBarValueChangedHandler(new BarValueChangedHandler() {
                        public void onBarValueChanged(BarValueChangedEvent event) {
                            //  valueBox.setValue("" + event.getValue());
                            Double d = new Double(event.getValue());
                            contestHolder.setRadius(d);
                            cfc.setRadius(event.getValue());
                        }
                    });
            sliderBarSimpleHorizontal.setValue(contestHolder.getRadius().intValue());
            iconStyleTextBox.setValue(contestHolder.getIconStyle().toString());
            GWT.log("iconStyleTextBox " + contestHolder.getIconStyle().toString());
            numberOfStarsTextBox.setValue(contestHolder.getNumberOfStars().toString());
            GWT.log("numberOfStarsTextBox " + contestHolder.getNumberOfStars().toString());
            defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);
            //do we have an iamge
            Image mainImage = getImage(contestHolder.getContentHolder(), "320x320");
            //contestImagePanel
            if (mainImage != null) {
                contestImagePanel.setWidget(mainImage);
            }
            Button cancelButton  = new Button();
            cancelButton.addClickHandler(cancelHandler2);
            DropPanel dropPanel = getDropPanel();
            this.cfc = new ContestFormComposite(cancelButton,nameTextBox, contentTextArea, numberOfStarsTextBox, iconStyleTextBox, startDatePicker, endDatePicker,
                    countryTextBox, stateTextBox, citySuggestBox, zipcodeTextField, address1TextField, tagSearchTextBox, selectedTagsPanel, appliesToListBox, saveButton,
                    sliderBarSimpleHorizontal, defaultUploader, contestImagePanel, mywebapp, contestHolder,suggestionsPanel,dropPanel);
            add(cfc);
            return;
        }

    }

    protected ClickHandler cancelHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //go back to all contests
            History.newItem(MyWebApp.CONTESTS);
        }
    };

    private void populate() {

//
        try {
            int i = Integer.parseInt(iconStyleTextBox.getValue());
            contestHolder.setIconStyle(i);
        } catch (NumberFormatException nfe) {
        }
        try {
            int i = Integer.parseInt(numberOfStarsTextBox.getValue());
            contestHolder.setNumberOfStars(i);
        } catch (NumberFormatException nfe) {
        }
        contestHolder.setName(nameTextBox.getValue());
        contestHolder.setDescription(contentTextArea.getValue());
        contestHolder.setStartDate(startDatePicker.getValue());
        contestHolder.setEndDate(endDatePicker.getValue());

        String appliesTo = appliesToListBox.getValue(appliesToListBox.getSelectedIndex());
        GWT.log("appliesTo:" + appliesTo);
        try {
            int i = Integer.parseInt(appliesTo);
            contestHolder.setAppliesTo(i);
        } catch (NumberFormatException nfe) {
        }
    }

    protected void doSave() {
        ContestRequest contestRequest = new ContestRequest();
        populate();
        contestRequest.setContestHolder(contestHolder);
        contestHolder.setAddressLine1(address1TextField.getValue());
        contestHolder.setCity(citySuggestBox.getValue());
        contestHolder.setState(stateTextBox.getValue());
        contestHolder.setZip(zipcodeTextField.getValue());
        contestHolder.setCountryCode(countryTextBox.getText());
        contestHolder.setShareOnFacebook(shareOnFacebookCheckbox.getValue());
        contestHolder.setGeoGlobal(geoGlobalCheckbox.getValue());
        //  String val = radiusListBox.getItemText(radiusListBox.getSelectedIndex());
        //    contestHolder.setRadius(new Double(val));
        contestHolder.setContentsToRemove(contentsToRemove);
        contestRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveContest(contestRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //i would have liked to go to the viewcontestpanel,
                    //this takes a solrDocument
                    //I will need to just go to the contests panel
                    com.spotmouth.gwt.client.dto.SolrDocument solrDocument = mobileResponse.getContestQueryResponse().getResults().get(0);
                    Long contestId = solrDocument.getFirstLong("contestid_l");
                    ViewContestPanel viewContestPanel = new ViewContestPanel(mywebapp, solrDocument);
                    mywebapp.swapCenter(viewContestPanel);
                    History.newItem(MyWebApp.CONTEST_DETAIL + contestId, false);
                    getMessagePanel().displayMessage("Contest saved");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
