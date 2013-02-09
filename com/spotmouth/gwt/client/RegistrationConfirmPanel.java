package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/8/12
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationConfirmPanel extends SpotBasePanel implements SpotMouthPanel {


    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Registration Confirmation";
    }


    //private TextBox passwordTextBox = new TextBox();
    private TextBox usernameTextBox = new TextBox();
    public RegistrationConfirmPanel() {}


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.passwordMobile();
        } else {
            return MyWebApp.resources.password();
        }
    }




    protected void doSave() {

        saveAccountSettings();
    }


    public RegistrationConfirmPanel(MyWebApp mywebapp) {
        super(mywebapp);
        this.clear();
        addRequired(newPasswordTextBox);
        usernameTextBox.setValue(mywebapp.getAuthenticatedUser().getUsername());
        Button saveButton = new Button("Submit");
        saveButton.addClickHandler(saveHandler);
        RegistrationConfirmComposite registrationConfirmComposite = new RegistrationConfirmComposite(usernameTextBox, newPasswordTextBox, saveButton);
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

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
