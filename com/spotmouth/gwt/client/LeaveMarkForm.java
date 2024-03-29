package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.mark.MarkComposite;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DropPanel;

public class LeaveMarkForm extends SpotBasePanel implements SpotMouthPanel {





    public LeaveMarkForm() {}

    public Button leaveMarkButton = new Button();


//    public ImageResource getImageResource() {
//        SpotHolder spotHolder = itemHolder.getSpotHolder();
//        if (spotHolder != null) {
//            if (spotHolder.getLicensePlate()) {
//                return MyWebApp.resources.plate();
//            } else if (spotHolder.isPlace()) {
//                return MyWebApp.resources.place();
//            }
//        } else {
//            return MyWebApp.resources.location();
//        }
//        return MyWebApp.resources.markspot();
//    }

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
    //private boolean locationOnly = true;
    private ItemHolder itemHolder = null;

    public LeaveMarkForm(MyWebApp mywebapp, Location location, boolean locationOnly, ItemHolder itemHolder) {
        super(mywebapp);
        this.itemHolder = itemHolder;
        this.spotreadonly = isSpotReadOnly();
        SpotHolder spotHolder = itemHolder.getSpotHolder();
        this.location = location;
        this.addStyleName("LeaveMarkForm");

          this.defaultUploader = new MultiUploader();
           // this.defaultUploader =  multiUploader;
            //IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages);
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler3);
            MarkData markData = new MarkData();
            markData.expandData = new ExpandData();
            markData.spotHolder = spotHolder;
            markData.saySomethingTextArea = contentTextArea;
            markData.formPanel = formPanel;
            widgetMarkDataMap.put(leaveMarkButton, markData);
            leaveMarkButton.addClickHandler(saveHandler2);
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            Button addTagButton = new Button();
            addTagButton.addClickHandler(addTagHandler);
            markData.tagSearchTextBox = tagSearchTextBox;
            Button shareOnFacebookButton = getFacebookButton(markData);
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
            DropPanel dropPanel = getDropPanel();
            MarkComposite markComposite = new MarkComposite(leaveMarkButton, contentTextArea, defaultUploader, panelImages, mywebapp, markData.tagSearchTextBox, selectedTagsPanel,
                    addTagButton, markData.secretKeyTextBox, shareOnFacebookButton, suggestionsPanel,dropPanel);
            markComposite.setLocationName(spotHolder.getName());
            markComposite.setFullAddress(spotHolder.getAddressLabel());
            markComposite.setPhoneNumber(spotHolder.getVoicephone());
            markComposite.setSpotId(spotHolder.getId());

            FormPanel myFormPanel = new FormPanel();
            setupRootPanelForm(myFormPanel, markData);
            myFormPanel.setWidget(markComposite);
            add(myFormPanel);

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

            GWT.log("doSave else");
            SpotHolder spotHolder = itemHolder.getSpotHolder();
            if (nameTextBox != null) {
                spotHolder.setName(nameTextBox.getValue());
            }
            //following stuff is for a plate
            if (address1TextField != null) {
                spotHolder.setAddressLine1(address1TextField.getValue());
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

        checkRequired(address1TextField, "Address is required");
        checkRequired(citySuggestBox, "City is required");
        checkRequired(stateTextBox, "State is required");
        checkRequired(zipcodeTextField, "Zipcode is required");
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

}
