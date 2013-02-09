package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.kiouri.sliderbar.client.event.BarValueChangedEvent;
import com.kiouri.sliderbar.client.event.BarValueChangedHandler;
import com.kiouri.sliderbar.client.solution.simplehorizontal.SliderBarSimpleHorizontal;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.contest.ViewContestPanel;
import com.spotmouth.gwt.client.dto.ContestHolder;
import com.spotmouth.gwt.client.dto.ContestRequest;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;

import java.util.Date;

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
            chatImagePanel.setWidget(image);
        }
    };

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.contestsMobile();
        } else {
            return MyWebApp.resources.contests();
        }
    }

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
        nameTextBox.setFocus(true);
    }

    public boolean isLoginRequired() {
        return false;
    }

    public ManageChatPanel() {
    }

   // private CheckBox geoGlobalCheckbox = new CheckBox("GEO Global");

    protected TextBox address1TextBox = null;
    protected TextBox zipcodeTextBox = null;
    private SimplePanel chatImagePanel = new SimplePanel();




    private ChatFormComposite cfc = null;

    //TextBox contestName
    public ManageChatPanel(MyWebApp mywebapp, final ItemHolder itemHolder) {
        super(mywebapp, false, true, false);
       setItemHolder(itemHolder);
        if (MyWebApp.isDesktop()) {
            nameTextBox = new TextBox();
            nameTextBox.setTabIndex(1);
            nameTextBox.getElement().setAttribute("placeholder", "Chat Name");
            nameTextBox.setValue(itemHolder.getTitle());

            contentTextArea = new TextArea();
            contentTextArea.setStyleName("mc_desc");
            contentTextArea.setVisibleLines(4);
            contentTextArea.setTabIndex(4);
            contentTextArea.setValue(itemHolder.getTextData());
            startDatePicker.setValue(itemHolder.getStartDate());
            endDatePicker.setValue(itemHolder.getEndDate());
            startDatePicker.setTabIndex(2);
            startDatePicker.getElement().setAttribute("placeholder", "Start Date");
            endDatePicker.setTabIndex(3);
            endDatePicker.getElement().setAttribute("placeholder", "End Date");

//            countryTextBox = getCountrySuggestBox(contestHolder.getCountryCode());
//            countryTextBox.getElement().setId("mc_sel_country");
//            countryTextBox.setStyleName("mc_select");
//            countryTextBox.setTabIndex(5);
//            stateTextBox = getStateSuggestBox(contestHolder.getState());
//            stateTextBox.getElement().setId("mc_sel_state");
//            stateTextBox.setStyleName("mc_select");
//            stateTextBox.setTabIndex(6);
//            //only show stateTextBox if there is country set
//            countryTextBox.addValueChangeHandler(vch);
//            stateTextBox.addValueChangeHandler(vch);
//            citySuggestBox = getCitySuggestBox(contestHolder.getCity());
//            citySuggestBox.getElement().setId("mc_sel_city");
//            citySuggestBox.setStyleName("mc_select");
//            citySuggestBox.setTabIndex(7);
//            citySuggestBox.addValueChangeHandler(vch);
//            //		<input type="text" placeholder="Zip Code" maxlength="6" tabindex="9" onblur="inpValidate(this, this.value);mcShowRadius(this.value);"/>
//            zipcodeTextBox = new TextBox();
//            zipcodeTextBox.getElement().setAttribute("placeholder", "Zip Code");
//            zipcodeTextBox.setTabIndex(9);
//            zipcodeTextBox.setMaxLength(6);
//            zipcodeTextBox.setValue(contestHolder.getZip());
//            zipcodeTextBox.addValueChangeHandler(vch);
//            //	<input type="text" placeholder="Address"  tabindex="8" onblur="inpValidate(this, this.value);mcShowRadius(this.value);"/>
//            address1TextBox = new TextBox();
//            address1TextBox.getElement().setAttribute("placeholder", "Address");
//            address1TextBox.setTabIndex(8);
//            address1TextBox.addValueChangeHandler(vch);
//            address1TextBox.setValue(contestHolder.getAddressLine1());
            //tagHolders.addAll(contestHolder.getTagHolders());
            SuggestBox tagSearchTextBox = getTagSuggestBox(itemHolder.getTagHolders());
            tagSearchTextBox.getElement().setId("mc_tags_inp");
            tagSearchTextBox.getElement().setAttribute("placeholder", "Start typing");
            //tagSearchTextBox.setTabIndex(11);
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);

            Button saveButton = saveButton();


            defaultUploader = new MultiUploader();
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler2);
            //do we have an iamge
            Image mainImage = getImage(itemHolder.getContentHolder(), "320x320");
            //contestImagePanel
            if (mainImage != null) {
                chatImagePanel.setWidget(mainImage);
            }
            this.cfc = new ChatFormComposite(nameTextBox, contentTextArea, startDatePicker, endDatePicker,
                 tagSearchTextBox, selectedTagsPanel, saveButton,
                     defaultUploader, chatImagePanel, mywebapp, itemHolder);
            add(cfc);
        }

    }

    private void populate() {

        getItemHolder().setStartDate(startDatePicker.getValue());
        getItemHolder().setEndDate(endDatePicker.getValue());
        //this is set directly via the iconStyleTextBox event in contestFormComposite


        getItemHolder().setTitle(nameTextBox.getValue());
        getItemHolder().setTextData(contentTextArea.getValue());



    }

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
        currentLocation.setName("Location");
        currentLocationResult.setLocation(mywebapp.getCurrentLocation());
        //let's set to type of 3, a location
        currentLocationResult.getLocation().setSpotType(3);
        mywebapp.saveLocationAsSpot(currentLocationResult, saveLocationAsSpotCallback2);


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

                    com.spotmouth.gwt.client.dto.SolrDocument solrDocument = mobileResponse.getSearchQueryResponse().getResults().get(0);
                    Long itemId = solrDocument.getFirstLong("georepoitemid_l");
                    ViewChatPanel viewChatPanel = new ViewChatPanel(mywebapp, solrDocument);
                    mywebapp.swapCenter(viewChatPanel);
                    History.newItem(MyWebApp.CHAT_DETAIL + itemId, false);
                    getMessagePanel().displayMessage("Chat saved");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}

