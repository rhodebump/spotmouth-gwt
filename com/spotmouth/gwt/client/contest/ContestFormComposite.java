package com.spotmouth.gwt.client.contest;

import com.spotmouth.gwt.client.common.BaseComposite;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.kiouri.sliderbar.client.solution.simplehorizontal.SliderBarSimpleHorizontal;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.dto.ContestHolder;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.*;
import org.vectomatic.file.events.*;
import com.spotmouth.gwt.client.common.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/20/13
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestFormComposite extends BaseComposite {
    interface MyUiBinder extends UiBinder<Widget, ContestFormComposite> {
    }

    @UiField
    SpanElement radiusSpan;

    public void setRadius(Integer name) {
        radiusSpan.setInnerText(name.toString());
    }

    @UiField
    InlineLabel numberOfStars;
    @UiField
    InlineLabel numberOfStars0;
    @UiField
    InlineLabel numberOfStars3;
    @UiField
    InlineLabel numberOfStars4;
    @UiField
    InlineLabel numberOfStars5;
    @UiField
    InlineLabel numberOfStars10;

    @UiHandler("numberOfStars0")
    public void onClick0(ClickEvent event) {
        numberOfStarsTextBox.setValue("0");
        numberOfStars.setText("0");
    }

    @UiHandler("numberOfStars3")
    public void onClick3(ClickEvent event) {
        numberOfStarsTextBox.setValue("3");
        numberOfStars.setText("3");
    }

    @UiHandler("numberOfStars4")
    public void onClick4(ClickEvent event) {
        numberOfStarsTextBox.setValue("4");
        numberOfStars.setText("4");
    }

    @UiHandler("numberOfStars5")
    public void onClick5(ClickEvent event) {
        numberOfStarsTextBox.setValue("5");
        numberOfStars.setText("5");
    }

    @UiHandler("numberOfStars10")
    public void onClick10(ClickEvent event) {
        numberOfStarsTextBox.setValue("10");
        numberOfStars.setText("10");
    }

    @UiField
    Image currentIcon;
    @UiField
    Image starImage;
    @UiField
    Image thumbUpImage;
    @UiField
    Image thumbDownImage;

    @UiHandler("thumbUpImage")
    public void onClickthump(ClickEvent event) {
        GWT.log("thumbUpImage");
        iconStyleTextBox.setValue("2");
        // contestHolder.setIconStyle(2);
        fixCurrentIcon();
    }

    @UiHandler("thumbDownImage")
    public void onClickDown(ClickEvent event) {
        GWT.log("thumbDownImage");
        iconStyleTextBox.setValue("3");
        //contestHolder.setIconStyle(3);
        fixCurrentIcon();
    }

    @UiHandler("starImage")
    public void onClickStar(ClickEvent event) {
        GWT.log("onClickStar");
        iconStyleTextBox.setValue("1");
        // contestHolder.setIconStyle(1);
        fixCurrentIcon();
    }

    private void fixCurrentIcon() {
        if (iconStyleTextBox.getValue().equals("1")) {
            currentIcon.setUrl(starImage.getUrl());
        } else if (iconStyleTextBox.getValue().equals("2")) {
            currentIcon.setUrl(thumbUpImage.getUrl());
        } else if (iconStyleTextBox.getValue().equals("3")) {
            currentIcon.setUrl(thumbDownImage.getUrl());
        }
    }

    @UiField(provided = true)
    final SuggestBox countryTextBox;
    @UiField(provided = true)
    final SuggestBox stateTextBox;
    @UiField(provided = true)
    final SuggestBox cityTextBox;
    @UiField(provided = true)
    final TextField zipcodeTextField;
    @UiField(provided = true)
    final TextBox address1TextBox;

    @UiField(provided = true)
    final TextField contestNameTextBox;
    @UiField(provided = true)
    final TextBox numberOfStarsTextBox;
    @UiField(provided = true)
    final TextBox iconStyleTextBox;
    @UiField(provided = true)
    final TextArea descriptionTextArea;
    @UiField(provided = true)
    final DateBox startDatePicker;
    @UiField(provided = true)
    final DateBox endDatePicker;


    @UiField(provided = true)
    final FlowPanel suggestionsPanel;




    @UiField(provided = true)
    final ListBox appliesToListBox;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;


    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;


    @UiField(provided = true)
    final Button saveButton;

    @UiField(provided = true)
    final Button cancelButton;

    @UiField(provided = true)
    final SliderBarSimpleHorizontal radiusSlider;

    @UiField(provided = true)
    final MultiUploader multiUploader;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private ContestHolder contestHolder = null;

    public ContestFormComposite(Button cancelButton, TextField contestNameTextBox, TextArea descriptionTextArea, TextBox numberOfStarsTextBox, TextBox iconStyleTextBox, DateBox startDatePicker, DateBox endDatePicker,
                                SuggestBox countryTextBox, SuggestBox stateTextBox, SuggestBox cityTextBox, TextField zipcodeTextField, TextBox address1TextBox, SuggestBox tagSearchTextBox,
                                FlowPanel selectedTagsPanel, ListBox appliesToListBox, Button saveButton,
                                SliderBarSimpleHorizontal radiusSlider, MultiUploader multiUploader, final SimplePanel imagePanel,
                                MyWebApp mywebapp, ContestHolder contestHolder,FlowPanel suggestionsPanel) {
        super(mywebapp,imagePanel);
        this.cancelButton = cancelButton;
        this.suggestionsPanel = suggestionsPanel;
        this.radiusSlider = radiusSlider;
        this.contestHolder = contestHolder;
        this.contestNameTextBox = contestNameTextBox;
        this.descriptionTextArea = descriptionTextArea;
        this.numberOfStarsTextBox = numberOfStarsTextBox;
        this.iconStyleTextBox = iconStyleTextBox;
        this.startDatePicker = startDatePicker;
        this.endDatePicker = endDatePicker;
        this.countryTextBox = countryTextBox;
        this.stateTextBox = stateTextBox;
        this.cityTextBox = cityTextBox;
        this.zipcodeTextField = zipcodeTextField;
        this.address1TextBox = address1TextBox;
        this.tagSearchTextBox = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;
        this.appliesToListBox = appliesToListBox;
        this.saveButton = saveButton;
        this.multiUploader = multiUploader;
        // this.panelImages = panelImages;
        initWidget(uiBinder.createAndBindUi(this));
        init();

        // class="mc_icon"
        numberOfStars0.setStyleName("mc_icon");
        numberOfStars3.setStyleName("mc_icon");
        numberOfStars4.setStyleName("mc_icon");
        numberOfStars5.setStyleName("mc_icon");
        numberOfStars10.setStyleName("mc_icon");
        //	<span class="mc_text_holder" id="mc_text_holder">0</span>
        numberOfStars.setStyleName("mc_text_holder");
        numberOfStars.getElement().setId("mc_text_holder");
        numberOfStars.setText(numberOfStarsTextBox.getValue());
        /*
   <img src="css/star.png" id="mc_icon_holder" class="mc_icon_holder"/>
               <img src="css/thumb-up.png" class="mc_icon"/>
                            <img src="css/thumb-down.png" class="mc_icon"/>
        */
        starImage.setUrl("css/star.png");
        thumbDownImage.setUrl("css/thumb-down.png");
        thumbUpImage.setUrl("css/thumb-up.png");
        currentIcon.setStyleName("mc_icon_holder");
        starImage.setStyleName("mc_icon");
        thumbDownImage.setStyleName("mc_icon");
        thumbUpImage.setStyleName("mc_icon");
        currentIcon.getElement().setId("mc_icon_holder");
        starImage.getElement().setId("mc_icon");
        thumbDownImage.getElement().setId("mc_icon");
        thumbUpImage.getElement().setId("mc_icon");
        fixCurrentIcon();

    }







}
