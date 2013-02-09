package com.spotmouth.gwt.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PostCommentPanel extends VerticalPanel {
    MyWebApp mywebapp = null;

    public PostCommentPanel(MyWebApp mywebapp) {
        super();
        add(new Label("This is a detail panel"));
    }
}
