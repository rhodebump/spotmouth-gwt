package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.ListItem;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.FacetCount;
import com.spotmouth.gwt.client.dto.FacetField;
import com.spotmouth.gwt.client.dto.QueryResponse;
import com.spotmouth.gwt.client.dto.TagHolder;

import java.util.HashMap;
import java.util.Map;

public class TagsPanel extends SpotBasePanel {
    public String getTitle() {
        return "Tags";
    }




    ClickHandler clickTagHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                FacetCount facetCount = tagClickMap.get(b);
                if (facetCount != null) {
                    mywebapp.getResultsPanel().getSearchParameters().getTags().clear();
                    TagHolder tagHolder = new TagHolder();
                    tagHolder.setName(facetCount.getName());
                    mywebapp.getResultsPanel().getSearchParameters().getTags().add(tagHolder);
                    //need to go back to first page of results
                    mywebapp.getResultsPanel().getSearchParameters().setOffset(0);
                    mywebapp.getResultsPanel().performSearch();
                }
            }
        }
    };
    Map<Widget, FacetCount> tagClickMap = new HashMap<Widget, FacetCount>();


    public TagsPanel(MyWebApp mywebapp, QueryResponse queryResponse) {
        super(mywebapp);
        Label mylabel = new Label("Tags");
        mylabel.setStyleName("h1");
        add(mylabel);
        ULPanel ul = new ULPanel();
        ul.setStyleName("tags");
        add(ul);
        GWT.log("TagPanel: queryResponse.getFacetFields().size() " + queryResponse.getFacetFields().size());
        int i = 0;
        for (FacetField facetField : queryResponse.getFacetFields()) {
            // GWT.log("looping 1");
            for (FacetCount facetCount : facetField.getFacetCounts()) {
                //GWT.log("Sublooping 2");
                //GWT.log("TagPanel: facetField.getFacetCounts()" + facetField.getFacetCounts().size());
                GWT.log("i=" + i);
                FlowPanel hp = new FlowPanel();
                ListItem li = new ListItem();
                //li.setStyleName("clearing");
                li.add(hp);
                ul.add(li);
                Label tagnameLabel = new Label(facetCount.getName() + " (" + facetCount.getValueCount() + ")");
                tagnameLabel.addClickHandler(clickTagHandler);
                tagnameLabel.addStyleName("linky");
                tagClickMap.put(tagnameLabel, facetCount);
                hp.add(tagnameLabel);
                i++;
            }
        }
        //add a back button
        //add(cancelButton("Back to search results"));
    }
}
