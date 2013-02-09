package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ContactRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 12/29/11
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactPanel extends SpotBasePanel implements SpotMouthPanel {


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.contactMobile();
        } else {
            return MyWebApp.resources.contact();
        }

    }


    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getContactPanel();
    }

    public String getTitle() {
        return "Contact Spotmouth";
    }

    public ContactPanel() {
    }

    TextBox messageSubject = new TextBox();
    TextBox yourName = new TextBox();
    TextBox email = new TextBox();

    public ContactPanel(MyWebApp mywebapp) {
        super(mywebapp);

        if (MyWebApp.isDesktop()) {
            addRequired(yourName);

            //placeholder="example@mail.com"
            addRequired(email);
            email.getElement().setAttribute("placeholder","example@mail.com");

            Button sendButton = new Button("send");
            sendButton.addClickHandler(saveHandler);
            contentTextArea.setVisibleLines(8);
            ContactComposite contactComposite = new ContactComposite(yourName,email,messageSubject,sendButton,contentTextArea);
            add(contactComposite);

            return;
        }

        addFieldset("Contact Us", "We would really love to hear from you.");


        yourName = addTextBox("Your Name*", "name", "");
        email = addTextBox("Your Email Address*", "email", "");
        messageSubject = addTextBox("Message Subject*", "es", "");
        contentTextArea = addTextArea("Your Message*", "message", "", false);
        Label btn = new Label("Send Message");
        btn.addClickHandler(saveHandler);
        fixButton(btn);
        add(btn);
        add(cancelButton());
    }

    protected boolean isValid() {
        getMessagePanel().clear();
        if (isEmpty(yourName)) {
            getMessagePanel().displayError("Name is required.");
        }
        if (isEmpty(messageSubject)) {
            getMessagePanel().displayError("Message Subject is required.");
        }

        if (isEmpty(email)) {
            getMessagePanel().displayError("Your email address is required.");
        }
        if (isEmpty(contentTextArea)) {
            getMessagePanel().displayError("A message is required.");
        }
        if (getMessagePanel().isHaveMessages()) {
            return false;
        }
        return true;
    }
//    @Override
//    public boolean isRootPanel() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public void toggleFirst() {
        yourName.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void doSave() {
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName(yourName.getValue());
        contactRequest.setEmail(email.getValue());
        contactRequest.setSubject(messageSubject.getValue());
        contactRequest.setMessage(contentTextArea.getValue());
        contactRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.contact(contactRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.toggleBack();
                    //mywebapp.displayMessage("Your message has been sent.");
                    mywebapp.getMessagePanel().displayMessage("Your message has been sent.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
