package com.spotmouth.gwt.client.location;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/2/13
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetLocationComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, SetLocationComposite> {
    }


    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true)
    final ULPanel previousLocations;

    @UiField(provided = true)
    final SuggestBox countryTextBox;
    @UiField(provided = true)
    final SuggestBox stateTextBox;
    @UiField(provided = true)
    final SuggestBox cityTextBox;
    @UiField(provided = true)
    final SuggestBox zipcodeTextBox;
    @UiField(provided = true)
    final TextField address1TextBox;

    @UiField(provided = true)
    final Button updateButton;

    @UiField(provided = true)
    final Button fromDeviceButton;

    @UiField(provided = true)
    final SimplePanel mapPanel;



    @UiField(provided = true)
    final  SimpleRadioButton mapRadioButton;




    public SetLocationComposite(ULPanel previousLocations,SuggestBox countryTextBox,
                                SuggestBox stateTextBox,SuggestBox cityTextBox,
                                SuggestBox zipcodeTextBox,TextField address1TextBox,Button updateButton,Button fromDeviceButton,SimplePanel mapPanel,
                                SimpleRadioButton mapRadioButton) {
        this.previousLocations = previousLocations;
        this.countryTextBox = countryTextBox;
        this.stateTextBox = stateTextBox;
        this.cityTextBox = cityTextBox;
        this.zipcodeTextBox = zipcodeTextBox;
        this.address1TextBox = address1TextBox;
        this.updateButton  = updateButton;
        this.fromDeviceButton = fromDeviceButton;
        this.mapPanel = mapPanel;
        this.mapRadioButton = mapRadioButton;
        initWidget(uiBinder.createAndBindUi(this));

    }
}
