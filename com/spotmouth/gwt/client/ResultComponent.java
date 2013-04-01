package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/25/12
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResultComponent extends Composite {
    interface MyUiBinder extends UiBinder<Widget, ResultComponent> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    @UiField(provided=true)
    final TextArea markTextArea;


    @UiField(provided=true)
    final Button leaveMarkButton;

    @UiField(provided=true)
    final Image userImage;

    @UiField(provided=true)
    final ULPanel latestMarksPanel;


    @UiField(provided=true)
    final Button showAdvancedButton;


    @UiField(provided=true)
    final Button leaveMarkFacebookButton;




    public ResultComponent(TextArea markTextArea,Button leaveMarkButton,Image userImage,ULPanel latestMarksPanel,Button showAdvancedButton,Button leaveMarkFacebookButton) {
        this.markTextArea = markTextArea;

        this.leaveMarkButton = leaveMarkButton;
        this.userImage = userImage;
        this.latestMarksPanel = latestMarksPanel;
        this.showAdvancedButton = showAdvancedButton;
        this.leaveMarkFacebookButton = leaveMarkFacebookButton;
        initWidget(uiBinder.createAndBindUi(this));
    }
}
