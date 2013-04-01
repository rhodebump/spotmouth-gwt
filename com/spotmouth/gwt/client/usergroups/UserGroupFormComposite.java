package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.TextField;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 1/28/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupFormComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, UserGroupFormComposite> {
    }


    @UiField
   SpanElement memberCountSpan;

    public void setMemberCount(Integer memberCount) {
        memberCountSpan.setInnerText(memberCount.toString());
    }



    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final Button saveButton;
    @UiField(provided = true)
    final Button cancelButton;
    @UiField(provided = true)
    final ULPanel membersULPanel;
    @UiField(provided = true)
    final TextField groupNameTextBox;
    @UiField(provided = true)
    final ULPanel availableULPanel;

    @UiField(provided = true)
    final Button inviteButton;


    public UserGroupFormComposite(Button saveButton,
                                  Button cancelButton, TextField groupNameTextBox, ULPanel membersULPanel, ULPanel availableULPanel,Button inviteButton) {
        this.saveButton = saveButton;
        this.cancelButton = cancelButton;
        this.membersULPanel = membersULPanel;
        this.groupNameTextBox = groupNameTextBox;
        this.availableULPanel = availableULPanel;
        this.inviteButton = inviteButton;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
