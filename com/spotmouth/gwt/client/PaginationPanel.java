package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.spotmouth.gwt.client.dto.SearchParameters;

public class PaginationPanel extends HorizontalPanel {
    public PaginationPanel(ClickHandler previousResultsHandler,
                           ClickHandler nextResultsHandler, SearchParameters searchParameters,
                           long numFound) {
        super();
        // is there a previous results?
        // searchParameters.setOffset(searchParameters.getOffset() - 10);
        if (searchParameters.getOffset() > 0) {
            add(previousResultsButton(previousResultsHandler));
        }
        if (searchParameters.getOffset() + searchParameters.getMax() < numFound) {
            add(nextResultsButton(nextResultsHandler));
        }
    }

    Button nextResultsButton(ClickHandler nextResultsHandler) {
        Button btn = new Button(">", nextResultsHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    Button previousResultsButton(ClickHandler previousResultsHandler) {
        Button btn = new Button("<", previousResultsHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }
}
