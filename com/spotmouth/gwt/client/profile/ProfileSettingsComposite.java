package com.spotmouth.gwt.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import gwtupload.client.MultiUploader;
import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.*;
import org.vectomatic.file.events.ErrorEvent;
import org.vectomatic.file.events.ErrorHandler;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 11/4/12
 * Time: 7:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileSettingsComposite extends Composite {
    @UiField(provided = true)
    final SuggestBox countryTextBox;
    @UiField(provided = true)
    final SuggestBox cityTextBox;
    @UiField(provided = true)
    final SuggestBox stateTextBox;
    @UiField(provided = true)
    final TextArea aboutMeTextArea;
    @UiField(provided = true)
    final Button saveButton;
    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;
    @UiField(provided = true)
    final Anchor addTagAnchor;
    @UiField(provided = true)
    final SimplePanel profileImagePanel;
    @UiField(provided = true)
    final Anchor removeProfileImageAnchor;
    @UiField(provided = true)
    DropPanel dropPanel;
    @UiField(provided = true)
    final MultiUploader multiUploader;
    @UiField(provided = true)
    final FlowPanel panelImages;
    @UiField(provided = true)
    final FlowPanel suggestionsPanel;


    interface MyUiBinder extends UiBinder<Widget, ProfileSettingsComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);


    private MyWebApp mywebapp = null;
    private ProfileSettingsPanel profileSettingsPanel = null;

    public ProfileSettingsComposite(SuggestBox countryTextBox, SuggestBox cityTextBox, SuggestBox stateTextBox,
                                    TextArea aboutMeTextArea, Button saveButton, FlowPanel selectedTagsPanel, SuggestBox tagSearchTextBox, Anchor addTagAnchor,
                                    Anchor removeProfileImageAnchor, MultiUploader multiUploader, FlowPanel panelImages, final SimplePanel profileImagePanel,MyWebApp mywebapp,
                                    ProfileSettingsPanel profileSettingsPanel,FlowPanel suggestionsPanel,DropPanel dropPanel) {
        this.dropPanel = dropPanel;
        this.suggestionsPanel = suggestionsPanel;

        this.mywebapp = mywebapp;
        this.profileSettingsPanel=  profileSettingsPanel;
        this.countryTextBox = countryTextBox;
        this.cityTextBox = cityTextBox;
        this.stateTextBox = stateTextBox;
        this.aboutMeTextArea = aboutMeTextArea;
        this.saveButton = saveButton;
        this.tagSearchTextBox = tagSearchTextBox;
        this.selectedTagsPanel = selectedTagsPanel;
        this.addTagAnchor = addTagAnchor;
        this.profileImagePanel = profileImagePanel;
        this.removeProfileImageAnchor = removeProfileImageAnchor;

        this.panelImages = panelImages;
        this.multiUploader = multiUploader;
        initWidget(uiBinder.createAndBindUi(this));

        reader = new FileReader();
        reader.addLoadEndHandler(new LoadEndHandler() {
            @Override
            public void onLoadEnd(LoadEndEvent event) {
                if (reader.getError() == null) {
                    if (readQueue.size() > 0) {
                        File file = readQueue.get(0);
                        try {
                            profileImagePanel.clear();
                            uploadFile(file);
                            Image image = createBitmapImage(file);
                            profileImagePanel.setWidget(image);
                        } finally {
                            readQueue.remove(0);
                            readNext();
                        }
                    }
                }
            }
        });
        reader.addErrorHandler(new ErrorHandler() {
            @Override
            public void onError(ErrorEvent event) {
                if (readQueue.size() > 0) {
                    File file = readQueue.get(0);
                    handleError(file);
                    readQueue.remove(0);
                    readNext();
                }
            }
        });
        readQueue = new ArrayList<File>();
    }

    /*
    private FlowPanel createThumbnail(File file) {
        FlowPanel thumbnail = new FlowPanel();
        //thumbnail.setStyleName(bundle.css().thumbnail());
        String type = file.getType();
        final String name = file.getName();
        final JsDate date = file.getLastModifiedDate();
        Widget image = null;
        if ("image/svg+xml".equals(type)) {
            //image = createSvgImage();
        } else if (type.startsWith("image/")) {
            image = createBitmapImage(file);
        } else if (type.startsWith("text/")) {
            image = createText(file);
        }
        SimplePanel thumbnailImage = new SimplePanel(image);
        //thumbnailImage.setStyleName(bundle.css().thumbnailImage());
        thumbnail.add(thumbnailImage);
        StringBuilder description = new StringBuilder(name);
        if (date != null) {
            description.append(" (");
            description.append(date.toLocaleDateString());
            description.append(")");
        }
        Label thumbnailText = new Label(description.toString());
        // thumbnailText.setStyleName(bundle.css().thumbnailText());
        thumbnail.add(thumbnailText);
        return thumbnail;
    }

    */

    private void uploadFile(final File file) {
        final String name = file.getName();
        String result = reader.getStringResult();
        String data = FileUtils.base64encode(result);


        profileSettingsPanel.doSaveImage(name,data);

        //String dataUrl = FileUtils.createDataUrl(file.getType(), result);
//        mywebapp.getApiServiceAsync().uploadFile(name, data, new AsyncCallback() {
//            public void onFailure(Throwable throwable) {
//               mywebapp.getMessagePanel().displayMessage("Error Occurred during file upload");
//            }
//
//            public void onSuccess(Object obj) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                //let's call the save user
//
//                MobileResponse mobileResponse = (MobileResponse) obj;
//                if (mobileResponse.getStatus() == 1) {
//                    profileSettingsPanel.doSaveImage();
//                } else {
//                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
//                }
//
//
//
//            }
//        });

    }

    private Image createBitmapImage(final File file) {

        String result = reader.getStringResult();
        String url = FileUtils.createDataUrl(file.getType(), result);
        final Image image = new Image();
        image.setVisible(false);
        image.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                int width = image.getWidth();
                int height = image.getHeight();
                GWT.log("size=" + width + "x" + height);
                image.setVisible(true);
            }
        });
        image.setUrl(url);
        return image;
    }



    @UiHandler("dropPanel")
    public void onDragOver(DragOverEvent event) {
        // Mandatory handler, otherwise the default
        // behavior will kick in and onDrop will never
        // be called
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void onDragEnter(DragEnterEvent event) {
        setBorderColor("red");
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void onDragLeave(DragLeaveEvent event) {
        setBorderColor("black");
        event.stopPropagation();
        event.preventDefault();
    }

    @UiHandler("dropPanel")
    public void onDrop(DropEvent event) {
        processFiles(event.getDataTransfer().<DataTransferExt>cast().getFiles());
        setBorderColor("black");
        event.stopPropagation();
        event.preventDefault();
    }





    protected FileReader reader;

    private void processFiles(FileList files) {
        GWT.log("length=" + files.getLength());
        for (File file : files) {
            readQueue.add(file);
        }
        readNext();
    }

    private void setBorderColor(String color) {
        dropPanel.getElement().getStyle().setBorderColor(color);
    }

    private void readNext() {
        if (readQueue.size() > 0) {
            File file = readQueue.get(0);
            String type = file.getType();
            try {
                if ("image/svg+xml".equals(type)) {
                    reader.readAsText(file);
                } else if (type.startsWith("image/")) {
                    reader.readAsBinaryString(file);
                } else if (type.startsWith("text/")) {
                    // If the file is larger than 1kb, read only the first 1000 characters
                    Blob blob = file;
                    if (file.getSize() > 0) {
                        blob = file.slice(0, 1000, "text/plain; charset=utf-8");
                    }
                    reader.readAsText(blob);
                }
            } catch (Throwable t) {
                // Necessary for FF (see bug https://bugzilla.mozilla.org/show_bug.cgi?id=701154)
                // Standard-complying browsers will to go in this branch
                handleError(file);
                readQueue.remove(0);
                readNext();
            }
        }
    }

    protected List<File> readQueue;

    private void handleError(File file) {
        FileError error = reader.getError();
        String errorDesc = "";
        if (error != null) {
            ErrorCode errorCode = error.getCode();
            if (errorCode != null) {
                errorDesc = ": " + errorCode.name();
            }
        }
        Window.alert("File loading error for file: " + file.getName() + "\n" + errorDesc);
    }
}