package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/7/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageSpotPanel extends SpotBasePanel implements SpotMouthPanel {
    public boolean showBackToResults() {
        return false;
    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getManageSpotPanel();
    }

    private SpotHolder spotHolder = null;
    ClickHandler changeListingHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            changeListingInfo();
        }
    };
    ClickHandler manageProductsHandler = new ClickHandler() {
        public void onClick(ClickEvent clickEvent) {
            manageProducts();
        }
    };
    ClickHandler manageMediaHandler = new ClickHandler() {
        public void onClick(ClickEvent clickEvent) {
            manageMedia();
        }
    };
    ClickHandler newCouponHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            CouponForm placeForm = new CouponForm(mywebapp, spotHolder);
            mywebapp.swapCenter(placeForm);
        }
    };
    ClickHandler lockSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            lockSpot();
        }
    };

    private void lockSpot() {
        getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        SaveSpotRequest spotRequest = new SaveSpotRequest();
        spotRequest.setSpotHolder(spotHolder);
        spotRequest.setAuthToken(mywebapp.getAuthToken());
        myService.lockSpot(spotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Spot is now locked");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    ClickHandler newEventHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            String token = MyWebApp.CREATE_EVENT + spotHolder.getId();
            History.newItem(token, false);
            EventForm eventForm = new EventForm(mywebapp, spotHolder);
            mywebapp.swapCenter(eventForm);
        }
    };
    ClickHandler deleteSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            deleteSpot();
        }
    };

    private void deleteSpot() {
        SaveSpotRequest ssr = new SaveSpotRequest();
        ssr.setSpotHolder(spotHolder);
        ssr.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteSpot(ssr, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //need to toggle back and display success message
                    mywebapp.toggleMenu();
                    getMessagePanel().displayMessage("This spot has been deleted");
                } else {
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public ManageSpotPanel(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        //we are here, in a management screen,
        //if this isn't locked, let's lock it now
        this.spotHolder = spotHolder;
        //june 2012, let's not automatically lock it, but allow users to lock it
//        if (!spotHolder.isLockedForEdit()) {
//             lockSpot();
//        }
        //change listing info
        add(changeListingInfoButton());
        add(manageMediaButton());
        add(manageProductsButton());
        FlowPanel topPanel = new FlowPanel();
        topPanel.setStyleName("menugrouping");
        topPanel.addStyleName("clearing");
        add(topPanel);
        addImageIcon(newCouponHandler, "Create Coupon", new Image(MyWebApp.resources.coupon()), topPanel, "Create Coupon");
        addImageIcon(newEventHandler, "Create Event", new Image(MyWebApp.resources.event()), topPanel, "Create Event");
        //addImageIcon(viewMapHandler, "View Map", MyWebApp.resources.locationmanualBig(), topPanel, "View this spot on a map");
        //groups
        //let's display all groups for this spot, clicking on the group takes one to manage the group
        //wait... lets
        GWT.log("spotHolder.isLockedForEdit() " + spotHolder.isLockedForEdit());
        if (!spotHolder.isLockedForEdit()) {
            //spot  is always locked now
            addImageIcon(lockSpotHandler, "Lock Spot", new Image(MyWebApp.resources.lockSpot()), topPanel, "Lock Spot");
        } else {
        }
        //spot detail page
        addSpotLink(spotHolder);
        //add to favorites
        addToFavorites(spotHolder);
        addDeleteButton();
    }

    protected void addDeleteButton() {
        Label deleteMarkLabel = new Label("Delete Spot");
        fixButton(deleteMarkLabel);
        deleteMarkLabel.addClickHandler(deleteSpotHandler);
        addImageToButton(deleteMarkLabel, MyWebApp.resources.deleteX(), MyWebApp.resources.deleteX());
        add(deleteMarkLabel);
    }

    Label manageProductsButton() {
        Label btn = new Label("Manage Products");
        btn.addClickHandler(manageProductsHandler);
        fixButton(btn);
        return btn;
    }

    Label manageMediaButton() {
        Label btn = new Label("Manage Photos");
        btn.addClickHandler(manageMediaHandler);
        fixButton(btn);
        return btn;
    }

    Label changeListingInfoButton() {
        Label btn = new Label("Change Listing Info");
        btn.addClickHandler(changeListingHandler);
        fixButton(btn);
        return btn;
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void changeListingInfo() {
        PlaceForm placeForm = new PlaceForm(mywebapp, spotHolder);
        mywebapp.swapCenter(placeForm);
    }

    private void manageProducts() {
        //let's update the url so we want to come back to manage spot screen (here), we do!
        //while this will issue location event, nothing will respond to it
        getMessagePanel().clear();
        String token = MyWebApp.MANAGE_PRODUCTS + spotHolder.getId();
        History.newItem(token, false);
        ManageProductsPanel manageProductsPanel = new ManageProductsPanel(mywebapp, spotHolder);
        mywebapp.swapCenter(manageProductsPanel);
    }

    public static String MANAGE_MEDIA = "managemedia/";

    private void manageMedia() {
        //let's update the url so we want to come back to manage spot screen (here), we do!
        //while this will issue location event, nothing will respond to it
        History.newItem(MANAGE_MEDIA, false);
        MediaForm mediaForm = new MediaForm(mywebapp, spotHolder);
        mywebapp.swapCenter(mediaForm);
    }
}
