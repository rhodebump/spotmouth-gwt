package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/16/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvitedComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, InvitedComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);



    @UiField(provided = true)
    final Button inviteFriendButton;


    @UiField(provided = true)
    final ULPanel invitedUL;


    public InvitedComposite( Button inviteFriendButton,ULPanel invitedUL) {
        this.inviteFriendButton = inviteFriendButton;
        this.invitedUL = invitedUL;


        initWidget(uiBinder.createAndBindUi(this));
    }


}

