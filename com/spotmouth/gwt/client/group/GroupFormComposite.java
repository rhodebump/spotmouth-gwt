package com.spotmouth.gwt.client.group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/20/12
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupFormComposite extends Composite {

    interface MyUiBinder extends UiBinder<Widget, GroupFormComposite> {
    }


    @UiField(provided = true)
    final TextBox groupNameTextBox;


    @UiField(provided = true)
    final TextArea descriptionTextArea;

    @UiField(provided = true)
    final TextBox groupCodeTextBox;

    @UiField(provided = true)
    final TextArea groupCodeDescriptionTextArea;

    @UiField(provided = true)
    final SimpleCheckBox visibleToMembersCheckbox;

    @UiField(provided = true)
    final SimpleCheckBox manageByMembersCheckbox;

    @UiField(provided = true)
    final SimpleCheckBox openEnrollmentCheckbox;


    @UiField(provided = true)
    final SimpleCheckBox optinRequiredCheckbox;
    @UiField(provided = true)
    final SimpleCheckBox authoritativeCSVCheckbox;



//    @UiField(provided = true)

//    final InlineLabel memberCountLabel;


    @UiField
    SpanElement memberCountSpan;

    public void setMemberCount(String memberCount) {
        memberCountSpan.setInnerText(memberCount);
    }




    @UiField(provided = true)
    final Button saveGroupButton;


    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
     public GroupFormComposite(TextBox groupNameTextBox,TextArea descriptionTextArea,TextBox groupCodeTextBox,TextArea groupCodeDescriptionTextArea,
                               SimpleCheckBox visibleToMembersCheckbox,SimpleCheckBox manageByMembersCheckbox,SimpleCheckBox openEnrollmentCheckbox,SimpleCheckBox authoritativeCSVCheckbox,
                               SimpleCheckBox optinRequiredCheckbox,
                      Button saveGroupButton ) {
         this.groupNameTextBox  = groupNameTextBox;
          this.descriptionTextArea = descriptionTextArea;
         this.groupCodeTextBox = groupCodeTextBox;
         this.groupCodeDescriptionTextArea = groupCodeDescriptionTextArea;
         this.visibleToMembersCheckbox = visibleToMembersCheckbox;
         this.manageByMembersCheckbox = manageByMembersCheckbox;
         this.openEnrollmentCheckbox = openEnrollmentCheckbox;
         this.authoritativeCSVCheckbox = authoritativeCSVCheckbox;
        // this.memberCountLabel = memberCountLabel;
         this.optinRequiredCheckbox = optinRequiredCheckbox;
         this.saveGroupButton = saveGroupButton;
          initWidget(uiBinder.createAndBindUi(this));
      }
}
