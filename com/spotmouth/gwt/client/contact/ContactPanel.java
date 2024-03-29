package com.spotmouth.gwt.client.contact;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.ContactRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 12/29/11
 * Time: 10:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContactPanel extends SpotBasePanel implements SpotMouthPanel {





    public String getTitle() {
        return "Contact Spotmouth";
    }

    public ContactPanel() {
    }

    TextBox messageSubject = new TextBox();
    TextField yourName = new TextField();
    TextField email = new TextField();

    public ContactPanel(MyWebApp mywebapp) {
        super(mywebapp);


            addRequired(yourName);

            //placeholder="example@mail.com"
            //addRequired(email);
           // email.getElement().setAttribute("placeholder","example@mail.com");

            Button sendButton = new Button("send");
            sendButton.addClickHandler(saveHandler);
            contentTextArea.setVisibleLines(8);
            ContactComposite contactComposite = new ContactComposite(yourName,email,messageSubject,sendButton,contentTextArea);
            add(contactComposite);


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
        } else {
            boolean valid = email.getValue().matches(emailPattern);
            if (!valid) {
                getMessagePanel().displayError("Email address " + email.getValue() + " is  not invalid.");
            }

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

                    //we need to clear out this form
                    //History.newItem(MyWebApp.CONTACT);

                    mywebapp.toggleContact();
                    mywebapp.getMessagePanel().displayMessage("Your message has been sent.");
                    //toggleMenu


                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
