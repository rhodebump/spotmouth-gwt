package com.spotmouth.gwt.client.mark;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/1/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpotMarkComposite  extends Composite {
    interface MyUiBinder extends UiBinder<Widget, SpotMarkComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField
        SpanElement usernameSpan;

    public void setUsername(String username) {
        usernameSpan.setInnerText(username);
    }

    @UiField
        SpanElement markContentSpan;

    public void setMarkContent(String markContent) {
        markContentSpan.setInnerText(markContent);
    }


    @UiField(provided = true)
    final Anchor readMoreAnchor;


    @UiField(provided = true)
    final Anchor markImageAnchor;



    public SpotMarkComposite(Anchor markImageAnchor,Anchor readMoreAnchor ) {
             this.markImageAnchor  = markImageAnchor;
        this.readMoreAnchor = readMoreAnchor;
        this.readMoreAnchor.setStyleName("marks_more");
        this.readMoreAnchor.setText("Read more");

        //"marks_more">Read more
initWidget(uiBinder.createAndBindUi(this));


    }

}
