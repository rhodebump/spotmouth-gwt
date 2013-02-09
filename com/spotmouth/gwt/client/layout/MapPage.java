package com.spotmouth.gwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/29/12
 * Time: 6:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapPage extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MapPage> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField
    SimplePanel body;

    @UiField
    SimplePanel messages;

    @UiField
    SimplePanel searchBox;


    @UiField(provided = true)
    final ListBox tagListBox;

    @UiField(provided = true)
    final ListBox sortingListBox;

    @UiField(provided = true)
    SimpleCheckBox toggleMilesCheckBox;
    @UiField(provided = true)
    SimpleCheckBox toggleMapMode;


    @UiField(provided = true)
    Button markSpotButton;

    //simplePanel,popularPanel,latestPanel,mp,tagCloudPanel,locationPanel,tagCloudPanel2,googleMapPanel
    public MapPage(Panel bodyPanel,Panel popularPostsPanel, Panel latestPostsPanel,Panel messagePanel, Panel tagCloudPanel, Panel searchBoxPanel,
                   Panel googleMapPanel,  SimpleCheckBox toggleMilesCheckBox,SimpleCheckBox toggleMapMode,Button markSpotButton, ListBox tagListBox ,ListBox sortingListBox) {
        this.toggleMapMode = toggleMapMode;
        this.toggleMilesCheckBox = toggleMilesCheckBox;
        this.markSpotButton = markSpotButton;
        this.tagListBox = tagListBox;
        this.sortingListBox = sortingListBox;
        initWidget(uiBinder.createAndBindUi(this));
        body.setWidget(bodyPanel);

        messages.setWidget(messagePanel);


        searchBox.setWidget(searchBoxPanel);

    }
}
