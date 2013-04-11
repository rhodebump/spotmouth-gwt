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
        setActiveTabId("menuli");

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



    }









    public void toggleFirst() {
    }


}
