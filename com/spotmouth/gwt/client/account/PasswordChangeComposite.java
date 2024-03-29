package com.spotmouth.gwt.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/5/13
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordChangeComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, PasswordChangeComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided=true)
    final TextBox usernameTextBox;



    @UiField(provided=true)
    final TextBox passwordTextBox;


    @UiField(provided=true)
    final Button saveButton;



            public PasswordChangeComposite(TextBox usernameTextBox,TextBox passwordTextBox,Button saveButton ) {
                this.usernameTextBox = usernameTextBox;
                this.passwordTextBox = passwordTextBox;
                this.saveButton = saveButton;
    initWidget(uiBinder.createAndBindUi(this));


}



}