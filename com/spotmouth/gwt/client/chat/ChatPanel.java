package com.spotmouth.gwt.client.chat;

import org.vectomatic.dnd.DropFlowPanel;
import com.spotmouth.gwt.client.dto.ItemHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.*;
import com.spotmouth.gwt.client.MyWebApp;
import com.spotmouth.gwt.client.SpotMouthPanel;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import org.atmosphere.gwt.client.AtmosphereClient;
import org.atmosphere.gwt.client.AtmosphereGWTSerializer;
import org.atmosphere.gwt.client.AtmosphereListener;
import com.spotmouth.gwt.client.dto.Event;
import org.vectomatic.dnd.DropPanel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/4/13
 * Time: 9:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatPanel extends SpotBasePanel implements SpotMouthPanel {
    public String getPageTitle() {
        return getTitle();
    }

    public String getTitle() {
        return itemHolder.getTitle();
    }



    private ItemHolder itemHolder = null;

    public ChatPanel(MyWebApp mywebapp, ItemHolder itemHolder) {
        super(mywebapp);
        this.itemHolder = itemHolder;

        if (mywebapp.getAuthenticatedUser() != null) {
            author = mywebapp.getAuthenticatedUser().getUsername();
        }  else {
            author = "Anonymous";
        }
        label = new Label(LABEL_TYPE_MESSAGE);
        add(label);





        input = new TextBox();
        input.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    sendMessage(input.getValue());
                    input.setText("");
                }
            }
        });
        //add(input);
        Button send = new Button("Send");
        send.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                sendMessage(input.getValue());
                input.setText("");
            }
        });
        //add(send);
        DropPanel dropPanel = getDropPanel();

        ChattingComposite chattingComposite = new ChattingComposite(mywebapp,chat,input,send,itemHolder,dropPanel);

        add(chattingComposite);



        HTMLPanel logPanel = new HTMLPanel("") {
            @Override
            public void add(Widget widget) {
                super.add(widget);
                widget.getElement().scrollIntoView();
            }
        };
        add(logPanel);
    }

    public void addedToDom() {
        super.addedToDom();
        joinChat(itemHolder.getId().toString());
    }

    public void toggleFirst() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    static final Logger logger = Logger.getLogger(ChatsPanel.class.getName());
    //static final String LABEL_ENTER_ROOM = "Type your name to enter the room";
    static final String LABEL_TYPE_MESSAGE = "Type a message to send to the room";
    static final String MESSAGE_JOINED_ROOM = "&lt;joined the room&gt;";
    static final String MESSAGE_LEFT_ROOM = "&lt;left the room&gt;";
    static final String MESSAGE_ROOM_CONNECTED = "[connected to room]";
    static final String MESSAGE_ROOM_DISCONNECTED = "[disconnected from room]";
    static final String MESSAGE_ROOM_ERROR = "Error: ";
    static final String COLOR_SYSTEM_MESSAGE = "grey";
    static final String COLOR_MESSAGE_SELF = "green";
    static final String COLOR_MESSAGE_OTHERS = "red";
   // int count = 0;
    AtmosphereClient client;
    MyCometListener cometListener = new MyCometListener();
    AtmosphereGWTSerializer serializer = GWT.create(EventSerializer.class);
    String author = null;
    Label label;
    TextBox input;
    FlowPanel chat = new FlowPanel();


    //String room = "room1";

    void sendMessage(String message) {
            client.broadcast(new Event(author, message));
    }

    String getUrl(String room) {
        return GWT.getModuleBaseURL() + "gwtComet/" + room;
    }

    private void joinChat(final String newRoom) {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                GWT.log("scheduleDeferred, new AtmosphereClient");
                //room = newRoom;

                String url = getUrl(newRoom);
                GWT.log("url=" + url);
                //   client = new AtmosphereClient(GWT.getModuleBaseURL() + "gwtComet", serializer, cometListener);
                client = new AtmosphereClient(url, serializer, cometListener);

                GWT.log("clearChat");
                clearChat();
                label.setText(LABEL_TYPE_MESSAGE);
                GWT.log("client start");
                client.start();
                GWT.log("client start");
            }
        });
    }

    void clearChat() {
        chat.clear();
    }

    void addChatLine(String line, String color) {
        GWT.log("addChatLine " + line);
        HTML newLine = new HTML(line);
        GWT.log("addChatLine1");
        newLine.getElement().getStyle().setColor(color);
        GWT.log("addChatLine2");
        //chat.appendChild(newLine.getElement());
        chat.add(newLine);
        //GWT.log("addChatLine3");
        //newLine.getElement().scrollIntoView();
    }

    private class MyCometListener implements AtmosphereListener {
        DateTimeFormat timeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.TIME_MEDIUM);

        @Override
        public void onConnected(int heartbeat, String connectionUUID) {
            logger.info("comet.connected [" + heartbeat + ", " + connectionUUID + "]");
            addChatLine(MESSAGE_ROOM_CONNECTED, COLOR_SYSTEM_MESSAGE);
        }

        @Override
        public void onBeforeDisconnected() {
            logger.log(Level.INFO, "comet.beforeDisconnected");
            if (author != null) {
                client.broadcast(new Event(author, MESSAGE_LEFT_ROOM));
            }
        }

        @Override
        public void onDisconnected() {
            logger.info("comet.disconnected");
            addChatLine(MESSAGE_ROOM_DISCONNECTED, COLOR_SYSTEM_MESSAGE);
        }

        @Override
        public void onError(Throwable exception, boolean connected) {
            int statuscode = -1;
            if (exception instanceof StatusCodeException) {
                statuscode = ((StatusCodeException) exception).getStatusCode();
            }
            logger.log(Level.SEVERE, "comet.error [connected=" + connected + "] (" + statuscode + ")", exception);
            addChatLine(MESSAGE_ROOM_ERROR + exception.getMessage(), COLOR_SYSTEM_MESSAGE);
        }

        @Override
        public void onHeartbeat() {
            logger.info("comet.heartbeat [" + client.getConnectionUUID() + "]");
        }

        @Override
        public void onRefresh() {
            logger.info("comet.refresh [" + client.getConnectionUUID() + "]");
        }

        @Override
        public void onAfterRefresh(String connectionUUID) {
            logger.info("comet.afterRefresh [" + client.getConnectionUUID() + "]");
        }

        @Override
        public void onMessage(List<?> messages) {
            for (Object obj : messages) {
                if (obj instanceof Event) {
                    Event e = (Event) obj;
                    String line = timeFormat.format(e.getTime())
                            + " <b>" + e.getAuthor() + "</b> " + e.getMessage();
                    if (e.getAuthor().equals(author)) {
                        addChatLine(line, COLOR_MESSAGE_SELF);
                    } else {
                        addChatLine(line, COLOR_MESSAGE_OTHERS);
                    }
                }
            }
        }
    }
}
