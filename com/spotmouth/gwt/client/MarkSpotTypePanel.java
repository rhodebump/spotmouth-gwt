package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.TagHolder;
import com.spotmouth.gwt.client.help.HelpResources;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DropPanel;

import java.util.ArrayList;
import java.util.List;

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
            multiUploader.addOnFinishUploadHandler(onFinishUploaderHandler3);
            ULPanel pickSpotULPanel = getPickSpotULPanel();


            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            Button leaveMarkButton = new Button();
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            MarkData markData = new MarkData();
            markData.saySomethingTextArea = contentTextArea;
            markData.tagSearchTextBox = tagSearchTextBox;
            ExpandData expandData = new ExpandData();
            markData.expandData = expandData;
            widgetMarkDataMap.put(leaveMarkButton, markData);
            leaveMarkButton.addClickHandler(saveHandler2);
            FormPanel myFormPanel = new FormPanel();
            setupRootPanelForm(myFormPanel, markData);



            Button shareOnFacebookButton =   getFacebookButton(markData);
            Button addTagButton = new Button();
            addTagButton.addClickHandler(addTagHandler);
            PlateSearchComposite plateSearchComposite = new PlateSearchComposite(plateSearchPanel.colorsListBox, plateSearchPanel.plateNameTextBox,
                    plateSearchPanel.keywordsTextBox, plateSearchPanel.manufacturersListBox, plateSearchPanel.stateTextBox, plateSearchPanel.vehicleTypeListBox,
                    plateSearchPanel.plateSearchButton, markData.tagSearchTextBox,
                    markData.secretKeyTextBox, markData.saySomethingTextArea, selectedTagsPanel, leaveMarkButton, multiUploader, panelImages,
                    pickSpotULPanel, mywebapp, shareOnFacebookButton, addTagButton,suggestionsPanel);
            plateSearchComposite.tab2.setValue(true);
            myFormPanel.setWidget(plateSearchComposite);
            add(myFormPanel);
        }

    }

;

    public void toggleFirst() {
    }

    public boolean isLoginRequired() {
        return false;
    }
}
