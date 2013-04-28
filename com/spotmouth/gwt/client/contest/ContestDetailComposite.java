package com.spotmouth.gwt.client.contest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/27/13
 * Time: 6:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestDetailComposite extends Composite {



    @UiField(provided = true)
    final Button cancelButton;


    @UiTemplate("ContestDetailComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, ContestDetailComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("ContestDetailCompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, ContestDetailComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);




    @UiField
    InlineLabel photoSpan;

    public void setImageUrl(String url) {
        String style= "background: url('" + url + "') no-repeat center center";

        photoSpan.getElement().setAttribute("style",style);

    }



    @UiField
   SpanElement radiusSpan;

    public void setRadius(String name) {
        radiusSpan.setInnerText(name);
    }



    @UiField
   SpanElement starCountSpan;

    public void setStarCount(Integer name) {
        starCountSpan.setInnerText(name.toString());
    }


    @UiField
   SpanElement locationSpan;

    public void setLocation(String location) {
        locationSpan.setInnerText(location);
    }

    @UiField
   Button editButton;

    @UiField
   Button mostVotedButton;


    @UiField
   Button winnersButton;




    @UiField
   SpanElement startDateSpan;

    public void setStartDate(String name) {
        startDateSpan.setInnerText(name);
    }



    @UiField
   SpanElement endDateSpan;

    public void setEndDate(String name) {
        endDateSpan.setInnerText(name);
    }



    @UiField
   SpanElement descriptionSpan;

    public void setDescription(String name) {
        descriptionSpan.setInnerText(name);
    }



    @UiField
   SpanElement nameSpan;

    public void setName(String name) {
        nameSpan.setInnerText(name);
    }


    public ContestDetailComposite( Button cancelButton) {

        this.cancelButton = cancelButton;
        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }



        photoSpan.setStyleName("ac_photo");
    }




    private Long contestId = null;

    public void setContestId(Long contestId) {
        this.contestId = contestId;

    }


    @UiHandler("winnersButton")
    public void onClick0(ClickEvent event) {
        String token = MyWebApp.CONTEST_AVERAGE_VOTES + contestId;

        History.newItem(token);
    }

    @UiHandler("mostVotedButton")
    public void onClick2(ClickEvent event) {
        String token = MyWebApp.CONTEST_TOTAL_VOTES + contestId;

        History.newItem(token);
    }


    @UiHandler("editButton")
    public void onClick3(ClickEvent event) {
       String token =  MyWebApp.MANAGE_CONTEST + contestId;

        History.newItem(token);
    }



}
