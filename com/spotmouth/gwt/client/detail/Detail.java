package com.spotmouth.gwt.client.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 10/22/12
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Detail extends Composite {


    @UiField
    SpanElement labelSpan;

     public void setLabel(String label) {
         labelSpan.setInnerText(label);
     }




    @UiTemplate("Detail.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, Detail> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);




    @UiField SpanElement nameSpan;

    @UiField(provided=true)
    final Anchor backToSearchResultsAnchor;

    @UiField(provided=true)
    final Anchor addMyBizAnchor;

    @UiField(provided=true)
    final Anchor addtoFavoriteAnchor;

    @UiField(provided=true)
    final Anchor websiteAnchor;


    @UiField(provided=true)
    final Anchor adminSpotAnchor;

    @UiField(provided=true)
    final Anchor phoneAnchor;


    @UiField(provided=true)
    final Anchor groupsAnchor;

    @UiField(provided=true)
    final Anchor descriptionAnchor;


    @UiField(provided=true)
    final Anchor mapAnchor;



    @UiField(provided=true)
    final FlowPanel groupsPanel;


    @UiField(provided=true)
    final Anchor markSpotAnchor;

    @UiField(provided=true)
    final Button addGroupButton;

    @UiField(provided=true)
    final Image mainImage;


    @UiField(provided=true)
    final HTML hoursHTML;


    @UiField(provided=true)
    final SimplePanel mapPanel;


    @UiField SpanElement descriptionSpan;

    public void setDescription(String name) {
        descriptionSpan.setInnerText(name);
    }


    @UiField SpanElement addressSpan;

     public void setName(String name) {
         nameSpan.setInnerText(name);
     }
    public void setAddress(String address) {
        addressSpan.setInnerText(address);
    }



//    @UiField
//    SimplePanel googleMap;
    public Detail(Anchor backToSearchResultsAnchor,Anchor addMyBizAnchor,Anchor addtoFavoriteAnchor,Anchor adminSpotAnchor,Anchor phoneAnchor, Anchor groupsAnchor,Anchor mapAnchor,Image mainImage,
                  Anchor markSpotAnchor,FlowPanel groupsPanel,Button addGroupButton,HTML hoursHTML,Anchor websiteAnchor,SimplePanel mapPanel,Anchor descriptionAnchor) {
        this.mapPanel = mapPanel;
        this.backToSearchResultsAnchor      = backToSearchResultsAnchor;
        this.addMyBizAnchor = addMyBizAnchor;
        this.addtoFavoriteAnchor  = addtoFavoriteAnchor;
        this.adminSpotAnchor = adminSpotAnchor;
        this.phoneAnchor = phoneAnchor;
        this.groupsAnchor = groupsAnchor;
        this.mapAnchor   = mapAnchor;
        this.mainImage = mainImage;
        this.markSpotAnchor = markSpotAnchor;
        this.groupsPanel = groupsPanel;
        this.addGroupButton = addGroupButton;
        this.hoursHTML = hoursHTML;
        this.websiteAnchor = websiteAnchor;
        this.descriptionAnchor = descriptionAnchor;
        initWidget(desktopBinder.createAndBindUi(this));

    }
}
