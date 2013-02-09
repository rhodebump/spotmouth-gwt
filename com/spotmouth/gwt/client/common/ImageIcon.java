package com.spotmouth.gwt.client.common;
//import com.spotmouth.commons.AuthenticationResponse;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

//used to display error messages and such
public class ImageIcon extends FlowPanel {


    public ImageIcon(Image img, String labelText, String  token, String tooltip) {

        super();
        this.setStyleName("ImageIcon");
        Hyperlink hyperlink = new Hyperlink();
        hyperlink.setTargetHistoryToken(token);
        add(hyperlink);
        //alsdf
        hyperlink.getElement().appendChild(img.getElement());
        //add(img);
        img.setStyleName("spoticon");
        img.addStyleName("linky");
        img.setTitle(tooltip);
        //img.addClickHandler(clickHandler);
        Hyperlink lbl = new Hyperlink(labelText,token);
        lbl.setStyleName("icontext");
        //lbl.addClickHandler(clickHandler);
        add(lbl);

    }
    public ImageIcon(Image img, String labelText, ClickHandler clickHandler, String tooltip) {
        super();
        this.setStyleName("ImageIcon");
        add(img);
        img.setStyleName("spoticon");
        img.addStyleName("linky");
        img.setTitle(tooltip);
        img.addClickHandler(clickHandler);
        Label lbl = new Label(labelText);
        lbl.setStyleName("icontext");
        lbl.addStyleName("linky");
        lbl.addClickHandler(clickHandler);
        add(lbl);
    }
}
