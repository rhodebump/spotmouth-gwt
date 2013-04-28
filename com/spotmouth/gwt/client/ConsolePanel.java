package com.spotmouth.gwt.client;
//import com.spotmouth.commons.AuthenticationResponse;
import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.spotmouth.gwt.client.common.SpotBasePanel;
//import com.allen_sauer.gwt.log.client.Log;

public class ConsolePanel extends SpotBasePanel implements SpotMouthPanel {


    public String getTitle() {
        return "Console";
    }
    public ConsolePanel() {

    }



    public ConsolePanel(MyWebApp mywebapp) {
        super(mywebapp);
        Widget divLogger = Log.getLogger(DivLogger.class).getWidget();
        add(divLogger);




        //ADD A TEXT TO APPEND TO
    }

    public void init() {
        GWT.log("consolePanel init");
       if (Log.isLoggingEnabled()) {
            final DivLogger divLogger = Log.getLogger(DivLogger.class);
           GWT.log("logging enabled");

               add(divLogger.getWidget());
        }  else {
           GWT.log("logging NOT enabled");
       }

    }

    @Override
    public void toggleFirst() {
        // TODO Auto-generated method stub
    }



}
