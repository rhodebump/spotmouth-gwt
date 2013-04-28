package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.detail.SpotDetailPanel;
import com.spotmouth.gwt.client.dto.GroupHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.SpotHolder;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 12/30/11
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewGroupsPanel extends SpotBasePanel implements SpotMouthPanel {


    public String getTitle() {
        return "Groups";
    }



    private MobileResponse mobileResponse = null;
    private SpotHolder spotHolder = null;


    ClickHandler spotDetailHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            SpotDetailPanel spotDetailPanel = new SpotDetailPanel(mywebapp, mobileResponse);
            mywebapp.swapCenter(spotDetailPanel);
        }
    };



    public ViewGroupsPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);
        this.mobileResponse = mobileResponse;
        this.spotHolder = mobileResponse.getSpotHolder();


        Label spotNameButton = new Label("Go to this spot");
        fixButton(spotNameButton);
        spotNameButton.addClickHandler(spotDetailHandler);
        add(spotNameButton);




        addGroupsHeader(spotHolder);

        //do link back to spotholder




        List<GroupHolder> groupHolders = spotHolder.getGroupHolders();
        if (groupHolders.isEmpty()) {
            Label label = new Label("There are currently no groups.");
            add(label);
        }
        ULPanel ul = new ULPanel();
        //ul.setStyleName("results");
        add(ul);
        for (GroupHolder groupHolder : groupHolders) {
            //FlowPanel hp = new FlowPanel();
            ListItem li = new ListItem();
            //li.setStyleName("clearing");
            //li.add(hp);
            ul.add(li);
            Label groupLabel = new Label(groupHolder.getName());
            groupLabel.addClickHandler(selectGroupHandler);
            groupMap.put(groupLabel, groupHolder);
            //groupLabel.addStyleName("linky");
            groupLabel.addStyleName("linky");

           // hp.add(groupLabel);
            li.add(groupLabel);
        }

        add(getAddGroupButton());



    }

    public void toggleFirst() {
    }





    protected ClickHandler addNewGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //we need to be logged in
            AsyncCallback loginCallback = new AsyncCallback() {
                public void onFailure(Throwable throwable) {
                    getMessagePanel().displayError(throwable.getMessage());
                }

                public void onSuccess(Object response) {
                    toggleAddGroup(spotHolder);
                }
            };
            if (mywebapp.isLoggedIn(loginCallback)) {
                loginCallback.onSuccess(null);
            }
        }
    };

    protected Button getAddGroupButton() {
        Button btn = new Button("Add New Group");
        btn.addClickHandler(addNewGroupHandler);
        //  btn.setStyleName("whiteButton");
        return btn;
    }






}
