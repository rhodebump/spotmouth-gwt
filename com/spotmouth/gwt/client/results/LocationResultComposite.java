package com.spotmouth.gwt.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/28/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocationResultComposite extends Composite implements HasClickHandlers {
    @UiTemplate("LocationResultCompositeM.ui.xml")
    interface MPageBinder extends UiBinder<Widget, LocationResultComposite> {}
    private static MPageBinder mobileBinder = GWT.create(MPageBinder.class);

    @UiField
       SpanElement locationDescriptionSpan;



    @UiField
       SpanElement distanceSpan;

    @UiField
       SpanElement unitSpan;



//    @UiField(provided = true)
//    final Anchor detailAnchor;


    @UiField(provided = true)
    final Image image;

    @UiField(provided = true)
    final FlowPanel tagsPanel;



    public LocationResultComposite(Anchor detailAnchor,Image image,FlowPanel tagsPanel) {
       // this.detailAnchor = detailAnchor;
        this.image = image;
        this.tagsPanel = tagsPanel;
        initWidget(mobileBinder.createAndBindUi(this));
    }


    public void setLocationDescription(String locationDescription) {
        locationDescriptionSpan.setInnerText(locationDescription);
    }



    public void setDistance(String distance) {
        distanceSpan.setInnerText(distance);
    }
    public void setUnit(String unit) {
        unitSpan.setInnerText(unit);
    }



    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

}
