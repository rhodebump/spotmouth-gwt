package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/3/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class FavoritesComposite  extends Composite {


    @UiField(provided=true)
    final Panel spotsAreaPanel;


    interface MyUiBinder extends UiBinder<Widget, FavoritesComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);



    public FavoritesComposite(Panel spotsAreaPanel) {
        this.spotsAreaPanel = spotsAreaPanel;
        initWidget(uiBinder.createAndBindUi(this));

    }





}
