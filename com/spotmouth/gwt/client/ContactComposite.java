package com.spotmouth.gwt.client;

import com.spotmouth.gwt.client.common.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/18/12
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ContactComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);



    @UiField(provided=true)
    final TextField yourNameTextBox;
    @UiField(provided=true)
    final TextField email;

    @UiField(provided=true)
    final TextBox messageSubject;
    @UiField(provided=true)
    final Button sendButton;
    @UiField(provided=true)
    final TextArea contentTextArea;



    public ContactComposite(TextField yourNameTextBox,TextField email, TextBox messageSubject,Button sendButton,TextArea contentTextArea   ) {
        this.yourNameTextBox = yourNameTextBox;
        this.email = email;
        this.messageSubject = messageSubject;
        this.sendButton = sendButton;
        this.contentTextArea = contentTextArea;

//        this.usernameTextBox = usernameTextBox;
//        this.emailAddressTextBox = emailAddressTextBox;
//        this.oldPasswordTextBox = oldPasswordTextBox;
//        this.newPasswordTextBox = newPasswordTextBox;
//        this.smsPhoneNumberTextBox = smsPhoneNumberTextBox;
//        this.saveAccountSettingsButton = saveAccountSettingsButton;
//        this.removeProfileAnchor = removeProfileAnchor;

initWidget(uiBinder.createAndBindUi(this));

    }


}
