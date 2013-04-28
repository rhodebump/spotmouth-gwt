package com.spotmouth.gwt.client.friends;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.H2;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FriendHolder;

import java.util.HashMap;
import java.util.Map;

/*
 * this constructs links to all application menus
 */
public class InvitedPanel extends SpotBasePanel implements SpotMouthPanel {
    private Map<Widget, FriendHolder> clickMap = new HashMap<Widget, FriendHolder>();

    public String getTitle() {
        return "Friends";
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public InvitedPanel() {
    }

    ClickHandler addHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.NEW_FRIEND);
        }
    };

    private ULPanel getInvitedULPanel() {
        /*
             			<ul id="f_list_c" class="f_inv_list">



     				<li class="f_list_item">
     					<h2 class="f_inv_email">SomeName@email.com</h2>
     					<h2 class="f_inv_phone">8 800 555 200</h2>
     					<!-- Information about Acceptance is in title attr -->
     					<span class="f_inv_status f_sms_inv f_true" title="SMS Invite Status: Accepted"></span>
     					<span class="f_inv_status f_sms_acc f_true" title="SMS Acceptance Status: Accepted"></span>
     					<span class="f_inv_status f_em_inv f_false" title="Email Invite Status: Expectation"></span>
     					<span class="f_inv_status f_em_acc f_true" title="Email Acceptance Status: Accepted"></span>
     				</li>


         */
        ULPanel ulPanel = new ULPanel();
        ulPanel.getElement().setId("f_list_c");
        ulPanel.setStyleName("f_inv_list");
        if (mywebapp.getFriendsAndGroups() == null) {
            return ulPanel;
        }
        for (FriendHolder friendHolder : mywebapp.getFriendsAndGroups().getFriendHolders()) {
            ListItem friendListItem = new ListItem();
            friendListItem.setStyleName("f_list_item");
            H2 h2 = new H2();
            h2.setStyleName("f_inv_email");
            if (isEmpty(friendHolder.getEmailAddress())) {
                HTML html = new HTML("<p contenteditable=\"true\" class=\"f_nop\">Not provided</p>");
                h2.add(html);
            } else {
                h2.setText(friendHolder.getEmailAddress());
            }
            friendListItem.add(h2);
            H2 phoneH2 = new H2();
            phoneH2.setStyleName("f_inv_email");
            if (isEmpty(friendHolder.getSmsPhoneNumber())) {
                //h2.setText("Not provided");
                HTML html = new HTML("<p contenteditable=\"true\" class=\"f_nop\">Not provided</p>");
                phoneH2.add(html);
            } else {
                phoneH2.setText(friendHolder.getSmsPhoneNumber());
            }
            friendListItem.add(phoneH2);
            //	<span class="f_inv_status f_sms_inv f_true" title="SMS Invite Status: Accepted"></span>
            InlineLabel status1 = new InlineLabel();
            status1.setStyleName("f_inv_status");
            status1.addStyleName("f_sms_inv");
            if (friendHolder.getSmsInviteSent()) {
                status1.addStyleName("f_true");
                status1.setTitle("SMS Invite Status: Sent");
            } else {
                status1.addStyleName("f_false");
                status1.setTitle("SMS Invite Status: Pending");
            }
            friendListItem.add(status1);
            //<span class="f_inv_status f_sms_acc f_true" title="SMS Acceptance Status: Accepted"></span>
            InlineLabel status2 = new InlineLabel();
            status2.setStyleName("f_inv_status");
            status2.addStyleName("f_sms_acc");
            if (friendHolder.getSmsAccountValidated()) {
                status2.addStyleName("f_true");
                status2.setTitle("SMS Acceptance Status: Accepted");
            } else {
                status2.addStyleName("f_false");
                status2.setTitle("SMS Acceptance Status: Pending");
            }
            friendListItem.add(status2);
            //	<span class="f_inv_status f_em_inv f_false" title="Email Invite Status: Expectation"></span>
            InlineLabel status3 = new InlineLabel();
            status3.setStyleName("f_inv_status");
            status3.addStyleName("f_em_inv");
            if (friendHolder.getEmailInviteSent()) {
                status3.addStyleName("f_true");
                status3.setTitle("Email Invite Status: Sent");
            } else {
                status3.addStyleName("f_false");
                status3.setTitle("Email Invite Status: Pending");
            }
            friendListItem.add(status3);
            //		<span class="f_inv_status f_em_acc f_true" title="Email Acceptance Status: Accepted"></span>
            InlineLabel status4 = new InlineLabel();
            status4.setStyleName("f_inv_status");
            status4.addStyleName("f_em_acc");
            if (friendHolder.getEmailInviteAccepted()) {
                status4.addStyleName("f_true");
                status4.setTitle("Email Acceptance Status: Accepted");
            } else {
                status4.addStyleName("f_false");
                status4.setTitle("Email Acceptance Status: Pending");
            }
            friendListItem.add(status4);
            ulPanel.add(friendListItem);
        }
        return ulPanel;
    }

    public InvitedPanel(MyWebApp mywebapp) {
        super(mywebapp);
        Button inviteFriendButton = new Button();
        inviteFriendButton.addClickHandler(addHandler);
        ULPanel invitedUL = getInvitedULPanel();
        InvitedComposite invitedComposite = new InvitedComposite(inviteFriendButton, invitedUL);
        add(invitedComposite);
    }
}
