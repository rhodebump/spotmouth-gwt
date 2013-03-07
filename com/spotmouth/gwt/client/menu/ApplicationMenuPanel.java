package com.spotmouth.gwt.client.menu;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.coupon.Coupons8Panel;
import com.spotmouth.gwt.client.directory.DirectoryCountriesPanel;
import com.spotmouth.gwt.client.friends.ManageFriendsPanel;
import com.spotmouth.gwt.client.location.SetLocationManuallyPanel;
import com.spotmouth.gwt.client.messaging.MessagingPanel;
import com.spotmouth.gwt.client.messaging.NotificationsPanel;
import com.spotmouth.gwt.client.usergroups.ViewUserGroupsPanel;

/*
* this constructs links to all application menus
*/
public class ApplicationMenuPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Menu";
    }






    //Grid grid = null;
    public ApplicationMenuPanel(MyWebApp mywebapp) {
        super(mywebapp,false);
        if (MyWebApp.isDesktop()) {
            MenuComposite menuComposite = new MenuComposite();

            int personalCount = 0;

            if (mywebapp.getMessages() != null) {
                int inboxCount = mywebapp.getMessages().getInMessages().size();
                menuComposite.setMessageCount(inboxCount);
                personalCount += inboxCount;

            }
            if (mywebapp.getFriendsAndGroups() != null) {
                int groupCount = mywebapp.getFriendsAndGroups().getGroupHolders().size();
                menuComposite.setGroupCount(groupCount);
                personalCount += groupCount;
                int friendCount = mywebapp.getFriendsAndGroups().getFriendHolders().size();
                menuComposite.setFriendCount(friendCount);

                personalCount += friendCount;
            }
            if (mywebapp.getNotifications() != null) {
                int notificationCount = mywebapp.getNotifications().size();
                menuComposite.setNotificationCount(notificationCount);
                personalCount += notificationCount;
            }

           if (personalCount > 0) {
               menuComposite.setPersonalCount(personalCount);
           }


            add(menuComposite);


            return;
        }
        Label frequent = new Label("Frequent");
        frequent.setStyleName("h2");
        add(frequent);


        add(getTopPanel());


        //addImageIcon(searchHandler, "Search", mywebapp.resources.searchBig(), mywebapp.resources.searchSmall(), topPanel);
        FlowPanel personalPanel = new FlowPanel();
        personalPanel.setStyleName("menugrouping");
        personalPanel.addStyleName("clearing");
        Label Personal = new Label("Personal");
        Personal.setStyleName("h2");
        add(Personal);
        add(personalPanel);
        addImageIcon(manageProfileHandler, new AccountSettingsPanel(), personalPanel);
        addImageIcon(manageFriendsHandler, new ManageFriendsPanel(), personalPanel);
        addImageIcon(toggleViewUserGroupsHandler, new ViewUserGroupsPanel(), personalPanel);

      //  Hyperlink messagingHyperLink = new Hyperlink("Messaging",MyWebApp.MESSAGING);
        addImageIcon(MyWebApp.MESSAGING, new MessagingPanel(), personalPanel);



        addImageIcon(showNotificationsHandler, new NotificationsPanel(), personalPanel);


        addImageIcon(MyWebApp.FAVORITES, new FavoritesPanel(), personalPanel);


        FlowPanel linksPanel = new FlowPanel();
        linksPanel.setStyleName("menugrouping");
        linksPanel.addStyleName("clearing");
        Label Find = new Label("Find");
        Find.setStyleName("h2");
        add(Find);
        add(linksPanel);
        addImageIcon(showMostRecentlyVoted, "Most Recent Votes", mywebapp.resources.thumbsUp(), mywebapp.resources.thumbsUpMobile(), linksPanel, null);


        addImageIcon("Dining",MyWebApp.DINING,  mywebapp.resources.dining(), mywebapp.resources.diningMobile(), linksPanel, null);



        addImageIcon("Lodging",MyWebApp.LODGING, mywebapp.resources.lodging(), mywebapp.resources.lodgingMobile(), linksPanel, null);






        addImageIcon("Drinking",MyWebApp.DRINKING,  mywebapp.resources.drinking(), mywebapp.resources.drinkingMobile(), linksPanel, null);
         //addImageIcon(String token, String text, ImageResource bigImage, ImageResource mobileImage,FlowPanel flowPanel, String tooltip)

        addImageIcon("Fun" , MyWebApp.FUN, mywebapp.resources.fun(), mywebapp.resources.funMobile(), linksPanel, null);

        addImageIcon(MyWebApp.COUPONS, new Coupons8Panel(), linksPanel);


        addImageIcon(MyWebApp.SECRET_KEY, new SecretKeyForm(), linksPanel);
        FlowPanel bottomPanel = new FlowPanel();
        bottomPanel.setStyleName("menugrouping");
        bottomPanel.addStyleName("clearing");
        Label tools = new Label("Tools");
        tools.setStyleName("h2");
        add(tools);
        add(bottomPanel);
        addImageIcon(MyWebApp.SET_LOCATION, new SetLocationManuallyPanel(mywebapp), bottomPanel);
        //addImageIcon(setLocationFromDeviceHandler, "Location From Device", mywebapp.resources.locationDevice(), mywebapp.resources.locationDeviceMobile(), bottomPanel, null);
        addImageIcon(settingsHandler, new SettingsPanel(), bottomPanel);
        addImageIcon(MyWebApp.CONTACT, new ContactPanel(), bottomPanel);

        //Hyperlink aboutHyperLink = new Hyperlink("About",MyWebApp.ABOUT);
        addImageIcon(MyWebApp.ABOUT, new AboutPanel(), bottomPanel);


        addImageIcon(MyWebApp.CONSOLE, new ConsolePanel(), bottomPanel);



        addImageIcon(MyWebApp.DIRECTORY, new DirectoryCountriesPanel(), bottomPanel);

    }










//    ClickHandler aboutHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleAbout();
//        }
//    };
//    ClickHandler useSecretKeyHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleSecretKey(true);
//        }
//    };



//    ClickHandler setLocationManuallyHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleSetLocationManually(true);
//        }
//    };


//    ClickHandler showFunHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleFun(true);
//        }
//    };



    //ClickHandler directoryHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleDirectory(true);
//        }
//    };




    ClickHandler showMostRecentlyVoted = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleMostRecentlyVoted(true);
        }
    };


    //ClickHandler showDrinkingHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleDrinking(true);
//        }
//    };
//    ClickHandler showDiningHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleDining(true);
//        }
//    };

//    ClickHandler showLodgingHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleLodging(true);
//        }
//    };
//    ClickHandler showMessagingHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleMessaging(true);
//        }
//    };
    ClickHandler showNotificationsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleNotifications(true);
        }
    };


//    ClickHandler favoritesHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleFavorites(true);
//        }
//    };

    ClickHandler toggleViewUserGroupsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
           // mywebapp.toggleViewUserGroups();
            History.newItem(MyWebApp.VIEW_USER_GROUPS);
        }
    };
    ClickHandler manageProfileHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleAccountSettings(true);
        }
    };
    ClickHandler manageFriendsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //mywebapp.toggleManageFriends(null);
            History.newItem(MyWebApp.MANAGE_FRIENDS);
        }
    };









    ClickHandler settingsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mywebapp.toggleSettings(true);
        }
    };
   // ClickHandler contactHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            mywebapp.toggleContact(true);
//        }
//    };


    public boolean isLoginRequired() {
        return false;
    }

    public void toggleFirst() {
    }


}
