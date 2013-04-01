package com.spotmouth.gwt.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.spotmouth.gwt.client.ULPanel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/11/13
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/src/com/google/gwt/user/client/ui/SuggestBox.java?r=11260
 */
public class CustomSuggestionDisplay extends SuggestionDisplay
        implements HasAnimation {
    private FlowPanel panelToAddTo = null;

    public CustomSuggestionDisplay(FlowPanel panelToAddTo) {
        //<ul class="pop_tag_list" id="pop_tag_list">  ]
        this.panelToAddTo = panelToAddTo;
    }

    @Override
    protected Suggestion getCurrentSelection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void hideSuggestions() {
        //To change body of implemented methods use File | Settings | File Templates.
        panelToAddTo.setVisible(false);
    }

    @Override
    protected void moveSelectionDown() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void moveSelectionUp() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    Map<ListItem, Suggestion> mapSuggestion = new HashMap<ListItem, Suggestion>();
    private boolean hideWhenEmpty = true;

    @Override
    protected void showSuggestions(final SuggestBox suggestBox,
                                   Collection<? extends Suggestion> suggestions,
                                   boolean isDisplayStringHTML, boolean isAutoSelectEnabled,
                                   final SuggestionCallback callback) {
        //To change body of implemented methods use File | Settings | File Templates.
        //ulPanel.clear();
        panelToAddTo.clear();
        ULPanel ulPanel = new ULPanel();
        ulPanel.setStyleName("pop_tag_list");
        ulPanel.getElement().setId("pop_tag_list");
        // Hide the popup if there are no suggestions to display.
        boolean anySuggestions = (suggestions != null && suggestions.size() > 0);
        if (!anySuggestions && hideWhenEmpty) {
            hideSuggestions();
            return;
        }
        // Hide the popup before we manipulate the menu within it. If we do not
        // do this, some browsers will redraw the popup as items are removed
        // and added to the menu.
        if (panelToAddTo.isAttached()) {
            panelToAddTo.setVisible(false);
        }
        panelToAddTo.clear();
        ClickHandler suggestionHandler = new ClickHandler() {
            public void onClick(ClickEvent event) {
                ListItem listItem = (ListItem) event.getSource();
                Suggestion suggestion = mapSuggestion.get(listItem);
                callback.onSuggestionSelected(suggestion);
            }
        };
        for (final Suggestion curSuggestion : suggestions) {
            ListItem listItem = new ListItem(curSuggestion.getDisplayString());
            mapSuggestion.put(listItem, curSuggestion);
            listItem.addClickHandler(suggestionHandler);
            ulPanel.add(listItem);
        }
        panelToAddTo.setVisible(true);
        SuggestionsComposite suggestionsComposite = new SuggestionsComposite(ulPanel);
        panelToAddTo.add(suggestionsComposite);
    }

    public boolean isAnimationEnabled() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAnimationEnabled(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
