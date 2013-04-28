package com.spotmouth.gwt.client.contest;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.ULPanel;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 4/18/13
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContestsComposite extends Composite {




    @UiField(provided=true)
    final Button createContestButton;



    @UiField(provided=true)
    final ULPanel contestResults;

    @UiTemplate("ContestsComposite.ui.xml")
    interface DesktopBinder extends UiBinder<Widget, ContestsComposite> {}
    private static DesktopBinder desktopBinder = GWT.create(DesktopBinder.class);

    @UiTemplate("ContestsCompositeM.ui.xml")
    interface MobileBinder extends UiBinder<Widget, ContestsComposite> {}
    private static MobileBinder mobileBinder = GWT.create(MobileBinder.class);

    public ContestsComposite(Button createContestButton,ULPanel contestResults) {
        this.createContestButton = createContestButton;
        this.contestResults = contestResults;

        if (MyWebApp.isDesktop()) {
            initWidget(desktopBinder.createAndBindUi(this));
        }else {
            initWidget(mobileBinder.createAndBindUi(this));
        }



    }

}
