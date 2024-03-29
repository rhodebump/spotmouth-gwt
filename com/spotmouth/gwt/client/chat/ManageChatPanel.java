package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import org.vectomatic.dnd.DropPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/2/13
 * Time: 12:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageChatPanel extends SpotBasePanel implements SpotMouthPanel {
    //https://github.com/laaglu/lib-gwt-file-test/blob/master/src/main/java/org/vectomatic/file/client/TestAppMain.java
    // Load the image in the document and in the case of success attach it to the viewer
    private IUploader.OnFinishUploaderHandler onFinishUploaderHandler2 = new IUploader.OnFinishUploaderHandler() {
        public void onFinish(IUploader uploader) {
            if (uploader.getStatus() == IUploadStatus.Status.SUCCESS) {
                new PreloadedImage(uploader.fileUrl(), showImage2);
                // The server sends useful information to the client by default
                IUploader.UploadedInfo info = uploader.getServerInfo();
                System.out.println("File name " + info.name);
                System.out.println("File content-type " + info.ctype);
                System.out.println("File size " + info.size);
                // You can send any customized message and parse it
                System.out.println("Server message " + info.message);
                //okay, we don't have a final "URL" for this image, and we need one to be able to insert into
                //a wysiwyg editor
                //let's call the server and sweep through the session files and convert these to content
                //saveSessionContents();
            }
        }
    };
    // Attach an image to the pictures viewer
    private PreloadedImage.OnLoadPreloadedImageHandler showImage2 = new PreloadedImage.OnLoadPreloadedImageHandler() {
        public void onLoad(PreloadedImage image) {
            GWT.log("ManageContestPanel, showImage2");
            chatImagePanel.setWidget(image);
        }
    };



    /*
    called to push photo to contest
     */
    public void doSaveImage(String name, String data) {
        getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.uploadFile(name, data, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public String getTitle() {
        return "Manage Chat";
    }



    protected boolean isValid() {
        checkRequired(nameTextBox, "Name is required");
        checkRequired(contentTextArea, "Description is required");
        populate();
        validateDateRange();
        return (!getMessagePanel().isHaveMessages());
    }

    public void toggleFirst() {
        //this wipes out the placeholder
       // nameTextBox.setFocus(true);
    }



    public ManageChatPanel() {
    }



//    protected TextBox address1TextBox = null;
//    protected TextBox zipcodeTextBox = null;
    private SimplePanel chatImagePanel = new SimplePanel();


    public ClickHandler saveHandlerFacebook2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
          GWT.log("saveHandler.onClick");
            getItemHolder().setShareOnFacebook(true);
            saveIt();
        }
    };

    private ChatFormComposite cfc = null;


    public ManageChatPanel(MyWebApp mywebapp, final ItemHolder itemHolder) {
        super(mywebapp, false, true, false);
       setItemHolder(itemHolder);
        if (MyWebApp.isDesktop()) {
            nameTextBox = new TextField();
            nameTextBox.setValue(itemHolder.getTitle());

            contentTextArea = new TextArea();

            contentTextArea.setValue(itemHolder.getTextData());
            startDatePicker.setValue(itemHolder.getStartDate());
            endDatePicker.setValue(itemHolder.getEndDate());
            startDatePicker.setTabIndex(2);
            startDatePicker.getElement().setAttribute("placeholder", "Start Date");
            endDatePicker.setTabIndex(3);
            endDatePicker.getElement().setAttribute("placeholder", "End Date");

            SuggestBox tagSearchTextBox = getTagSuggestBox(itemHolder.getTagHolders());
            tagSearchTextBox.getElement().setId("mc_tags_inp");
            tagSearchTextBox.getElement().setAttribute("placeholder", "Start typing");
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
            FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);

            Button saveButton = saveButton();


            this.defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);
            Image mainImage = getImage(itemHolder.getContentHolder(), "320x320");
            if (mainImage != null) {
                chatImagePanel.setWidget(mainImage);
            }

            Button cancelButton  = new Button();
            cancelButton.addClickHandler(cancelChatHandler);
            DropPanel dropPanel = getDropPanel();


            Button shareOnFacebookButton = new Button();
            shareOnFacebookButton.addClickHandler(saveHandlerFacebook2);

            if (! mywebapp.isFacebookUser()) {
                //need to do directly on this element

                hideElement(shareOnFacebookButton.getElement());
            }

            this.cfc = new ChatFormComposite(nameTextBox, contentTextArea, startDatePicker, endDatePicker,
                 tagSearchTextBox, selectedTagsPanel, saveButton, cancelButton,
                     defaultUploader, chatImagePanel, mywebapp, itemHolder,suggestionsPanel,dropPanel,shareOnFacebookButton);
            add(cfc);
        }

    }


    private void populate() {

        getItemHolder().setStartDate(startDatePicker.getValue());
        getItemHolder().setEndDate(endDatePicker.getValue());
        getItemHolder().setTitle(nameTextBox.getValue());
        getItemHolder().setTextData(contentTextArea.getValue());



    }

    protected ClickHandler cancelChatHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.CHATS);
        }
    };


    AsyncCallback saveLocationAsSpotCallback2 = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            MobileResponse mobileResponse = (MobileResponse) response;
            if (mobileResponse.getStatus() == 1) {

                saveChat(mobileResponse.getSpotHolder().getId());
            } else {
                mywebapp.verifyDisplay();
                getMessagePanel().clear();
                getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
            }
        }
    };


    protected void doSave(){
        LocationResult currentLocationResult = new LocationResult();
        Location currentLocation = mywebapp.getCurrentLocation();
        if (currentLocation == null) {
            getMessagePanel().displayError("Need to set location first before saving chat");
            return;
        }
        currentLocation.setName("Location");
        currentLocationResult.setLocation(mywebapp.getCurrentLocation());
        //let's set to type of 3, a location
        currentLocationResult.getLocation().setSpotType(3);
        mywebapp.saveLocationAsSpot(currentLocationResult,"", saveLocationAsSpotCallback2);


    }

    protected void saveChat(Long spotId) {
        ChatRequest chatRequest = new ChatRequest();
        populate();
        getItemHolder().getSpotHolder().setId(spotId);
        chatRequest.setItemHolder(getItemHolder());

        chatRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveChat(chatRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                    ItemHolder itemHolder = mobileResponse.getItemHolder();
                    ViewChatPanel viewChatPanel = new ViewChatPanel(mywebapp, itemHolder);
                    mywebapp.swapCenter(viewChatPanel);
                    History.newItem(MyWebApp.CHAT_DETAIL + itemHolder.getId(), false);
                    getMessagePanel().displayMessage("Chat saved");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}

