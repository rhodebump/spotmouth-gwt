package com.spotmouth.gwt.client.mark;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import gwtupload.client.MultiUploader;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/15/12
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmallAdvancedForm extends Composite {
    interface MyUiBinder extends UiBinder<Widget, SmallAdvancedForm> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public SmallAdvancedForm(FlowPanel selectedTagsPanel,SuggestBox tagSearchTextBox,TextBox secretKeyTextBox,MultiUploader multiUploader,FlowPanel imagesPanel,FlowPanel suggestionsPanel) {

      this.suggestionsPanel = suggestionsPanel;
        this.selectedTagsPanel = selectedTagsPanel;
        this.tagSearchTextBox = tagSearchTextBox;
        this.secretKeyTextBox = secretKeyTextBox;
        this.multiUploader = multiUploader;
        this.imagesPanel = imagesPanel;
        this.imagesPanel.setStyleName("ma_files");
        initWidget(uiBinder.createAndBindUi(this));

    }

    @UiField(provided=true)
    final TextBox secretKeyTextBox;


    @UiField(provided=true)
    final SuggestBox tagSearchTextBox;

    @UiField(provided=true)
    final FlowPanel suggestionsPanel;




    @UiField(provided = true)
    final MultiUploader multiUploader;

    @UiField(provided=true)
    final FlowPanel selectedTagsPanel;


    @UiField(provided=true)
    final FlowPanel imagesPanel;



}
