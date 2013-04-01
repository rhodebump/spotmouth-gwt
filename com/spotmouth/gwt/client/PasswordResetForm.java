package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.LoginRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/11/12
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordResetForm extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        return MyWebApp.resources.passwordReset();
    }

    public String getTitle() {
        return "Reset Password";
    }

    protected ClickHandler cancelHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleHome();
        }
    };



    public PasswordResetForm(MyWebApp mywebapp) {
        super(mywebapp,false);
        Label label = new Label("Resetting your password will cause a email to be sent to your registered email address with a link to login.");
        add(label);
        usernameTextBox = addTextBox("Username", "username", "");
        usernameTextBox.setFocus(true);
        add(resetPasswordButton());
        Button cancelButton = new Button("Cancel");

        cancelButton.addClickHandler(cancelHandler2);
        //addImageToButton(btn, MyWebApp.resources.cancelButton(), MyWebApp.resources.cancelButtonMobile());
        //fixButton(btn);
        cancelButton.setStyleName("btn_blue");
        add(cancelButton);
    }


    protected boolean isValid() {
        mywebapp.log("PasswordResetForm isValid");


        checkRequired(usernameTextBox, "Username is required");
        return (!getMessagePanel().isHaveMessages());
    }


    public void toggleFirst() {
        usernameTextBox.setFocus(true);
    }


    protected void doSave() {
        mywebapp.log("doSave");
        resetPassword();
    }

    AsyncCallback passwordResetMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your password has been reset.  Please check your email.");
        }
    };


    private void resetPassword() {
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(usernameTextBox.getValue());
        myService.resetpassword(loginRequest, new AsyncCallback() {

            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError("Failure:" + caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse loginResponse = (MobileResponse) result;
                if (loginResponse.getStatus() == 1) {

                    mywebapp.toggleHome(passwordResetMessageCallback);

                } else {
                    mywebapp.log("login status was zero");
                    mywebapp.log("resetPassword failure 1");
                    getMessagePanel().displayErrors(loginResponse.getErrorMessages());
                }
            }
        });
    }

//    ClickHandler resetPasswordHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            resetPassword();
//        }
//    };

    Button resetPasswordButton() {
        Button btn = new Button("Reset Password");
        btn.addClickHandler(saveHandler);
        btn.setStyleName("btn_blue");
        //fixButton(btn);
        return btn;
    }
}
