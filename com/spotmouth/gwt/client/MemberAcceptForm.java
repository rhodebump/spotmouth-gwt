package com.spotmouth.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MemberRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.group.GroupPanel;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/15/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
//form used to allow users to accept a member joining a group
public class MemberAcceptForm extends SpotBasePanel implements SpotMouthPanel {

    private MobileResponse mobileResponse = null;

    private CheckBox smsCheckBox = new CheckBox("Receive SMS Notifications");

    private CheckBox emailCheckBox = new CheckBox("Receive Email Notifications");

    private CheckBox deviceCheckBox = new CheckBox("Receive Device Notifications (iphone and android)");
    //private GroupHolder //groupHolder = null;

    public MemberAcceptForm(MyWebApp myWebApp, MobileResponse mobileResponse) {
        super(myWebApp);
        this.mobileResponse = mobileResponse;

       // this.groupHolder = mobileResponse.getGroupHolder();



        addSpotHeader(mobileResponse.getSpotHolder());



        addGroupHeader(mobileResponse.getGroupHolder());

        String message = "Above group has requested to add you as a member on spotmouth.  You can either accept or ignore this request.";
        HTML html = new HTML(message);
        //description.setStyleName("text");
        //add(description);
        addFieldset(html, "Member Request", "message");


        VerticalPanel cp = new VerticalPanel();
        cp.add(smsCheckBox);
        cp.add(emailCheckBox);
        cp.add(deviceCheckBox);



        addFieldset(cp, "", "na4");


        add(acceptButton());
        //do not accept button
        add(ignoreButton());
    }
//    @Override
//    public boolean isRootPanel() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Label acceptButton() {
        Label btn = new Label("Accept Request");
        btn.addClickHandler(saveHandler);
        fixButton(btn);
        return btn;
    }

    public Label ignoreButton() {
        Label btn = new Label("Ignore Request");
        btn.addClickHandler(cancelHandler);
        fixButton(btn);
        return btn;
    }

    protected void doSave() {
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        MemberRequest memberRequest = new MemberRequest();
        memberRequest.setMemberHolder(mobileResponse.getMemberHolder());
        mobileResponse.getMemberHolder().setEmailInviteAccepted(true);
//        memberRequest.setAuthToken(mywebapp.getAuthToken());
        memberRequest.getMemberHolder().setDeviceNotifications(deviceCheckBox.getValue());
        memberRequest.getMemberHolder().setEmailNotifications(emailCheckBox.getValue());
        memberRequest.getMemberHolder().setSmsNotifications(smsCheckBox.getValue());


        myService.respondMemberInvitation(memberRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mr = (MobileResponse) result;
                if (mr.getStatus() == 1) {
                    //where to now??

                    //using the groupHolder that is returned since we are not a member of this group
                    GroupPanel groupPanel = new GroupPanel(mywebapp, mr.getGroupHolder(),mobileResponse.getSpotHolder());
                    mywebapp.swapCenter(groupPanel);
                    getMessagePanel().displayMessage("You are now member of this group");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
