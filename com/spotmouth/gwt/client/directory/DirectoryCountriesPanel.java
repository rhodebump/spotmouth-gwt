package com.spotmouth.gwt.client.directory;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.CountryHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SearchRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/1/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryCountriesPanel extends SpotBasePanel implements SpotMouthPanel {

    public String getTitle() {
        return "Countries";
    }





    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {

            return MyWebApp.resources.directoryMobile();
        }  else {
            return MyWebApp.resources.directory();
        }

    }

    /*
    This will do a wildcard search and get all the countries
     */
    private void fetchAllCountries() {
        SearchRequest searchRequest = new SearchRequest();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.searchCountries(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                    for (CountryHolder countryHolder : mobileResponse.getCountryHolders()) {
                        ListItem li = new ListItem();
                        String token = MyWebApp.COUNTRY  + countryHolder.getId();
                        Hyperlink countryLabel = new Hyperlink(countryHolder.getFullName(),token);

                        li.add(countryLabel);
                        ul.add(li);
                    }
;
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });


    }

    ULPanel ul = new ULPanel();
    public DirectoryCountriesPanel(MyWebApp mywebapp) {
        super(mywebapp);
        setActiveTabId("dirli");
        this.setStyleName("directory");
        ul.setStyleName("results");
        add(ul);
        fetchAllCountries();

    }
    public DirectoryCountriesPanel() {
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
