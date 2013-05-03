package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.*;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DropPanel;

//3 buttons to choose type of spot to mark
public class MarkSpotTypePanel extends SpotBasePanel implements SpotMouthPanel {
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
        PlateSearchPanel plateSearchPanel = new PlateSearchPanel(mywebapp);
        this.defaultUploader = new MultiUploader();
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler3);

        //one panel is used for desktop, other for mobile
        ULPanel pickSpotULPanel = new ULPanel();
        FlowPanel pickSpotPanel =  new FlowPanel();
        if (MyWebApp.isDesktop()) {
            getPickSpotULPanel(pickSpotULPanel);
        }else {
            getPickSpotMobile(pickSpotPanel);
        }




        SuggestBox tagSearchTextBox = getTagSuggestBox(null);
        Button leaveMarkButton = new Button();
        FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
        FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
        MarkData markData = new MarkData();
        markData.saySomethingTextArea = contentTextArea;
        markData.tagSearchTextBox = tagSearchTextBox;
        ExpandData expandData = new ExpandData();
        markData.expandData = expandData;
        markData.spotDescriptionTextArea = spotDescriptionTextArea;
        widgetMarkDataMap.put(leaveMarkButton, markData);
        leaveMarkButton.addClickHandler(saveHandler2);
        FormPanel myFormPanel = new FormPanel();
        setupRootPanelForm(myFormPanel, markData);
        Button shareOnFacebookButton = getFacebookButton(markData);
        Button addTagButton = new Button();
        addTagButton.addClickHandler(addTagHandler);
        DropPanel dropPanel = getDropPanel();


        PlateSearchComposite plateSearchComposite = new PlateSearchComposite(plateSearchPanel.colorsListBox, plateSearchPanel.plateNameTextBox,
                plateSearchPanel.keywordsTextBox, plateSearchPanel.manufacturersListBox, plateSearchPanel.stateTextBox, plateSearchPanel.vehicleTypeListBox,
                plateSearchPanel.plateSearchButton, markData.tagSearchTextBox,
                markData.secretKeyTextBox, markData.saySomethingTextArea, selectedTagsPanel, leaveMarkButton, defaultUploader, panelImages,
                pickSpotULPanel, mywebapp, shareOnFacebookButton, addTagButton, suggestionsPanel, markData.spotDescriptionTextArea, dropPanel, plateSearchPanel.countryTextBox,pickSpotPanel);
        plateSearchComposite.tab2.setValue(true);
        myFormPanel.setWidget(plateSearchComposite);
        add(myFormPanel);
    }

    public void toggleFirst() {
    }





}
