package com.spotmouth.gwt.client.messaging;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ItemHolder;

import java.util.Comparator;

public class NotificationsPanel extends SpotBasePanel implements SpotMouthPanel {

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.notificationsMobile();
        } else {
            return MyWebApp.resources.notifications();
        }

    }

    public String getPageTitle() {
        return getTitle();
    }




    public String getTitle() {
        return "Notifications";
    }

    public void toggleFirst() {
    }

    public boolean isLoginRequired() {
        return false;
    }

    public boolean isRootPanel() {
        return false;
    }

    public NotificationsPanel() {
    }

    public NotificationsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        //this.setStyleName("panel");
        this.addStyleName("ResultsPanel");
        //need to display the list in reverse order!
        // List<ItemHolder> items = new ArrayList<ItemHolder>();
        //need to sort...
        //items.addAll(mywebapp.getNotifications());
        Comparator comparator = new Comparator() {
            public int compare(Object o, Object o1) {
                ItemHolder i1 = (ItemHolder) o;
                ItemHolder i2 = (ItemHolder) o1;
                return i1.getCreateDateDisplay().compareTo(i2.getCreateDateDisplay());
            }
        };
        //sort(List list, Comparator c)
        java.util.Collections.sort(mywebapp.getNotifications(), comparator);
        addItems(mywebapp.getNotifications(), "Notifications", itemHandler);
        //addTitle();
    }

    ClickHandler itemHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ItemHolder itemHolder = clickMapItemHolder.get(b);
                viewMessage(itemHolder);
            }
        }
    };
}
