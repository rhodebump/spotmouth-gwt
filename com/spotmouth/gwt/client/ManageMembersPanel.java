package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.GroupHolder;
import com.spotmouth.gwt.client.dto.MemberHolder;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.group.ManageGroupPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/1/12
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManageMembersPanel extends SpotBasePanel implements SpotMouthPanel {
    private Map<Widget, MemberHolder> clickMemberMap = new HashMap<Widget, MemberHolder>();

    public void showSelectedMembers(List<MemberHolder> memberHolders) {

        selectedMembersPanel.clear();
        for (MemberHolder memberHolder : memberHolders) {
            GWT.log("MemberHolder" + memberHolder.getId());

            Label label = new Label(memberHolder.getLabel());
            selectedMembersPanel.add(label);
            clickMemberMap.put(label, memberHolder);
            label.addClickHandler(memberDetailsHandler);
            label.setStyleName("linky");
            ImageResource deleteImageIR = MyWebApp.resources.deleteX();
            Image deleteImage = new Image(deleteImageIR);
            selectedMembersPanel.add(deleteImage);
            deleteImage.addClickHandler(removeMemberHandler);
            deleteImage.setTitle("Remove Member " +  memberHolder.getId());
            clickMemberMap.put(deleteImage, memberHolder);
        }
    }

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.groupsMobile();
        }   else {

            return MyWebApp.resources.groups();
        }

    }

    public String getTitle() {
        return "Members";
    }



    private GroupHolder groupHolder = null;
    private SpotHolder spotHolder = null;

    public ManageMembersPanel(MyWebApp myWebApp, GroupHolder groupHolder, SpotHolder spotHolder) {
        super(myWebApp);
        this.groupHolder = groupHolder;
        this.spotHolder = spotHolder;
        add(returnToGroupButton());

        addGroupHeader(groupHolder);
        selectedMembersPanel.setStyleName("selectedMembersPanel");
        addFieldset(selectedMembersPanel, "Members in Group", "membersx");


        showSelectedMembers(groupHolder.getMemberHolders());

        Label addNewMemberButton = new Label("Add NEW Member to Group");
        addNewMemberButton.addClickHandler(addNewMemberToGroupHandler);
        fixButton(addNewMemberButton);
        add(addNewMemberButton);



        GWT.log("isUploadSupported=" + isUploadSupported());
        if (isUploadSupported()) {
            add(uploadCsvButton());
        }


        //add a upload csv file here
        //addUpload("Upload CSV file of members");
        //back to the group panel
        add(returnToGroupButton());
    }

    private Label uploadCsvButton() {



        Label csvButton = new Label("Upload Member File");
        csvButton.addClickHandler(csvHandler);
        fixButton(csvButton);
        return csvButton;
    }

    private Label returnToGroupButton() {
        Label backToGroupButton = new Label("Return to Group");
        backToGroupButton.addClickHandler(backToGroupHandler);
        fixButton(backToGroupButton);
        return backToGroupButton;
    }

    ClickHandler memberDetailsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //get the member
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget widget = (Widget) sender;
                MemberHolder memberHolder = clickMemberMap.get(widget);
                MemberDetailsPanel mgp = new MemberDetailsPanel(mywebapp, memberHolder,ManageMembersPanel.this);
                mywebapp.swapCenter(mgp);
            }
        }
    };
    ClickHandler removeMemberHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget widget = (Widget) sender;
                MemberHolder memberHolder = clickMemberMap.get(widget);
                List<MemberHolder> list = new ArrayList<MemberHolder>();
                list.addAll(groupHolder.getMemberHolders());
                groupHolder.getMemberHolders().clear();
                for (MemberHolder mh : list) {
                    if (mh.getKey().equals(memberHolder.getKey())) {
                        //this is the one we are removing
                    } else {
                        groupHolder.getMemberHolders().add(mh);
                    }
                }
            }
            refresh();
        }
    };

    /*
        We are overriding the refresh method
     */
    public void refresh() {
        showSelectedMembers(groupHolder.getMemberHolders());
    }


    ClickHandler backToGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {


            mywebapp.toggleManageGroup(groupHolder,spotHolder);

        }
    };
    AsyncCallback memberCreatedCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            MemberHolder memberHolder = (MemberHolder) response;
            groupHolder.getMemberHolders().add(memberHolder);
            GWT.log("memberCreatedCallback onSuccess " + memberHolder.toString());
            //refresh();
            //mywebapp.toggleManageGroup(groupHolder,spotHolder);
            ManageGroupPanel mgp = new ManageGroupPanel(mywebapp, groupHolder, spotHolder);
            mywebapp.swapCenter(mgp);

            getMessagePanel().displayMessage("Member has been added");
        }
    };
    ClickHandler addNewMemberToGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            MemberHolder memberHolder = new MemberHolder();
            memberHolder.setEmailInviteAccepted(false);
            memberHolder.setEmailInviteSent(false);
            ManageMemberPanel mgp = new ManageMemberPanel(mywebapp, memberHolder, memberCreatedCallback,groupHolder);
            mywebapp.swapCenter(mgp);
        }
    };

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    ClickHandler csvHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            MemberUploadPanel mgp = new MemberUploadPanel(mywebapp, groupHolder);
            mywebapp.swapCenter(mgp);
        }
    };
}
