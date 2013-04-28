package com.spotmouth.gwt.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/28/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocationResultComposite extends Composite {
    @UiTemplate("LocationResultCompositeM.ui.xml")
    interface MPageBinder extends UiBinder<Widget, LocationResultComposite> {}
    private static MPageBinder mobileBinder = GWT.create(MPageBinder.class);



    public LocationResultComposite() {
        initWidget(mobileBinder.createAndBindUi(this));

    }
}
