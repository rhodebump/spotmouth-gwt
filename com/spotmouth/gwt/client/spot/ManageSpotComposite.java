package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.dto.SpotHolder;
import gwtupload.client.MultiUploader;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/12/13
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageSpotComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ManageSpotComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final MultiUploader multiUploader;
    private ManageSpotPanel manageSpotPanel = null;

    public ManageSpotComposite(ManageSpotPanel manageSpotPanel, SimplePanel spotImagePanel, MultiUploader multiUploader, SpotHolder spotHolder) {
        this.manageSpotPanel = manageSpotPanel;
        this.spotImagePanel = spotImagePanel;
        this.multiUploader = multiUploader;
        initWidget(uiBinder.createAndBindUi(this));
        toggleLock(spotHolder.isLockedForEdit());
    }
    ////	<a href="#">Change Listing Info</a>
    //nameSpan
    @UiField
    SpanElement nameSpan;

    public void setName(String name) {
        nameSpan.setInnerText(name.toString());
    }

    @UiField
    Anchor changeListingInfo;
    @UiField
    Anchor manageProducts;
    @UiField
    Anchor createEvent;
    @UiField
    Anchor spotDetailsAnchor;
    @UiField
    Anchor lockSpotAnchor;
    @UiField
    Anchor unlockSpotAnchor;
    @UiField
    Anchor createCoupon;
    @UiField
    Anchor addToFavorites;

    @UiHandler("manageProducts")
    public void onClick2(ClickEvent event) {
        manageSpotPanel.manageProducts();
    }

    @UiHandler("changeListingInfo")
    public void onClick1(ClickEvent event) {
        manageSpotPanel.changeListingInfo();
    }

    @UiHandler("createEvent")
    public void onClickEvent(ClickEvent event) {
        manageSpotPanel.createEvent();
    }

    @UiHandler("createCoupon")
    public void onClickCoupon(ClickEvent event) {
        manageSpotPanel.createCoupon();
    }

    @UiHandler("deleteSpotAnchor")
    public void onClickDeleteSpot(ClickEvent event) {
        manageSpotPanel.deleteSpot();
    }

    @UiHandler("spotDetailsAnchor")
    public void onClickDetails(ClickEvent event) {
        manageSpotPanel.doDetails();
    }

    @UiHandler("addToFavorites")
    public void onClickFavs(ClickEvent event) {
        manageSpotPanel.addToFavorites();
    }

    private void toggleLock(boolean lock) {
        unlockSpotAnchor.setVisible(lock);
        lockSpotAnchor.setVisible(!lock);
    }

    @UiHandler("unlockSpotAnchor")
    public void onClickUnLock(ClickEvent event) {
        manageSpotPanel.unlockSpot();
        toggleLock(false);
    }

    @UiHandler("lockSpotAnchor")
    public void onClickLock(ClickEvent event) {
        manageSpotPanel.lockSpot();
        toggleLock(true);
    }

    @UiField(provided = true)
    final SimplePanel spotImagePanel;
}
