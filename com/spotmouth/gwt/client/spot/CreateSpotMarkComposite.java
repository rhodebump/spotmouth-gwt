package com.spotmouth.gwt.client.spot;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/24/13
 * Time: 7:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateSpotMarkComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, CreateSpotMarkComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    public CreateSpotMarkComposite(TextField spotNameTextField,
                                   SuggestBox countryTextBox, SuggestBox stateTextBox, SuggestBox cityTextBox, SuggestBox zipcodeTextField, TextField address1TextBox,
                                   Button saveButton,Button cancelButton) {
        this.spotNameTextField = spotNameTextField;
        this.countryTextBox = countryTextBox;
        this.stateTextBox = stateTextBox;
        this.cityTextBox = cityTextBox;
        this.zipcodeTextField = zipcodeTextField;
        this.address1TextBox = address1TextBox;
        this.saveButton = saveButton;
        this.cancelButton = cancelButton;

        initWidget(uiBinder.createAndBindUi(this));


    }
    @UiField(provided = true)
    final Button saveButton;

    @UiField(provided = true)
    final Button cancelButton;


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
    final TextField spotNameTextField;

}
