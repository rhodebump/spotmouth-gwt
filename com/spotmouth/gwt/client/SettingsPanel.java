package com.spotmouth.gwt.client;
//import com.spotmouth.commons.AuthenticationResponse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.spotmouth.gwt.client.common.SpotBasePanel;
import com.spotmouth.gwt.client.help.HelpResources;

public class SettingsPanel extends SpotBasePanel implements SpotMouthPanel {
    public ImageResource getImageResource() {
        if (MyWebApp.isSmallFormat()) {

            return MyWebApp.resources.settingsMobile();
        }  else {
            return MyWebApp.resources.settings();
        }

    }

    public TextResource getHelpTextResource() {
        return HelpResources.INSTANCE.getSettingsPanel();
    }

    public String getTitle() {
        return "Settings";
    }

    public SettingsPanel() {
    }

    TextBox usernameTextBox = null;
    TextBox passwordTextBox =null;

    public SettingsPanel(MyWebApp mywebapp) {
        super(mywebapp);
        this.setStyleName("panel");
        this.setWidth("100%");

        usernameTextBox = addTextBoxNoMessing("Username", "username", "");
        passwordTextBox = addTextBoxNoMessing("Password", "password", "");

        if (Storage.isLocalStorageSupported()) {
            Storage localStorage = Storage.getLocalStorageIfSupported();
            String usernameVal = localStorage.getItem("username");
            usernameTextBox.setValue(usernameVal);

            String passwordVal = localStorage.getItem("password");
            passwordTextBox.setValue(passwordVal);

        }

        add(saveSettingsButton());
        Label wipeSettingsButton = new Label("Clean All Settings");
        wipeSettingsButton.setStyleName("whiteButton");
        wipeSettingsButton.addClickHandler(wipeOutSettings);
        add(wipeSettingsButton);
        //grid.setWidget(4, 0, saveSettingsButton());
    }

    /*
    Button testLoginButton() {
        Button btn =  new Button("Test Login", testLoginHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler testLoginHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            testLogin();

        }
    };
    */
    ClickHandler wipeOutSettings = new ClickHandler() {
        public void onClick(ClickEvent event) {
            boolean success = Window.confirm("Delete all settings?");
            if (success) {
                if (Storage.isLocalStorageSupported()) {
                    Storage localStorage = Storage.getLocalStorageIfSupported();
                    localStorage.clear();
                }

            }
        }
    };

    Label saveSettingsButton() {
        Label btn = new Label("Save Settings");
        btn.addClickHandler(saveSettingsHandler);
        btn.setStyleName("whiteButton");
        return btn;
    }

    ClickHandler saveSettingsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            Storage localStorage = Storage.getLocalStorageIfSupported();
            if (Storage.isLocalStorageSupported()) {
                localStorage.setItem("username", usernameTextBox.getValue());
                localStorage.setItem("password", passwordTextBox.getValue());
            } else {
                GWT.log("local storage NOT supported");
            }
            mywebapp.toggleOptions();
        }
    };

    @Override
    public void toggleFirst() {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean isLoginRequired() {
        // TODO Auto-generated method stub
        return false;
    }
//    @Override
//    public boolean isRootPanel() {
//        // TODO Auto-generated method stub
//        return false;
//    }
}
