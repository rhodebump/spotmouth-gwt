package com.spotmouth.gwt.client.directory;

import com.google.gwt.user.client.ui.Hyperlink;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.StateProvinceHolder;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 8/1/12
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class DirectoryStatesPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getTitle() {
        return "States/Provinces";
    }


    public DirectoryStatesPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);
        setActiveTabId("dirli");
        this.setStyleName("directory");
        //fetchStates(countryId);
        //need to fetch the states for the given country
        displayStates(mobileResponse);
    }


    private void displayStates(MobileResponse mobileResponse) {
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (StateProvinceHolder stateProvinceHolder : mobileResponse.getStateProvinceHolders()) {
            ListItem li = new ListItem();
           // li.setStyleName("clearing");
            String token = MyWebApp.STATE  + stateProvinceHolder.getId();
            Hyperlink label = new Hyperlink(stateProvinceHolder.getFullName(),token);
            li.add(label);
            ul.add(li);
        }
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
