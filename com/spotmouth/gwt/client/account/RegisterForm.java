package com.spotmouth.gwt.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.DataOperationDialog;
import com.spotmouth.gwt.client.MyHtmlResources;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.RegistrationRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/*
* Facebook login is a little tricky
* We need to invoke a childbrowser that will login to facebook and return to us the accesstoken
*
* I send the accesstoken to spotmouth that will then go to facebook and get the userid
*/
public class RegisterForm extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Register";
    }





    ClickHandler showTerms = new ClickHandler() {
        public void onClick(ClickEvent event) {

            ugcPanel.setVisible(true);



        }
    };



   private       FlowPanel ugcPanel = new FlowPanel();
    private SimpleCheckBox acceptCheckbox = new SimpleCheckBox();


    private TextField emailAddressTextBox = new TextField();

    public RegisterForm(MyWebApp mywebapp) {
        super(mywebapp,false);

        if (MyWebApp.isDesktop()) {
            Button registrationButton = new Button("Submit");
            registrationButton.addClickHandler(registerHandler);
            usernameTextBox = new TextField();
            addRequired(usernameTextBox);
            addRequired(emailAddressTextBox);

            String ugcText = MyHtmlResources.INSTANCE.getUGC().getText();
          //  this.ugcHtml.setHTML(ugcText);
            ugcPanel.setVisible(false);

            HTML html = new HTML(ugcText);
            ugcPanel.add(html);

            Anchor showTermsAnchor = new Anchor();
            showTermsAnchor.addClickHandler(showTerms);

            RegistrationComposite registrationComposite = new RegistrationComposite(usernameTextBox,emailAddressTextBox,smsPhoneNumberTextBox,acceptCheckbox,registrationButton,ugcPanel,showTermsAnchor);
            add(registrationComposite);




        }


    }


    ClickHandler registerHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            if (usernameTextBox.getValue().isEmpty()) {
                getMessagePanel().displayMessage("Username is required.");
            }
            if (emailAddressTextBox.getValue().isEmpty()) {
                getMessagePanel().displayMessage("Email is required.");
            }
            if (acceptCheckbox.getValue() == false) {
                getMessagePanel().displayMessage("Please accept the terms & conditions");
            }
            if (getMessagePanel().isHaveMessages()) {
                return;
            }
            performRegister();
        }
    };

    public void performRegister() {
        // http://localhost:8080/georepo/appSpot/spotdetail/1?offset=10&max=10&sort=createDate&order=desc
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail(emailAddressTextBox.getValue());
        registrationRequest.setUsername(usernameTextBox.getValue());
        registrationRequest.setSmsPhoneNumber(smsPhoneNumberTextBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.doregister(registrationRequest, new AsyncCallback() {
            DataOperationDialog validateLoginDialog = new DataOperationDialog(
                    "Performing Registration");

            public void onFailure(Throwable caught) {
                validateLoginDialog.hide();
                mywebapp.log(caught.toString());
                GWT.log("testLogin", caught);
                getMessagePanel().displayMessage("Registration failure:" + caught.toString());
            }

            public void onSuccess(Object result) {
                validateLoginDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.toggleMenu();
                    getMessagePanel().displayMessage("Registration was successful");
                    getMessagePanel().displayMessage("Please check your email to finish your registration");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleFirst() {
        usernameTextBox.setFocus(true);
    }

}
