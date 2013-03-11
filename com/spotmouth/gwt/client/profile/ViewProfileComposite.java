package com.spotmouth.gwt.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/25/12
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewProfileComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ViewProfileComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);




    @UiField
    InlineLabel photoSpan;

    public void setImageUrl(String url) {
        String style= "background: url('" + url + "') no-repeat center center";

        photoSpan.getElement().setAttribute("style",style);

    }





    @UiField
    SpanElement aboutSpan;

    @UiField
    SpanElement firstNameSpan;

    @UiField
    SpanElement lastNameSpan;



    @UiField
    SpanElement citySpan;

    public void setCity(String city) {
        citySpan.setInnerText(city);
    }



    @UiField
    SpanElement stateSpan;


    public void setState(String state) {
        stateSpan.setInnerText(state);
    }



    @UiField
    SpanElement countrySpan;


    @UiField(provided=true)
    final Button addToFriendsButton;

    public void setCountryCode(String countryCode) {
        countrySpan.setInnerText(countryCode);
    }


    public void setAbout(String about) {
        aboutSpan.setInnerText(about);
    }


    public void setFirstName(String firstName) {
        firstNameSpan.setInnerText(firstName);
    }

    public void setLastName(String lastName) {
        lastNameSpan.setInnerText(lastName);
    }



    public ViewProfileComposite( Button addToFriendsButton ) {
        this.addToFriendsButton = addToFriendsButton;

//        this.usernameTextBox = usernameTextBox;
//        this.emailAddressTextBox = emailAddressTextBox;
        initWidget(uiBinder.createAndBindUi(this));
       photoSpan.getElement().setId("pp_ava");
    }
}
