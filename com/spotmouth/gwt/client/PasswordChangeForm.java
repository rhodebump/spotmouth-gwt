package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.UserHolder;
import com.spotmouth.gwt.client.dto.UserRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/5/13
 * Time: 8:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordChangeForm extends SpotBasePanel implements SpotMouthPanel {


    public PasswordChangeForm() {}





    AsyncCallback passwordSaved = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your password has been updated.");
        }
    };


    protected void saveResetPassword() {
        GWT.log("saveResetPassword");
        UserRequest userRequest = new UserRequest();
        userRequest.setAuthToken(mywebapp.getAuthToken());
        UserHolder userHolder = userRequest.getUserHolder();
        userRequest.setNewPassword(newPasswordTextBox.getValue());
        //should we check if valid??
        //don't know old password, and everything is optional, so no!
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        final DataOperationDialog saveAccountSettingsDialog = new DataOperationDialog("Saving account settings");
        myService.saveResetPassword(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                saveAccountSettingsDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                saveAccountSettingsDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //UserHolder uh = mobileResponse.getUserHolder();
                    mywebapp.setAuthenticatedUser(mobileResponse.getUserHolder());
                    //we should exit the account settings page, but where to go??
                    mywebapp.setHomeCallback(passwordSaved);
                    History.newItem(MyWebApp.ACCOUNT_SETTINGS);


                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


    protected void doSave() {

        saveResetPassword();
    }


    public PasswordChangeForm(MyWebApp mywebapp) {
        super(mywebapp);
        this.clear();
        addRequired(newPasswordTextBox);
        usernameTextBox.setValue(mywebapp.getAuthenticatedUser().getUsername());
        usernameTextBox.setReadOnly(true);
        Button saveButton = new Button("Submit");
        saveButton.addClickHandler(saveHandler);
        PasswordChangeComposite registrationConfirmComposite = new PasswordChangeComposite(usernameTextBox, newPasswordTextBox, saveButton);
        add(registrationConfirmComposite);
        return;



    }


    protected boolean isValid() {
        mywebapp.getMessagePanel().clear();

        if (newPasswordTextBox.getValue().length() == 0) {
            mywebapp.getMessagePanel().displayMessage("Password is required");
        }

        return true;
    }


    public void toggleFirst() {
        newPasswordTextBox.setFocus(true);
    }


}
