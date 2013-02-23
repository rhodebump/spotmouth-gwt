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
//
//    @Source("medal.png")
//    @ImageOptions(width = 72)
//    ImageResource contests();
//
//    @Source("medal.png")
//    @ImageOptions(width = mobileSize)
//    ImageResource contestsMobile();



    @Source("medal.png")
    @ImageOptions(width = buttonSize)
    ImageResource contestButton();


    @Source("medal.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource contestButtonMobile();




    @Source("eagle_bulkhead_door.png")
    @ImageOptions(width = 72)
    ImageResource lockSpot();

    @Source("beos_mailbox.png")
    @ImageOptions(width = 72)
    ImageResource mailinglist();

    @Source("location2.png")
    @ImageOptions(width = 72)
    ImageResource location();


    @Source("location2.png")
    @ImageOptions(width = mobileSize)
    ImageResource locationMobile();

    @Source("plate.png")
    @ImageOptions(width = 72)
    ImageResource plate();

    @Source("qdoba.png")
    @ImageOptions(width = 72)
    ImageResource place();
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


    @Source("save.png")
    @ImageOptions(width = buttonSize)
    ImageResource saveButton();

    @Source("save.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource saveButtonMobile();



    @ImageOptions(width = buttonSize)
    @Source("stop.png")
    ImageResource cancelButton();

    @ImageOptions(width = buttonSizeMobile)
    @Source("stop.png")
    ImageResource cancelButtonMobile();



    @Source("icon-register.png")
    @ImageOptions(width = buttonSize)
    ImageResource registerButton();


    @Source("icon-register.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource registerButtonMobile();



    @Source("password.png")
    @ImageOptions(width = 72)
    ImageResource password();

    @Source("password.png")
    @ImageOptions(width = mobileSize)
    ImageResource passwordMobile();

    @Source("icon-register.png")
    @ImageOptions(width = 72)
    ImageResource register();

    @Source("icon-register.png")
    @ImageOptions(width = mobileSize)
    ImageResource registerMobile();


    @Source("help_dialog_question.png")
    @ImageOptions(width = buttonSize)
    ImageResource helpButton();


    @Source("help_dialog_question.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource helpButtonMobile();


    @Source("Question-mark-icon.png")
    @ImageOptions(width = 72)
    ImageResource passwordReset();

    @Source("Question-mark-icon.png")
    @ImageOptions(width = mobileSize)
    ImageResource passwordResetMobile();


//    @Source("password.png")
//    @ImageOptions(width = 30)
//    ImageResource passwordSmall();

    @Source("spotmouth-50x50.png")
    ImageResource spotmouth50x50();

    @Source("android-developer-minidroid.jpg.png")
    ImageResource android();

    @Source("ajax-loader.gif")
    ImageResource ajaxLoader();

    @Source("anon128x128.png")
    ImageResource anon130x130();

    @Source("anon128x128.png")
    @ImageOptions(width = 57)
    ImageResource anon57x57();



    @Source("anon128x128.png")
    @ImageOptions(width = 90)
    ImageResource anon130x130Mobile();



    @Source("ava.png")
    @ImageOptions(width = 64)
    ImageResource spot_image_placeholder57x57();


    //spot-image-placeholder130x130.png

    @Source("ava.png")
    ImageResource spot_image_placeholder130x130();




//    @Source("guy128.png")
//    @ImageOptions(width = 90)
//    ImageResource spot_image_placeholder130x130Mobile();

    @Source("guy128.png")
    @ImageOptions(width = 57)
    ImageResource spotImageMini();



    @Source("guy128.png")
    ImageResource spot_image_placeholder320x320();

    @Source("thumbs_down.png")
    @ImageOptions(width = 72)
    ImageResource thumbsDownBig();

//    @Source("thumbs_down.png")
//    @ImageOptions(width = 30)
//    ImageResource thumbsDownSmall();

    @Source("thumbs_up.png")
    @ImageOptions(width = 72)
    ImageResource thumbsUp();

    @Source("thumbs_up.png")
    @ImageOptions(width = mobileSize)
    ImageResource thumbsUpMobile();

    @Source("beos_people.png")
    @ImageOptions(width = 72)
    ImageResource groups();

    @Source("beos_people.png")
    @ImageOptions(width = mobileSize)
    ImageResource groupsMobile();

    @Source("agt_family.png")
    @ImageOptions(width = 72)
    ImageResource friends();



    @Source("agt_family.png")
    @ImageOptions(width = mobileSize)
    ImageResource friendsMobile();

    @Source("info_128.png")
    @ImageOptions(width = 72)
    ImageResource about();

    @Source("info_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource aboutMobile();

//    @Source("bestplaces.png")
//    @ImageOptions(width = 72)
//    ImageResource bestplacesBig();




    @Source("restaurant_food-128.png")
    @ImageOptions(width = 72)
    ImageResource dining();

    @Source("restaurant_food-128.png")
    @ImageOptions(width = mobileSize)
    ImageResource diningMobile();

//    @Source("restaurant_food-64.png")
//    @ImageOptions(width = 30)
//    ImageResource diningSmall();

    @Source("beer_128.png")
    @ImageOptions(width = 72)
    ImageResource drinking();

    @Source("beer_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource drinkingMobile();


//    @Source("beer_64.png")
//    @ImageOptions(width = 30)
//    ImageResource drinkingSmall();

    @Source("favorites2.png")
    @ImageOptions(width = 72)
    ImageResource favorites();

    @Source("favorites2.png")
    @ImageOptions(width = mobileSize)
    ImageResource favoritesMobile();

//
//    @Source("favorites2.png")
//    @ImageOptions(width = 30)
//    ImageResource favoritesSmall();

    @Source("cake_128.png")
    @ImageOptions(width = 72)
    ImageResource fun();


    //console128.png

    @Source("console128.png")
    @ImageOptions(width = 72)
    ImageResource console();

    @Source("console128.png")
    @ImageOptions(width = mobileSize)
    ImageResource consoleMobile();



    @Source("cake_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource funMobile();

    @Source("bird_vista_archigraphs.png")
    @ImageOptions(width = 72)
    ImageResource followSpot();

    @Source("bird_vista_archigraphs.png")
    @ImageOptions(width = mobileSize)
    ImageResource followSpotMobile();


    @Source("setlocation.png")
    @ImageOptions(width = 72)
    ImageResource locationmanual();

    @Source("setlocation.png")
    @ImageOptions(width = mobileSize)
    ImageResource locationmanualMobile();

    @Source("setlocation.png")
    @ImageOptions(width = 30)
    ImageResource mapButton();

    @Source("setlocation.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource mapButtonMobile();



    @Source("setlocation.png")
    @ImageOptions(width = mobileSize)
    ImageResource locationManualMobile();




    @Source("phone_transfer.png")
    @ImageOptions(width = mobileSize)
    ImageResource locationDeviceMobile();




    @Source("sleep_hotel_128.png")
    @ImageOptions(width = 72)
    ImageResource lodging();

    @Source("sleep_hotel_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource lodgingMobile();

    @Source("lock_128.png")
    @ImageOptions(width = 72)
    ImageResource login();

    @Source("lock_128.png")
    @ImageOptions(width = 30)
    ImageResource loginMobile();



    @Source("lock_128.png")
    @ImageOptions(width = 72)
    ImageResource loginSmall();

    @Source("unlock_128.png")
    @ImageOptions(width = 72)
    ImageResource logout();

    @Source("unlock_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource logoutMobile();

    @Source("markspot2.png")
    @ImageOptions(width = 72)
    ImageResource markspot();

    @Source("markspot2.png")
    @ImageOptions(width = mobileSize)
    ImageResource markspotMobile();



    @Source("removeX.png")
    ImageResource deleteX();

    @Source("add.png")
        //@ImageOptions(width = 72)
    ImageResource add();

    //it's 72 high
    //, SpotHolder spotHolder
    @Source("coupon_cut.png")
    @ImageOptions(width = 100)
    ImageResource coupon();

    @Source("coupon_cut.png")
    @ImageOptions(width = mobileSize)
    ImageResource couponMobile();


    @Source("calendar.png")
    @ImageOptions(width = 72)
    ImageResource event();


    @Source("system_preferences.png")
    @ImageOptions(width = 72)
    ImageResource settings();

    @Source("system_preferences.png")
    @ImageOptions(width = mobileSize)
    ImageResource settingsMobile();

    @Source("email.png")
    @ImageOptions(width = 72)
    ImageResource messaging();

    @Source("email.png")
    @ImageOptions(width = 30)
    ImageResource messagingMobile();

    @Source("notifications2.png")
    @ImageOptions(width = 72)
    ImageResource notifications();

    @Source("notifications2.png")
    @ImageOptions(width = 30)
    ImageResource notificationsMobile();

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


    @Source("lock_key.png")
    @ImageOptions(width = 72)
    ImageResource secretkey();

    @Source("lock_key.png")
    @ImageOptions(width = mobileSize)
    ImageResource secretkeyMobile();

//    @Source("really_angry.png")
//    @ImageOptions(width = 72)
//    ImageResource worsedriversBig();
//    @Source("really_angry.png")
//    @ImageOptions(width = 30)
//    ImageResource worsedriversSmall();
//    @Source("worseplaces.png")
//    @ImageOptions(width = 72)
//    ImageResource worseplacesBig();
//    @Source("worseplaces.png")
//    @ImageOptions(width = 30)
//    ImageResource worseplacesSmall();

    @Source("modem2_128.png")
    @ImageOptions(width = 72)
    ImageResource contact();

    @Source("modem2_128.png")
    @ImageOptions(width = mobileSize)
    ImageResource contactMobile();

    @Source("8coupons-med.png")
    ImageResource coupons8();

    @ImageOptions(width = 30)
    @Source("email_reply_01.png")
    ImageResource emailReplySmall();

//    @Source("markspot2.png")
//    @ImageOptions(width = 30)
//    ImageResource markspot30();


    @Source("advanced_options.png")
    @ImageOptions(width = buttonSize)
    ImageResource advancedOptionsButton();


    @Source("advanced_options.png")
    @ImageOptions(width = buttonSizeMobile)
    ImageResource advancedOptionsButtonMobile();




    /*
    there are four sizes for each image
    2 sets of 2
    1 set is for the header of the page
    2nd set are for any buttons

    sometimes we do not need a second set of images if we do not show the image on any buttons
     */
    @Source("cute_vehicle.png")
    @ImageOptions(width = 72)
    ImageResource drivers();



    @Source("cute_vehicle.png")
    @ImageOptions(width = mobileSize)
    ImageResource driversMobile();





    @Source("directory.png")
    @ImageOptions(width = 72)
    ImageResource directory();



    @Source("directory.png")
    @ImageOptions(width = mobileSize)
    ImageResource directoryMobile();


}
