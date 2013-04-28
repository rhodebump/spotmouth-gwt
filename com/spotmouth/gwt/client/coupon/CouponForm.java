package com.spotmouth.gwt.client.coupon;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.MultiUploader;

public class CouponForm extends SpotBasePanel implements SpotMouthPanel {




    protected boolean isValid() {
        populateItemHolder();
        validateDateRange();
        return (!getMessagePanel().isHaveMessages());
    }

    private void populateItemHolder() {
        getItemHolder().setStartDate(startDatePicker.getValue());
        getItemHolder().setEndDate(endDatePicker.getValue());
    }

    protected void doSave() {
        //coupons are just like marks
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        //leaveMarkRequest.setSpotHolder(spotHolder);
        ItemHolder itemHolder = new ItemHolder();
        leaveMarkRequest.setItemHolder(itemHolder);
        itemHolder.setTitle(titleTextBox.getValue());
        itemHolder.setTextData(contentTextArea.getValue());
        itemHolder.setCoupon(true);
        // startDatePicker.get
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
                    getMessagePanel().displayMessage("Coupon has been saved.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public String getTitle() {
        return "Coupon";
    }

    public String getPageTitle() {
        return getTitle();
    }

    private SpotHolder spotHolder = null;

    public CouponForm(MyWebApp mywebapp, SpotHolder spotHolder, ItemHolder couponItemHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        setItemHolder(couponItemHolder);
        if (MyWebApp.isDesktop()) {
            Button saveButton = new Button();
            saveButton.addClickHandler(saveHandler);
            Button cancelButton = new Button();
            saveButton.addClickHandler(cancelHandler);
            titleTextBox = new TextField();
            this.defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);
            //do we have an iamge
            Image mainImage = getImage(couponItemHolder.getContentHolder(), "320x320");
            //contestImagePanel
            if (mainImage != null) {
                imageUploaderImagePanel.setWidget(mainImage);
            }
            SuggestBox tagSearchTextBox = getTagSuggestBox(couponItemHolder.getTagHolders());
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
            initControls(couponItemHolder);
            Anchor spotDetailsAnchor = new Anchor();
            spotDetailsAnchor.addClickHandler(spotDetailsHandler);
            CouponFormComposite cfc = new CouponFormComposite(cancelButton, imageUploaderImagePanel, defaultUploader, startDatePicker, endDatePicker, titleTextBox,
                    contentTextArea, saveButton, suggestionsPanel, tagSearchTextBox, selectedTagsPanel,spotDetailsAnchor);
            add(cfc);
            return;
        }
        addSpotHeader(spotHolder);
        titleTextBox = addTextBox("Coupon Title", "title", "");
        contentTextArea = addTextArea("Description", "textData", "", false);
        addMediaFields("Upload images OR video for coupon");
        addDateRange();
        add(saveButton());
        add(cancelButton());
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

    public void toggleFirst() {
        titleTextBox.setFocus(true);
    }

    protected ClickHandler spotDetailsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            String token = MyWebApp.SPOT_DETAIL + spotHolder.getId();


            History.newItem(token);


        }
    };



}
