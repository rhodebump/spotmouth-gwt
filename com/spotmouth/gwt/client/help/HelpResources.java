package com.spotmouth.gwt.client.help;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/3/12
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface HelpResources extends ClientBundle {
    public static final HelpResources INSTANCE = GWT.create(HelpResources.class);

    @Source("30secs.html")
    public TextResource get30Secs();

    @Source("PlaceForm.html")
    public TextResource getPlaceForm();

    @Source("SecretKeyForm.html")
    public TextResource getSecretKeyForm();

    @Source("LeaveMarkForm.html")
    public TextResource getLeaveMarkForm();

    @Source("MailingListPanel.html")
    public TextResource getMalingListPanel();

    @Source("ManageSpotPanel.html")
    public TextResource getManageSpotPanel();


    @Source("Help.html")
    public TextResource getHelp();

    @Source("ContactPanel.html")
    public TextResource getContactPanel();

    @Source("MarkSpotTypePanel.html")
    public TextResource getMarkSpotTypePanel();

    @Source("SettingsPanel.html")
    public TextResource getSettingsPanel();

    @Source("BizOwner.html")
    public TextResource getBizOwner();

    @Source("Favorites.html")
    public TextResource getFavorites();

    @Source("GroupPanel.html")
    public TextResource getGroupPanel();





    @Source("Results.html")
    public TextResource getResultsPanel();

    @Source("SpotDetailPanel.html")
    public TextResource getSpotDetailPanel();

    @Source("SetLocation.html")
    public TextResource getSetLocation();
}
