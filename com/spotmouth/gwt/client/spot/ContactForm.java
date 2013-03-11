package com.spotmouth.gwt.client.spot;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ContactRequest;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

public class ContactForm extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Contact Spot";
    }

    public String getPageTitle() {
        return getTitle();
    }


    public boolean isRootPanel() {
        return true;
    }

    private SpotHolder spotHolder = null;
    TextBox yourName = null;
    TextBox email = null;
    TextArea contentTextArea = null;

    public ContactForm(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        yourName = addTextBox("Your Name", "name", "");
        email = addTextBox("Your Email Address", "email", "");
        contentTextArea = addTextArea("Your Message", "message", "", false);
        Label btn = new Label("Send Message");
        btn.addClickHandler(saveHandler);
        fixButton(btn);
        add(btn);
        add(cancelButton());
    }

    public void toggleFirst() {
    }



    protected void doSave() {
        //if we are here, the big files were posted, now we do the actual mark rpc
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName(yourName.getValue());
        contactRequest.setEmail(email.getValue());
        contactRequest.setMessage(contentTextArea.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.contactSpot(contactRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
            }
        });
    }
}
