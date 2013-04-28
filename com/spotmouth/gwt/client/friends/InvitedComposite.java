package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/16/13
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvitedComposite extends Composite {


    @UiTemplate("InvitedComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, InvitedComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("InvitedCompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, InvitedComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);






    @UiField(provided = true)
    final Button inviteFriendButton;


    @UiField(provided = true)
    final ULPanel invitedUL;


    public InvitedComposite( Button inviteFriendButton,ULPanel invitedUL) {
        this.inviteFriendButton = inviteFriendButton;
        this.invitedUL = invitedUL;



        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }


    }


}

