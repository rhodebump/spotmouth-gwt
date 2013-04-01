package com.spotmouth.gwt.client.friends;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;
import com.spotmouth.gwt.client.dto.ImageScaleHolder;
import com.spotmouth.gwt.client.dto.UserHolder;
import com.spotmouth.gwt.client.messaging.MessagingHomeComposite;

import java.util.HashMap;
import java.util.Map;

/*
 * this constructs links to all application menus
 */
public class ManageFriendsPanel extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {
            return MyWebApp.resources.friendsMobile();
        } else {
            return MyWebApp.resources.friends();
        }
    }

    private Map<Widget, FriendHolder> clickMap = new HashMap<Widget, FriendHolder>();

    public String getTitle() {
        return "Friends";
    }

    public ManageFriendsPanel() {
    }


    ClickHandler sendMessageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            GWT.log("sendMessageHandler");

            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                FriendHolder friendHolder = clickMap.get(b);
                mywebapp.toggleMessaging();
               // mywebapp.swapCenter(mgp);
                MessagingHomeComposite mhc = mywebapp.getMessagingPanel().getMessagingHomeComposite();
                mhc.newMessageClick();

                mywebapp.getMessagingPanel().getItemHolder().getFriendHolders().add(friendHolder);
                mywebapp.getMessagingPanel().refresh();


            }
        }
    };



    private ULPanel getFriendsUL() {
        /*

        .f_item_ava {
        	display: block;
        	width: 100px;
        	height: 100px;
        	backgroundPCR: url('ava.jpg') no-repeat center center;


              			<ul id="f_list_c">

      				<li class="f_list_item">
      					<a href="#" class="f_item_ava">
      						<div class="f_item_options">
      							<button class="button f_text" title="Send Message"><img src="inbox.png"/></button>
      							<button class="button f_remove" onclick="fRemove(this.parentNode.parentNode.parentNode);">x</button>
      						</div>
      					</a>
      					<a href="#" class="f_item_name" title="go to User Name's page">User Name</a>

         */


        ULPanel ulPanel = new ULPanel();
        ulPanel.getElement().setId("f_list_c");

        for (FriendHolder friendHolder : mywebapp.getFriendsAndGroups().getFriendHolders()) {
            if (friendHolder.isAccepted()) {

                ListItem friendListItem = new ListItem();
                 friendListItem.setStyleName("f_list_item");


                Anchor friendAnchor = new Anchor();
               friendAnchor.setStyleName("f_item_ava");

                //backgroundPCR: url('ava.jpg') no-repeat center center;


                String imageUrl = null;

                UserHolder userHolder = friendHolder.getUserHolder();
                if (userHolder != null) {

                    ImageScaleHolder imageScaleHolder = getImageScaleHolder(userHolder.getContentHolder(), "320x320");
                    if (imageScaleHolder != null) {
                        imageUrl = imageScaleHolder.getWebServerAssetHolder().getUrl();
                    }


                }

                if (imageUrl == null) {
                    imageUrl = "css/ava.png";
                }

                String theStyle ="background: url('" + imageUrl + "') no-repeat center center;";
                friendAnchor.getElement().setAttribute("style",theStyle);


                friendListItem.add(friendAnchor);

                FlowPanel   f_item_options = new FlowPanel();
                f_item_options.setStyleName("f_item_options");
                friendAnchor.getElement().appendChild(f_item_options.getElement());

                Button sendMessageButton =new Button();
                sendMessageButton.setStyleName("button");
                sendMessageButton.addStyleName("f_text");
                sendMessageButton.setTitle("Send Message");
                Image image = new Image("css/inbox.png");
                clickMap.put(image,friendHolder);
                image.addClickHandler(sendMessageHandler);
                image.setStyleName("pcr");
                sendMessageButton.getElement().appendChild(image.getElement());
                sendMessageButton.addClickHandler(sendMessageHandler);
                clickMap.put(sendMessageButton,friendHolder);
                f_item_options.add(sendMessageButton);
                //friendAnchor.getElement().appendChild(sendMessageButton.getElement());


                Button removeFriendButton =new Button("x");
                removeFriendButton.setStyleName("button");
                removeFriendButton.addStyleName("f_remove");
                //friendAnchor.getElement().appendChild(removeFriendButton.getElement());
                f_item_options.add(removeFriendButton);

                clickMap.put(removeFriendButton, friendHolder);
                removeFriendButton.addClickHandler(deleteFriendHandler);

                Anchor userProfileAnchor = new Anchor(friendHolder.getLabel());
                clickMap.put(userProfileAnchor, friendHolder);
                userProfileAnchor.addClickHandler(friendHandler);


                //userProfileAnchor.setTitle("go to " + friendHolder.getLabel() + " page");
                friendListItem.add(userProfileAnchor);

                ulPanel.add(friendListItem);

            }

        }
        return ulPanel;
    }

    public ManageFriendsPanel(MyWebApp mywebapp) {
        super(mywebapp);


        if (MyWebApp.isDesktop()) {
            ULPanel friendsULPanel = getFriendsUL();

            //	<button class="btn_blue f_invite">Invite Friends</button>
            Button inviteFriendButton = new Button("Invite Friends");
            inviteFriendButton.setStyleName("btn_blue");
            inviteFriendButton.addStyleName("f_invite");
            inviteFriendButton.addClickHandler(addHandler);
            FriendsComposite friendsComposite = new FriendsComposite(friendsULPanel,inviteFriendButton);


            friendsComposite.setFriendCount(mywebapp.getFriendsAndGroups().getFriendHolders().size());
            add(friendsComposite);

            return;


        }

        add(addFriendButton());
        //add or create button
        //list of friends
        //clicking on friend goes to edit page or you can delete
        if (mywebapp.getFriendsAndGroups().getFriendHolders().isEmpty()) {
            Label label = new Label("There are no friends available.");
            add(label);
        }
        ULPanel ul = new ULPanel();
        add(ul);
        for (FriendHolder friendHolder : mywebapp.getFriendsAndGroups().getFriendHolders()) {
            FlowPanel hp = new FlowPanel();
            ListItem li = new ListItem();
            li.setStyleName("clearing");
            li.add(hp);
            ul.add(li);
            Label friendLabel = new Label(friendHolder.getLabel());
            friendLabel.setStyleName("friend");
            friendLabel.addStyleName("linky");
            clickMap.put(friendLabel, friendHolder);
            li.add(friendLabel);
            friendLabel.addClickHandler(friendHandler);
        }
    }

    ClickHandler addHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.NEW_FRIEND);
        }
    };

    Button addFriendButton() {
        Button btn = new Button("Add New Friend");
        btn.addClickHandler(addHandler);
        btn.setStyleName("btn_blue");
        return btn;
    }

    public void toggleFirst() {
    }

    ClickHandler friendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                FriendHolder friendHolder = clickMap.get(b);
                ManageFriendPanel mgp = new ManageFriendPanel(mywebapp,
                        friendHolder, null);
                mywebapp.swapCenter(mgp);
            }
        }
    };


    ClickHandler deleteFriendHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean delete = Window.confirm("Delete your friend?");
            if (delete) {
                Object sender = event.getSource();
                Widget b = (Widget) sender;
                FriendHolder friendHolder = clickMap.get(b);
                //deletebtn.setEnabled(false);
                deleteFriend(friendHolder);
            }
        }
    };




}
