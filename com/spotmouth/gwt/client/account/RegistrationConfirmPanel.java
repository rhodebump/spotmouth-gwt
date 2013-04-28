package com.spotmouth.gwt.client.account;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;

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



    public RegistrationConfirmPanel() {}






    protected void doSave() {

        saveAccountSettings();
    }
    protected PasswordTextBox maskedPasswordTextBox = new PasswordTextBox();

    public RegistrationConfirmPanel(MyWebApp mywebapp) {
        super(mywebapp);
        this.clear();
        addRequired(newPasswordTextBox);
        usernameTextBox = new TextField();
        if (mywebapp.getAuthenticatedUser() != null) {
            usernameTextBox.setValue(mywebapp.getAuthenticatedUser().getUsername());
        }

        Button saveButton = new Button("Submit");
        saveButton.addClickHandler(saveHandler);

        RegistrationConfirmComposite registrationConfirmComposite = new RegistrationConfirmComposite(usernameTextBox, newPasswordTextBox, maskedPasswordTextBox,saveButton);
        add(registrationConfirmComposite);




    }


    protected boolean isValid() {
        mywebapp.getMessagePanel().clear();
        if (newPasswordTextBox.isVisible()) {

            if (isEmpty(newPasswordTextBox)) {
                mywebapp.getMessagePanel().displayMessage("Password is required");
            }

        }
        if (maskedPasswordTextBox.isVisible()) {

            if (isEmpty(maskedPasswordTextBox)) {
                mywebapp.getMessagePanel().displayMessage("Password is required");
            }
        }

        if (getMessagePanel().isHaveMessages()) {
            return false;
        }else {
            return true;
        }
    }


    public void toggleFirst() {
        newPasswordTextBox.setFocus(true);
    }


}
