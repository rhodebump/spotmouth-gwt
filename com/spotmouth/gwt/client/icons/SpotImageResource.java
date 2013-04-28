package com.spotmouth.gwt.client.icons;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

//public class SpotImageBundle {
public interface SpotImageResource extends ClientBundle {



    //trying to use 128 (instead of 130) since it's a more standard image format size
    int mobileSize = 35;

    int buttonSize = 28;

    int buttonSizeMobile = 20;


    //following is 300 width

    @Source("spotmouth_logo.png")
    ImageResource spotmouthLogo();



    @Source("spotmouth_logo.png")
    ImageResource worldlogo();

//
//    @Source("icon-search.gif")
//    ImageResource iconsearch();




    //used on startup
    @Source("splash-image.png")
    ImageResource spotmouthSplash();

    @Source("poweredby_36x36.png")
    ImageResource fourSquare();


    @Source("star-3.png")
    ImageResource youAreHere();

    @Source("powered-by-google-on-white.png")
    ImageResource googlePowered();

    @Source("upcoming_logo2.gif")
    ImageResource yahooUpcoming();




    //@Source("yelp_logo_75x38.png")
    @Source("reviewsFromYelpRED.gif")
    ImageResource yelp();


//
//    @Source("phone_transfer.png")
//    @ImageOptions(width = 72)
//    ImageResource locationDevice();
//
//
//    @Source("phone_transfer.png")
//    @ImageOptions(width = buttonSize)
//    ImageResource locationDeviceButton();
//
//    @Source("phone_transfer.png")
//    @ImageOptions(width = buttonSizeMobile)
//    ImageResource locationDeviceButtonMobile();




    //eagle_bulkhead_door.png

    @Source("cbg-starhover.png")
    ImageResource star();

    @Source("thumbs_down.png")
    @ImageOptions(width = 28)
    ImageResource thumbsDown16();

    @Source("thumbs_up_deselected.png")
    @ImageOptions(width = 28)
    ImageResource thumbsDownDeselected16();

    @Source("thumbs_up.png")
    @ImageOptions(width = 28)
    ImageResource thumbsUp16();

    @Source("thumbs_up_deselected.png")
    @ImageOptions(width = 28)
    ImageResource thumbsUpDeselected16();




    @Source("plate.png")
    @ImageOptions(width = 72)
    ImageResource plate();

;

    @Source("facebook.png")
    @ImageOptions(width = 72)
    ImageResource facebook();

    @Source("facebook.png")
    @ImageOptions(width = mobileSize)
    ImageResource facebookMobile();


    @Source("facebook.png")
    @ImageOptions(width = buttonSize)
    ImageResource facebookButton();

    @Source("facebook.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource facebookButtonMobile();







    @Source("spotmouth-50x50.png")
    ImageResource spotmouth50x50();

    @Source("android-developer-minidroid.jpg.png")
    ImageResource android();

    @Source("ajax-loader.gif")
    ImageResource ajaxLoader();





    @Source("ava.png")
    @ImageOptions(width = 64)
    ImageResource spot_image_placeholder57x57();



    @Source("ava.png")
    ImageResource spot_image_placeholder130x130();




    @Source("guy128.png")
    @ImageOptions(width = 57)
    ImageResource spotImageMini();



    @Source("guy128.png")
    ImageResource spot_image_placeholder320x320();

    @Source("thumbs_down.png")
    @ImageOptions(width = 72)
    ImageResource thumbsDownBig();



    @Source("thumbs_up.png")
    @ImageOptions(width = 72)
    ImageResource thumbsUp();

    @Source("thumbs_up.png")
    @ImageOptions(width = mobileSize)
    ImageResource thumbsUpMobile();







    @Source("find.png")
    @ImageOptions(width = 72)
    ImageResource search();

    @Source("find.png")
    @ImageOptions(width = mobileSize)
    ImageResource searchMobile();


    @Source("find.png")
    @ImageOptions(width = 30)
    ImageResource searchButton();

    @Source("find.png")
    @ImageOptions(width = 20)
    ImageResource searchButtonSmall();



    @Source("8coupons-med.png")
    ImageResource coupons8();





}
