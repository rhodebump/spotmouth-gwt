package com.spotmouth.gwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;

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

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
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
    @UiField
    SimplePanel searchBox;
    @UiField(provided = true)
    SimplePanel profilePicPanel;

    @UiField(provided = true)
    final TextBox keywordsTextBox;
    @UiField(provided = true)
    final TextBox locationTextBox;
    @UiField
    SpanElement usernameSpan;

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



    public void setUsername(String name) {
        usernameSpan.setInnerText(name);
    }

    //simplePanel,popularPanel,latestPanel,mp,tagCloudPanel,locationPanel,tagCloudPanel2,googleMapPanel
    public Page(Panel bodyPanel, Panel messagePanel, Panel searchBoxPanel, TextBox keywordsTextBox, TextBox locationTextBox, SimplePanel profilePicPanel,ULPanel previousLocationsULPanel,
                SimpleCheckBox toggleMilesCheckBox,SimpleCheckBox toggleMapMode,Button markSpotButton, ListBox tagListBox ,ListBox sortingListBox,Button searchButton) {
        this.keywordsTextBox = keywordsTextBox;

        this.markSpotButton = markSpotButton;
        this.locationTextBox = locationTextBox;
        this.previousLocationsULPanel=previousLocationsULPanel;
        this.toggleMapMode = toggleMapMode;
        this.toggleMilesCheckBox = toggleMilesCheckBox;
       this.tagListBox = tagListBox;
       this.sortingListBox = sortingListBox;

        this.profilePicPanel = profilePicPanel;
        this.searchButton = searchButton;
        initWidget(uiBinder.createAndBindUi(this));

        body.setWidget(bodyPanel);
        //popularPosts.setWidget(popularPostsPanel);
        //latestPosts.setWidget(latestPostsPanel);
        messages.setWidget(messagePanel);
        //tagCloud.setWidget(tagCloudPanel);

       // googleMap.setWidget(googleMapPanel);
        searchBox.setWidget(searchBoxPanel);
        // sidebar.setWidget(sidebarPanel);

    }
}
