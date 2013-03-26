package com.spotmouth.gwt.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.UserHolder;
import com.spotmouth.gwt.client.dto.UserRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import com.spotmouth.gwt.client.dto.ContentHolder;
import com.spotmouth.gwt.client.profile.*;
import org.vectomatic.dnd.DropPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/4/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSettingsPanel extends SpotBasePanel implements SpotMouthPanel {


    //https://github.com/laaglu/lib-gwt-file-test/blob/master/src/main/java/org/vectomatic/file/client/TestAppMain.java
    // Load the image in the document and in the case of success attach it to the viewer
    protected IUploader.OnFinishUploaderHandler onFinishUploaderHandler2 = new IUploader.OnFinishUploaderHandler() {
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
            //image.setWidth("75px");
            profileImagePanel.setWidget(image);
            // panelImages.add(image);
            //saveSessionContents();
            doSaveImage();
        }
    };

    public String getTitle() {
        return "Profile Settings";
    }

    public String getPageTitle() {
        return getTitle();
    }

    public ProfileSettingsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        UserHolder user = mywebapp.getAuthenticatedUser();
        if (user == null) {
            user = new UserHolder();
        }
        countryTextBox = getCountrySuggestBox(user.getCountryCode());
        stateTextBox = getStateSuggestBox(user.getState());
        citySuggestBox = getCitySuggestBox(user.getCity());
        zipcodeTextField = initZipCodeTextBox(user.getZip());
       // zipcodeTextField.setValue(user.getZip());
        contentTextArea.setValue(user.getAboutMe());
        //add(saveButton());
        Button saveButton = new Button();
        saveButton.addClickHandler(saveHandler);

        SuggestBox  tagSearchTextBox = getTagSuggestBox(user.getTagHolders());
        tagSearchTextBox.getElement().setId("tagbox");
        Anchor addTagAnchor = new Anchor("Add");
        addTagAnchor.getElement().setId("addTag");
        addTagAnchor.addClickHandler(addTagHandler);
        doProfilePic();
        Anchor removeProfileImageAnchor = new Anchor();
        removeProfileImageAnchor.addClickHandler(removeProfileImageHandler);

        defaultUploader = new MultiUploader();
        defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);

        FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);
        selectedTagsPanel.getElement().setAttribute("id", "seltags");

        FlowPanel suggestionsPanel = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);
        DropPanel dropPanel = getDropPanel();
        ProfileSettingsComposite profileSettingsComposite = new ProfileSettingsComposite(countryTextBox, citySuggestBox,
                stateTextBox, contentTextArea, saveButton, selectedTagsPanel, tagSearchTextBox, addTagAnchor,
                removeProfileImageAnchor, defaultUploader, panelImages, profileImagePanel, mywebapp,this,suggestionsPanel,dropPanel);
        add(profileSettingsComposite);
    }


    private void doProfilePic() {
        UserHolder user = mywebapp.getAuthenticatedUser();
        Image profileImage = null;
        if (user != null) {
            profileImage = getImage(user.getContentHolder(), "320x320");
        }
        if (profileImage == null) {
            profileImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
        }
        profileImagePanel.setWidget(profileImage);
    }

    SimplePanel profileImagePanel = new SimplePanel();
    ClickHandler removeProfileImageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
           // getMessagePanel().clear();
            //this will just remove all the contents for the user
            for (ContentHolder contentHolder : mywebapp.getAuthenticatedUser().getContentHolder().getContentHolders()) {
                GWT.log("Removing content " + contentHolder.getFileName());
                contentsToRemove.add(contentHolder);
            }
            doSaveImage();
        }
    };



    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    public void doSaveImage() {
        doSaveImage(null,null);
    }
    public void doSaveImage(String name,String data) {
        getMessagePanel().clear();
        GWT.log("doSaveImage");
        UserRequest userRequest = new UserRequest();
        userRequest.setFileName(name);
        userRequest.setFileData(data);
        userRequest.setUserHolder(mywebapp.getAuthenticatedUser());
        userRequest.setAuthToken(mywebapp.getAuthToken());
        UserHolder userHolder = userRequest.getUserHolder();
        userHolder.setContentsToRemove(contentsToRemove);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveUser(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.setAuthenticatedUser(mobileResponse.getUserHolder());
                    //need to update our own panel

                    doProfilePic();
                    getMessagePanel().displayMessage("Your profile has been saved");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    protected void doSave() {
        GWT.log("do save");
        UserRequest userRequest = new UserRequest();
        userRequest.setUserHolder(mywebapp.getAuthenticatedUser());
        userRequest.setAuthToken(mywebapp.getAuthToken());
        UserHolder userHolder = userRequest.getUserHolder();
        if (userHolder == null) {
            GWT.log("userholder is null");
        }
        userHolder.setAboutMe(contentTextArea.getValue());
        if (citySuggestBox != null) {
            userHolder.setCity(citySuggestBox.getValue());
        }
        if (stateTextBox != null) {
            userHolder.setState(stateTextBox.getValue());
        }
        if (zipcodeTextField != null) {
            userHolder.setZip(zipcodeTextField.getValue());
        }
        if (countryTextBox != null) {
            userHolder.setCountryCode(countryTextBox.getValue());
        }
        userHolder.setContentsToRemove(contentsToRemove);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveUser(userRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    UserHolder uh = mobileResponse.getUserHolder();
                    mywebapp.setAuthenticatedUser(mobileResponse.getUserHolder());
                    //do they have a profile picture?
                    //if they don't let's stay here and give them a nudge
                    boolean havePic = havePicture(mobileResponse);
                    if (!havePic) {
                        mywebapp.toggleViewUserProfile(uh.getId());
                        getMessagePanel().displayMessage("Your profile has been saved. ");
                        getMessagePanel().displayMessage("This would be a good time to set a profile picture and tell us about yourself.");
                    } else {
                        mywebapp.toggleMenu();
                        getMessagePanel().displayMessage("Your profile has been saved");
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
