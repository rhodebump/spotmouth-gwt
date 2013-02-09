package com.spotmouth.gwt.client.mark;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 12/13/12
 * Time: 6:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReplyComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ReplyComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    public ReplyComposite(Image itemUserImage,FlowPanel replyContents) {
        this.itemUserImage = itemUserImage;
        this.replyContents  = replyContents;
        replyContents.setStyleName("md_rep_att_files");
        initWidget(uiBinder.createAndBindUi(this));

    }




    @UiField
    SpanElement usernameSpan;


    public void setUsername(String username) {
        usernameSpan.setInnerText(username);
    }

    @UiField(provided=true)
    final FlowPanel replyContents;


    @UiField(provided=true)
    final Image itemUserImage;




    @UiField
    SpanElement replyTextSpan;


    public void setReplyText(String replyText) {
        replyTextSpan.setInnerText(replyText);
    }


}
