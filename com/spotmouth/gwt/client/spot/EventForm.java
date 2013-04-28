package com.spotmouth.gwt.client.spot;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.LeaveMarkRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

public class EventForm extends SpotBasePanel implements SpotMouthPanel {


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

    public EventForm(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
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


}
