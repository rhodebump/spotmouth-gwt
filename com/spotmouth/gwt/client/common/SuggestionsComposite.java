package com.spotmouth.gwt.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/11/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class SuggestionsComposite extends Composite {

    interface MyUiBinder extends UiBinder<Widget, SuggestionsComposite> {
    }


    @UiField(provided = true)
    final ULPanel popTagList;

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public SuggestionsComposite(ULPanel popTagList) {
        this.popTagList = popTagList;
        initWidget(uiBinder.createAndBindUi(this));

    }


}
