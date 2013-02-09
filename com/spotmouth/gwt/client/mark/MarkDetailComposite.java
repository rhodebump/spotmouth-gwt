package com.spotmouth.gwt.client.mark;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.H1;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DataTransferExt;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/10/12
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkDetailComposite extends Composite {


    /*
    document.getElementById("md_key").onclick = function(){
    	for (i=0; i < mdTabs.length; i++) {
    		mdTabs[i].style.display = "none";
    	}
    	document.getElementById("md_key_input").style.display = "block";
    }


    document.getElementById("md_tag").onclick = function(){
    	for (i=0; i < mdTabs.length; i++) {
    		mdTabs[i].style.display = "none";
    	}
    	document.getElementById("md_tag_input").style.display = "block";
    }


     */
    interface MyUiBinder extends UiBinder<Widget, MarkDetailComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);



    @UiField(provided=true)
    final H1 repliesH1;

    @UiField(provided=true)
    final Anchor backToSearchResultsAnchor;


    @UiField(provided=true)
    final FlowPanel markContentPanel;


    @UiField(provided=true)
    final ULPanel repliesULPanel;


    @UiField(provided=true)
    final Image bigImage;

    @UiField(provided=true)
    final InlineLabel killPhoto;

    @UiField(provided=true)
    final FlowPanel itemTagsPanel;


    @UiField(provided=true)
    final InlineLabel replyButton;


    @UiField(provided=true)
    final InlineLabel cancelButton;

    @UiField(provided=true)
    final FlowPanel selectedTagsPanel;



    @UiField(provided=true)
    final SuggestBox tagSearchTextBox;

    @UiField(provided=true)
    final TextBox secretKeyTextBox;


    @UiField(provided=true)
    final TextArea contentTextArea;

    @UiField
    InlineLabel secretKeyLabel;


    @UiField
    InlineLabel killtag;



    @UiField
    InlineLabel killkey;



    @UiField
    InlineLabel tagSearchLabel;


    @UiField(provided=true)
    final Button saveReplyButton;



    @UiField(provided=true)
    final Button addTagButton;



    @UiField(provided = true)
    final MultiUploader multiUploader;



    @UiField(provided = true)
    final FlowPanel panelImages;


    public MarkDetailComposite(Image mainImage,Button markSpotButton,Button viewMapButton,Anchor usernameAnchor,Image profileImage,FlowPanel markContentPanel,ULPanel repliesULPanel,
                               Image bigImage,    InlineLabel killPhoto,FlowPanel itemTagsPanel,Anchor backToSearchResultsAnchor,InlineLabel replyButton,InlineLabel cancelButton,
                               FlowPanel selectedTagsPanel,SuggestBox tagSearchTextBox,TextBox secretKeyTextBox,TextArea contentTextArea,Button saveReplyButton,MultiUploader multiUploader,FlowPanel panelImages,
                               Button addTagButton, H1 repliesH1) {
        this.repliesH1 = repliesH1;
        this.tagSearchTextBox = tagSearchTextBox;
        this.secretKeyTextBox = secretKeyTextBox;
        this.addTagButton = addTagButton;
        this.replyButton = replyButton;
        this.cancelButton = cancelButton;
        this.mainImage = mainImage;
        this.markSpotButton  = markSpotButton;
        mainImage.setStyleName("md_spot_photo");
        this.viewMapButton = viewMapButton;
        this.usernameAnchor = usernameAnchor;
        this.profileImage = profileImage;
        this.markContentPanel = markContentPanel;
        this.repliesULPanel = repliesULPanel;
        //<img class="md_el_big" id="md_img_big" src=""/>
        profileImage.setStyleName("md_el_big");
        profileImage.getElement().setId("md_img_big");
            this.panelImages = panelImages;
        this.multiUploader = multiUploader;
        this.contentTextArea = contentTextArea;
        this.itemTagsPanel = itemTagsPanel;
        this.bigImage = bigImage;
        this.killPhoto = killPhoto;
        this.backToSearchResultsAnchor = backToSearchResultsAnchor;
        this.selectedTagsPanel = selectedTagsPanel;
        this.saveReplyButton = saveReplyButton;
        initWidget(uiBinder.createAndBindUi(this));

        //	<span id="md_key" class="btm_tab">Secret Key</span>
        secretKeyLabel.setStyleName("btm_tab");
        secretKeyLabel.getElement().setId("md_key");
        secretKeyLabel.setText("Secret Key");

             //	<span id="md_tag" class="btm_tab">Tag Search</span>
        tagSearchLabel.setStyleName("btm_tab");
        tagSearchLabel.getElement().setId("md_tag");
        tagSearchLabel.setText("Tag Search");


        killkey.setStyleName("kill_this");
        killkey.setText("x");

        //<span class="kill_this">x</span>

        killtag.setStyleName("kill_this");
        killtag.setText("x");


        addTagButton.setStyleName("btn_blue");

    }


    @UiField
    SpanElement markDateSpan;


    public void setMarkDate(String markDate) {
        markDateSpan.setInnerText(markDate);
    }



    @UiField
    SpanElement spotNameSpan;


    public void setSpotName(String locationName) {
        spotNameSpan.setInnerText(locationName);
    }

    @UiField
    SpanElement phoneNumberSpan;

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberSpan.setInnerText(phoneNumber);
    }

    @UiField
    SpanElement fullAddressSpan;

    public void setFullAddress(String fullAddress) {
        fullAddressSpan.setInnerText(fullAddress);
    }



    @UiField
    SpanElement markContentSpan;

    public void setMarkContent(String markContent) {
        markContentSpan.setInnerText(markContent);
    }


    @UiField(provided=true)
    final Anchor usernameAnchor;



    @UiField(provided=true)
    final Image profileImage;




    @UiField(provided=true)
    final Image mainImage;


    @UiField(provided=true)
    final Button markSpotButton;

    @UiField(provided=true)
    final Button viewMapButton;




    @UiHandler("tagSearchLabel")
    public void onClick1(ClickEvent event) {

        hideAll();
        //document.getElementById("md_key_input").style.display = "block";
        Element myInput = DOM.getElementById("md_tag_input");
        myInput.removeClassName("hideme");
    }


    @UiHandler("secretKeyLabel")
    public void onClick2(ClickEvent event) {

        hideAll();
        //document.getElementById("md_key_input").style.display = "block";

        Element myInput = DOM.getElementById("md_key_input");
        myInput.removeClassName("hideme");
    }




    @UiHandler("killtag")
    public void onClick3(ClickEvent event) {

        hideAll();

    }


    @UiHandler("killkey")
    public void onClick4(ClickEvent event) {

        hideAll();
    }


    private void hideAll() {

        Element myInput = DOM.getElementById("md_key_input");
        myInput.addClassName("hideme");

        Element myInput2 = DOM.getElementById("md_tag_input");
        myInput2.addClassName("hideme");


    }


}
