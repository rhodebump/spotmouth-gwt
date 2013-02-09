package com.spotmouth.gwt.client.group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/15/12
 * Time: 8:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupDetailsComposite extends Composite {

    interface MyUiBinder extends UiBinder<Widget, GroupDetailsComposite> {
    }



    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiField(provided = true)
    final Button joinGroupButton;

    @UiField(provided = true)
    final Button leaveGroupButton;

    @UiField(provided = true)
    final Button manageGroupButton;


    @UiField
    SpanElement groupDescriptionSpan;

    public void setGroupDescription(String groupDescription) {
        groupDescriptionSpan.setInnerText(groupDescription);
    }


    @UiField
    SpanElement groupCodeDescriptionSpan;

    public void setGroupCodeDescription(String groupCodeDescription) {
        groupCodeDescriptionSpan.setInnerText(groupCodeDescription);
    }


    @UiField
    SpanElement groupNameSpan;

    public void setGroupName(String groupName) {
        groupNameSpan.setInnerText(groupName);
    }



    @UiField(provided = true)
    final SimpleCheckBox smsCheckBox;

    @UiField(provided = true)
    final SimpleCheckBox emailCheckBox;

    @UiField(provided = true)
    final SimpleCheckBox deviceCheckBox;
    @UiField(provided = true)
    final TextBox groupCodeTextBox;

    @UiField(provided = true)
        final Anchor backToSearchResultsAnchor;


    @UiField(provided = true)
    final ULPanel gdpUL;


     public GroupDetailsComposite( Button joinGroupButton ,Button leaveGroupButton,Button manageGroupButton,SimpleCheckBox smsCheckBox,SimpleCheckBox emailCheckBox,SimpleCheckBox deviceCheckBox,TextBox groupCodeTextBox,
                                   Anchor backToSearchResultsAnchor,ULPanel gdpUL) {
         this.joinGroupButton = joinGroupButton;
         this.leaveGroupButton = leaveGroupButton;
         this.manageGroupButton = manageGroupButton;
         this.smsCheckBox = smsCheckBox;
         this.emailCheckBox = emailCheckBox;
         this.deviceCheckBox = deviceCheckBox;
         this.groupCodeTextBox = groupCodeTextBox;
         this.backToSearchResultsAnchor = backToSearchResultsAnchor;
         this.gdpUL = gdpUL;
         initWidget(uiBinder.createAndBindUi(this));

         groupCodeDescriptionSpan.setClassName("gdp_code_cont");
         //gdp_code_cont

     }
}
