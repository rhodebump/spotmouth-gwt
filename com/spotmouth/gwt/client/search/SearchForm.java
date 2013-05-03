package com.spotmouth.gwt.client.search;
//import com.spotmouth.commons.AuthenticationResponse;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;

public class SearchForm extends SpotBasePanel implements SpotMouthPanel {
    public SearchForm() {
    }



    public String getTitle() {
        return "Search";
    }

    public SearchForm(MyWebApp mywebapp) {
        super(mywebapp, false);

    keywordsTextBox.addKeyDownHandler(new KeyDownHandler() {
        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                performSearch();
            }
        }
    });
    geoFilterCheckbox = new SimpleCheckBox();
    geoFilterCheckbox.setName("filtering");
    spotFilterCheckbox = new SimpleCheckBox();
    spotFilterCheckbox.setName("filtering");
    plateFilterCheckbox = new SimpleCheckBox();
    plateFilterCheckbox.setName("filtering");
    markFilterCheckbox = new SimpleCheckBox();
    markFilterCheckbox.setName("filtering");
    contestFilterCheckbox = new SimpleCheckBox();
    contestFilterCheckbox.setName("filtering");

    Button searchButton = new Button();
    searchButton.addClickHandler(searchHandler);
    SuggestBox tagSearchTextBox = getTagSuggestBox(null);
    FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
    FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

    initVehicleType("");
    initColorListBox("");
    initManufacturersListBox(null);


    SearchFormComposite sfc = new SearchFormComposite(keywordsTextBox, geoFilterCheckbox, spotFilterCheckbox, plateFilterCheckbox,
            markFilterCheckbox, contestFilterCheckbox, tagSearchTextBox, selectedTagsPanel, vehicleTypeListBox,colorsListBox, manufacturersListBox,searchButton,suggestionsPanel);
    add(sfc);

    }




    public void toggleFirst() {
        keywordsTextBox.setFocus(true);
    }


}
