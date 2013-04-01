package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.common.TextField;
import com.spotmouth.gwt.client.dto.ItemHolder;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DropPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/2/13
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatFormComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ChatFormComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final TextField nameTextBox;
    @UiField(provided = true)
    final TextArea descriptionTextArea;
    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final FlowPanel suggestionsPanel;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;
    @UiField(provided = true)
    final Button saveButton;
    @UiField(provided = true)
    final Button cancelButton;
    @UiField(provided = true)
    final DropPanel dropPanel;
    @UiField(provided = true)
    final MultiUploader multiUploader;
    @UiField(provided = true)
    final SimplePanel imagePanel;
    private MyWebApp mywebapp = null;
    private ItemHolder itemHolder = null;


    @UiField(provided = true)
    final Button shareOnFacebookButton;



    public ChatFormComposite(TextField nameTextBox, TextArea descriptionTextArea, DateBox startDatePicker, DateBox endDatePicker,
                             SuggestBox tagSearchTextBox,
                             FlowPanel selectedTagsPanel, Button saveButton, Button cancelButton,
                             MultiUploader multiUploader, final SimplePanel imagePanel,
                             MyWebApp mywebapp, ItemHolder itemHolder, FlowPanel suggestionsPanel, DropPanel dropPanel,Button shareOnFacebookButton) {
        this.suggestionsPanel = suggestionsPanel;
        this.nameTextBox = nameTextBox;
        this.descriptionTextArea = descriptionTextArea;
        this.cancelButton = cancelButton;
        this.tagSearchTextBox = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;
        this.saveButton = saveButton;
        this.multiUploader = multiUploader;
        this.imagePanel = imagePanel;
        this.mywebapp = mywebapp;
        this.itemHolder = itemHolder;
        this.dropPanel = dropPanel;
        this.shareOnFacebookButton = shareOnFacebookButton;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
