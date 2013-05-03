package com.spotmouth.gwt.client.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.ListItem;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/19/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeaturesComposite   extends Composite {

    @UiField
    ListItem usersListItem;

    @UiField
    ListItem bizListItem;

    @UiField
    ListItem usersListItem2;

    @UiField
    ListItem bizListItem2;



    @UiTemplate("FeaturesComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, FeaturesComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("FeaturesCompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, FeaturesComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);


    public FeaturesComposite() {
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }
    }


    @UiHandler("usersListItem")
    public void onClick1(ClickEvent event) {
        resetall();
        usersListItem.addStyleName("_active");
        usersListItem2.addStyleName("_active");
    }

    @UiHandler("bizListItem")
    public void onClick2(ClickEvent event) {
        resetall();
        bizListItem.addStyleName("_active");
        bizListItem2.addStyleName("_active");
    }

    private void resetall() {
        bizListItem.removeStyleName("_active");
        bizListItem2.removeStyleName("_active");

        usersListItem.removeStyleName("_active");
        usersListItem2.removeStyleName("_active");

    }

}
