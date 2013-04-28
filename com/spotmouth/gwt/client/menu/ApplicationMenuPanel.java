package com.spotmouth.gwt.client.menu;

import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;

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
