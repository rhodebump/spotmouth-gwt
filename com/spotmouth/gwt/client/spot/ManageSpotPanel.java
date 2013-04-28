package com.spotmouth.gwt.client.spot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.coupon.CouponForm;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SaveSpotRequest;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.event.EventForm;
import com.spotmouth.gwt.client.product.ManageProductsPanel;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/7/12
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageSpotPanel extends SpotBasePanel implements SpotMouthPanel {

    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Manage Spot";
    }



    protected IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                new PreloadedImage(uploader.fileUrl(), showImage);
                // The server sends useful information to the client by default
                IUploader.UploadedInfo info = uploader.getServerInfo();
                System.out.println("File name " + info.name);
                System.out.println("File content-type " + info.ctype);
                System.out.println("File size " + info.size);
                // You can send any customized message and parse it
                System.out.println("Server message " + info.message);

            }
        }
    };
    // Attach an image to the pictures viewer
    private PreloadedImage.OnLoadPreloadedImageHandler showImage = new PreloadedImage.OnLoadPreloadedImageHandler() {
        public void onLoad(PreloadedImage image) {
            //image.setWidth("75px");
            spotImagePanel.setWidget(image);
            doSaveImage();
        }
    };

    public void doSaveImage() {
        doSaveImage(null, null);
    }

    public void doSaveImage(String name, String data) {
        getMessagePanel().clear();
        SaveSpotRequest userRequest = new SaveSpotRequest();
        userRequest.setFileName(name);
        userRequest.setFileData(data);
        userRequest.setSpotHolder(spotHolder);
        userRequest.setAuthToken(mywebapp.getAuthToken());
        //UserHolder userHolder = userRequest.getUserHolder();
        userRequest.setContentsToRemove(contentsToRemove);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSpot(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Spot has been saved");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }




    private SpotHolder spotHolder = null;



    public void unlockSpot() {
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
                    getMessagePanel().displayMessage("Spot is now  un-locked");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
    public void lockSpot() {
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

    public void createCoupon() {
        ItemHolder itemHolder = new ItemHolder();
        CouponForm placeForm = new CouponForm(mywebapp, spotHolder,itemHolder);
        mywebapp.swapCenter(placeForm);
    }
    public void doDetails() {

        String token = MyWebApp.SPOT_DETAIL + spotHolder.getId();


        History.newItem(token);
    }

    public void addToFavorites() {
        String token = MyWebApp.ADD_SPOT_FRIEND + spotHolder.getId();
        History.newItem(token);
    }

    public void createEvent() {
        String token = MyWebApp.CREATE_EVENT + spotHolder.getId();
        History.newItem(token, false);
        ItemHolder itemHolder = new ItemHolder();

        EventForm eventForm = new EventForm(mywebapp, spotHolder,itemHolder);
        mywebapp.swapCenter(eventForm);
    }

    ClickHandler newEventHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            createEvent();
        }
    };



    AsyncCallback spotDeletedCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("This spot has been deleted");
            getMessagePanel().displayMessage(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("This spot has been deleted");
        }
    };


    AsyncCallback profileDeletedMessage = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayMessage("Could not delete profile.");
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Your profile has been deleted");
        }
    };
    public void deleteSpot() {
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
                    //mywebapp.toggleMenu();
                    mywebapp.getResultsPanel().setDirty(true);
                    mywebapp.toggleSearch(spotDeletedCallback);
                    //History.newItem(MyWebApp.RE);

                } else {
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private SimplePanel spotImagePanel = new SimplePanel();

    public ManageSpotPanel(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;

            Image mainImage = getImage(spotHolder.getContentHolder(), "320x320");
            if (mainImage == null) {
                mainImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
            }
            spotImagePanel.setWidget(mainImage);
            this.defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
            ManageSpotComposite msc = new ManageSpotComposite(this, spotImagePanel, defaultUploader,spotHolder);
            msc.setName(spotHolder.getName());
            add(msc);
            return;


    }




    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    public void changeListingInfo() {
        //we need to update the URL, so if we need to, we can go back to manage spot panel
        String token = MyWebApp.LISTING_INFO + spotHolder.getId();

        History.newItem(token,false );
        PlaceForm placeForm = new PlaceForm(mywebapp, spotHolder);
        mywebapp.swapCenter(placeForm);
    }

    public void manageProducts() {
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
