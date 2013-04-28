package com.spotmouth.gwt.client.coupon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/18/13
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouponResult  extends Composite {

    @UiTemplate("CouponResult.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, CouponResult> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("CouponResultM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, CouponResult> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);



    @UiField
   SpanElement venueSpan;

    public void setVenue(String location) {
        venueSpan.setInnerText(location);
    }

    @UiField
        SpanElement couponTitle;

    public void setTitle(String title) {
        couponTitle.setInnerText(title);
    }


    @UiField
    SpanElement endDate;

    public void setEndDate(String ed) {
        endDate.setInnerText(ed);
    }


    @UiField(provided = true)
    final Anchor detailAnchor;


    public CouponResult(Anchor detailAnchor) {
        this.detailAnchor  = detailAnchor;

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        } else {
            initWidget(mobileBinder.createAndBindUi(this));
        }
    }

}
