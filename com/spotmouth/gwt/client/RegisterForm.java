package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
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


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.registerMobile();
        }
        return MyWebApp.resources.register();
    }
   private       FlowPanel ugcPanel = new FlowPanel();
    private SimpleCheckBox acceptCheckbox = new SimpleCheckBox();
    private TextBox usernameTextBox = new TextBox();
    private TextBox smsPhoneNumberBox = new TextBox();
    private TextBox emailAddressTextBox = new TextBox();

    public RegisterForm(MyWebApp mywebapp) {
        super(mywebapp,false);

        if (MyWebApp.isDesktop()) {
            Button registrationButton = new Button("Submit");
            registrationButton.addClickHandler(registerHandler);
            addRequired(usernameTextBox);
            addRequired(emailAddressTextBox);

            String ugcText = MyHtmlResources.INSTANCE.getUGC().getText();
          //  this.ugcHtml.setHTML(ugcText);
            ugcPanel.setVisible(false);

            HTML html = new HTML(ugcText);
            ugcPanel.add(html);

            Anchor showTermsAnchor = new Anchor();
            showTermsAnchor.addClickHandler(showTerms);

            RegistrationComposite registrationComposite = new RegistrationComposite(usernameTextBox,emailAddressTextBox,smsPhoneNumberBox,acceptCheckbox,registrationButton,ugcPanel,showTermsAnchor);
            add(registrationComposite);


            return;

        }

        this.addStyleName("RegisterPanel");
        //need to add the policy here
        ScrollPanel sp = new ScrollPanel();
        sp.setHeight("200px");
        String html = MyHtmlResources.INSTANCE.getUGC().getText();
        Label ugcLabel = new Label("User Generated Content Policy");
        ugcLabel.setStyleName("h1");
        sp.setWidget(new HTML(html));
        add(ugcLabel);
        add(sp);

        //add(acceptCheckbox);
        //this.acceptCheckbox = new SimplCheckBox("I accept these terms & conditions.");
        FlowPanel fp =new FlowPanel();
        fp.add(acceptCheckbox);
        fp.add(new Label("I accept these terms & conditions."));
        // addVoting(mywebapp.resources.thumbsDownBig(), mywebapp.resources.thumbsUpBig(), null);
        addFieldset(fp, "", "na4");
        Label label = new Label("You will need to provide a valid email address to activate your account.");
        add(label);
        usernameTextBox = addTextBox("Username *", "username", "");
        emailAddressTextBox = addTextBox("Email *", "email", "");

        Label smsLabel = new Label("If you want to receive text alerts, you need to provide a phone number.");
        add(smsLabel);

        smsPhoneNumberBox = addTextBox("SMS Phone Number", "smsPhoneNumber", "");

        add(registerButton());
        add(cancelButton());
        usernameTextBox.setFocus(true);
    }

    Label registerButton() {
        Label btn = new Label("Register");
        btn.addClickHandler(registerHandler);
        fixButton(btn);
        ImageResource ir = MyWebApp.resources.registerButton();

        if (MyWebApp.isSmallFormat()) {
            ir = MyWebApp.resources.registerButtonMobile();
        }

        Image img = new Image(ir);
        btn.getElement().appendChild(img.getElement());
        return btn;
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
        registrationRequest.setSmsPhoneNumber(smsPhoneNumberBox.getValue());
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

    public boolean isLoginRequired() {
        return false;
    }
}
