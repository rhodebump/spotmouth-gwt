package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 10/29/12
 * Time: 6:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegistrationComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, RegistrationComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);




    @UiField(provided=true)
    final TextBox usernameTextBox;



    @UiField(provided=true)
    final TextBox smsPhoneNumberBox;


    @UiField(provided=true)
    final TextBox emailAddressTextBox;

    @UiField(provided=true)
    final SimpleCheckBox acceptCheckbox;

    @UiField(provided=true)
    final Button registrationButton;
    @UiField(provided=true)
    final FlowPanel ugcPanel;

    @UiField(provided=true)
    final Anchor showTermsAnchor;



    public RegistrationComposite(TextBox usernameTextBox, TextBox emailAddressTextBox, TextBox smsPhoneNumberBox,SimpleCheckBox acceptCheckbox,Button registrationButton,FlowPanel ugcPanel, Anchor showTermsAnchor) {

        this.usernameTextBox  = usernameTextBox;
        this.emailAddressTextBox = emailAddressTextBox;
        this.smsPhoneNumberBox = smsPhoneNumberBox;
        this.acceptCheckbox = acceptCheckbox;
        this.registrationButton  = registrationButton;
        this.ugcPanel = ugcPanel;
        this.showTermsAnchor = showTermsAnchor;
        initWidget(uiBinder.createAndBindUi(this));

    }

}
