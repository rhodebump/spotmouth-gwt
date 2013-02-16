package com.spotmouth.gwt.client.product;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
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

    private ULPanel getInstalledProductsULPanel(SpotHolder spotHolder) {
        ULPanel ulPanel = new ULPanel();
        for (ProductInstallHolder productInstallHolder : spotHolder.getProductInstallHolders()) {
            ListItem listItem = new ListItem();

            /*
                  				<li><b class="_emails"></b><span>Emails</span></li>
      				<li class="_waiting" title="Installing product, please wait..."><b class="_acc"></b><span>Front Accounting</span></li>
             */

            ProductHolder productHolder = productInstallHolder.getProductHolder();


            if (! productInstallHolder.isInstalled()) {

                listItem.addStyleName("_waiting");
                listItem.setTitle("Installing product, please wait...");
            }


            InlineLabel inlineLabel1 = new InlineLabel();
            inlineLabel1.setStyleName("_" + productHolder.getHostSuffix());

            listItem.add(inlineLabel1);

            InlineLabel inlineLabel = new InlineLabel(productHolder.getName());

            listItem.add(inlineLabel);


            productInstallClickMap.put(listItem, productInstallHolder);
            listItem.addClickHandler(pickProductInstallHandler);



        }

        return ulPanel;
    }
    private  ULPanel getAvailableProductsULPanel() {
        ULPanel ulPanel = new ULPanel();
        for (ProductHolder productHolder : mywebapp.getProductHolders()) {
            ListItem listItem = new ListItem();
           /*
                  				<li><b class="_poll"></b><span>Create Poll</span></li>
      				<li><b class="_acc"></b><span>Front Accounting</span></li>
      				<li><b class="_lime"></b><span>LimeSurvei</span></li>
      				<li><b class="_wiki"></b><span>Mediawiki</span></li>
      				<li><b class="_wordpress"></b><span>WordPress</span></li>
      				<li><b class="_html"></b><span>Static HTML</span></li>
      				<li><b class="_sphider"></b><span>Sphider</span></li>
      				<li><b class="_phplist"></b><span>phpList</span></li>
      				<li><b class="_emails"></b><span>Emails</span></li>
             */
            InlineLabel inlineLabel1 = new InlineLabel();
            inlineLabel1.setStyleName("_" + productHolder.getHostSuffix());

            listItem.add(inlineLabel1);

            InlineLabel inlineLabel = new InlineLabel(productHolder.getName());

            listItem.add(inlineLabel);
            productClickMap.put(listItem, productHolder);


            listItem.addClickHandler(pickProductHandler);

            ulPanel.add(listItem);
        }

        return ulPanel;


    }

    public ManageProductsPanel(MyWebApp mywebapp, SpotHolder spotHolder) {
            super(mywebapp);
            this.spotHolder = spotHolder;

        if (MyWebApp.isDesktop()) {
            ULPanel availableProductsULPanel = getAvailableProductsULPanel();



            ULPanel installedProductsULPanel = getInstalledProductsULPanel(spotHolder);

            ManageProductsComposite mpc = new ManageProductsComposite(availableProductsULPanel,installedProductsULPanel);
            mpc.setName(spotHolder.getName());
            mpc.setLocation(spotHolder.getAddressLabel());

            add(mpc);
            return;
        }


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
