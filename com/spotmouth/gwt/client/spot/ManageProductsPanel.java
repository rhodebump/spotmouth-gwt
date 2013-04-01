package com.spotmouth.gwt.client.spot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ProductHolder;
import com.spotmouth.gwt.client.dto.ProductInstallHolder;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.help.HelpResources;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 9/9/12
 * Time: 5:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageProductsPanel  extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Manage Products";
    }

    protected ClickHandler pickProductHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                ProductHolder productHolder = productClickMap.get(b);
                ProductPanel productPanel = new ProductPanel(mywebapp, spotHolder, productHolder);
                mywebapp.swapCenter(productPanel);
            }
        }
    };
    private SpotHolder spotHolder = null;
    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getPlaceForm();
    }

    public ManageProductsPanel(MyWebApp mywebapp, SpotHolder spotHolder) {
            super(mywebapp);
            this.spotHolder = spotHolder;

                //list product installs
        addHeader("Installed Products");
        if (spotHolder.getProductInstallHolders().isEmpty()) {
            add(new Label("No product installations at this time"));
        }
        ULPanel ulPanel = new ULPanel();
        add(ulPanel);
        for (ProductInstallHolder productInstallHolder : spotHolder.getProductInstallHolders()) {
            ListItem li = new ListItem();
            ulPanel.add(li);
            ProductHolder productHolder = productInstallHolder.getProductHolder();
            //li.setStyleName("clearing");
            FlowPanel fp = new FlowPanel();
            li.add(fp);
            Label productNameLabel = new Label(productHolder.getName());
            fp.add(productNameLabel);
            productInstallClickMap.put(productNameLabel, productInstallHolder);
            productNameLabel.addClickHandler(pickProductInstallHandler);
            Anchor anchor = new Anchor(productInstallHolder.getProductUrl(), false, productInstallHolder.getProductUrl());
            fp.add(anchor);

            //add(anchor);
            //addFieldset(anchor, "Spotmouth-Powered Website", "na");
        }
        //list products
        addHeader("Available Products");
        ULPanel ulPanel2 = new ULPanel();
        ulPanel2.setStyleName("AvailableProducts");
        add(ulPanel2);
        for (ProductHolder productHolder : mywebapp.getProductHolders()) {
            ListItem li = new ListItem();
            ulPanel2.add(li);
            //li.setStyleName("clearing");
            FlowPanel fp = new FlowPanel();
            li.add(fp);
            Label productNameLabel = new Label(productHolder.getName());
            productNameLabel.addStyleName("linky");
            fp.add(productNameLabel);
            productClickMap.put(productNameLabel, productHolder);

            HTML productDescLabel = new HTML(productHolder.getDescription());
            productDescLabel.addStyleName("linky");
            fp.add(productDescLabel);
            productDescLabel.addClickHandler(pickProductHandler);

            productClickMap.put(productDescLabel, productHolder);
            productNameLabel.addClickHandler(pickProductHandler);
        }


        addSpotLink(spotHolder);
    }



}
