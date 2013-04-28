package com.spotmouth.gwt.client.contact;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/18/12
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactComposite extends Composite {

    @UiTemplate("ContactComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, ContactComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("MContactComposite.ui.xml")
    interface MobileBinder extends UiBinder<Widget, ContactComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);

    //private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final TextField yourNameTextBox;
    @UiField(provided = true)
    final TextField email;
    @UiField(provided = true)
    final TextBox messageSubject;
    @UiField(provided = true)
    final Button sendButton;
    @UiField(provided = true)
    final TextArea contentTextArea;

    public ContactComposite(TextField yourNameTextBox, TextField email, TextBox messageSubject, Button sendButton, TextArea contentTextArea) {
        this.yourNameTextBox = yourNameTextBox;
        this.email = email;
        this.messageSubject = messageSubject;
        this.sendButton = sendButton;
        this.contentTextArea = contentTextArea;
        //initWidget(uiBinder.createAndBindUi(this));

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }



    }
}
