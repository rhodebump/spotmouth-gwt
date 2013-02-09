package com.spotmouth.gwt.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.GroupHolder;
import com.spotmouth.gwt.client.dto.GroupRequest;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/21/12
 * Time: 8:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MemberUploadPanel extends SpotBasePanel implements SpotMouthPanel {
    private GroupHolder groupHolder = null;

    public MemberUploadPanel(MyWebApp mywebapp, GroupHolder groupHolder) {
        super(mywebapp);
        this.groupHolder = groupHolder;
        if (MyWebApp.isDesktop()) {
            Anchor exportAnchor = new Anchor("Export Member File");
            String url = mywebapp.getHostUrl() + "/exportGroupToCsv";
            url += "?auth_token=" +  mywebapp.getAuthToken();
            url += "&group_id=" + groupHolder.getId().toString();
            exportAnchor.setHref(url);
            add(exportAnchor);


        }


        addUpload("Member File Upload (CSV Format)",this);
        add(processFileButton());
        add(cancelButton());
    }

    Label processFileButton() {
        Label processFileButton = new Label("Process File");
        processFileButton.addClickHandler(saveHandler);
        fixButton(processFileButton);
        return processFileButton;
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void doSave() {
        //if we are here, the big files were posted, now we do the actual mark rpc
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setAuthToken(mywebapp.getAuthToken());
        groupRequest.setGroupHolder(groupHolder);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.processMemberFile(groupRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                    //we should redirect to the group edit page
                    //History.newItem(MyWebApp.SPOT_GROUPS + sp);
                    //we want to go refetch the
                    //mywebapp.toggleGroup()

                    mywebapp.getMessagePanel().displayError("You file was uploaded successfully and will be processed ASAP");
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
}
