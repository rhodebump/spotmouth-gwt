package com.spotmouth.gwt.client.favorites;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/3/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class FavoritesComposite  extends Composite {



    @UiTemplate("FavoritesComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, FavoritesComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("MFavoritesComposite.ui.xml")
    interface MobileBinder extends UiBinder<Widget, FavoritesComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);


    @UiField(provided=true)
    final Panel spotsAreaPanel;





    public FavoritesComposite(Panel spotsAreaPanel) {
        this.spotsAreaPanel = spotsAreaPanel;

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }

    }





}
