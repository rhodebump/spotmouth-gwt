package com.spotmouth.gwt.client.search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/4/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchFormComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, SearchFormComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final TextField keywordsTextBox;
    @UiField(provided = true)
    final SimpleCheckBox geoFilterCheckbox;
    @UiField(provided = true)
    final SimpleCheckBox spotFilterCheckbox;
    @UiField(provided = true)
    final SimpleCheckBox plateFilterCheckbox;
    @UiField(provided = true)
    final SimpleCheckBox markFilterCheckbox;
    @UiField(provided = true)
    final SimpleCheckBox contestsFilterCheckbox;
    @UiField(provided = true)
    final Button searchButton;
    @UiField(provided = true)
    final FlowPanel suggestionsPanel;
    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;
    @UiField(provided = true)
    final ListBox vehicleTypeListBox;
    @UiField(provided = true)
    final ListBox colorsListBox;
    @UiField(provided = true)
    final ListBox manufacturersListBox;

    public SearchFormComposite(TextField keywordsTextBox, SimpleCheckBox geoFilterCheckbox, SimpleCheckBox spotFilterCheckbox,
                               SimpleCheckBox plateFilterCheckbox, SimpleCheckBox markFilterCheckbox, SimpleCheckBox contestsFilterCheckbox,
                               SuggestBox tagSearchTextBox, FlowPanel selectedTagsPanel,
                               ListBox vehicleTypeListBox, ListBox colorsListBox,
                               ListBox manufacturersListBox,
                               Button searchButton, FlowPanel suggestionsPanel) {
        this.suggestionsPanel = suggestionsPanel;
        this.keywordsTextBox = keywordsTextBox;
        this.geoFilterCheckbox = geoFilterCheckbox;
        this.spotFilterCheckbox = spotFilterCheckbox;
        this.plateFilterCheckbox = plateFilterCheckbox;
        this.markFilterCheckbox = markFilterCheckbox;
        this.contestsFilterCheckbox = contestsFilterCheckbox;
        this.tagSearchTextBox = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;
        this.vehicleTypeListBox = vehicleTypeListBox;
        this.searchButton = searchButton;
        this.manufacturersListBox = manufacturersListBox;
        this.colorsListBox = colorsListBox;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
