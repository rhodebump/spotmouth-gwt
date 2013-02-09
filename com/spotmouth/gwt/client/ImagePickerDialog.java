package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.spotmouth.gwt.client.dto.ItemHolder;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/10/12
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePickerDialog extends DialogBox {
    ClickHandler closeHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            ImagePickerDialog.this.hide();
        }
    };

    public ImagePickerDialog(MyWebApp mywebapp, ItemHolder itemHolder, RichTextArea.ExtendedFormatter extended) {
        super();
        ImagePickerPanel imagePickerPanel = new ImagePickerPanel(mywebapp, itemHolder, extended);
        imagePickerPanel.setStyleName("ImagePickerPanel");
        Label cancelLabel = new Label("Close");
        cancelLabel.addClickHandler(closeHandler);
        cancelLabel.setStyleName("whiteButton");
        imagePickerPanel.add(cancelLabel);
        this.setWidget(imagePickerPanel);
        this.center();
    }
}


