package com.spotmouth.gwt.client;
/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/6/12
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Image;

public class ImageAnchor extends Anchor {
    public ImageAnchor() {
    }

    public void setResource(ImageResource imageResource) {
        Image img = new Image(imageResource);
        DOM.insertBefore(getElement(), img.getElement(), DOM
                .getFirstChild(getElement()));
    }
}