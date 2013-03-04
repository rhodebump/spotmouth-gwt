package com.spotmouth.gwt.client.directory;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.CountryHolder;

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



    public DirectoryCountriesPanel(MyWebApp mywebapp) {
        super(mywebapp);
        this.setStyleName("directory");
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (CountryHolder countryHolder : mywebapp.getCountryHolders()) {

            ListItem li = new ListItem();
            String token = MyWebApp.COUNTRY  + countryHolder.getId();
            Hyperlink countryLabel = new Hyperlink(countryHolder.getFullName(),token);

            li.add(countryLabel);
            ul.add(li);

        }

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
