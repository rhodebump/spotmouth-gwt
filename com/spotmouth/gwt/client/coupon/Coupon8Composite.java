package com.spotmouth.gwt.client.coupon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/30/12
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Coupon8Composite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, Coupon8Composite> {
    }

    @UiField(provided = true)
    final ULPanel couponResultsUL;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public Coupon8Composite(ULPanel couponResultsUL) {
        this.couponResultsUL      = couponResultsUL;
initWidget(uiBinder.createAndBindUi(this));

    }

}
