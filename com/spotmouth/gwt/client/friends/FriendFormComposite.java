package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;


/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/15/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class FriendFormComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, FriendFormComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    final TextBox emailAddressTextBox;

    @UiField(provided = true)
    final TextBox smsPhoneNumberBox;

    @UiField(provided = true)
    final Button inviteFriendButton;

    @UiField(provided = true)
    final TextBox friendNameTextBox;

    @UiField(provided = true)
    final TextArea friendJoinMessageTextArea;


    public FriendFormComposite( TextBox emailAddressTextBox,TextBox smsPhoneNumberBox,Button inviteFriendButton,TextBox friendNameTextBox,TextArea friendJoinMessageTextArea) {

        this.emailAddressTextBox = emailAddressTextBox;

        this.smsPhoneNumberBox = smsPhoneNumberBox;
        this.inviteFriendButton = inviteFriendButton;
        this.friendNameTextBox = friendNameTextBox;
        this.friendJoinMessageTextArea = friendJoinMessageTextArea;


        initWidget(uiBinder.createAndBindUi(this));
    }


}
