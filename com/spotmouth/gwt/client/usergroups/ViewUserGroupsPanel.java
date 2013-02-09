package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.dto.UserGroupHolder;
import com.spotmouth.gwt.client.usergroups.ManageUserGroupPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 6/19/12
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewUserGroupsPanel extends SpotBasePanel implements SpotMouthPanel {
    private MobileResponse mobileResponse = null;

    public String getTitle() {
        return "Groups";
    }

    public ViewUserGroupsPanel() {
    }

    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.groupsMobile();
        } else {
            return MyWebApp.resources.groups();
        }
    }

    ULPanel userGroupsULPanel = new ULPanel();


    public ViewUserGroupsPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);

        if (MyWebApp.isDesktop()) {
            //<ul id="user_groups_list" class="user_groups_list">
            userGroupsULPanel.setStyleName("user_groups_list");
            userGroupsULPanel.getElement().setId("user_groups_list");

            List<UserGroupHolder> userGroupHolders = mobileResponse.getUserGroupHolders();
            for(UserGroupHolder ugh:userGroupHolders) {

                ListItem listItem = new ListItem();
                listItem.setStyleName("ug_item");
                InlineLabel ug_item_inside = new InlineLabel();
                ug_item_inside.setStyleName("ug_item_inside");

                int currentFriendCount = 0;
                int totalFriendCount = ugh.getFriendHolders().size();
                /*
                kind of nightmare, different markup depends upon how many friends
                 */
                for (FriendHolder friendHolder : ugh.getFriendHolders()) {
                    //Image image = new Image();
                    Image image = getImage(friendHolder.getUserHolder().getContentHolder(), "130x130");
                    if (image == null) {
                        image = new Image();

                        image.setUrl("css/ava.png");


                    }


                    ug_item_inside.getElement().appendChild(image.getElement());
                    currentFriendCount++;
                    if (currentFriendCount == 4) {
                        break;
                    } else if (totalFriendCount == 3) {
                        if (currentFriendCount == 3) {
                            String attr = "width: 127px; height: 127px";
                            image.getElement().setAttribute("style", attr);
                        }
                    } else if (totalFriendCount == 2) {
                        //<img src="2.jpg" style="width: 127px; height: 127px; margin-right: -62px"/>
                        //String attr = "width: 127px; height: 127px; margin-right: -62px";
                        String attr = null;
                        if (currentFriendCount == 0) {
                            attr = "width: 127px; height: 127px; margin-left: -62px";
                        } else if (currentFriendCount == 1) {
                            attr = "width: 127px; height: 127px; margin-right: -62px";
                        }
                        image.getElement().setAttribute("style", attr);
                    } else if (totalFriendCount == 1) {
                        String attr = "width: 127px; height: 127px;";
                        image.getElement().setAttribute("style", attr);
                    }
                }



                Anchor editGroupLink = new Anchor(ugh.getName());
                listItem.add(editGroupLink);
                editGroupLink.addClickHandler(selectGroupHandler);
                groupMap.put(editGroupLink, ugh);
                userGroupsULPanel.add(listItem);

            }
            //<button class="btn_blue">Add Group</button>

            Button addGroupButton = new Button("Add Group");
            addGroupButton.setStyleName("btn_blue");
            addGroupButton.addClickHandler(addUserGroupHandler);
            UserGroupsComposite ugc = new UserGroupsComposite(userGroupsULPanel,addGroupButton);
            add(ugc);

            return;


        }


        this.mobileResponse = mobileResponse;

        //do link back to spotholder
        List<UserGroupHolder> userGroupHolders = mobileResponse.getUserGroupHolders();
        if (userGroupHolders.isEmpty()) {
            Label label = new Label("There are currently no groups.");
            add(label);
        }
        ULPanel ul = new ULPanel();
        add(ul);
        for (UserGroupHolder userGroupHolder : userGroupHolders) {
            //FlowPanel hp = new FlowPanel();
            ListItem li = new ListItem();
            li.setStyleName("clearing");
            //li.add(hp);
            ul.add(li);
            Label groupLabel = new Label(userGroupHolder.getName());
            groupLabel.addClickHandler(selectGroupHandler);
            groupMap.put(groupLabel, userGroupHolder);
            //groupLabel.addStyleName("linky");
            groupLabel.addStyleName("linky");
            // hp.add(groupLabel);
            li.add(groupLabel);
        }
        add(addGroupButton());
    }

    public void toggleFirst() {
    }

    public boolean isLoginRequired() {
        return false;
    }

    Button addGroupButton() {
        Button btn = new Button("Add New Group");
        btn.addClickHandler(addUserGroupHandler);
        btn.setStyleName("btn_blue");
        return btn;
    }

    protected void toggleAddUserGroup() {
        GWT.log("toggleAddGroup");
        History.newItem(MyWebApp.NEW_USER_GROUP);

    }

    ClickHandler addUserGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //we need to be logged in
            AsyncCallback loginCallback = new AsyncCallback() {
                public void onFailure(Throwable throwable) {
                    getMessagePanel().displayError(throwable.getMessage());
                }

                public void onSuccess(Object response) {
                    toggleAddUserGroup();
                }
            };
            if (mywebapp.isLoggedIn(loginCallback)) {
                loginCallback.onSuccess(null);
            }
        }
    };
    Map<Widget, UserGroupHolder> groupMap = new HashMap<Widget, UserGroupHolder>();
    ClickHandler selectGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                UserGroupHolder userGroupHolder = groupMap.get(b);
                if (userGroupHolder != null) {
                    //need to popup panel about group and
                    //with button to join
                    //let required a login to go into the grouppanel
                    ManageUserGroupPanel groupPanel = new ManageUserGroupPanel(mywebapp, userGroupHolder);
                    mywebapp.swapCenter(groupPanel);
                    //toggleUserGroup(groupHolder);
                }
            }
        }
    };
}
