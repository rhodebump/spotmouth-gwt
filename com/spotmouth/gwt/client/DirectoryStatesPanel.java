package com.spotmouth.gwt.client;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.StateProvinceHolder;

import java.util.HashMap;
import java.util.Map;

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

//    ClickHandler stateHandler = new ClickHandler() {
//        public void onClick(ClickEvent event) {
//            Object sender = event.getSource();
//            if (sender instanceof Widget) {
//                GWT.log("Got a Label");
//                Widget b = (Widget) sender;
//                StateProvinceHolder sph = stateOrProvinceHolderClickMap.get(b);
//                if (sph != null) {
//                    mywebapp.toggleState(true, sph.getId());
//                }
//            }
//        }
//    };

    public DirectoryStatesPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);
        //fetchStates(countryId);
        //need to fetch the states for the given country
        displayStates(mobileResponse);
    }

    Map<Widget, StateProvinceHolder> stateOrProvinceHolderClickMap = new HashMap<Widget, StateProvinceHolder>();

    private void displayStates(MobileResponse mobileResponse) {
        ULPanel ul = new ULPanel();
        ul.setStyleName("results");
        add(ul);
        for (StateProvinceHolder stateProvinceHolder : mobileResponse.getStateProvinceHolders()) {
            ListItem li = new ListItem();
           // li.setStyleName("clearing");
            String token = MyWebApp.STATE  + stateProvinceHolder.getId();
            Hyperlink label = new Hyperlink(stateProvinceHolder.getFullName(),token);

            stateOrProvinceHolderClickMap.put(label, stateProvinceHolder);
            li.add(label);
            ul.add(li);
        }
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isLoginRequired() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
