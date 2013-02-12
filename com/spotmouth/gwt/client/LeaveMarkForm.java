package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.mark.MarkComposite;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

public class LeaveMarkForm extends SpotBasePanel implements SpotMouthPanel {
    public LeaveMarkForm() {}
    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getLeaveMarkForm();
    }
    public Button leaveMarkButton = new Button();


    public ImageResource getImageResource() {
        SpotHolder spotHolder = itemHolder.getSpotHolder();
        if (spotHolder != null) {
            if (spotHolder.getLicensePlate()) {
                return MyWebApp.resources.plate();
            } else if (spotHolder.isPlace()) {
                return MyWebApp.resources.place();
            }
        } else if (!locationOnly) {
            return MyWebApp.resources.place();
        } else {
            return MyWebApp.resources.location();
        }
        return MyWebApp.resources.markspot();
    }

    public String getTitle() {
        return "Leave Mark";
    }

    private boolean isSpotReadOnly() {
        //no spotholder at all
        if (itemHolder.getSpotHolder() == null) {
            return false;
            //this is a already saved spotholder
        } else if (itemHolder.getSpotHolder().getId() != null) {
            return true;
        } else if (itemHolder.getSpotHolder().getFactualId() != null) {
            return true;
        }
        return false;
    }


    private boolean spotreadonly = true;
    private Location location = null;
    private boolean locationOnly = true;
    private ItemHolder itemHolder = null;

    public LeaveMarkForm(MyWebApp mywebapp, Location location, boolean locationOnly, ItemHolder itemHolder) {
        super(mywebapp);
        this.itemHolder = itemHolder;
        this.spotreadonly = isSpotReadOnly();
        SpotHolder spotHolder = itemHolder.getSpotHolder();
        this.locationOnly = locationOnly;
        this.location = location;
        this.addStyleName("LeaveMarkForm");



        if (MyWebApp.isDesktop()) {

            MultiUploader multiUploader = new MultiUploader();
            //FlowPanel panelImages = new FlowPanel();
            IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages);
            multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

            MarkData markData = new MarkData();

            markData.expandData = new ExpandData();
            markData.spotHolder = spotHolder;



            markData.saySomethingTextArea = contentTextArea;
            markData.formPanel = formPanel;
            widgetMarkDataMap.put(leaveMarkButton,markData);


            leaveMarkButton.addClickHandler(saveHandler2);
            //MarkComposite markComposite = new MarkComposite(leaveMarkButton, advancedMarkData.saySomethingTextArea, multiUploader, panelImages, mywebapp);
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            Button addTagButton = new Button("Add");
            //addTagAnchor.getElement().setId("addTag");
            addTagButton.addClickHandler(addTagHandler);

            markData.tagSearchTextBox = tagSearchTextBox;

            //SimpleCheckBox shareOnFacebookCheckbox = new SimpleCheckBox();
            //markData.shareOnFacebookCheckbox = shareOnFacebookCheckbox;


            Button shareOnFacebookButton =   getFacebookButton(markData);



            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);


            MarkComposite markComposite = new MarkComposite(leaveMarkButton,contentTextArea,multiUploader,panelImages,mywebapp,markData.tagSearchTextBox,selectedTagsPanel,
                    addTagButton,markData.secretKeyTextBox,shareOnFacebookButton,suggestionsPanel);
            markComposite.setLocationName(spotHolder.getName());
            markComposite.setFullAddress(spotHolder.getAddressLabel());
            markComposite.setPhoneNumber(spotHolder.getVoicephone());

            FormPanel myFormPanel = new FormPanel();
            setupRootPanelForm(myFormPanel, markData);
            //add(markComposite);

            myFormPanel.setWidget(markComposite);
            add(myFormPanel);

            return;

            //leaveMarkButton.addClickHandler(saveHandler);
        }
        if (spotHolder.getLicensePlate()) {
            doPlate(spotHolder, spotreadonly);
        } else if (spotHolder.isPlace()) {
            doPlace(spotHolder, spotreadonly);
        } else if (location != null && !locationOnly) {
            String address = location.getAddress1() + ", " + location.getCity() + " " + location.getState() + " " + location.getZipcode();
            Label addressLabel = new Label(address);
            addFieldset(addressLabel, location.getName(), "displayName");
        }
        if (itemHolder.getParentItemHolder() != null) {
            ItemHolder parentItemHolder = itemHolder.getParentItemHolder();
            HTML textData = new HTML(parentItemHolder.getTextData());
            addFieldset(textData, "Reply to", "na1");
            addContentHolder(parentItemHolder.getContentHolder(), false, false);



        }
            contentTextArea = addTextArea("Your Mark", "textData", "", false);
        //addMediaFields("Upload images OR videos for mark");


        addMediaFields("Upload images OR video for spot");
        add(contentsPanel);
        addContentHolder(itemHolder.getContentHolder(), true, true);



        add(leaveMarkButton());
        if (mywebapp.isFacebookUser()) {
           // addCheckbox(shareOnFacebookCheckbox, null,this);
        }
        secretKeyTextBox = addTextBox("Secret Key", "secretKey", "", false);
        addTagHolderForm(itemHolder.getTagHolders());
        add(leaveMarkButton());
        add(cancelButton());
    }


    protected void doSave() {
        GWT.log("doSave");
        //if we are here, the big files were posted, now we do the actual mark rpc
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        leaveMarkRequest.setAuthToken(mywebapp.getAuthToken());
        leaveMarkRequest.setItemHolder(itemHolder);
        itemHolder.setLocationLabel(mywebapp.getCurrentLocation().getFullAddress());

        itemHolder.setTextData(contentTextArea.getValue());
       // itemHolder.setShareOnFacebook(shareOnFacebookCheckbox.getValue());
        itemHolder.setPassword(secretKeyTextBox.getValue());
        leaveMarkRequest.setAuthToken(mywebapp.getAuthToken());
        leaveMarkRequest.setMobileDevice(mywebapp.isMobileDevice());
        if (locationOnly) {
            GWT.log("doSave locationOnly is true");
            //just marking a location
            //leaveMarkRequest.setSpotHolder(null);
            leaveMarkRequest.setLocation(location);
            //leaveMarkRequest.set
            leaveMarkRequest.getItemHolder().setLocationMark(true);
        } else if (location != null) {
            GWT.log("doSave location is not null");
            //we want to create a new spot from the location
            //leaveMarkRequest.setSpotHolder(null);
            GWT.log("setting location of leavemarkrequest ");
            GWT.log("country code is " + location.getCountryCode());
            leaveMarkRequest.setLocation(location);
            //leaveMarkRequest.setFromGisgraphy(true);
        } else if (spotreadonly) {
            GWT.log("doSave spotreadonly is true");
            //SpotHolder sh = leaveMarkRequest.getSpotHolder();
            // sh.setId(spotHolder.getId());
        } else {
            GWT.log("doSave else");
            SpotHolder spotHolder = itemHolder.getSpotHolder();
            if (nameTextBox != null) {
                spotHolder.setName(nameTextBox.getValue());
            }
            //following stuff is for a plate
            if (address1TextBox != null) {
                spotHolder.setAddressLine1(address1TextBox.getValue());
            }
            if (citySuggestBox != null) {
                spotHolder.setCity(citySuggestBox.getValue());
            }
            if (stateTextBox != null) {
                spotHolder.setState(stateTextBox.getValue());
            }

            String color = getValue(colorsListBox);
            spotHolder.setColor(color);


            Long manufacturerId = getManufacturerId();
            spotHolder.getManufacturerHolder().setId(manufacturerId);

            String vehicleType = getValue(vehicleTypeListBox);
            spotHolder.setVehicleType(vehicleType);





            GWT.log("spotType=" + spotHolder.getSpotType());
        }
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.leavemark(leaveMarkRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                GWT.log("leavemark onFailure");
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                GWT.log("leavemark onSuccess");
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ItemDetailPanel idp = new ItemDetailPanel(mywebapp, mobileResponse);
                    mywebapp.swapCenter(idp);
                    mywebapp.getMessagePanel().displayMessage("Your mark has been saved");
                } else {

                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }



    protected boolean isValid() {
        mywebapp.log("isValid");
        SpotHolder spotHolder = itemHolder.getSpotHolder();
        if (spotHolder.getLicensePlate()) {
            checkRequired(nameTextBox, "Plate Number is required");
        } else {
            checkRequired(nameTextBox, "Name is required");
        }

        checkRequired(address1TextBox, "Address is required");
        checkRequired(citySuggestBox, "City is required");
        checkRequired(stateTextBox, "State is required");
        checkRequired(zipcodeTextBox, "Zipcode is required");
        checkRequired(contentTextArea, "To submit a mark, you need to say something.");
        boolean isvalid =  (!getMessagePanel().isHaveMessages());
        mywebapp.log("isvalid=" +isvalid);
        return (!getMessagePanel().isHaveMessages());
    }

    public void toggleFirst() {
        if (nameTextBox != null) {
            nameTextBox.setFocus(true);
        } else {
            contentTextArea.setFocus(true);
        }
    }

    public boolean isLoginRequired() {
        return false;
    }
}
