package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;

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
    final TextBox groupNameTextBox;
    @UiField(provided = true)
    final ULPanel availableULPanel;

    @UiField(provided = true)
    final Button inviteButton;


    public UserGroupFormComposite(Button saveButton,
                                  Button cancelButton, TextBox groupNameTextBox, ULPanel membersULPanel, ULPanel availableULPanel,Button inviteButton) {
        this.saveButton = saveButton;
        this.cancelButton = cancelButton;
        this.membersULPanel = membersULPanel;
        this.groupNameTextBox = groupNameTextBox;
        this.availableULPanel = availableULPanel;
        this.inviteButton = inviteButton;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
