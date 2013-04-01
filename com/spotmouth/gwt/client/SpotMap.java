package com.spotmouth.gwt.client;

import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.user.client.ui.SimplePanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.Location;

public class SpotMap extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Map";
    }

    public void toggleFirst() {
    }



    Location location = null;

    public SpotMap(MyWebApp mywebapp, Location location) {
        super(mywebapp);
        this.addStyleName("SpotMap");
        this.location = location;
        if (Maps.isLoaded()) {
            doMap();
        } else {
            Maps.loadMapsApi(mywebapp.getGoogleMapKey(), "2", false, new Runnable() {
                public void run() {
                    doMap();
                }
            });
        }
    }

    private void doMap() {
        final SimplePanel mapPanel = new SimplePanel();
        geocoder = new Geocoder();
        initMap(location, mapPanel);
        add(mapPanel);
    }
}
