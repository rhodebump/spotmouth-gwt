package com.spotmouth.gwt.client.event;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SuggestBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.coupon.CouponFormComposite;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.LeaveMarkRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.MultiUploader;

public class EventForm extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        return MyWebApp.resources.event();
    }

    protected boolean isValid() {
        populateItemHolder();
        validateDateRange();
        return (!getMessagePanel().isHaveMessages());
    }

    private void populateItemHolder() {
        getItemHolder().setStartDate(startDatePicker.getValue());
        getItemHolder().setEndDate(endDatePicker.getValue());
    }

    private void initControls(ItemHolder itemHolder) {
        titleTextBox.setValue(itemHolder.getTitle());
        contentTextArea.setValue(itemHolder.getTextData());
        if (itemHolder.getStartDate() != null) {
            startDatePicker.setValue(itemHolder.getStartDate());
        }
        if (itemHolder.getEndDate() != null) {
            endDatePicker.setValue(itemHolder.getEndDate());
        }
    }


    protected void doSave() {
        //coupons are just like marks
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        //leaveMarkRequest.setSpotHolder(spotHolder);
        ItemHolder itemHolder = new ItemHolder();
        leaveMarkRequest.setItemHolder(itemHolder);
        itemHolder.setTitle(titleTextBox.getValue());
        itemHolder.setTextData(contentTextArea.getValue());
        itemHolder.setEvent(true);
        populateItemHolder();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.leavemark(leaveMarkRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Event has been saved.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private SpotHolder spotHolder = null;

    public String getTitle() {
        return "Event";
    }

    public EventForm(MyWebApp mywebapp, SpotHolder spotHolder,ItemHolder eventItemHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        setItemHolder(eventItemHolder);
        if (MyWebApp.isDesktop()) {
            Button saveButton = new Button();
            saveButton.addClickHandler(saveHandler);
            Button cancelButton = new Button();
            saveButton.addClickHandler(cancelHandler);
            titleTextBox = new TextField();
            defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);
            //do we have an iamge
            Image mainImage = getImage(eventItemHolder.getContentHolder(), "320x320");
            //contestImagePanel
            if (mainImage != null) {
                imageUploaderImagePanel.setWidget(mainImage);
            }
            SuggestBox tagSearchTextBox = getTagSuggestBox(eventItemHolder.getTagHolders());
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
            initControls(eventItemHolder);
            EventFormComposite cfc = new EventFormComposite(cancelButton, imageUploaderImagePanel, defaultUploader, startDatePicker, endDatePicker, titleTextBox,
                    contentTextArea, saveButton, suggestionsPanel, tagSearchTextBox, selectedTagsPanel);
            add(cfc);
            return;
        }


        addSpotHeader(spotHolder);
        titleTextBox = addTextBox("Event Title", "title", "");
        contentTextArea = addTextArea("Description", "textData", "", false);
        addMediaFields("Upload images OR video for event");
        addDateRange();
        add(saveButton());
        add(cancelButton());
    }

    public void toggleFirst() {
        titleTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;
    }
}
