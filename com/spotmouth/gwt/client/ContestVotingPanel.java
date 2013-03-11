package com.spotmouth.gwt.client;

import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.QueryResponse;
import org.cobogw.gwt.user.client.ui.Rating;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/14/12
 * Time: 7:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestVotingPanel extends SpotBasePanel implements SpotMouthPanel {
    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }



    Map<Long, Rating> ratingMap = new HashMap<Long, Rating>();


    public ContestVotingPanel(MyWebApp mywebapp, QueryResponse contestQueryResponse, Long voteForId) {
        super(mywebapp, false);
        if (contestQueryResponse == null) {
            return;
        }
        displayContests(contestQueryResponse, true, voteForId);
    }
}
