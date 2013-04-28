package com.spotmouth.gwt.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationConfirmComposite   extends Composite {
    interface MyUiBinder extends UiBinder<Widget, RegistrationConfirmComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided=true)
    final TextBox usernameTextBox;



    @UiField(provided=true)
    final TextField clearPasswordTextField;



    @UiField(provided=true)
    final PasswordTextBox maskedPasswordTextBox;



    @UiField(provided=true)
    final Button saveButton;


    @UiField(provided=false)
    SimpleCheckBox showTypingCheckBox;


            public RegistrationConfirmComposite(TextBox usernameTextBox,TextField clearPasswordTextField,PasswordTextBox maskedPasswordTextBox , Button saveButton ) {
                this.usernameTextBox = usernameTextBox;
                this.clearPasswordTextField = clearPasswordTextField;
                this.clearPasswordTextField.setVisible(false);
                this.maskedPasswordTextBox = maskedPasswordTextBox;
                this.saveButton = saveButton;
        initWidget(uiBinder.createAndBindUi(this));


}

    @UiHandler("showTypingCheckBox")
    public void onClick0(ClickEvent event) {
        if (showTypingCheckBox.getValue()) {
            //show typing
            clearPasswordTextField.setVisible(true);
            maskedPasswordTextBox.setVisible(false);
        }   else {
            clearPasswordTextField.setVisible(false);
            maskedPasswordTextBox.setVisible(true);
        }

    }


}
