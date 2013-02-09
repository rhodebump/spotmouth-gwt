package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 2/6/12
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class SortsPanel extends SpotBasePanel {
    public String getTitle() {
        return "Sorting";
    }

    ClickHandler clickSortHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            getMessagePanel().clear();
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                String sortkey = clickMap.get(b);
                if (sortkey != null) {
                   // if (sortkey.equals("distance"))
                    mywebapp.getResultsPanel().getSearchParameters().setSortKey(sortkey);
                    mywebapp.getResultsPanel().performSearch();
                }
            }
        }
    };
    Map<Widget, String> clickMap = new HashMap<Widget, String>();

    private void add(String sortkey, String text, ULPanel ulPanel) {
        FlowPanel hp = new FlowPanel();
        ListItem li = new ListItem();
        //li.setStyleName("clearing");
        li.add(hp);
        ulPanel.add(li);
        Label sortLabel = new Label(text);
        sortLabel.addClickHandler(clickSortHandler);
        sortLabel.addStyleName("linky");
        clickMap.put(sortLabel, sortkey);
        hp.add(sortLabel);
    }

    public SortsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        ULPanel ul = new ULPanel();
        Label mylabel = new Label("Sorting");
        mylabel.setStyleName("h1");
        add(mylabel);
        ul.setStyleName("sorts");
        add(ul);
        add("updatedate_dt", "Most Recently Marked or Updated", ul);

        add("username_s", "User", ul);
        add("totalratings_d", "Total Ratings", ul);
        add("averagerating_d", "Average Rating", ul);
        add("markcount_i", "Total # Marks", ul);
        add("geodist()", "Distance", ul);
    }
}
