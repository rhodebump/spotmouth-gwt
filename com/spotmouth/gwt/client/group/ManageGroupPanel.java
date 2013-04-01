package com.spotmouth.gwt.client.group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ManageMembersPanel;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.GroupHolder;
import com.spotmouth.gwt.client.dto.GroupRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/*
 * this constructs links to all application menus
 */
public class ManageGroupPanel extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.groupsMobile();
        }   else {

            return MyWebApp.resources.groups();
        }

    }



    public String getTitle() {
        return "Manage Group";
    }
    public String getPageTitle() {
        return getTitle();
    }

    @Override
    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getGroupPanel();
    }

    GroupHolder groupHolder = null;
    private SimpleCheckBox visibleToMembersCheckbox = new SimpleCheckBox();
    private SimpleCheckBox openEnrollmentCheckbox = new SimpleCheckBox();
    private SimpleCheckBox manageByMembersCheckbox = new SimpleCheckBox();
    private SimpleCheckBox authoritativeCSVCheckbox = new SimpleCheckBox();
    private SimpleCheckBox optinRequiredCheckbox = new SimpleCheckBox();

    private SpotHolder spotHolder = null;


    //,
    public ManageGroupPanel(MyWebApp mywebapp, GroupHolder groupHolder,SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;
        this.groupHolder = groupHolder;
        if (MyWebApp.isDesktop()) {
            groupNameTextBox.setValue(groupHolder.getName());
            groupNameTextBox.setName("gr_name");

            //a cols="200" rows="3"
            descriptionTextArea.setValue(groupHolder.getDescription());
            descriptionTextArea.setVisibleLines(3);
            descriptionTextArea.setCharacterWidth(200);

            //gr_code
            groupCodeTextBox.setValue(groupHolder.getGroupCode());
            groupCodeTextBox.setName("gr_code");

            // <input type="text" name="gr_code_p"/>
            groupCodeDescriptionTextArea.setValue(groupHolder.getGroupCodeDescription());
            groupCodeDescriptionTextArea.setName("gr_code_p");

            visibleToMembersCheckbox.setValue(groupHolder.getVisibleToMembers());


            manageByMembersCheckbox.setValue(groupHolder.getManageByMembers());

            openEnrollmentCheckbox.setValue(groupHolder.getOpenEnrollment());

            authoritativeCSVCheckbox.setValue(groupHolder.getAuthoritativeCsv());

            optinRequiredCheckbox.setValue(groupHolder.getOptInRequired());

//            InlineLabel memberCountLabel = new InlineLabel();
//            memberCountLabel.setText("" + groupHolder.getMemberHolders().size());

            Button saveGroupButton = new Button("Save Group");
            saveGroupButton.addClickHandler(saveHandler);
            GroupFormComposite groupFormComposite = new GroupFormComposite(groupNameTextBox,descriptionTextArea,groupCodeTextBox,groupCodeDescriptionTextArea,visibleToMembersCheckbox,manageByMembersCheckbox,
                    openEnrollmentCheckbox,authoritativeCSVCheckbox,optinRequiredCheckbox,saveGroupButton);
            groupFormComposite.setMemberCount("" + groupHolder.getMemberHolders().size());
            add(groupFormComposite);



            return;
        }
        init1();
    }




    private void init1() {
        this.clear();

        addGroupsHeader(spotHolder);
        if (groupHolder.getId() != null) {
            add(manageMembersButton());
        }
        groupNameTextBox = addTextBox("Name", "groupNameTextBox", groupHolder.getName());
        descriptionTextArea = addTextArea("Description", "description", groupHolder.getDescription(), false);

        groupCodeTextBox = addTextBox("Group Code", "groupCodeTextBox", groupHolder.getGroupCode());
        Label groupCodeLabel = new Label("If you provide a group code, users who provide a valid group code automatically become members.");
        add(groupCodeLabel);
        groupCodeDescriptionTextArea = addTextArea("Group Code Prompt", "groupCodeDescription", groupHolder.getGroupCodeDescription(), false);
        Label visibleToMembers = new Label("Visible to members allows all members of the group to see each other.");
        add(visibleToMembers);


        //


        //visibleToMembersCheckbox = addCheckbox2("Visible To Members?", "visibleToMembers", groupHolder.getVisibleToMembers(), null);
        Label manageByMembers = new Label("Manage by members allows all members of the group to add/remove/approve members of the group.");
        add(manageByMembers);

        Fieldset fs = new Fieldset();
        fs.setText("Manage By Members");
        manageByMembersCheckbox.setValue(groupHolder.getManageByMembers());
        fs.add(manageByMembersCheckbox);
        add(fs);

        //manageByMembersCheckbox = addCheckbox2("Manage By Members?", "manageByMembers", groupHolder.getManageByMembers(), null);
        Label openEnrollment = new Label("Open enrollment allows anyone who wants to join the group to join");
        add(openEnrollment);
        //openEnrollmentCheckbox = addCheckbox2("Open Enrollment?", "openEnrollment", groupHolder.getOpenEnrollment(), null);
        fs = new Fieldset();
        fs.setText("Open Enrollment?");
        openEnrollmentCheckbox.setValue(groupHolder.getManageByMembers());
        fs.add(openEnrollmentCheckbox);
        add(fs);


        Label authCSV = new Label("Authoritative CSV means files you upload will remove members.");
        add(authCSV);
       // authoritativeCSVCheckbox = addCheckbox2("Authoritative CSV", "acsv", groupHolder.getAuthoritativeCsv(), null);

        fs = new Fieldset();
        fs.setText("Authoritative CSV");
        authoritativeCSVCheckbox.setValue(groupHolder.getAuthoritativeCsv());
        fs.add(authoritativeCSVCheckbox);
        add(fs);





        if (mywebapp.getAuthenticatedUser().isGoldenMember()) {
            Label optin = new Label("Users have to opt-in to your group to join.");
            add(optin);

            fs = new Fieldset();
            fs.setText("Opt-in required");
            optinRequiredCheckbox.setValue(groupHolder.getOptInRequired());
            fs.add(optinRequiredCheckbox);
            add(fs);


            //optinRequiredCheckbox = addCheckbox2("Opt-in required", "optin", groupHolder.getOptInRequired(), null);
        }




        Label memberCount = new Label("" + groupHolder.getMemberHolders().size());
        addFieldset(memberCount, "Member Count", "memberCount");

        add(saveButton());

        if (groupHolder.getId() != null) {
            Label deleteButton = new Label("Delete Group");
            deleteButton.addClickHandler(deleteGroupHandler);
            deleteButton.setStyleName("whiteButton");
            add(deleteButton);
            add(manageMembersButton());
        }


        add(cancelButton());


    }

    private Label manageMembersButton() {
        Label manageMembership = new Label("Manage Members");
        manageMembership.addClickHandler(manageMembershipHandler);
        fixButton(manageMembership);
        return manageMembership;
    }

    protected boolean isValid() {
        checkRequired(groupNameTextBox, "Name is required");
        checkRequired(descriptionTextArea, "Description is required");


        if (!isEmpty(groupCodeTextBox)) {
            checkRequired(groupCodeDescriptionTextArea, "Group code prompt is required if you have a group code.");
        }


        return (!getMessagePanel().isHaveMessages());
    }


    ClickHandler deleteGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean delete = Window.confirm("Delete group?");
            if (delete) {
                deleteGroup();
            }
        }
    };
    ClickHandler manageMembershipHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ManageMembersPanel mmp = new ManageMembersPanel(mywebapp, groupHolder, spotHolder);
            mywebapp.swapCenter(mmp);
        }
    };
    private TextBox groupNameTextBox = new TextBox();
    protected TextArea descriptionTextArea = new TextArea();
    protected TextBox groupCodeTextBox = new TextBox();
    protected TextArea groupCodeDescriptionTextArea = new TextArea();

    public void toggleFirst() {
        groupNameTextBox.setFocus(true);
    }

    protected void doSave() {
        GWT.log("saveGroup");
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupHolder(groupHolder);
        groupRequest.setSpotHolder(spotHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        groupHolder.setName(groupNameTextBox.getValue());
        groupHolder.setDescription(descriptionTextArea.getValue());
        groupHolder.setGroupCode(groupCodeTextBox.getValue());
        groupHolder.setGroupCodeDescription(groupCodeDescriptionTextArea.getValue());
        groupHolder.setOpenEnrollment(openEnrollmentCheckbox.getValue());
        groupHolder.setVisibleToMembers(visibleToMembersCheckbox.getValue());
        groupHolder.setManageByMembers(manageByMembersCheckbox.getValue());
        groupHolder.setAuthoritativeCsv(authoritativeCSVCheckbox.getValue());
        groupHolder.setOptInRequired(optinRequiredCheckbox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.getMessagePanel().clear();

                    mywebapp.fetchFriendsAndGroups(null);
                    //init(mobileResponse.getGroupHolder());

                    GroupPanel groupPanel = new GroupPanel(mywebapp, mobileResponse.getGroupHolder(),spotHolder);
                    mywebapp.swapCenter(groupPanel);
                    getMessagePanel().displayMessage("Group saved.");


                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    AsyncCallback deleteMessageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            getMessagePanel().displayMessage("Group deleted.");
        }
    };




    private void deleteGroup() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupHolder(groupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.deleteGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.getMessagePanel().clear();

                    mywebapp.toggleSpotGroups(deleteMessageCallback,spotHolder.getId());


                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }


}
