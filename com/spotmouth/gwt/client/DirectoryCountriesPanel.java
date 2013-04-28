package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.chat.ChatsPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.CountryHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SearchParameters;
import com.spotmouth.gwt.client.dto.SearchRequest;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
import java.util.*;

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





    private void showCountries(List<CountryHolder> countryHolders) {
        for (CountryHolder countryHolder : countryHolders) {

            ListItem li = new ListItem();
            String token = MyWebApp.COUNTRY  + countryHolder.getId();
            Hyperlink countryLabel = new Hyperlink(countryHolder.getFullName(),token);

            li.add(countryLabel);
            ul.add(li);

        }
    }

    ULPanel ul = new ULPanel();

    public DirectoryCountriesPanel(MyWebApp mywebapp) {
        super(mywebapp);
        ul.setStyleName("results");
        add(ul);
        SearchRequest searchRequest = new SearchRequest();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.searchCountries(searchRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    showCountries(mobileResponse.getCountryHolders());
                } else {
                    getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }
    public DirectoryCountriesPanel() {
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
