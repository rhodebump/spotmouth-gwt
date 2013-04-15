package com.spotmouth.gwt.client.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/11/13
 * Time: 8:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class AboutComposite extends Composite {

    @UiTemplate("AboutComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, AboutComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("MAboutComposite.ui.xml")
    interface MobileBinder extends UiBinder<Widget, AboutComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);


    public AboutComposite() {


        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }



    }


}
