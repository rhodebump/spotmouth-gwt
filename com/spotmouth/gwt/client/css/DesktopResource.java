package com.spotmouth.gwt.client.css;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.*;
import  com.google.gwt.core.client.GWT;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 10/6/12
 * Time: 8:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DesktopResource  extends ClientBundle {

    public static final DesktopResource INSTANCE =  GWT.create(DesktopResource.class);

//    @CssResource.NotStrict
//    @Source("style.css")
//    public CssResource style();


//    @CssResource.NotStrict
//    @Source("spotmouth.css")
//    public CssResource spotmouth();
//
//
//
//
//    @CssResource.NotStrict
//    @Source("default.css")
//    public CssResource default1();
//
//
//    @CssResource.NotStrict
//    @Source("pagenavi-css.css")
//    public CssResource pagenavi();
//
//
//    @CssResource.NotStrict
//    @Source("reset.css")
//    public CssResource reset();


    @Source("whiteButton.png")
    DataResource whiteButton();


//    @Source("twitter-standing.png")
//    DataResource twitterStanding();


    @Source("icon-facebook.png")
    DataResource iconFacebook();

    @Source("icon-twitter.png")
    DataResource iconTwitter();

    @Source("icon-youtube.png")
    DataResource iconYoutube();



//    @Source("manual.pdf")
//    public DataResource ownersManual();




//    @CssResource.NotStrict
//    @Source("MyWebApp.css")
//    public CssResource MyWebApp();


//    @CssResource.NotStrict
//    @Source("iui.css")
//    public CssResource iui();



//
//    @CssResource.NotStrict
//    @Source("default-theme.css")
//    public CssResource defaultTheme();
//
//
//
//
//    @Source("blue.png")
//    DataResource blueBack();


//
//    @CssResource.NotStrict
//    @Source("Upload.css")
//    public CssResource upload();


//    @CssResource.NotStrict
//    @Source("cat-menu.css")
//    public CssResource catmenu();


//    @CssResource.NotStrict
//    @Source("topnav.css")
//    public CssResource topNav();


//
//    @Source("add-img.png")
//    DataResource addImg();
//
//    @Source("ava.png")
//    DataResource avaImg();


//    @CssResource.NotStrict
//    @Source("new-search.css")
//    public CssResource newSearchCss();



}
