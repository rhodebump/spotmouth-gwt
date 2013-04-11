package com.spotmouth.gwt.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.H1;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.mark.MarkDetailComposite;
import com.spotmouth.gwt.client.mark.ReplyComposite;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
//import com.google.gwt.user.client.Element;

public class ItemDetailPanel extends SpotBasePanel implements SpotMouthPanel {




    protected ClickHandler replyHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {

            Element replyPanel = DOM.getElementById("md_reply");
            replyPanel.removeClassName("hideme");


            Element cancelButton = DOM.getElementById("md_cancel_btn");
            cancelButton.removeClassName("hideme");


            //	mdCancelBtn.style.display = "block";
            replyButton.setVisible(false);

        }
    };


    protected ClickHandler cancelHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {


            Element replyPanel = DOM.getElementById("md_reply");
            replyPanel.addClassName("hideme");


            Element cancelButton = DOM.getElementById("md_cancel_btn");
            cancelButton.addClassName("hideme");
            replyButton.setVisible(true);

        }
    };



    public void addedToDom() {
        super.addedToDom();
        //do nothing but override
        Element replyPanel = DOM.getElementById("md_reply");
        if (replyPanel != null) {
            replyPanel.addClassName("hideme");
        }
        Element cancelButton = DOM.getElementById("md_cancel_btn");
        if (cancelButton != null) {
            cancelButton.addClassName("hideme");
        }


        //get anything with this class and hide
        //btm_input_h
//
        Element md_key_input = DOM.getElementById("md_key_input");

        if (md_key_input != null) {
            md_key_input.addClassName("hideme");
        }

        Element md_tag_input = DOM.getElementById("md_tag_input");

        if (md_tag_input != null) {
            md_tag_input.addClassName("hideme");
        }







    }


    protected ClickHandler killPhotoHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {

            Element element = DOM.getElementById("md_photo_view");
            element.setClassName("md_hid");
            //need to add "md_photo_view" class="md_hid"


        }
    };




    private String title = "Mark";

    public String getTitle() {
        return title;
    }

    public boolean isRootPanel() {
        return false;
    }


    InlineLabel replyButton = new InlineLabel("reply");



    private Long selectedItemId = null;
    private MobileResponse itemResponse = null;

    public ItemDetailPanel(MyWebApp mywebapp, MobileResponse itemResponse) {
        super(mywebapp);
        this.itemResponse = itemResponse;
        if (MyWebApp.isDesktop()) {
            ItemHolder itemHolder = itemResponse.getItemHolder();
            SpotHolder spotHolder = itemHolder.getSpotHolder();
            Image mainImage = getImage(spotHolder.getContentHolder(), "320x320");
            if (mainImage == null) {
                mainImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
            }
            //<button class="btn_blue">Mark Spot</button>
            Button markSpotButton = new Button("Mark Spot");
            markSpotButton.setStyleName("btn_blue");
            markSpotButton.addClickHandler(markSpotHandler);
            Button viewMapButton = new Button("View Map");
            viewMapButton.getElement().setId("md_show_map");
            viewMapButton.addClickHandler(viewMapHandler);

            Anchor usernameAnchor = new Anchor();
            if (itemHolder.getUserHolder() == null) {
                usernameAnchor.setText("Anonymous");
            } else {
                usernameAnchor.setText(itemHolder.getUserHolder().getUsername());
                String token = "#" + MyWebApp.VIEW_USER_PROFILE + itemHolder.getUserHolder().getId();
                usernameAnchor.setHref(token);
            }
            UserHolder user = mywebapp.getAuthenticatedUser();
            Image profileImage = null;
            if (user != null) {
                profileImage = getImage(user.getContentHolder(), "320x320");
            }
            if (profileImage == null) {
                profileImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
            }

            FlowPanel markContentPanel = new FlowPanel();
            markContentPanel.getElement().setId("md_mark_photo");
            if (itemHolder.getContentHolder() != null) {
                for (ContentHolder contentHolder : itemHolder.getContentHolder().getContentHolders()) {


                    Image image = getImage(contentHolder, "320x320");
                    image.setStyleName("md_pop");
                    SpotSpan spotSpan = new SpotSpan();
                    spotSpan.setStyleName("md_photo_thumb");
                    spotSpan.add(image);
                    markContentPanel.add(spotSpan);

                    //we need to add handler to each image so when clicked we bring it up big
                    imageMap.put(image,contentHolder);
                    image.addClickHandler(imageClickHandler);
                }
            }
            ULPanel repliesULPanel = new ULPanel();
            //<ul id="md_replies" class="md_reply_marks">
            GWT.log("There are " + itemHolder.getChildrenItemHolders().size() + " replies");

            repliesULPanel.getElement().setId("md_replies");
            repliesULPanel.setStyleName("md_reply_marks");
            for (ItemHolder replyHolder : itemHolder.getChildrenItemHolders()) {
                UserHolder itemUser = replyHolder.getUserHolder();
                Image itemUserImage = null;
                if (itemUser != null) {
                    itemUserImage = getImage(itemUser.getContentHolder(), "320x320");
                }
                if (itemUserImage == null) {
                    itemUserImage = new Image(MyWebApp.resources.spot_image_placeholder320x320());
                }
                itemUserImage.setStyleName("md_ava");
                FlowPanel replyContents = new FlowPanel();
                //<h5>Attached Files</h5>
                replyContents.add(new HTML("<h5>Attached Files</h5>"));
                //	<span><img src="september-12-autumn_invaders__97-calendar-1280x1024 (1).jpg" class="md_pop"/></span>
                if (replyHolder.getContentHolder() != null) {
                    for (ContentHolder contentHolder : replyHolder.getContentHolder().getContentHolders()) {
                        SpotSpan span = new SpotSpan();
                        Image image = getImage(contentHolder, "320x320");
                        image.setStyleName("md_pop");
                        replyContents.add(span);
                    }
                }

                ReplyComposite replyComposite = new ReplyComposite(itemUserImage, replyContents);
                replyComposite.setStyleName("md_reply_mark");
                repliesULPanel.add(replyComposite);
                if (itemUser == null) {
                    replyComposite.setUsername("Anonymous");
                } else {
                    replyComposite.setUsername(itemUser.getUsername());
                }
                replyComposite.setReplyText(replyHolder.getTextData());

            }
            //span id="kill_photo">x</span
            InlineLabel killPhoto = new InlineLabel("x");
            killPhoto.getElement().setId("kill_photo");
            killPhoto.addClickHandler(killPhotoHandler);

            FlowPanel itemTagsPanel = new FlowPanel();
            for(TagHolder itemTag:itemHolder.getTagHolders()) {
                InlineLabel inlineLabel = new InlineLabel(itemTag.getName());
                itemTagsPanel.add(inlineLabel);
            }

            Anchor backToSearchResultsAnchor = getBackAnchor();



            replyButton.setStyleName("md_func");
            replyButton.addStyleName("button");
            replyButton.getElement().setId("md_reply_btn");
            replyButton.addClickHandler(replyHandler);

            InlineLabel cancelButton = new InlineLabel("cancel");
            cancelButton.setStyleName("md_func");
            cancelButton.addStyleName("button");
            cancelButton.getElement().setId("md_cancel_btn");
            cancelButton.addClickHandler(cancelHandler);


            SuggestBox tagSearchTextBox = getTagSuggestBox(null);
            FlowPanel selectedTagsPanel = widgetSelectedTagsPanelMap.get(tagSearchTextBox);

            FlowPanel selectedTagsPanel2 = widgetSelectedTagsPanelMap2.get(tagSearchTextBox);


            selectedTagsPanel.setStyleName("selected_tags");
            TextArea saySomethingTextArea = new TextArea();
            //we are using markData1 because we are creating new form, not using the old non-advanced form
            MarkData replyMarkData = new MarkData();
            initAutoGroup(saySomethingTextArea, replyMarkData);


            Button saveReplyButton = new Button("reply");
            saveReplyButton.addClickHandler(saveHandler2);
            widgetMarkDataMap.put(saveReplyButton, replyMarkData);


            //LocationResult locationResult = markData.expandData.locationResult;
            replyMarkData.expandData = new ExpandData();

            replyMarkData.replyItemHolder = itemHolder;
            replyMarkData.spotHolder = itemHolder.getSpotHolder();


           this.defaultUploader = new MultiUploader();
           // this.defaultUploader =  multiUploader;
            FlowPanel panelImages = new FlowPanel();

            IUploader.OnFinishUploaderHandler onFinishUploaderHandler = getOnFinishUploaderHandlerDetails(panelImages);
            defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

            Button addTagButton = new Button("Add");
              addTagButton.addClickHandler(addTagHandler);

            H1 repliesH1 = new H1();
            repliesH1.setText("Replies");


            if ( itemHolder.getChildrenItemHolders().isEmpty()) {
                repliesH1.setVisible(false);

            }

            this.markDetailComposite = new MarkDetailComposite(mainImage, markSpotButton, viewMapButton, usernameAnchor, profileImage, markContentPanel, repliesULPanel,bigImage,
                    killPhoto,itemTagsPanel,backToSearchResultsAnchor,replyButton,cancelButton,selectedTagsPanel,tagSearchTextBox,replyMarkData.secretKeyTextBox,replyMarkData.saySomethingTextArea,
                    saveReplyButton,defaultUploader,panelImages,addTagButton,repliesH1,selectedTagsPanel2);
            markDetailComposite.setMarkContent(itemHolder.getTextData());
            markDetailComposite.setFullAddress(spotHolder.getAddressLabel());
            markDetailComposite.setPhoneNumber(spotHolder.getVoicephone());
            markDetailComposite.setSpotName(spotHolder.getName());
            markDetailComposite.setMarkDate(itemHolder.getCreateDateDisplay());


            FormPanel myform = new FormPanel();
            setupRootPanelForm(myform, replyMarkData);
            myform.setWidget(markDetailComposite);
            add(myform);

            hideMapAnchor.setStyleName("hidemap");
            hideMapAnchor.addClickHandler(hideMapHandler);
            hideMapAnchor.setVisible(false);
            add(hideMapAnchor);
            add(mapHolderPanel);


            return;
        }
        this.addStyleName("ItemDetailPanel");
        this.itemResponse = itemResponse;
        displayResponse(itemResponse);
    }

    private MarkDetailComposite markDetailComposite = null;



    ClickHandler markSpotHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ItemHolder itemHolder = itemResponse.getItemHolder();
            SpotHolder spotHolder = itemHolder.getSpotHolder();
            History.newItem(MyWebApp.LEAVE_SPOT_MARK + spotHolder.getId());
        }
    };
    ClickHandler viewMapHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Location location = new Location();
            ItemHolder itemHolder = itemResponse.getItemHolder();
            location.setLatitude(itemHolder.getLatitude());
            location.setLongitude(itemHolder.getLongitude());
            History.newItem(MyWebApp.MAP);
            SpotMap spotMap = new SpotMap(mywebapp, location);
            mywebapp.swapCenter(spotMap);
        }
    };

    FlowPanel mapHolderPanel = new FlowPanel();
    private Anchor hideMapAnchor = new Anchor("Show Information");

    ClickHandler hideMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            mapHolderPanel.clear();
            markDetailComposite.setVisible(true);
            hideMapAnchor.setVisible(false);
        }
    };
    ClickHandler viewMapHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Location location = new Location();
            ItemHolder itemHolder = itemResponse.getItemHolder();
            location.setLatitude(itemHolder.getLatitude());
            location.setLongitude(itemHolder.getLongitude());
            SpotMap spotMap = new SpotMap(mywebapp, location);
            //let's set the detail object to not display
            //add this and a link to enable detail again
            markDetailComposite.setVisible(false);
            hideMapAnchor.setVisible(true);
            mapHolderPanel.add(spotMap);
        }
    };


    ClickHandler replyMarkHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ItemHolder replyItem = new ItemHolder();
            replyItem.setParentItemHolder(getItemHolder());
            replyItem.setSpotHolder(getItemHolder().getSpotHolder());
            mywebapp.toggleLeaveMark(replyItem);
        }
    };
    ClickHandler deleteMarkHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            deleteItem();
        }
    };

    private void deleteItem() {
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        leaveMarkRequest.setItemHolder(getItemHolder());
        leaveMarkRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteItem(leaveMarkRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //need to toggle back and display success message
                    mywebapp.toggleMenu();
                    getMessagePanel().displayMessage("This item has been deleted");
                } else {
                    getMessagePanel().clear();
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void addDateRangeDisplay(ItemHolder itemHolder) {
        HTML dateRangeLabel = new HTML(itemHolder.getStartDate() + " though " + itemHolder.getEndDate());
        addFieldset(dateRangeLabel, "When", "daterange");
    }

    private void displayResponse(MobileResponse mobileResponse) {
        this.itemResponse = mobileResponse;
        setItemHolder(mobileResponse.getItemHolder());
        SpotHolder spotHolder = getItemHolder().getSpotHolder();
        boolean isSpot = (spotHolder != null && spotHolder.getId() != null);
        this.title = spotHolder.getName();
        if (isSpot) {
            if (spotHolder.isPlace()) {
                doPlace(spotHolder, true);
            } else {
                doPlate(spotHolder, true);
            }
            Hyperlink spotNameButton = getSpotLink("Go to this spot", spotHolder.getId());
            spotNameButton.setStyleName("whiteButton");
            add(spotNameButton);
        }
        add(contentsPanel);
        addContentHolder(getItemHolder().getContentHolder(), false, true);
        HTML textData = new HTML(getItemHolder().getTextData());
        addFieldset(textData, "Mark", "mark");
        HTML createdAtLabel = new HTML(getItemHolder().getCreateDateDisplay());
        //Label spotNameLabel = new Label(itemHolder.getCreateDateDisplay());
        //if it's a coupon or event, let's show the date range
        if (getItemHolder().isCoupon()) {
            addDateRangeDisplay(getItemHolder());
        } else if (getItemHolder().isEvent()) {
            addDateRangeDisplay(getItemHolder());
        }
        addFieldset(createdAtLabel, "Created At:", "createdat");
        ContestVotingPanel cvp = new ContestVotingPanel(mywebapp, mobileResponse.getContestQueryResponse(), getItemHolder().getId());
        add(cvp);
        Label replyMarkLabel = new Label("Reply");
        fixButton(replyMarkLabel);
        replyMarkLabel.addClickHandler(replyMarkHandler);

        //addImageToButton(replyMarkLabel, MyWebApp.resources.emailReplySmall(), MyWebApp.resources.emailReplySmall());
        add(replyMarkLabel);

        if (isSpot) {
            FlowPanel topPanel = new FlowPanel();
            topPanel.setStyleName("menugrouping");
            topPanel.addStyleName("clearing");
            add(topPanel);
            if (MyWebApp.isSmallFormat()) {
                addImageIcon(markSpotHandler, "Mark Spot", new Image(MyWebApp.resources.markspotMobile()), topPanel, "Mark this spot");
                addImageIcon(viewMapHandler, "View Map", new Image(MyWebApp.resources.locationmanualMobile()), topPanel, "View this mark on a map");
            } else {
                addImageIcon(markSpotHandler, "Mark Spot", new Image(MyWebApp.resources.markspot()), topPanel, "Mark this spot");
                ///addImageIcon(viewMapHandler, "View Map", new Image(MyWebApp.resources.locationmanual()), topPanel, "View this mark on a map");
            }
        }
        if (getItemHolder().getUserHolder() != null) {
            String token = MyWebApp.VIEW_USER_PROFILE + getItemHolder().getUserHolder().getId();
            Hyperlink userLabel = new Hyperlink(getItemHolder().getUserHolder().getUsername(), token);
            userLabel.setStyleName("whiteButton");
            //userLabel.addClickHandler(clickUserHandler);
            add(userLabel);
        }
        if (haveAccess()) {
            addDeleteButton();
        }
    }

    private boolean haveAccess() {
        if (mywebapp.getAuthenticatedUser() == null) {
            return false;
        } else {
            if (mywebapp.getAuthenticatedUser().isAdmin()) {
                return true;
            }
            ItemHolder itemHolder = itemResponse.getItemHolder();
            UserHolder userHolder = itemHolder.getUserHolder();
            if (userHolder == null) {
                return false;
            }
            if (mywebapp.getAuthenticatedUser().getId().equals(userHolder.getId())) {
                return true;
            }
        }
        return false;
    }

    public void toggleFirst() {
    }



    protected void addDeleteButton() {
        Label deleteMarkLabel = new Label("Delete Item");
        fixButton(deleteMarkLabel);
        deleteMarkLabel.addClickHandler(deleteMarkHandler);
        addImageToButton(deleteMarkLabel, MyWebApp.resources.deleteX(), MyWebApp.resources.deleteX());
        add(deleteMarkLabel);
    }
}
