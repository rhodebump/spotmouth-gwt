package com.spotmouth.gwt.client.coupon;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.DateTextField;
import com.spotmouth.gwt.client.common.TextField;
import gwtupload.client.MultiUploader;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/14/13
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CouponFormComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, CouponFormComposite> {
    }

    @UiField(provided = true)
    final Button cancelButton;
    @UiField
    SpanElement nameSpan;

    public void setName(String name) {
        nameSpan.setInnerText(name);
    }

    @UiField
    SpanElement locationSpan;

    public void setLocation(String name) {
        locationSpan.setInnerText(name);
    }

    @UiField(provided = true)
    final SimplePanel imagePanel;
    @UiField(provided = true)
    final MultiUploader multiUploader;
    @UiField(provided = true)
    final DateTextField startDatePicker;
    @UiField(provided = true)
    final DateTextField endDatePicker;
    @UiField(provided = true)
    final TextField titleTextField;
    @UiField(provided = true)
    final TextArea descriptionTextArea;
    @UiField(provided = true)
    final FlowPanel suggestionsPanel;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;
    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final Button saveButton;
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public CouponFormComposite(Button cancelButton, SimplePanel imagePanel, MultiUploader multiUploader, DateTextField startDatePicker, DateTextField endDatePicker, TextField titleTextField,
                               TextArea descriptionTextArea, Button saveButton,FlowPanel suggestionsPanel,SuggestBox tagSearchTextBox, FlowPanel selectedTagsPanel) {
        this.cancelButton = cancelButton;
        this.imagePanel = imagePanel;
        this.multiUploader = multiUploader;
        this.startDatePicker = startDatePicker;
        this.endDatePicker = endDatePicker;
        this.titleTextField = titleTextField;
        this.descriptionTextArea = descriptionTextArea;
        this.saveButton = saveButton;
        this.suggestionsPanel = suggestionsPanel;
        this.tagSearchTextBox     = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;


        initWidget(uiBinder.createAndBindUi(this));
    }
}
