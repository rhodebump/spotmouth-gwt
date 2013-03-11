package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.FriendRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 1/15/12
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class FriendAcceptForm extends SpotBasePanel implements SpotMouthPanel {


    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Accept Friend Request";
    }


    private MobileResponse mobileResponse = null;

    public FriendAcceptForm(MyWebApp myWebApp, MobileResponse mobileResponse) {
        super(myWebApp);
        this.mobileResponse = mobileResponse;
        /*
       ${user.username} has requested to add you as a friend on spotmouth

To accept this friend request, you will have to click on the following link:
${acceptLink}

You may need to copy the link into a browser for it to function.

        */
        //
        //accept button
        FriendHolder friendHolder = mobileResponse.getFriendHolder();
        String username = friendHolder.getUserHolder().getUsername();
        String firstName = friendHolder.getUserHolder().getFirstName();
        String lastName = friendHolder.getUserHolder().getLastName();
        String fullName = "";
        if (!isEmpty(firstName))
            fullName = " (" + firstName + " " + lastName + ") ";
        String message = username + fullName + " has requested to add you as a friend on spotmouth.  You can either accept or ignore this request.";
        HTML html = new HTML(message);
        addFieldset(html, "Friend Request", "message");
        add(acceptButton());
        //do not accept button
        add(ignoreButton());
    }

    AsyncCallback messageCallback = new AsyncCallback() {
        public void onFailure(Throwable throwable) {
            getMessagePanel().displayError(throwable.getMessage());
        }

        public void onSuccess(Object response) {
            if (ignore) {
                getMessagePanel().displayMessage("You are no longer friends.");
            } else {
                getMessagePanel().displayMessage("You are now friends.");
            }
        }
    };

    protected void doSave() {
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFriendHolder(mobileResponse.getFriendHolder());
        if (ignore) {
            friendRequest.getFriendHolder().setEmailInviteAccepted(false);
        } else {
            friendRequest.getFriendHolder().setEmailInviteAccepted(true);
        }
        myService.respondFriendInvitation(friendRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    mywebapp.toggleHome(messageCallback);
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    public Button acceptButton() {
        Button btn = new Button("Accept Request");
        btn.addClickHandler(saveHandler);
        btn.setStyleName("btn_blue");
        //fixButton(btn);
        return btn;
    }

    private boolean ignore = false;

    public Button ignoreButton() {
        Button btn = new Button("Ignore Request");
        btn.addClickHandler(ignoreHandler);
        btn.setStyleName("btn_blue");
        //fixButton(btn);
        return btn;
    }

    public ClickHandler ignoreHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ignore = true;
            saveIt();
        }
    };
//    @Override
//    public boolean isRootPanel() {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
