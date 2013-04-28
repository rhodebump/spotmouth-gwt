package com.spotmouth.gwt.client.contest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/3/13
 * Time: 5:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestResultComposite extends Composite {



    public ContestResultComposite(Image contestImage,Button mostVotedButton,Button winnersButton) {

        this.contestImage = contestImage;

        this.mostVotedButton = mostVotedButton;

        this.winnersButton = winnersButton;




    }


    @UiField
    Anchor detailAnchor;




    @UiField(provided=true)
    final Button mostVotedButton;


    @UiField(provided=true)
    final Button winnersButton;



    @UiField
    SpanElement nameSpan;


    public void setContestName(String contestName) {
        nameSpan.setInnerText(contestName);
    }



    @UiField(provided=true)
    final Image contestImage;




    @UiField
    SpanElement descriptionSpan;


    public void setDescription(String replyText) {
        descriptionSpan.setInnerText(replyText);
    }


}