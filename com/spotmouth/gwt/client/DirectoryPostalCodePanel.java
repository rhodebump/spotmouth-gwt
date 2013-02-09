package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SearchRequest;
import com.spotmouth.gwt.client.dto.SpotHolder;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/1/12
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryPostalCodePanel extends SpotBasePanel implements SpotMouthPanel {

    public String getTitle() {
        return "PostalCode";
    }


//    ClickHandler spotDetailHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget b = (Widget) sender;
//                SpotHolder spotHolder = spotHolderClickMap.get(b);
//                if (spotHolder != null) {
//                   // mywebapp.togglePostalCode(sph);
//                    mywebapp.toggleSpotDetail(true,spotHolder.getId());
//
//                }
//            }
//        }
//    };

    //list all zips for a state
    public DirectoryPostalCodePanel(MyWebApp mywebapp, Long postalCodeId) {
        super(mywebapp);
        //fetchSpots(postalCodeId);

    }

   // Map<Widget, SpotHolder> spotHolderClickMap = new HashMap<Widget, SpotHolder>();

    private void fetchSpots2(Long postalCodeId) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.getSearchParameters().setPostalCodeId(postalCodeId);
        final DataOperationDialog spotDetailDialog = new DataOperationDialog(
                "Retrieving spots.");
        spotDetailDialog.show();
        spotDetailDialog.center();
//        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
//        myService.fetchSpots(searchRequest, new AsyncCallback() {
//            public void onFailure(Throwable caught) {
//                mywebapp.verifyDisplay();
//                getMessagePanel().clear();
//                spotDetailDialog.hide();
//                getMessagePanel().displayError(caught.getMessage());
//            }
//
//            public void onSuccess(Object result) {
//                spotDetailDialog.hide();
//                MobileResponse mobileResponse = (MobileResponse) result;
//                if (mobileResponse.getStatus() == 1) {
//                    displayResults(mobileResponse);
//                } else {
//                    mywebapp.verifyDisplay();
//                    getMessagePanel().clear();
//                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
//                }
//            }
//        });
    }

//    private void displayResults(MobileResponse mobileResponse) {
//        ULPanel ul = new ULPanel();
//        ul.setStyleName("results");
//        add(ul);
//        for (SpotHolder spotHolder : mobileResponse.getSpotHolders()) {
//            GWT.log("got spotholder");
//            ListItem li = new ListItem();
//          //  li.setStyleName("clearing");
////            Label label = new Label(spotHolder.getName());
////            label.addClickHandler(spotDetailHandler);
//            Hyperlink label = getSpotLink(spotHolder.getName(),spotHolder.getId());
//
//            li.add(label);
//            ul.add(li);
//        }
//    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
