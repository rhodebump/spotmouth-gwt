package com.spotmouth.gwt.client.contest;

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
import com.spotmouth.gwt.client.dto.SolrDocument;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/10/12
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestsPanel extends SpotBasePanel implements SpotMouthPanel {
    Map<Widget, Long> widgetContestIdMap = new HashMap<Widget, Long>();



    public String getTitle() {
        return "Contests";
    }

    public void toggleFirst() {
    }


    public ContestsPanel() {
    }

    private ClickHandler addContestHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            History.newItem(MyWebApp.ADD_CONTEST);
        }
    };

    Button addContestButton() {
        Button buttonLabel = new Button("Add Contest");
        buttonLabel.setStyleName("btn_blue");
        buttonLabel.addClickHandler(addContestHandler);
        return buttonLabel;
    }

    private ClickHandler mostVotedHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget button = (Widget) event.getSource();
            Long contestId = widgetContestIdMap.get(button);
            String token = MyWebApp.CONTEST_TOTAL_VOTES + contestId;
            History.newItem(token);
        }
    };
    private ClickHandler winnersHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget button = (Widget) event.getSource();
            Long contestId = widgetContestIdMap.get(button);
            String token = MyWebApp.CONTEST_AVERAGE_VOTES + contestId;
            History.newItem(token);
        }
    };
    private ClickHandler contestDetailHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Widget button = (Widget) event.getSource();
            Long contestId = widgetContestIdMap.get(button);
            String token = MyWebApp.CONTEST_DETAIL + contestId;
            History.newItem(token);
        }
    };


    ULPanel ulPanel = new ULPanel();

    private void initResults() {

        ulPanel.setStyleName("_cl");
        for (SolrDocument solrDocument : mywebapp.getContests().getContestQueryResponse().getResults()) {
            Image contestImage = new Image();
            String imgUrl = solrDocument.getFirstString("image_thumbnail_130x130_url_s");
            if (imgUrl != null) {
                contestImage.setUrl(imgUrl);
            }
            Long contestId = solrDocument.getFirstLong("contestid_l");
            contestImage.addClickHandler(contestDetailHandler);
            widgetContestIdMap.put(contestImage,contestId);
            //CONTEST_DETAIL
            String name = solrDocument.getFirstString("name");
            String desc = solrDocument.getFirstString("description_s");

            Button mostVotedButton = new Button();
            mostVotedButton.addClickHandler(mostVotedHandler);
            widgetContestIdMap.put(mostVotedButton, contestId);
            Button winnersButton = new Button();
            winnersButton.addClickHandler(winnersHandler);
            widgetContestIdMap.put(winnersButton, contestId);
            ContestResultComposite crc = new ContestResultComposite(contestImage, mostVotedButton, winnersButton);

            crc.nameAnchor.addClickHandler(contestDetailHandler);
            widgetContestIdMap.put(crc.nameAnchor,contestId);

            crc.setContestName(name);
            crc.setDescription(desc);
            ulPanel.add(crc);
        }
    }

    public ContestsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        setActiveTabId("contestsli");
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.setStyleName("contests_list_page");
        flowPanel.getElement().setId("contests_list_page");
        add(flowPanel);
        FlowPanel cl_top = new FlowPanel();
        cl_top.setStyleName("cl_top");
        cl_top.add(addContestButton());
        flowPanel.add(cl_top);

        flowPanel.add(ulPanel);
    }

    public void addedToDom() {
        super.addedToDom();
        initResults();
    }


}
