package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapUIOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.Overlay;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.Location;

public class SpotMap extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Map";
    }

    public void toggleFirst() {
    }

    public boolean isLoginRequired() {
        return false;
    }






    public SpotMap(MyWebApp mywebapp, final Location location) {
        super(mywebapp);
        this.addStyleName("SpotMap");

        final SimplePanel mapPanel = new SimplePanel();

        Maps.loadMapsApi(mywebapp.getGoogleMapKey(), "2", false, new Runnable() {
            public void run() {
                geocoder = new Geocoder();
                initMap(location,mapPanel);
            }
        });

//        LatLng latLng = LatLng.newInstance(location.getLatitude(), location.getLongitude());
//        mapWidget.setCenter(latLng);
        add(mapPanel);

    }





}
