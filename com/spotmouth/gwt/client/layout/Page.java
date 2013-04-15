package com.spotmouth.gwt.client.layout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.menu.TopNav;
import  com.google.gwt.uibinder.client.*;

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
    interface PageBinder extends UiBinder<Widget, Page> {}
    private static PageBinder desktopBinder = GWT.create(PageBinder.class);

    @UiTemplate("MPage.ui.xml")
    interface MPageBinder extends UiBinder<Widget, Page> {}
    private static MPageBinder mobileBinder = GWT.create(MPageBinder.class);


//    protected Page(UiBinder<Widget, Page> binder) {
//      initWidget(uiBinder.createAndBindUi(this));
//    }


  // private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


//
//    public static Page createRedPicker() {
//      return new Page(redBinder);
//    }



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
    public Page(Panel bodyPanel, Panel messagePanel, Panel searchBoxPanel, TextBox keywordsTextBox, TextBox locationTextBox,ULPanel previousLocationsULPanel,
                SimpleCheckBox toggleMilesCheckBox,SimpleCheckBox toggleMapMode,Button markSpotButton, ListBox tagListBox ,
                ListBox sortingListBox,Button searchButton,
                TopNav topNav,Button menuButton) {
        this.keywordsTextBox = keywordsTextBox;

        this.markSpotButton = markSpotButton;
        this.locationTextBox = locationTextBox;
        this.previousLocationsULPanel=previousLocationsULPanel;
        this.toggleMapMode = toggleMapMode;
        this.toggleMilesCheckBox = toggleMilesCheckBox;
       this.tagListBox = tagListBox;
       this.sortingListBox = sortingListBox;
        this.topNav = topNav;


        this.searchButton = searchButton;
      this.menuButton = menuButton;

        this.menuButton.getElement().setId("show-menu");
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            GWT.log("mpage binder");
            initWidget(mobileBinder.createAndBindUi(this));
        }



        body.setWidget(bodyPanel);
        messages.setWidget(messagePanel);
        //searchBox.setWidget(searchBoxPanel);

    }
}
