package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;


/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/5/13
 * Time: 6:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupsComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, UserGroupsComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided = true)
    final ULPanel userGroupsULPanel;



    @UiField(provided = true)
    final ULPanel spotGroupsULPanel;


    @UiField(provided = true)
    final Button addGroupButton;



    public UserGroupsComposite(ULPanel userGroupsULPanel,Button addGroupButton,ULPanel spotGroupsULPanel) {
        this.userGroupsULPanel = userGroupsULPanel;
        this.addGroupButton = addGroupButton;
        this.spotGroupsULPanel = spotGroupsULPanel;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
