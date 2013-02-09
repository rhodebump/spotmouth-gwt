package com.spotmouth.gwt.client.spot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ContactPanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.ProductHolder;
import com.spotmouth.gwt.client.dto.ProductInstallHolder;
import com.spotmouth.gwt.client.dto.ProductInstallRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/8/12
 * Time: 7:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductInstallPanel extends SpotBasePanel implements SpotMouthPanel {
    private void resendEmails() {
        ProductInstallRequest pir = new ProductInstallRequest();
        pir.setProductInstallHolder(productInstallHolder);
        pir.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.resendEmails(pir, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //lets go back
                    //mywebapp.toggleBack();
                    getMessagePanel().displayMessage("Product Activation Emails to each administrator of this spot.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void requestSupport() {
        ContactPanel contactPanel = new ContactPanel(mywebapp);
        mywebapp.swapCenter(contactPanel);
    }

    ClickHandler resendEmailsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                resendEmails();
            }
        }
    };
    ClickHandler requestSupportHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                requestSupport();
            }
        }
    };

    private Label resendEmailsButton() {
        Label btn = new Label("Resend Emails");
        btn.addClickHandler(resendEmailsHandler);
        fixButton(btn);
        //ImageResource ir = MyWebApp.resources.save();
        //Image img = new Image(ir);
        //btn.getElement().appendChild(img.getElement());
        return btn;
    }

    private Label requestSupportButton() {
        Label btn = new Label("Request Support");
        btn.addClickHandler(requestSupportHandler);
        fixButton(btn);
        //ImageResource ir = MyWebApp.resources.save();
        //Image img = new Image(ir);
        //btn.getElement().appendChild(img.getElement());
        return btn;
    }

    private ProductInstallHolder productInstallHolder = null;

    public ProductInstallPanel(MyWebApp myWebApp, ProductInstallHolder productInstallHolder) {
        super(myWebApp);
        this.productInstallHolder = productInstallHolder;
        //product info
        ProductHolder productHolder = productInstallHolder.getProductHolder();
        Label nameLabel = new HTML(productHolder.getName());
        addFieldset(nameLabel, "Product", "desc");
        HTML description = new HTML(productHolder.getDescription());
        addFieldset(description, "Description", "desc");
        //url
        Anchor anchor = new Anchor(productInstallHolder.getProductUrl(), false, productInstallHolder.getProductUrl());
        add(anchor);
        //create date
        Label createDate = new Label(productInstallHolder.getCreateDate().toString());
        addFieldset(createDate, "Created At", "xx");
        //resend emails
        add(resendEmailsButton());
        //request support
        add(requestSupportButton());
        add(cancelButton());
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
