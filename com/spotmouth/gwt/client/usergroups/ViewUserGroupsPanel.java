package com.spotmouth.gwt.client.usergroups;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.H2;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.*;

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

    ULPanel userGroupsULPanel = new ULPanel();
    ULPanel spotGroupsULPanel = new ULPanel();

    public ViewUserGroupsPanel(MyWebApp mywebapp, MobileResponse mobileResponse) {
        super(mywebapp);

        if (MyWebApp.isDesktop()) {
            populateUserGroups(mobileResponse);
            populateSpotGroups(mobileResponse);
        }  else {
            populateUserGroupsMobile(mobileResponse);
            populateSpotGroupsMobile(mobileResponse);
        }

        Button addGroupButton = new Button();
        addGroupButton.addClickHandler(addUserGroupHandler);
        UserGroupsComposite ugc = new UserGroupsComposite(userGroupsULPanel, addGroupButton, spotGroupsULPanel);
        add(ugc);
    }

    /*
    mobile version
    							<a href="javascript:;">
								<h2>Users Group</h2>
								<div class="img_block">
									<img src="css/img/mcd.jpg"/>
									<img src="css/img/3.jpg"/>
									<img src="css/img/2.jpg"/>
									<img src="css/img/1.jpg"/>
								</div>
							</a>
     */


    private void populateSpotGroupsMobile(MobileResponse mobileResponse) {
        List<GroupHolder> groupHolders = mobileResponse.getGroupHolders();
        for (GroupHolder groupHolder : groupHolders) {
            ListItem listItem = new ListItem();
            H2 h2 = new H2();
            h2.setText(groupHolder.getName());
            listItem.add(h2);
            FlowPanel img_block = new FlowPanel();
            listItem.add(img_block);
            int currentFriendCount = 0;
            for (MemberHolder memberHolder : groupHolder.getMemberHolders()) {
                //Image image = new Image();
                Image image = getImage(memberHolder.getUserHolder().getContentHolder(), "130x130");
                if (image == null) {
                    image = new Image();
                    image.setUrl("css/ava.png");
                }
                img_block.add(image);
                currentFriendCount++;
                if (currentFriendCount == 4) {
                    break;
                }
            }

            listItem.addClickHandler(selectGroupHandler);
            groupMap.put(listItem, groupHolder);
            spotGroupsULPanel.add(listItem);
        }
    }


    private void populateUserGroupsMobile(MobileResponse mobileResponse) {
        List<UserGroupHolder> userGroupHolders = mobileResponse.getUserGroupHolders();
        for (UserGroupHolder ugh : userGroupHolders) {
            ListItem listItem = new ListItem();
            H2 h2 = new H2();
            h2.setText(ugh.getName());
            listItem.add(h2);
            FlowPanel img_block = new FlowPanel();
            listItem.add(img_block);
            int currentFriendCount = 0;
            for (FriendHolder friendHolder : ugh.getFriendHolders()) {
                Image image = null;
                if (friendHolder.getUserHolder() != null) {
                    image = getImage(friendHolder.getUserHolder().getContentHolder(), "130x130");
                }
                if (image == null) {
                    image = new Image();
                    image.setUrl("css/ava.png");
                }
                img_block.add(image);
                currentFriendCount++;
                if (currentFriendCount == 4) {
                    break;
                }
            }
            //Anchor editGroupLink = new Anchor(ugh.getName());
            //listItem.add(editGroupLink);
            listItem.addClickHandler(selectUserGroupHandler);
            userGroupMap.put(listItem, ugh);
            userGroupsULPanel.add(listItem);
        }


    }


    private void populateUserGroups(MobileResponse mobileResponse) {
        List<UserGroupHolder> userGroupHolders = mobileResponse.getUserGroupHolders();
        for (UserGroupHolder ugh : userGroupHolders) {
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
                Image image = null;
                if (friendHolder.getUserHolder() != null) {
                    image = getImage(friendHolder.getUserHolder().getContentHolder(), "130x130");
                }
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
            editGroupLink.addClickHandler(selectUserGroupHandler);
            userGroupMap.put(editGroupLink, ugh);
            userGroupsULPanel.add(listItem);
        }


    }
    private void populateSpotGroups(MobileResponse mobileResponse) {
        List<GroupHolder> groupHolders = mobileResponse.getGroupHolders();
        for (GroupHolder groupHolder : groupHolders) {
            ListItem listItem = new ListItem();
            listItem.setStyleName("ug_item");
            InlineLabel ug_item_inside = new InlineLabel();
            ug_item_inside.setStyleName("ug_item_inside");
            int currentFriendCount = 0;
            int totalFriendCount = groupHolder.getMemberHolders().size();
            /*
            kind of nightmare, different markup depends upon how many friends
             */
            for (MemberHolder memberHolder : groupHolder.getMemberHolders()) {
                //Image image = new Image();
                Image image = getImage(memberHolder.getUserHolder().getContentHolder(), "130x130");
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
            Anchor editGroupLink = new Anchor(groupHolder.getName());
            listItem.add(editGroupLink);
            editGroupLink.addClickHandler(selectGroupHandler);
            groupMap.put(editGroupLink, groupHolder);
            spotGroupsULPanel.add(listItem);
        }
    }

    public void toggleFirst() {
    }

    protected void toggleAddUserGroup() {
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
    Map<Widget, GroupHolder> groupMap = new HashMap<Widget, GroupHolder>();
    Map<Widget, UserGroupHolder> userGroupMap = new HashMap<Widget, UserGroupHolder>();
    ClickHandler selectUserGroupHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                Widget b = (Widget) sender;
                UserGroupHolder userGroupHolder = userGroupMap.get(b);
                if (userGroupHolder != null) {
                    //need to popup panel about group and
                    //with button to join
                    //let required a login to go into the grouppanel
                    History.newItem(MyWebApp.MANAGE_USER_GROUP + userGroupHolder.getId());
                }
            }
        }
    };
}
