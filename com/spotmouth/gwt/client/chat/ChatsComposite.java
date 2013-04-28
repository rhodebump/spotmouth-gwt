package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/18/13
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChatsComposite extends Composite {




    @UiField(provided=true)
    final Button createChatButton;



    @UiField(provided=true)
    final ComplexPanel chatResults;

    @UiTemplate("ChatsComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, ChatsComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("ChatsCompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, ChatsComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);

    public ChatsComposite(Button createChatButton,ComplexPanel chatResults) {
        this.createChatButton = createChatButton;
        this.chatResults = chatResults;

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }



    }



}
