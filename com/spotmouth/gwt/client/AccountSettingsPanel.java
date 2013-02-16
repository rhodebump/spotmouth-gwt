package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.UserHolder;
import com.spotmouth.gwt.client.dto.UserRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/11/12
 * Time: 6:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountSettingsPanel extends SpotBasePanel implements SpotMouthPanel {


    ClickHandler saveAccountSettingsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            saveAccountSettings();
        }
    };

    public boolean isValidToToggle() {
        if (MyWebApp.isDesktop()) {
            return true;
        }
        //we do not want to leave this if they do not have a password set yet
        //GWT.log();
        //GWT.log("password valid=" + mywebapp.getAuthenticatedUser().isPasswordValid());
        if (mywebapp.isFacebookUser()) {
            return true;
        }
        if (!mywebapp.getAuthenticatedUser().isPasswordValid()) {
            mywebapp.getMessagePanel().clear();
            mywebapp.getMessagePanel().displayMessage("Please use the form below to change your password now.");
            return false;
        }
        return true;
    }

    public String getTitle() {
        //Manage Profile got cut off
        return "Profile";
    }

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.passwordMobile();
        } else {
            return MyWebApp.resources.password();
        }
        //return MyWebApp.resources.locationmanualBig();
    }

    private TextBox usernameTextBox = new TextBox();

    private TextBox passwordTextBox = new TextBox();
    private TextBox confirmPasswordTextBox = new TextBox();



    private TextBox firstNameTextBox = new TextBox();
    private TextBox lastNameTextBox = new TextBox();

    protected boolean isValid() {
        mywebapp.getMessagePanel().clear();
//        if (!registrationMode) {
//            //means password not required, but if it is, needs to match
//            if (passwordTextBox.getValue().equals("")) {
//                return true;
//            }
//        }
        if (passwordTextBox.getValue().length() == 0) {
            mywebapp.getMessagePanel().displayMessage("Password is required");
        }
        if (confirmPasswordTextBox.getValue().length() == 0) {
            mywebapp.getMessagePanel().displayMessage("Confirm password is required");
        }
        if (!confirmPasswordTextBox.getValue().equals(passwordTextBox.getValue())) {
            mywebapp.getMessagePanel().displayMessage("Password and Confirm password must match.");
        }
        if (getMessagePanel().isHaveMessages()) {
            return false;
        }
        return true;
    }

    public AccountSettingsPanel() {
    }
    //private boolean registrationMode = false;
    public ClickHandler removeProfileHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //saveIt();
            GWT.log("removeProfileHandler onCLick");
            // ConfirmRemoveDialogBox box
            new ConfirmRemoveDialogBox().show();
        }
    };
    AsyncCallback profileDeletedMessage = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not delete profile.");
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your profile has been deleted");
        }
    };

    public void deleteUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserHolder(mywebapp.getAuthenticatedUser());
        userRequest.setAuthToken(mywebapp.getAuthToken());
        //UserHolder userHolder = userRequest.getUserHolder();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteUser(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setAuthenticatedUser(null);
                    mywebapp.toggleHome(profileDeletedMessage);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private class ConfirmRemoveDialogBox extends DialogBox {
        public ConfirmRemoveDialogBox() {
            // Set the dialog box's caption.
            setText("Confirm");
            // DialogBox is a SimplePanel, so you have to set its widget property to
            // whatever you want its contents to be.
            Button okButton = new Button("OK");
            Button cancelButton = new Button("Cancel");
            VerticalPanel vp = new VerticalPanel();
            vp.add(new Label("Please confirm that you wish to delete your profile."));
            HorizontalPanel hp = new HorizontalPanel();
            hp.add(okButton);
            hp.add(cancelButton);
            okButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    AccountSettingsPanel.this.deleteUser();
                    ConfirmRemoveDialogBox.this.hide();
                }
            });
            cancelButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent clickEvent) {
                    ConfirmRemoveDialogBox.this.hide();
                }
            });
            vp.add(hp);
            setWidget(vp);
            //setWidget(ok);
            this.center();
        }
    }

    public AccountSettingsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        UserHolder user = mywebapp.getAuthenticatedUser();
        if (user == null) {
            user = new UserHolder();
        }
        addRequired(usernameTextBox);
        usernameTextBox.setValue(user.getUsername());
        usernameTextBox.setReadOnly(true);
        emailTextField = new TextField();
        emailTextField.setValue(user.getEmailAddress());


        smsPhoneNumberTextBox.setValue(user.getSmsPhoneNumber());
        if (MyWebApp.isDesktop()) {
            //it's not registration, just profile
            //usernameTextBox.setStyleName("pcheck");
           // emailTextField.setStyleName("pcheck");
           // smsPhoneNumberTextBox.setStyleName("pcheck");
            Button saveAccountSettingsButton = new Button();
            saveAccountSettingsButton.addClickHandler(saveAccountSettingsHandler);
            //<a href="remove-profile.html" title="Remove Profile">x</a>
            Anchor removeProfileAnchor = new Anchor("x");
            removeProfileAnchor.setTitle("Remove Profile");
            removeProfileAnchor.addClickHandler(removeProfileHandler);
            //removeProfileAnchor.setStyleName("ddkill");
            AccountSettingsComposite accountSettingsComposite = new AccountSettingsComposite(usernameTextBox, emailTextField, oldPasswordTextBox, newPasswordTextBox,
                    smsPhoneNumberTextBox, saveAccountSettingsButton, removeProfileAnchor);
            add(accountSettingsComposite);
            return;
        }
        usernameTextBox = addTextBox("Username", "username", user.getUsername());
        usernameTextBox.setReadOnly(true);
        Fieldset passwordFieldSet = new Fieldset();
        VerticalPanel passwordVerticalPanel = new VerticalPanel();
        passwordTextBox = addTextBox("Password", "password", "", false, passwordVerticalPanel);
        confirmPasswordTextBox = addTextBox("Confirm Password", "password", "", false, passwordVerticalPanel);
        passwordFieldSet.add(passwordVerticalPanel);
        add(passwordFieldSet);
        //changing your email address or sms number will require re-validation of your accounts
        //username is readonly
        //email, make edit
        //first
        //last
        //password
        //add uploaders for profile pic
        // if (!registrationMode) {
        Label changeWarning = new Label("If you change your phone number or email, you will have to re-validate those accounts");
        add(changeWarning);
        smsPhoneNumberTextBox = addTextBox("SMS Phone Number", "smsPhoneNumber", user.getSmsPhoneNumber());
        emailTextField = addTextBox("Email", "email", user.getEmailAddress());
        Fieldset nameFieldset = new Fieldset();
        VerticalPanel nameVP = new VerticalPanel();
        firstNameTextBox = addTextBox("First Name", "fn", user.getFirstName(), false, nameVP);
        lastNameTextBox = addTextBox("Last Name", "ln", user.getLastName(), false, nameVP);
        nameFieldset.add(nameVP);
        Label label = new Label("Your location is only required if you want to participate in contests");
        add(label);
        //cityTextBox = addTextBox("City", "na4", user.getCity(), false);
        countryTextBox = addCountry(user.getCountryCode(), this);
        stateTextBox = addState(user.getState(), this);
        citySuggestBox = addCity(user.getCity(), this);
        //stateTextBox = addTextBox("State", "na5", user.getState(), false);
        zipcodeTextField = addTextBox("Zip", "na6", user.getZip(), false);
        //dont want to show an email address since one cannot change it
        //emailTextBox = addTextBox("Email Address", "emaial", mywebapp.getAuthenticatedUser().getEmailAddress());
        Label lbl = new Label("Tell us a little about yourself");
        add(lbl);
        contentTextArea = addTextArea("About Me", "na", user.getAboutMe(), false);
        //about me
        addMediaFields("Profile Picture");
        add(contentsPanel);
        addContentHolder(user.getContentHolder(), true, true);
        addTagHolderForm(user.getTagHolders());
        //  }
        add(saveButton());
        //if (!registrationMode) {
        add(cancelButton());
        //  }
    }

    public void toggleFirst() {
        passwordTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;
    }

    protected void doSave() {
        GWT.log("do save");
        UserRequest userRequest = new UserRequest();
        userRequest.setUserHolder(mywebapp.getAuthenticatedUser());
        userRequest.setAuthToken(mywebapp.getAuthToken());
        UserHolder userHolder = userRequest.getUserHolder();
        if (emailTextField != null) {
            userHolder.setEmailAddress(emailTextField.getValue());
        }
        if (passwordTextBox.getValue().length() > 0) {
            userHolder.setPassword(passwordTextBox.getValue());
        }
        userHolder.setAboutMe(contentTextArea.getValue());
        if (citySuggestBox != null) {
            userHolder.setCity(citySuggestBox.getValue());
        }
        if (stateTextBox != null) {
            userHolder.setState(stateTextBox.getValue());
        }
        if (zipcodeTextField != null) {
            userHolder.setZip(zipcodeTextField.getValue());
        }
        if (countryTextBox != null) {
            userHolder.setCountryCode(countryTextBox.getValue());
        }
        if (firstNameTextBox != null) {
            userHolder.setFirstName(firstNameTextBox.getValue());
        }
        if (lastNameTextBox != null) {
            userHolder.setLastName(lastNameTextBox.getValue());
        }
        userHolder.setEmailAddress(emailTextField.getValue());
        userHolder.setSmsPhoneNumber(smsPhoneNumberTextBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveUser(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    UserHolder uh = mobileResponse.getUserHolder();
                    GWT.log("uh.city is " + uh.getCity());
                    mywebapp.setAuthenticatedUser(mobileResponse.getUserHolder());
                    //do they have a profile picture?
                    //if they don't let's stay here and give them a nudge
                    boolean havePic = havePicture(mobileResponse);
                    if (!havePic) {
                        mywebapp.toggleViewUserProfile(uh.getId());
                        getMessagePanel().displayMessage("Your profile has been saved. ");
                        getMessagePanel().displayMessage("This would be a good time to set a profile picture and tell us about yourself.");
                    } else {
                        mywebapp.toggleMenu();
                        getMessagePanel().displayMessage("Your profile has been saved");
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
