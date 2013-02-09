package com.spotmouth.gwt.client.group;

import com.spotmouth.gwt.client.common.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.Fieldset;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

public class GroupPanel extends SpotBasePanel implements SpotMouthPanel {
    ClickHandler joinGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (mywebapp.getAuthenticatedUser() == null) {
                getMessagePanel().displayError("Please login to manage a group");
                return;
            }
            joinGroup();
        }
    };
    ClickHandler updateNotificationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            saveMember();
        }
    };
    ClickHandler leaveGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            leaveGroup();
        }
    };

    public String getTitle() {
        return "Group";
    }

    public String getPageTitle() {
        return getTitle();
    }

    public void toggleFirst() {
        // TODO Auto-generated method stub
    }

    public boolean isLoginRequired() {
        return false;
    }

    ClickHandler manageGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (mywebapp.getAuthenticatedUser() == null) {
                getMessagePanel().displayError("Please login to manage a group");
                return;
            }
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                mywebapp.toggleManageGroup(groupHolder, spotHolder);
                //mywebapp.toggleGroup(groupHolder.getId());
            }
        }
    };
    private TextBox groupCodeTextBox = new TextBox();
    private GroupHolder groupHolder = null;
    //"Receive SMS Notifications"
    private SimpleCheckBox smsCheckBox = new SimpleCheckBox();
    //"Receive Email Notifications"
    private SimpleCheckBox emailCheckBox = new SimpleCheckBox();
    //"Receive Device Notifications (iphone and android)"
    private SimpleCheckBox deviceCheckBox = new SimpleCheckBox();
    private SpotHolder spotHolder = null;

    public GroupPanel(MyWebApp mywebapp, GroupHolder groupHolder, SpotHolder spotHolder) {
        super(mywebapp);
        this.groupHolder = groupHolder;
        this.spotHolder = spotHolder;
        if (MyWebApp.isDesktop()) {
            Button leaveGroupButton = new Button("Leave this Group");
            leaveGroupButton.addClickHandler(leaveGroupHandler);
            Button joinGroupButton = new Button("Join this Group");
            joinGroupButton.addClickHandler(joinGroupHandler);
            if (isMember()) {
                joinGroupButton.setVisible(false);
            } else {
                leaveGroupButton.setVisible(false);
            }
            Button manageGroupButton = new Button("Manage Group");
            manageGroupButton.addClickHandler(manageGroupHandler);
            MemberHolder memberHolder = getMemberHolder();
            if (memberHolder != null) {
                smsCheckBox.setValue(memberHolder.getSmsNotifications());
                emailCheckBox.setValue(memberHolder.getEmailNotifications());
                deviceCheckBox.setValue(memberHolder.getDeviceNotifications());
            }
            //hide this is no groupcode required
            if (!groupHolder.isGroupCodeRequired()) {
                groupCodeTextBox.setVisible(false);
            }
            //<a class="gwt-Anchor" id="goback" title="Back to Search Results"></a>
            ULPanel gdpUL = new ULPanel();
            if (groupHolder.getVisibleToMembers()) {
                ListItem lbl = new ListItem("This group allows all members to see each other.");
                gdpUL.add(lbl);
            } else {
                ListItem lbl = new ListItem("This group does not allow members to see each other.");
                gdpUL.add(lbl);
            }
            if (groupHolder.getOpenEnrollment()) {
                ListItem lbl = new ListItem("This group is open and anyone can join.");
                gdpUL.add(lbl);
            } else {
                ListItem lbl = new ListItem("This group requires membership approval to join");
                gdpUL.add(lbl);
            }
            if (groupHolder.getManageByMembers()) {
                ListItem lbl = new ListItem("This group is managed by it's members.  Once a member of this group, one can help manage it.");
                gdpUL.add(lbl);
            } else {
                ListItem lbl = new ListItem("This group is managed by its creator.   Members do not manage the group.");
                gdpUL.add(lbl);
            }
            if (groupHolder.getAuthoritativeCsv()) {
                ListItem lbl = new ListItem("CSV file updload will be authoritative.  This means members not in CSV file will be removed.");
                gdpUL.add(lbl);
            } else {
                Label lbl = new Label("CSV file updload is not authoritative.  This means members will only be added via csv file upload.");
                gdpUL.add(lbl);
            }
            if (groupHolder.getOptInRequired()) {
                ListItem lbl = new ListItem("This group required users to opt-in.");
                gdpUL.add(lbl);
            } else {
                ListItem lbl = new ListItem("This group does not require users to opt-in");
                gdpUL.add(lbl);
            }



            Anchor backToSearchResultsAnchor = getBackAnchor();
            GroupDetailsComposite gdc =
                    new GroupDetailsComposite(leaveGroupButton, joinGroupButton, manageGroupButton, smsCheckBox, emailCheckBox, deviceCheckBox, groupCodeTextBox, backToSearchResultsAnchor,gdpUL);
            gdc.setGroupDescription(groupHolder.getDescription());
            gdc.setGroupName(groupHolder.getName());
            gdc.setGroupCodeDescription(groupHolder.getGroupCodeDescription());
            add(gdc);
            return;
        }
        addGroupHeader(groupHolder);
        if (groupHolder.isGroupCodeRequired()) {
            HTML groupCodeDesc = new HTML(groupHolder.getGroupCodeDescription());
            addFieldset(groupCodeDesc, "Group Code Help", "groupCode2");
            groupCodeTextBox = addTextBox("Group Code", "groupCode", "");
        }
        GWT.log("there are " + groupHolder.getMemberHolders().size() + " members");
        VerticalPanel cp = new VerticalPanel();
        cp.add(smsCheckBox);
        cp.add(emailCheckBox);
        cp.add(deviceCheckBox);
        addFieldset(cp, "", "na4");
        MemberHolder memberHolder = getMemberHolder();
        if (memberHolder != null) {
            smsCheckBox.setValue(memberHolder.getSmsNotifications());
            emailCheckBox.setValue(memberHolder.getEmailNotifications());
            deviceCheckBox.setValue(memberHolder.getDeviceNotifications());
        }
        VerticalPanel gvp = new VerticalPanel();
        //display member policy
        if (groupHolder.getVisibleToMembers()) {
            Label lbl = new Label("This group allows all members to see each other.");
            gvp.add(lbl);
        } else {
            Label lbl = new Label("This group does not allow members to see each other.");
            gvp.add(lbl);
        }
        if (groupHolder.getOpenEnrollment()) {
            Label lbl = new Label("This group is open and anyone can join.");
            gvp.add(lbl);
        } else {
            Label lbl = new Label("This group requires membership approval to join");
            gvp.add(lbl);
        }
        if (groupHolder.getManageByMembers()) {
            Label lbl = new Label("This group is managed by it's members.  Once a member of this group, one can help manage it.");
            gvp.add(lbl);
        } else {
            Label lbl = new Label("This group is managed by its creator.   Members do not manage the group.");
            gvp.add(lbl);
        }
        if (groupHolder.getAuthoritativeCsv()) {
            Label lbl = new Label("CSV file updload will be authoritative.  This means members not in CSV file will be removed.");
            gvp.add(lbl);
        } else {
            Label lbl = new Label("CSV file updload is not authoritative.  This means members will only be added via csv file upload.");
            gvp.add(lbl);
        }
        if (groupHolder.getOptInRequired()) {
            Label lbl = new Label("This group required users to opt-in.");
            gvp.add(lbl);
        } else {
            Label lbl = new Label("This group does not require users to opt-in");
            gvp.add(lbl);
        }
        Fieldset groupFS = new Fieldset();
        groupFS.add(gvp);
        add(groupFS);
        //are you a member of this group already?
        //if (groupHolder.isMember()) {
        if (isMember()) {
            Label updateNotificationSettingsLabel = new Label("Update Notification Settings");
            updateNotificationSettingsLabel.addClickHandler(updateNotificationHandler);
            fixButton(updateNotificationSettingsLabel);
            add(updateNotificationSettingsLabel);
            Label leaveGroup = new Label("Leave this Group");
            leaveGroup.addClickHandler(leaveGroupHandler);
            fixButton(leaveGroup);
            add(leaveGroup);
        } else {
            Label joinButton = new Label("Join this Group");
            joinButton.addClickHandler(joinGroupHandler);
            fixButton(joinButton);
            add(joinButton);
        }
        //let's add a manage group
        Label manageGroupButton = new Label("Manage this Group");
        manageGroupButton.addClickHandler(manageGroupHandler);
        fixButton(manageGroupButton);
        add(manageGroupButton);
        add(cancelButton());
    }

    private boolean isMember() {
        MemberHolder memberHolder = getMemberHolder();
        if (memberHolder == null) {
            return false;
        }
        return true;
    }

    private MemberHolder getMemberHolder() {
        for (MemberHolder memberHolder : groupHolder.getMemberHolders()) {
            if (memberHolder.getUserHolder() == null) {
                //next one
            } else if (mywebapp.getAuthenticatedUser().getId().equals(memberHolder.getUserHolder().getId())) {
                return memberHolder;
            }
        }
        return null;
    }

    private void saveMember() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupHolder(groupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        MemberHolder memberHolder = getMemberHolder();
        groupRequest.setMemberHolder(memberHolder);
        groupRequest.getMemberHolder().setDeviceNotifications(deviceCheckBox.getValue());
        groupRequest.getMemberHolder().setEmailNotifications(emailCheckBox.getValue());
        groupRequest.getMemberHolder().setSmsNotifications(smsCheckBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveMember(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                getMessagePanel().clear();
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Your membership has been saved.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void joinGroup() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupHolder(groupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        groupRequest.setGroupCode(groupCodeTextBox.getValue());
        groupRequest.getMemberHolder().setDeviceNotifications(deviceCheckBox.getValue());
        groupRequest.getMemberHolder().setEmailNotifications(emailCheckBox.getValue());
        groupRequest.getMemberHolder().setSmsNotifications(smsCheckBox.getValue());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.joinGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                getMessagePanel().clear();
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Your group join request was successfully processed");
                    if (groupHolder.getOpenEnrollment()) {
                        getMessagePanel().displayMessage("You are now a member of this group");
                    } else {
                        getMessagePanel().displayMessage("Once your membership is approved, you will become a member of this group.");
                    }
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void leaveGroup() {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupHolder(groupHolder);
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.leaveGroup(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                getMessagePanel().clear();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    getMessagePanel().displayMessage("Your have successfully left the group.");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}