package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.TextField;
import gwtupload.client.MultiUploader;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/13/13
 * Time: 7:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpotFormComposite   extends Composite {
    interface MyUiBinder extends UiBinder<Widget, SpotFormComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    final TextField websiteTextField;

    @UiField(provided = true)
    final TextArea spotDescriptionTextArea;

    //<textarea rows="4" tabindex="4"></textarea>

    @UiField(provided = true)
    final TextField spotNameTextField;



    @UiField(provided = true)
    final TextField voicePhoneTextField;

    @UiField(provided = true)
    final Button cancelButton;


    @UiField(provided = true)
    final Button saveButton;


    @UiField(provided = true)
    final SimpleCheckBox lodgingCheckBox;
    @UiField(provided = true)
    final SimpleCheckBox funCheckBox;

    @UiField(provided = true)
    final SimpleCheckBox drinkingCheckBox;

    @UiField(provided = true)
    final SimpleCheckBox diningCheckBox;


    @UiField(provided = true)
    final SuggestBox countryTextBox;
    @UiField(provided = true)
    final SuggestBox stateTextBox;
    @UiField(provided = true)
    final SuggestBox cityTextBox;
    @UiField(provided = true)
    final SuggestBox zipcodeTextField;
    @UiField(provided = true)
    final TextField address1TextBox;

    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;


    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;

    @UiField(provided = true)
    final FlowPanel suggestionsPanel;


    @UiField(provided = true)
    final TextField factualTextField;


    @UiField(provided = true)
    final TextField woeIDTextField;


    @UiField(provided = true)
    final TextField yelpIDTextField;

    @UiField(provided = true)
    final TextField emailTextField;


    public SpotFormComposite(Button cancelButton,TextField spotNameTextField,TextField voicePhoneTextField,TextField websiteTextField,   TextField emailTextField,
                             TextArea spotDescriptionTextArea,Button saveButton,
                             SimpleCheckBox lodgingCheckBox,SimpleCheckBox funCheckBox,SimpleCheckBox drinkingCheckBox,SimpleCheckBox diningCheckBox,
                             SuggestBox countryTextBox, SuggestBox stateTextBox, SuggestBox cityTextBox, SuggestBox zipcodeTextField, TextField address1TextBox,
                             SuggestBox tagSearchTextBox,
                                                            FlowPanel selectedTagsPanel,FlowPanel suggestionsPanel,
                                                            TextField factualTextField,TextField woeIDTextField,TextField yelpIDTextField) {
        this.cancelButton = cancelButton;
        this.spotNameTextField = spotNameTextField;
        this.voicePhoneTextField = voicePhoneTextField;
        this.websiteTextField = websiteTextField;
        this.emailTextField = emailTextField;
        this.spotDescriptionTextArea = spotDescriptionTextArea;
        this.saveButton = saveButton;
        this.lodgingCheckBox = lodgingCheckBox;
        this.funCheckBox = funCheckBox;
        this.drinkingCheckBox = drinkingCheckBox;
        this.diningCheckBox = diningCheckBox;

        this.countryTextBox = countryTextBox;
        this.stateTextBox = stateTextBox;
        this.cityTextBox = cityTextBox;
        this.zipcodeTextField = zipcodeTextField;
        this.address1TextBox = address1TextBox;
        this.tagSearchTextBox = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;
        this.suggestionsPanel = suggestionsPanel;
        this.factualTextField= factualTextField;
        this.woeIDTextField = woeIDTextField;
        this.yelpIDTextField    = yelpIDTextField;

        initWidget(uiBinder.createAndBindUi(this));
    }


}
