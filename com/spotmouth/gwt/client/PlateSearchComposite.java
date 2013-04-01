package com.spotmouth.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
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
 * Date: 10/25/12
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlateSearchComposite extends Composite {
    interface MyUiBinder extends UiBinder<Widget, PlateSearchComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    @UiField(provided = true)
    final ListBox colorsListBox;
    @UiField(provided = true)
    final TextBox plateNameTextBox;
    @UiField(provided = true)
    final TextBox keywordsTextBox;
    @UiField(provided = true)
    final ListBox manufacturersListBox;
    @UiField(provided = true)
    final SuggestBox stateTextBox;
    @UiField(provided = true)
    final ListBox vehicleTypeListBox;
    @UiField(provided = true)
    final Button plateSearchButton;
    @UiField(provided = true)
    final SuggestBox tagSearchTextBox;
    @UiField(provided = true)
    final TextBox secretKeyTextBox;
    @UiField(provided = true)
    final TextArea contentTextArea;

    @UiField(provided = true)
    final TextArea spotDescriptionTextArea;




    @UiField(provided = true)
    final FlowPanel selectedTagsPanel;
    @UiField(provided = true)
    final Button leaveMarkButton;


    @UiField(provided = true)
    final Button shareOnFacebookButton;

    @UiField(provided = true)
    final Button addTagButton;




    @UiField(provided = true)
    SimpleRadioButton tab1 = null;
    @UiField(provided = true)
    SimpleRadioButton tab2 = null;
    @UiField(provided = true)
    SimpleRadioButton tab3 = null;
    @UiField(provided = true)
    DropPanel dropPanel;
    @UiField(provided = true)
    final MultiUploader multiUploader;
    @UiField(provided = true)
    final FlowPanel panelImages;
    @UiField(provided = true)
    final ULPanel pickSpotUL;


    @UiField(provided = true)
    final FlowPanel suggestionsPanel;



    public void setTab3(boolean x) {
        tab3.setValue(x);
    }

    String group = "tab-group-1";

    private MyWebApp mywebapp = null;


    public PlateSearchComposite(ListBox colorsListBox, TextBox plateNameTextBox, TextBox keywordsTextBox, ListBox manufacturersListBox, SuggestBox stateTextBox, ListBox vehicleTypeListBox,
                                Button plateSearchButton, SuggestBox tagSearchTextBox, TextBox secretKeyTextBox, TextArea contentTextArea, FlowPanel selectedTagsPanel,
                                Button leaveMarkButton, MultiUploader multiUploader, FlowPanel panelImages1, ULPanel pickSpotUL,MyWebApp mywebapp,Button shareOnFacebookButton,Button addTagButton,
                                FlowPanel suggestionsPanel,TextArea spotDescriptionTextArea,DropPanel dropPanel
    ) {
        this.dropPanel = dropPanel;
        this.spotDescriptionTextArea = spotDescriptionTextArea;
        this.suggestionsPanel = suggestionsPanel;
        this.addTagButton = addTagButton;
        this.addTagButton.setStyleName("btn_blue");
        this.mywebapp = mywebapp;
        this.colorsListBox = colorsListBox;
        this.plateNameTextBox = plateNameTextBox;
        this.keywordsTextBox = keywordsTextBox;
        this.manufacturersListBox = manufacturersListBox;
        this.stateTextBox = stateTextBox;
        this.vehicleTypeListBox = vehicleTypeListBox;
        this.plateSearchButton = plateSearchButton;
        this.tagSearchTextBox = tagSearchTextBox;
        this.secretKeyTextBox = secretKeyTextBox;
        this.contentTextArea = contentTextArea;
        this.selectedTagsPanel = selectedTagsPanel;
        this.leaveMarkButton = leaveMarkButton;
        this.shareOnFacebookButton = shareOnFacebookButton;
        this.multiUploader = multiUploader;
        this.panelImages = panelImages1;
        this.pickSpotUL = pickSpotUL;
        this.tab1 = new SimpleRadioButton(group);
        this.tab2 = new SimpleRadioButton(group);
        this.tab3 = new SimpleRadioButton(group);
        this.tab1.setValue(false);
        this.tab2.setValue(false);
        this.tab3.setValue(false);
        tab1.getElement().setId("tab-1");
        tab2.getElement().setId("tab-2");
        tab3.getElement().setId("tab-3");
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
            GWT.log("filereader init error',e");
        }
        readQueue = new ArrayList<File>();
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

    protected List<File> readQueue;
    protected FileReader reader;

    private void processFiles(FileList files) {
        GWT.log("length=" + files.getLength());
        for (File file : files) {
            readQueue.add(file);
        }
        readNext();
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

    private void setBorderColor(String color) {
        dropPanel.getElement().getStyle().setBorderColor(color);
    }

    private void uploadFile(final File file) {
        final String name = file.getName();
        String result = reader.getStringResult();
        String data = FileUtils.base64encode(result);

        //we should probably upload file into session now

        doSaveImage(name, data);

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


}
