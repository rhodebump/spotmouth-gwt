package com.spotmouth.gwt.client.om;

import com.google.gwt.core.client.JavaScriptObject;

public class LocationOverlay extends JavaScriptObject {
    protected LocationOverlay() {
    }

    public final native void setLongitude(double lng) /*-{
        this.longitude = lng;
    }-*/;

    public final native void setLatitude(double lat) /*-{
        this.latitude = lat;
    }-*/;

    public final native double getLongitude() /*-{
        return this.longitude;
    }-*/;

    public final native double getLatitude() /*-{
        return this.latitude;
    }-*/;

    public final native String getName() /*-{
        return this.name;
    }-*/;

    public final native String getLabel() /*-{
        return this.label;
    }-*/;

    public final native void setLabel(String lbl) /*-{
        this.label = lbl;
    }-*/;

    public final native String getStreetAddress1() /*-{
        return this.streetAddress1;
    }-*/;

    public final native void setStreetAddress1(String streetAddress1) /*-{
        this.streetAddress1 = streetAddress1;
    }-*/;

    public final native void setCity(String city) /*-{
        this.city = city;
    }-*/;

    public final native void setState(String state) /*-{
        this.state = state;
    }-*/;

    public final native String getCountryCode() /*-{
        return this.countryCode;
    }-*/;


    public final native void setCountryCode(String countryCode) /*-{
        this.countryCode = countryCode;
    }-*/;


    public final native String getCity() /*-{
        return this.city;
    }-*/;

    public final native void setZipcode(String zipcode) /*-{
        this.zipcode = zipcode;
    }-*/;

    public final native String getZipcode() /*-{
        return this.zipcode;
    }-*/;

    public final native String getState() /*-{
        return this.state;
    }-*/;

    public final native String getGeocodeInput() /*-{
        return this.geocodeInput;
    }-*/;
    // public final native String getSpotId() /*-{ return this.state; }-*/;

    public static native LocationOverlay fromJson(String jsonString) /*-{
        return eval('(' + jsonString + ')');
    }-*/;

    public final native void setGeocoded(boolean geocoded) /*-{
        this.geocoded = geocoded;
    }-*/;

    public final native boolean getGeocoded() /*-{
        return this.geocoded;
    }-*/;


    public final native void setLastUsedDate(String lastUsedDate) /*-{
        this.lastUsedDate = lastUsedDate;
    }-*/;

    public final native String getLastUsedDate() /*-{
        return this.lastUsedDate;
    }-*/;


}
