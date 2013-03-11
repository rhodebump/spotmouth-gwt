package com.spotmouth.gwt.client;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Hyperlink;
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


//    ClickHandler countryHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget b = (Widget) sender;
//                CountryHolder countryHolder = countryHolderClickMap.get(b);
//                if (countryHolder != null) {
//                        mywebapp.toggleCountry(true,countryHolder.getId());
//                }
//            }
//        }
//    };
    //Map<Widget, CountryHolder> countryHolderClickMap = new HashMap<Widget, CountryHolder>();


    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {

            return MyWebApp.resources.directoryMobile();
        }  else {
            return MyWebApp.resources.directory();
        }

    }



    public DirectoryCountriesPanel(MyWebApp mywebapp) {
        super(mywebapp);
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (CountryHolder countryHolder : mywebapp.getCountryHolders()) {

            ListItem li = new ListItem();
            //li.setStyleName("clearing");
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
