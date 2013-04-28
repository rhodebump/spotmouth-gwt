package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.help.HelpResources;

public class SecretKeyForm extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "Secret Key";
    }




    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getSecretKeyForm();
    }


    //TextBox keywordsTextBox = new TextBox();

    public SecretKeyForm() {
    }

    public SecretKeyForm(MyWebApp mywebapp) {
        super(mywebapp);
        keywordsTextBox = addTextBox("Secret Key", "secretKey", "");
        add(secretKey());
        add(cancelButton());
    }

    Label secretKey() {
        Label btn = new Label("Use Secret Key");
        btn.addClickHandler(searchHandler);
        fixButton(btn);
        return btn;
    }

    ClickHandler searchHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            if (keywordsTextBox.getValue().isEmpty()) {
                getMessagePanel().displayMessage("Secret Key is required.");
                return;
            }

            if (mywebapp.getCurrentLocation() == null) {
                getMessagePanel().displayMessage("Please set your location.");
                return;
            }
            //we should just do the item detail screen if we have a secret key hit

           // mywebapp.toggleItemDetail(item);



//            SearchRequest searchRequest = new SearchRequest();
//            searchRequest.getSearchParameters().setPassword(keywordsTextBox.getValue());
//            //i don't know if we should restrict the password/secret search key by location?
//
//            final DataOperationDialog itemDetailDialog = new DataOperationDialog(
//                    "Retrieving item.");
//            itemDetailDialog.show();
//            itemDetailDialog.center();
//            ApiServiceAsync myService = mywebapp.getApiServiceAsync();
//            myService.itemdetail(searchRequest, new AsyncCallback() {
//                public void onFailure(Throwable caught) {
//                    mywebapp.verifyDisplay();
//                    itemDetailDialog.hide();
//                    getMessagePanel().clear();
//                    getMessagePanel().displayError(caught.getMessage());
//                }
//
//                public void onSuccess(Object result) {
//                    itemDetailDialog.hide();
//                    MobileResponse mobileResponse = (MobileResponse) result;
//                    if (mobileResponse.getStatus() == 1) {
//                        ItemDetailPanel spd = new ItemDetailPanel(mywebapp, mobileResponse);
//                        mywebapp.swapCenter(spd);
//                    } else {
//                        mywebapp.verifyDisplay();
//                        getMessagePanel().clear();
//                        getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
//                    }
//                }
//            });





            mywebapp.getResultsPanel().resetSearchParameters();
            mywebapp.addCurrentLocation();
            mywebapp.getResultsPanel().getSearchParameters().setPassword(
                    keywordsTextBox.getValue());
            mywebapp.getResultsPanel().getSearchParameters().setExcludeFactual(true);
            mywebapp.getResultsPanel().getSearchParameters().setMarks(true);

            mywebapp.getMessagePanel().clear();
            mywebapp.getResultsPanel().performSearch();
        }
    };

    public void toggleFirst() {
        keywordsTextBox.setFocus(true);
    }


//    @Override
//    public boolean isRootPanel() {
//        return false;
//    }
}
