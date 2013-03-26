package com.spotmouth.gwt.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import com.google.gwt.user.client.ui.Anchor;
import com.spotmouth.gwt.client.dto.TagHolder;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//used to display error messages and such
public class MessagePanel extends FlowPanel {


    protected ClickHandler killNotificationHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            clear();
        }
    };







    private Logger log = Logger.getLogger(getClass().getName());


    /*
    		<div id="notification" class="normal"><p>Scroll down and wait for 3 sec<a>details</a></p><a id="kill_notification">x</a><a id="resize_notification"></a></div>

     */
    //JsArrayString
    public void displayErrors(List<String> errorMessages) {
        GWT.log("displayErrors");
        for(String errorMessage:errorMessages) {
            displayError(errorMessage);
        }
    }

    FlowPanel messageHolderPanel = new FlowPanel();

    public MessagePanel() {
        setStyleName("normal");
        getElement().setId("notification");



        add(messageHolderPanel);

        Anchor kill_notification = new Anchor("x");
        kill_notification.setStyleName("kill_notification");
        kill_notification.getElement().setId("kill_notification");
        //<a id="kill_notification">x</a>
        add(kill_notification);
        kill_notification.addClickHandler(killNotificationHandler);



        Anchor resize_notification = new Anchor("x");
        resize_notification.getElement().setId("resize_notification");
        resize_notification.addClickHandler(resizeHandler);


        resize_notification.setStyleName("resize_notification");
        add(resize_notification);




    }

    protected ClickHandler resizeHandler = new ClickHandler() {
         public void onClick(ClickEvent event) {
            int pageY = Window.getScrollTop();

             com.google.gwt.dom.client.Element notElem = DOM.getElementById("notification");
             //int notTop = MyWebApp.getCoords(notElem);
            // if (pageY >= notTop + notElem.getOffsetHeight()) {
             if (Window.getScrollTop() == 0) {
                 notElem.setClassName( "normal");

             } else {
                 notElem.setClassName( "normal scrolled");
             }

         }
     };


    /*

 document.getElementById('resize_notification').onclick = function(){
 	pageY = window.pageYOffset || document.documentElement.scrollTop;
 	if (pageY >= notTop + notElem.offsetHeight) {
 		notElem.className = "normal scrolled";
 	} else {
 		notElem.className = "normal";
 	}
 	timer = 3000;
 	}


     */

    public void clear() {
        messageHolderPanel.clear();
        setVisible(false);
        haveMessages = false;


        //we need to add notification to clear this



    }


    //super long words break the ui
    private String chopMessage(String message) {
        log.log(Level.SEVERE,"chopMessage=" + message);
        if (message == null) return "";
        StringBuffer sb = new StringBuffer();
        if (message.length() == 0) {
            return "";
        }

        String[] words = message.split(" ");
        for (String word : words) {
            log.log(Level.SEVERE,"chopMessage word=" + word);

            if (word != null) {
                while (word.length() > 30) {
                    sb.append(word.substring(0, 30));
                    sb.append(" ");
                    word = word.substring(30);
                }
                sb.append(word);

            }


            sb.append(" ");
        }
        return sb.toString();


    }
    public void displayError(String errorMessage, Throwable t) {
        if (t != null) {
            displayError(errorMessage + ":" + t.toString());
        } else {
            displayError(errorMessage);
        }

    }

    private void fixNotification() {
        //we call this method whenever a new message is added, let's set minimized back to false
        setMinimized(false);
        GWT.log("scrolltop=" + Window.getScrollTop());
        com.google.gwt.dom.client.Element notElem = DOM.getElementById("notification");


        if (Window.getScrollTop() == 0) {
            notElem.setClassName("normal");
        } else {
            notElem.setClassName("normal scrolled");
        }



    }


    private boolean minimized = false;
    public void setMinimized(boolean minimized) {
        this.minimized = minimized;
    }
    public boolean isMinimized() {
       return minimized;
    }



    public void displayError(String errorMessage) {

        fixNotification();

        GWT.log("displayError " + errorMessage);
        //scrolling to top was annoying for location problems
        //shouldn't be getting location problems
        //we now have notification appear good
       // com.google.gwt.user.client.Window.scrollTo(0, 0);

        String m = chopMessage(errorMessage);
       // Label errorLabel = new Label(m);
        InlineHTML html   = new InlineHTML("<p>" + errorMessage + "</p>");
       // errorLabel.setStyleName("errorMessage");
       //errorLabel.setWidth("100%");
        messageHolderPanel.add(html);

        this.haveMessages = true;
        setVisible(haveMessages);
    }

    //this is same as displayError, but we will not scroll totop
    public void displayMessage(String message) {
         fixNotification();

        String m = chopMessage(message);
        InlineHTML html   = new InlineHTML("<p>" + message + "</p>");

        //Label errorLabel = new Label(m);
        //errorLabel.setStyleName("errormessage");
        //errorLabel.setWidth("100%");
        messageHolderPanel.add(html);

        this.haveMessages = true;
        setVisible(haveMessages);


    }

    private boolean haveMessages = false;

    public boolean isHaveMessages() {
        return haveMessages;
    }
}
