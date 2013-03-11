package com.spotmouth.gwt.client.directory;

import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.PostalCodeHolder;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/1/12
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryPostalCodesPanel extends SpotBasePanel implements SpotMouthPanel {

    public String getTitle() {
        return "PostalCodes";
    }




    //list all zips for a state
    public DirectoryPostalCodesPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);
        this.setStyleName("directory");
        //fetchPostalCodes(stateId);
        //need to fetch the states for the given country
        displayResults(mobileResponse);
    }

    //Map<Widget, PostalCodeHolder> postalCodeHolderClickMap = new HashMap<Widget, PostalCodeHolder>();



    private void displayResults(MobileResponse mobileResponse) {
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (PostalCodeHolder postalCodeHolder : mobileResponse.getPostalCodeHolders()) {
            ListItem li = new ListItem();
            String token =   MyWebApp.POSTALCODE + postalCodeHolder.getId();
            Hyperlink label = new Hyperlink(postalCodeHolder.getCityName() + ", " + postalCodeHolder.getPostalCode(),token);

            li.add(label);
            ul.add(li);
        }
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
