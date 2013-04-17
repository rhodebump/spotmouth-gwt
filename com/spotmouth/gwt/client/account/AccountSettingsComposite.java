package com.spotmouth.gwt.client.account;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/3/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountSettingsComposite   extends Composite {
//

    @UiTemplate("AccountSettingsComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, AccountSettingsComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("MAccountSettingsComposite.ui.xml")
    interface MobileBinder extends UiBinder<Widget, AccountSettingsComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);


    //private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided=true)
    final TextBox usernameTextBox;

    @UiField(provided=true)
    final TextBox emailAddressTextBox;

    @UiField(provided=true)
    final TextBox oldPasswordTextBox;

    @UiField(provided=true)
    final TextBox newPasswordTextBox;

    @UiField(provided=true)
    final Button saveAccountSettingsButton;

    @UiField(provided=true)
    final Anchor removeProfileAnchor;


    @UiField(provided=true)
    final TextBox smsPhoneNumberTextBox;
    public AccountSettingsComposite(TextBox usernameTextBox,TextBox emailAddressTextBox,TextBox oldPasswordTextBox,TextBox newPasswordTextBox,
                                    TextBox smsPhoneNumberTextBox, Button saveAccountSettingsButton,Anchor removeProfileAnchor  ) {
        this.usernameTextBox = usernameTextBox;
        this.emailAddressTextBox = emailAddressTextBox;
        this.oldPasswordTextBox = oldPasswordTextBox;
        this.newPasswordTextBox = newPasswordTextBox;
        this.smsPhoneNumberTextBox = smsPhoneNumberTextBox;
        this.saveAccountSettingsButton = saveAccountSettingsButton;
        this.removeProfileAnchor = removeProfileAnchor;

//initWidget(uiBinder.createAndBindUi(this));

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }


    }


}
