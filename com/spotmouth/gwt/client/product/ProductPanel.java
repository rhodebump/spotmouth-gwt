package com.spotmouth.gwt.client.product;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyHtmlResources;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/7/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
//user picked product, this will allow them to request it
public class ProductPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Product Install";
    }

    TextField domainNameTextBox = new TextField();
    TextField hostNameTextBox = new TextField();

    protected boolean isValid() {
        checkRequired(domainNameTextBox, "Domain is required");
        checkRequired(hostNameTextBox, "Hostname is required");
        if (!acceptTerms.getValue()) {
            getMessagePanel().displayError("You must accept the terms to get your website");
        }
        return (!getMessagePanel().isHaveMessages());
    }

    public String generateHostname() {
        String name = spotHolder.getName();
        String hostname = name.replaceAll("[^a-zA-Z0-9]", "-");
        return hostname + "-" + productHolder.getHostSuffix();
    }

    SimpleCheckBox acceptTerms = new SimpleCheckBox();
    private SpotHolder spotHolder = null;
    private ProductHolder productHolder = null;

    public ProductPanel(MyWebApp myWebApp, SpotHolder spotHolder, ProductHolder productHolder) {
        super(myWebApp);
        this.spotHolder = spotHolder;
        this.productHolder = productHolder;
        String hostName = generateHostname();
        hostNameTextBox.setValue(hostName);
        domainNameTextBox.setValue("spotmouth.com");
        Button activateButton = new Button();
        activateButton.addClickHandler(saveHandler);
        ProductComposite pic = new ProductComposite(hostNameTextBox, domainNameTextBox, activateButton, acceptTerms);
        pic.setProductClass("_" + productHolder.getHostSuffix());
        pic.setProductDescription(productHolder.getDescription());
        pic.setSpotName(spotHolder.getName());
        add(pic);
    }

    AsyncCallback productInstalledCallback = new AsyncCallback<MobileResponse>() {
        public void onFailure(Throwable throwable) {
        }

        public void onSuccess(MobileResponse response) {
            getMessagePanel().displayMessage("Your product request has been submitted.  An email will be sent within the next hour when the installation has been completed.");
        }
    };

    protected void doSave() {
        ProductInstallRequest pir = new ProductInstallRequest();
        ProductInstallHolder productInstallHolder = pir.getProductInstallHolder();
        productInstallHolder.setDomainName(domainNameTextBox.getValue());
        productInstallHolder.setHostName(hostNameTextBox.getValue());
        productInstallHolder.setSpotId(spotHolder.getId());
        productInstallHolder.setProductHolder(productHolder);
        pir.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveProductInstall(pir, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setHomeCallback(productInstalledCallback);
                    History.newItem(MyWebApp.MANAGE_SPOT + spotHolder.getId());
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public void toggleFirst() {
    }
}
