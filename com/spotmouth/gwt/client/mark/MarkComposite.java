package com.spotmouth.gwt.client.mark;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.dto.MobileResponse;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
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
 * Date: 12/4/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarkComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, MarkComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField
    SpanElement locationNameSpan;

    public void setLocationName(String locationName) {
        locationNameSpan.setInnerText(locationName);
    }

    @UiField
    SpanElement phoneNumberSpan;

    public void setPhoneNumber(String phoneNumber) {
        phoneNumberSpan.setInnerText(phoneNumber);
    }

    @UiField
    SpanElement fullAddressSpan;

    public void setFullAddress(String fullAddress) {
        fullAddressSpan.setInnerText(fullAddress);
    }

    @UiField(provided = true)
    final MultiUploader multiUploader;

    @UiField(provided = true)
    final FlowPanel panelImages;


    @UiField(provided=true)
    final Button leaveMarkButton;

    @UiField(provided=true)
    final TextArea contentTextArea;



    @UiField(provided=true)
    final TextBox secretKeyTextBox;

    @UiField(provided = true)
    DropPanel dropPanel;

    @UiField(provided=true)
    final SuggestBox tagSearchTextBox;

    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final FlowPanel suggestionsPanel;



    @UiField(provided = true)
    final Button addTagButton;


    @UiField(provided = true)
    final Button shareOnFacebookButton;


    @UiField(provided = false)
     Button cancelButton;




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
                GWT.log("MarkComposite.createBitmapImage size=" + width + "x" + height);
                image.setVisible(true);
            }
        });
        image.setUrl(url);
        return image;
    }

    private MyWebApp mywebapp = null;


    public MarkComposite(Button leaveMarkButton,TextArea contentTextArea,MultiUploader multiUploader,FlowPanel panelImages1,MyWebApp mywebapp,SuggestBox tagSearchTextBox,FlowPanel selectedTagsPanel,Button addTagButton,
                         TextBox secretKeyTextBox, Button shareOnFacebookButton,FlowPanel suggestionsPanel,DropPanel dropPanel) {
        this.dropPanel = dropPanel;
        this.suggestionsPanel = suggestionsPanel;

        this.leaveMarkButton = leaveMarkButton;
        this.contentTextArea  = contentTextArea;
        this.multiUploader = multiUploader;
        this.panelImages = panelImages1;
        this.selectedTagsPanel = selectedTagsPanel;
        this.tagSearchTextBox = tagSearchTextBox;
        this.mywebapp = mywebapp;
        this.secretKeyTextBox = secretKeyTextBox;
        this.shareOnFacebookButton = shareOnFacebookButton;



        this.addTagButton = addTagButton;


        initWidget(uiBinder.createAndBindUi(this));

        try {
            reader = new FileReader();
            reader.addLoadEndHandler(new LoadEndHandler() {
                @Override
                public void onLoadEnd(LoadEndEvent event) {
                    if (reader.getError() == null) {
                        if (readQueue.size() > 0) {
                            File file = readQueue.get(0);
                            try {
                                uploadFile(file);
                                Image image = createBitmapImage(file);
                                image.setStyleName("ddplaceholedr");
                                panelImages.setVisible(true);
                                panelImages.add(image);
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
        }catch (Exception e) {
            GWT.log("Caugh filereader init error",e);
        }
        readQueue = new ArrayList<File>();

    }
    private void uploadFile(final File file) {
        final String name = file.getName();
        String result = reader.getStringResult();
        String data = FileUtils.base64encode(result);

        //we should probably upload file into session now

        doSaveImage(name, data);



    }

   //cancelButton

    @UiHandler("cancelButton")
    public void onClickCancel(ClickEvent event) {
       String token =  MyWebApp.SPOT_DETAIL + spotId;

        History.newItem(token);
    }

    public void doSaveImage(String name,String data) {
        mywebapp.getMessagePanel().clear();

        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.uploadFile(name,data, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {

                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
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

    private Long spotId  = null;

   public void setSpotId(Long spotId) {
       this.spotId = spotId;
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


    public void hideTitle() {
        Element mstittle = DOM.getElementById("mstittle");

        mstittle.addClassName("hideme");
    }

}
