package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.*;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.LocationResult;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.dto.Location;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;

//3 buttons to choose type of spot to mark
public class MarkSpotTypePanel extends SpotBasePanel implements SpotMouthPanel {

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.markspotMobile();
        } else {
            return MyWebApp.resources.markspot();
        }
    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getMarkSpotTypePanel();
    }

    public String getTitle() {
        return "Mark Spot";
    }

    public MarkSpotTypePanel() {
    }


    @Override
    public String getBodyClassId() {

        return "mark_address";
    }



    /*
   changing the following to not have the callback defined now, we will define later
    */

    @Override
    protected void doSave(MarkData markData) {
        saveStatus(markData);
    }

    public MarkSpotTypePanel(MyWebApp mywebapp) {
        super(mywebapp);
        if (MyWebApp.isDesktop()) {
            PlateSearchPanel plateSearchPanel = new PlateSearchPanel(mywebapp);
            MultiUploader multiUploader = new MultiUploader();
            FlowPanel panelImages3 = new FlowPanel();
            IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandler(panelImages3);
            multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            ULPanel pickSpotULPanel = getPickSpotULPanel();
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            Button leaveMarkButton = new Button();
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            MarkData markData = new MarkData();
            markData.saySomethingTextArea = contentTextArea;
            markData.tagSearchTextBox = tagSearchTextBox;
            ExpandData expandData = new ExpandData();
            markData.expandData = expandData;
            widgetMarkDataMap.put(leaveMarkButton, markData);
            leaveMarkButton.addClickHandler(saveHandler2);
            //FlowPanel flowPanel = new FlowPanel();
            FormPanel myFormPanel = new FormPanel();
            setupRootPanelForm(myFormPanel, markData);

            Button shareOnFacebookButton =   getFacebookButton(markData);
            Button addTagButton = new Button("Add");
            addTagButton.addClickHandler(addTagHandler);
            PlateSearchComposite plateSearchComposite = new PlateSearchComposite(plateSearchPanel.colorsListBox, plateSearchPanel.plateNameTextBox,
                    plateSearchPanel.keywordsTextBox, plateSearchPanel.manufacturersListBox, plateSearchPanel.stateTextBox, plateSearchPanel.vehicleTypeListBox,
                    plateSearchPanel.plateSearchButton, markData.tagSearchTextBox,
                    markData.secretKeyTextBox, markData.saySomethingTextArea, selectedTagsPanel, leaveMarkButton, multiUploader, panelImages3, pickSpotULPanel, mywebapp, shareOnFacebookButton, addTagButton);
            plateSearchComposite.tab2.setValue(true);
            // flowPanel.add(plateSearchComposite);
            myFormPanel.setWidget(plateSearchComposite);
            add(myFormPanel);
            return;
        }
        ULPanel ulPanel = new ULPanel();
        ulPanel.setStyleName("hint");
        add(ulPanel);
        {
            ListItem listItem = new ListItem();
            listItem.add(new Label("Choose Mark Address for places like restaurants, stores and places that have street addresses"));
            ulPanel.add(listItem);
        }
        {
            ListItem listItem = new ListItem();
            listItem.add(new Label("Choose Mark Plate for vehicles, cars, trucks."));
            ulPanel.add(listItem);
        }
        {
            ListItem listItem = new ListItem();
            listItem.add(new Label("Choose Mark Location for places without a mailing address, like a park bench, a beach, or if you are just stuck in traffic."));
            ulPanel.add(listItem);
        }
        //add(ulPanel);
        FlowPanel fp = new FlowPanel();
        fp.setStyleName("menugrouping");
        fp.addStyleName("clearing");
        //Label Personal = new Label("Personal");
        //Personal.setStyleName("h2");
        add(fp);
        addImageIcon(markPlaceHandler, new MarkSpotPanel(), fp);
        addImageIcon(MyWebApp.MARK_PLATE, new PlateSearchPanel(), fp);
        //   addImageIcon(markCurrentLocationHandler, new UserProfileForm(), fp);
        //link.getElement().getFirstChild().appendChild(image.getElement());
        //Hyperlink markLocationHyperLink = new Hyperlink("Mark Location",MyWebApp.MARK_LOCATION);
        addImageIcon(MyWebApp.MARK_LOCATION, new MarkLocationForm(), fp);
        addFieldset(ulPanel, "Hint", "na");
    }

    ClickHandler markPlaceHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleMarkPlace();
        }
    };

    public void toggleFirst() {
    }

    public boolean isLoginRequired() {
        return false;
    }
}
