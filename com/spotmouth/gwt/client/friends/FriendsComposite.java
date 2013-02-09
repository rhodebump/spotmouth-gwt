package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/15/13
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class FriendsComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, FriendsComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);



    @UiField(provided = true)
    final ULPanel friendsULPanel;


    @UiField(provided = true)
    final Button inviteFriendButton;


    public FriendsComposite(ULPanel friendsULPanel, Button inviteFriendButton) {
        this.friendsULPanel    = friendsULPanel;
         this.inviteFriendButton = inviteFriendButton;

        initWidget(uiBinder.createAndBindUi(this));
    }


    @UiField
        SpanElement friendCountSpan;

    public void setFriendCount(int friendCount) {
        friendCountSpan.setInnerText(new Integer(friendCount).toString());
    }



}
