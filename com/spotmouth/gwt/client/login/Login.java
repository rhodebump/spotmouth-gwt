package com.spotmouth.gwt.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 10/21/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Login extends Composite {
    interface MyUiBinder extends UiBinder<Widget, Login> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    //placeholder="E-mail..."  type="email" required="required"

    @UiField(provided=true)
    final TextBox usernameTextBox;

    @UiField(provided=true)
    final TextBox passwordTextBox;


    @UiField(provided=true)
    final Button signUp;


    @UiField(provided=true)
    final Button loginButton;


    @UiField(provided=true)
    final PasswordTextBox maskedPasswordTextBox;


    @UiField(provided=true)
    final SimpleCheckBox showTypingCheckbox;

    @UiField(provided=true)
    final SimpleCheckBox rememberMeCheckbox;


    @UiField(provided=true)
    final Anchor facebookAnchor;

    @UiField(provided=true)
    final Anchor resetAnchor;

    @UiField(provided=true)
    final Anchor googleAnchor;

    @UiField(provided=true)
    final Anchor twitterAnchor;


    //<input type="submit" value="Log in"/>


    public Login( TextBox usernameTextBox, TextBox passwordTextBox,PasswordTextBox maskedPasswordTextBox, Button signUp,Button loginButton,SimpleCheckBox showTypingCheckbox,
                  SimpleCheckBox rememberMeCheckbox,Anchor facebookAnchor,Anchor resetAnchor,Anchor googleAnchor,Anchor twitterAnchor) {
        this.usernameTextBox = usernameTextBox;
        this.passwordTextBox = passwordTextBox;
        this.maskedPasswordTextBox = maskedPasswordTextBox;
        this.signUp =signUp;
        this.loginButton = loginButton;
        this.showTypingCheckbox = showTypingCheckbox;
        this.rememberMeCheckbox = rememberMeCheckbox;
        this.facebookAnchor = facebookAnchor;
        this.resetAnchor = resetAnchor;
        this.googleAnchor = googleAnchor;
        this.twitterAnchor = twitterAnchor;
        initWidget(uiBinder.createAndBindUi(this));


    }
}
