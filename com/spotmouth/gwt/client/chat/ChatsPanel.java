/*
 * Copyright 2012 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.spotmouth.gwt.client.chat;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.ULPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.QueryResponse;
import com.spotmouth.gwt.client.dto.SolrDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * @author p.havelaar
 */
public class ChatsPanel extends SpotBasePanel implements SpotMouthPanel {

    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return "Chats";
    }



    Map<Widget, Long> widgetChatIdMap = new HashMap<Widget, Long>();

    private ClickHandler createChatHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.CREATE_CHAT);

        }
    };

    Button addChatButton() {
        Button buttonLabel = new Button("Create Chat");

        buttonLabel.setStyleName("btn_blue");
        buttonLabel.addClickHandler(createChatHandler);

        return buttonLabel;
    }


    ULPanel ulPanel = new ULPanel();

    private void initResults(QueryResponse queryResponse) {

        ulPanel.setStyleName("_cl");
        for (SolrDocument solrDocument : queryResponse.getResults()) {
            Image chatImage = new Image();
            String imgUrl = solrDocument.getFirstString("latest_mark_thumbnail_320x320_url_s");
            if (imgUrl != null) {
                chatImage.setUrl(imgUrl);
            }
            String name = solrDocument.getFirstString("title_s");
            //post_s is the safe item full text
            String desc = solrDocument.getFirstString("post_s");
            Long georepoitemid_l = solrDocument.getFirstLong("georepoitemid_l");
            Button joinChatButton = new Button();
            joinChatButton.addClickHandler(joinChatHandler);
            widgetChatIdMap.put(joinChatButton, georepoitemid_l);
            ChatResultComposite crc = new ChatResultComposite(chatImage, joinChatButton);
            crc.setChatName(name);
            crc.setDescription(desc);
            ulPanel.add(crc);
        }
    }


    private ClickHandler joinChatHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget button = (Widget) event.getSource();
            Long chatId = widgetChatIdMap.get(button);
            String token = MyWebApp.JOIN_CHAT + chatId;
            History.newItem(token);
        }
    };




    public ChatsPanel(MyWebApp mywebapp, QueryResponse queryResponse) {
        super(mywebapp);
        setActiveTabId("chatsli");
        initResults(queryResponse);

                //not much to this page
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("contests_list_page");
        flowPanel.getElement().setId("contests_list_page");
        add(flowPanel);
        FlowPanel cl_top = new FlowPanel();
        cl_top.setStyleName("cl_top");
        cl_top.add(addChatButton());
        flowPanel.add(cl_top);

        flowPanel.add(ulPanel);







    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


}
