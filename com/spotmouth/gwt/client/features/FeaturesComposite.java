package com.spotmouth.gwt.client.features;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/19/13
 * Time: 8:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeaturesComposite   extends Composite {



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


}
