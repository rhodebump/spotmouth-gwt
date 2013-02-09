package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyHtmlResources;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import sun.nio.cs.HistoricallyNamedCharset;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/7/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
//user picked product, this will allow them to request it
public class ProductPanel extends SpotBasePanel implements SpotMouthPanel {
    TextBox domainNameTextBox = null;
    TextBox hostNameTextBox = null;

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




    CheckBox acceptTerms = null;
    private SpotHolder spotHolder = null;
    private ProductHolder productHolder = null;

    public ProductPanel(MyWebApp myWebApp, SpotHolder spotHolder, ProductHolder productHolder) {
        super(myWebApp);
        this.spotHolder = spotHolder;
        this.productHolder = productHolder;
        Label nameLabel = new HTML(productHolder.getName());
        addFieldset(nameLabel, "Product", "desc");

        if (! isEmpty(productHolder.getDescription())) {
            HTML description = new HTML(productHolder.getDescription());
            addFieldset(description, "Description", "desc");

        }




        ScrollPanel sp = new ScrollPanel();
        sp.setHeight("200px");
        String html = MyHtmlResources.INSTANCE.getTermsHtml().getText();
        Label myLabel = new Label("Terms & Conditions");
        myLabel.setStyleName("h1");
        sp.setWidget(new HTML(html));
        add(myLabel);
        add(sp);




        this.acceptTerms = new CheckBox("By checking this box, I accept the above terms and conditions.");
        acceptTerms.setName("acceptTerms");
        //add(acceptTerms);
        addFieldset(acceptTerms, "", "Na");


         advancedSettingsPanel = new VerticalPanel();

         settingsPanel = new VerticalPanel();
        settingsPanel.setWidth("100%");

        advancedSettingsPanel.add(hideAdvancedButton());
        Label help = new Label("The following is a recommended hostname and domain that will be used for your product.  Don't change this value unless you know what you are doing.");
        advancedSettingsPanel.add(help);
        String hostName = generateHostname();
        this.hostNameTextBox = addTextBox("Hostname", "na", hostName,false,advancedSettingsPanel);
        this.domainNameTextBox = addTextBox("Domain", "na", "spotmouth.com",false,advancedSettingsPanel);

        settingsPanel.add(showAdvancedButton());

        add(settingsPanel);

        //if this is a zipfile, need to provide a file upload
        if (productHolder.isUpload()) {
            addMediaFields("Upload zipfile");
        }
        add(createButton());
        add(cancelButton());
    }

    VerticalPanel settingsPanel = null;
    VerticalPanel advancedSettingsPanel  = null;

    protected Label showAdvancedButton() {
        Label btn = new Label("Show Advanced Settings");
        addImageToButton(btn,MyWebApp.resources.advancedOptionsButton(),MyWebApp.resources.advancedOptionsButtonMobile());
        btn.addClickHandler(showAdvancedHandler);
        fixButton(btn);
        return btn;
    }

    protected Label hideAdvancedButton() {
        Label btn = new Label("Hide Advanced Settings");
        addImageToButton(btn,MyWebApp.resources.advancedOptionsButton(),MyWebApp.resources.advancedOptionsButtonMobile());
        btn.addClickHandler(hideAdvancedHandler);
        fixButton(btn);
        return btn;
    }



    ClickHandler hideAdvancedHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            settingsPanel.clear();
            settingsPanel.add(showAdvancedButton());

        }
    };


    ClickHandler showAdvancedHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            settingsPanel.clear();
            settingsPanel.add(advancedSettingsPanel);

        }
    };


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

    public boolean isLoginRequired() {
        return false;
    }

    public Label createButton() {
        Label btn = new Label("Activate Product");
        btn.addClickHandler(saveHandler);
        fixButton(btn);
        addImageToButton(btn,MyWebApp.resources.saveButton(),MyWebApp.resources.saveButtonMobile());
        return btn;
    }
}
