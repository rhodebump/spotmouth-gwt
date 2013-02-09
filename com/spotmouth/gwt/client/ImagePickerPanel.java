package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.dto.ContentHolder;
import com.spotmouth.gwt.client.dto.ItemHolder;

/**
 * Created by IntelliJ IDEA.
 * User: prhodes
 * Date: 3/10/12
 * Time: 9:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImagePickerPanel extends SpotBasePanel {
    private RichTextArea.ExtendedFormatter extended = null;
    ClickHandler insertImageHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {

            GWT.log("insertImageHandler onClick");


            Object sender = event.getSource();
            if (sender instanceof Widget) {
                GWT.log("Got a Label");
                Widget b = (Widget) sender;
                ContentHolder contentHolder = mediaMap.get(b);
                String url = mywebapp.getUrl(contentHolder.getWebServerAssetHolder().getUrl());
                //String url = contentHolder.getWebServerAssetHolder().getUrl();
                GWT.log("ur=" + url);
                extended.insertImage(url);
            }
        }
    };
    ClickHandler imageUrlHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            String url = imageUrlTextBox.getValue();
            if (!url.startsWith("http://")) {
                getMessagePanel().displayError("Image URL needs to begin with http://");
            } else {
                extended.insertImage(url);
            }
        }
    };
    TextBox imageUrlTextBox = null;

    public ImagePickerPanel() {
    }

    public ImagePickerPanel(MyWebApp mywebapp, ItemHolder itemHolder, RichTextArea.ExtendedFormatter extended) {
        super(mywebapp,false);
        setItemHolder(itemHolder);
        this.extended = extended;
        imageUrlTextBox = addTextBox("Image URL", "na", "http://");
        Label useUrlLabel = new Label("Use URL");
        useUrlLabel.addClickHandler(imageUrlHandler);
        fixButton(useUrlLabel);
        add(useUrlLabel);
        //add okay
        //add label
        Label label = new Label("OR Click on image below to insert into your message");
        add(label);
        add(contentsPanel);
        //add(panelImages);
        addContentHolder(itemHolder.getContentHolder());

        //we should add to upload images here to review

        //need to loop through the images and add link
    }

    protected void addContentHolder(ContentHolder contentHolder) {
        contentsPanel.clear();
        if (contentHolder == null) {
            return;
        }
        if (contentHolder.getContentHolders() == null) {
            debug("contentHolder.getContentHolders() is null");
            return;
        }

        debug("contentHolder.getContentHolders().length()=" + contentHolder.getContentHolders().size());
        for (ContentHolder ch : contentHolder.getContentHolders()) {
            if (ch.getImage()) {
                debug("it is an image");
                Image image = addImageContent(contentsPanel, ch, "130x130");
                image.addClickHandler(insertImageHandler);
                mediaMap.put(image, ch);
            }
        }
    }
}
