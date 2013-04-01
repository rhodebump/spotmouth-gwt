package com.spotmouth.gwt.client.chat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.dto.*;
import com.spotmouth.gwt.client.rpc.ApiServiceAsync;
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
 * Date: 3/20/13
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChattingComposite extends Composite {
    @UiField(provided = true)
    final FlowPanel chat;
    @UiField(provided = true)
    final TextBox input;
    @UiField(provided = true)
    final Button send;
    @UiField(provided = true)
    final DropPanel dropPanel;

    interface MyUiBinder extends UiBinder<Widget, ChattingComposite> {
    }

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
    private MyWebApp mywebapp = null;
    private ItemHolder itemHolder = null;

    public ChattingComposite(MyWebApp mywebapp, FlowPanel chat, TextBox input, Button send, ItemHolder itemHolder, DropPanel dropPanel) {
        this.chat = chat;
        this.input = input;
        this.send = send;
        this.mywebapp = mywebapp;
        this.itemHolder = itemHolder;
        this.dropPanel = dropPanel;
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
        } catch (Exception e) {
            GWT.log("Caugh filereader init error", e);
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

    protected FileReader reader;

    private void processFiles(FileList files) {
        GWT.log("length=" + files.getLength());
        for (File file : files) {
            readQueue.add(file);
        }
        readNext();
    }

    private void setBorderColor(String color) {
        chat.getElement().getStyle().setBorderColor(color);
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
                } else {
                    //GWT.log(type);
                    reader.readAsBinaryString(file);
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

    private void uploadFile(final File file) {
        final String name = file.getName();
        String result = reader.getStringResult();
        String data = FileUtils.base64encode(result);
        //we should probably upload file into session now
        doSaveImage(name, data);
    }

    private void doSaveImage(String name, String data) {
        mywebapp.getMessagePanel().clear();
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.uploadFile(name, data, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    //okay, need to call and add item to the chat item, and then, send an event with the url
                    //or, can we just get URL from returned mobileResponse and send as event, not even save it!
                    //no, let's add it as content, piggy-back off the existing process
                    saveItemHolder();
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void saveItemHolder() {
        mywebapp.getMessagePanel().clear();
        ContentRequest contentRequest = new ContentRequest();
        // leaveMarkRequest.setItemHolder(itemHolder);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.saveSessionContents(contentRequest, new AsyncCallback() {
            public void onFailure(Throwable caught) {
                mywebapp.getMessagePanel().displayError(caught.getMessage());
            }

            public void onSuccess(Object result) {
                MobileResponse mobileResponse = (MobileResponse) result;
                if (mobileResponse.getStatus() == 1) {
                    ContentHolder contentHolder = mobileResponse.getContentHolder();
                    //where is our new content in the this itemholder
                    //let's do all for now
                    //ContentHolder contentHolder = updatedItemHolder.getContentHolder();
                    for (ContentHolder child : contentHolder.getContentHolders()) {
                        if (child.getImage()) {
                            //do image
                            Image image = new Image();
                            image.setUrl(child.getWebServerAssetHolder().getUrl());
                            chat.add(image);
                        } else {
                            //do link
                            Anchor anchor = new Anchor();
                            anchor.setHref(child.getWebServerAssetHolder().getUrl());
                            anchor.setText(child.getFileName());
                            chat.add(anchor);
                        }
                    }
                    //let's update the item with these contents, and we will cleanse the session
                    updateItemHolder();
                } else {
                    mywebapp.getMessagePanel().displayErrors(mobileResponse.getErrorMessages());
                }
            }
        });
    }

    private void updateItemHolder() {
        mywebapp.getMessagePanel().clear();
        LeaveMarkRequest leaveMarkRequest = new LeaveMarkRequest();
        leaveMarkRequest.setItemHolder(itemHolder);
        ApiServiceAsync myService = mywebapp.getApiServiceAsync();
        myService.updateItemWithContents(leaveMarkRequest, new AsyncCallback() {
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
