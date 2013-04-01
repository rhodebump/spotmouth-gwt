package com.spotmouth.gwt.client.spot;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SaveSpotRequest;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.help.HelpResources;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 9/12/12
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MediaForm extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Images";
    }


    public String getPageTitle() {
        return getTitle();
    }



    protected ClickHandler cancelHandler2 = new ClickHandler() {
        public void onClick(ClickEvent event) {
            toggleBack();
        }
    };

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getPlaceForm();
    }


    private void toggleBack() {
        MediaForm mediaForm = new MediaForm(mywebapp,spotHolder);
        mywebapp.swapCenter(mediaForm);

    }

    protected void doSave() {
        SaveSpotRequest saveSpotRequest = new SaveSpotRequest();
        saveSpotRequest.setSpotHolder(spotHolder);
        //no need to change anything here
        spotHolder.setContentsToRemove(contentsToRemove);
        saveSpotRequest.setAuthToken(mywebapp.getAuthToken());
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSpot(saveSpotRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                postDialog.hide();
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                postDialog.hide();
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //successfulSave();
                    //mywebapp.toggleSpotDetail();
                    //the spot is returned, so let's use that spot object to
                    //SpotHolder msh =mobileResponse.getSpotHolder();
                    ManageSpotPanel msp = new ManageSpotPanel( mywebapp,  mobileResponse.getSpotHolder());
                    mywebapp.swapCenter(msp);

                    mywebapp.getMessagePanel().displayMessage("Spot save was successful");
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private SpotHolder spotHolder = null;

    public MediaForm(MyWebApp mywebapp, SpotHolder spotHolder) {
        super(mywebapp);
        this.spotHolder = spotHolder;


        addMediaFields("Upload images OR video for spot");
        add(contentsPanel);
        addContentHolder(spotHolder.getContentHolder(), true, true);
        add(saveButton());
        add(cancelButton("Cancel"));
    }


    @Override
    public Button cancelButton(String label) {
        Button btn = new Button(label);


        btn.addClickHandler(cancelHandler2);

        addImageToButton(btn, MyWebApp.resources.cancelButton(),MyWebApp.resources.cancelButtonMobile());


        btn.setStyleName("btn_blue");
        //fixButton(btn);
        return btn;
    }





    public void toggleFirst() {

    }


}
