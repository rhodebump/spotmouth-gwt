package com.spotmouth.gwt.client.search;
//import com.spotmouth.commons.AuthenticationResponse;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleCheckBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.TagHolder;

import java.util.ArrayList;

public class SearchForm extends SpotBasePanel implements SpotMouthPanel {
    public SearchForm() {
    }

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.searchMobile();
        } else {
            return MyWebApp.resources.search();
        }
    }

    public String getTitle() {
        return "Search";
    }

    public SearchForm(MyWebApp mywebapp) {
        super(mywebapp, false);
        if (MyWebApp.isDesktop()) {
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
            searchButton.setStyleName("btn_blue");
            searchButton.addClickHandler(searchHandler);
            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            //tagSearchTextBox.getElement().setId("mc_tags_inp");
            tagSearchTextBox.getElement().setAttribute("placeholder", "Start typing");
            // tagSearchTextBox.setTabIndex(11);
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);

            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            initVehicleType("");
            initColorListBox("");
            initManufacturersListBox(null);


            SearchFormComposite sfc = new SearchFormComposite(keywordsTextBox, geoFilterCheckbox, spotFilterCheckbox, plateFilterCheckbox,
                    markFilterCheckbox, contestFilterCheckbox, tagSearchTextBox, selectedTagsPanel, vehicleTypeListBox,colorsListBox, manufacturersListBox,searchButton,suggestionsPanel);
            add(sfc);
            return;
        }
        this.addStyleName("SearchForm");
        keywordsTextBox = addTextBox("Keywords", "keywords", "");
        // Listen for keyboard events in the input box.
        keywordsTextBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    performSearch();
                }
            }
        });
        addTagHolderForm(new ArrayList<TagHolder>());
        FlowPanel checkBoxPanel = new FlowPanel();
        //"Filter by distance"
        geoFilterCheckbox = new SimpleCheckBox();
        checkBoxPanel.add(geoFilterCheckbox);
        //geoFilterCheckbox = addCheckbox2("Filter by distance", "geo1", true, null);
        // spotFilterCheckbox = addCheckbox2("Spots", "sdd", true, null);
        spotFilterCheckbox = new SimpleCheckBox();
        //("Spots");
        checkBoxPanel.add(spotFilterCheckbox);
        // plateFilterCheckbox = addCheckbox2("Plates", "geo2", true, null);
        plateFilterCheckbox = new SimpleCheckBox();
        //"Plates"
        checkBoxPanel.add(plateFilterCheckbox);
        //markFilterCheckbox  = addCheckbox2("Marks", "geo3", true, null);
        markFilterCheckbox = new SimpleCheckBox();
        checkBoxPanel.add(markFilterCheckbox);
        //contestFilterCheckbox  = addCheckbox2("Contests", "geo3", true, null);
        contestFilterCheckbox = new SimpleCheckBox();
        checkBoxPanel.add(contestFilterCheckbox);
        addFieldset(checkBoxPanel, "Filtering", "na");
        addColorListBox("");
        addManufacturersListBox(null);
        addVehicleType("");
        //add geofilter checkbox, default to on
        add(searchButton());
        add(clearSearchButton());
    }

    Label clearSearchButton() {
        Label btn = new Label("Reset");
        btn.addClickHandler(clearSearchHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler clearSearchHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            keywordsTextBox.setValue("");
        }
    };

    public void toggleFirst() {
        keywordsTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        // TODO Auto-generated method stub
        return false;
    }
}
