package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface MyHtmlResources extends ClientBundle {
    public static final MyHtmlResources INSTANCE = GWT.create(MyHtmlResources.class);

    @Source("terms.html")
    public TextResource getTermsHtml();

    @Source("about.html")
    public TextResource getAboutHtml();

    @Source("aboutus.html")
    public TextResource getAboutUSHtml();



    @Source("driver.html")
    public TextResource getDriver();

    @Source("UGC.html")
    public TextResource getUGC();

    @Source("features.html")
    public TextResource getFeatures();

}
