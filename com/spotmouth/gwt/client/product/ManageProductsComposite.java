package com.spotmouth.gwt.client.product;

import com.spotmouth.gwt.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/16/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class ManageProductsComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ManageProductsComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    final ULPanel availableProductsULPanel;

    @UiField(provided = true)
    final ULPanel installedProductsULPanel;

    public ManageProductsComposite(ULPanel availableProductsULPanel,ULPanel installedProductsULPanel) {
        this.availableProductsULPanel = availableProductsULPanel;
        this.installedProductsULPanel = installedProductsULPanel;
        initWidget(uiBinder.createAndBindUi(this));
    }


    @UiField
    SpanElement nameSpan;

    public void setName(String name) {
        nameSpan.setInnerText(name);
    }

    @UiField
    SpanElement locationSpan;

    public void setLocation(String name) {
        locationSpan.setInnerText(name);
    }



}
