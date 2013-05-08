package com.spotmouth.gwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.menu.TopNav;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/29/12
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Page extends Composite {
    interface MyUiBinder extends UiBinder<Widget, Page> {
    }


    @UiTemplate("Page.ui.xml")
    interface PageBinder extends UiBinder<Widget, Page> {
    }

    private static PageBinder desktopBinder = GWT.create(PageBinder.class);


    @UiField(provided = true)
    Button menuButton;


    //@UiField ListBox listBox;
    @UiField
    SimplePanel body;

    public void setId2(String id2) {
        body.getElement().setId(id2);
    }

    @UiField(provided = true)
    Button searchButton;


    @UiField
    SimplePanel messages;
//    @UiField
//    SimplePanel searchBox;


    @UiField(provided = true)
    final TextBox keywordsTextBox;
    @UiField(provided = true)
    final TextBox locationTextBox;


    @UiField(provided = true)
    SimpleCheckBox toggleMilesCheckBox;
    @UiField(provided = true)
    SimpleCheckBox toggleMapMode;


    @UiField(provided = true)
    final ULPanel previousLocationsULPanel;
    @UiField(provided = true)
    final Button markSpotButton;

    @UiField(provided = true)
    final ListBox tagListBox;

    @UiField(provided = true)
    final ListBox sortingListBox;

    @UiField(provided = true)
    TopNav topNav;


    //simplePanel,popularPanel,latestPanel,mp,tagCloudPanel,locationPanel,tagCloudPanel2,googleMapPanel
    public Page(Panel bodyPanel, Panel messagePanel, Panel searchBoxPanel, TextBox keywordsTextBox, TextBox locationTextBox, ULPanel previousLocationsULPanel,
                SimpleCheckBox toggleMilesCheckBox, SimpleCheckBox toggleMapMode, Button markSpotButton, ListBox tagListBox,
                ListBox sortingListBox, Button searchButton,
                TopNav topNav, Button menuButton) {
        this.keywordsTextBox = keywordsTextBox;

        this.markSpotButton = markSpotButton;
        this.locationTextBox = locationTextBox;
        this.previousLocationsULPanel = previousLocationsULPanel;
        this.toggleMapMode = toggleMapMode;
        this.toggleMilesCheckBox = toggleMilesCheckBox;
        this.tagListBox = tagListBox;
        this.sortingListBox = sortingListBox;
        this.topNav = topNav;


        this.searchButton = searchButton;
        this.menuButton = menuButton;

        this.menuButton.getElement().setId("show-menu");

        initWidget(desktopBinder.createAndBindUi(this));


        body.setWidget(bodyPanel);
        messages.setWidget(messagePanel);
        //searchBox.setWidget(searchBoxPanel);

    }
}
