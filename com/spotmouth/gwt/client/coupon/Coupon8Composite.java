package com.spotmouth.gwt.client.coupon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/30/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Coupon8Composite extends Composite {
    @UiTemplate("Coupon8Composite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, Coupon8Composite> {
    }

    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("Coupon8CompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, Coupon8Composite> {
    }

    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);
    @UiField(provided = true)
    final ULPanel couponResultsUL;

    public Coupon8Composite(ULPanel couponResultsUL) {
        this.couponResultsUL = couponResultsUL;
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        } else {
            initWidget(mobileBinder.createAndBindUi(this));
        }
    }
}
