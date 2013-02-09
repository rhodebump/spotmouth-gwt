package com.spotmouth.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.*;

public class LocationFormPanel extends VerticalPanel {
    Storage localStorage = Storage.getLocalStorageIfSupported();

    public LocationFormPanel() {
        Grid grid = new Grid(4, 2);
        add(grid);
        // add(new Label("Settings"));
        Label settings = new Label("Settings");
        grid.setWidget(0, 0, settings);
        TextBox username = new TextBox();
        String usernameVal = localStorage.getItem("username");
        username.setValue(usernameVal);
        Label usernameLabel = new Label("Username:");
        grid.setWidget(1, 0, usernameLabel);
        grid.setWidget(1, 1, username);
        TextBox password = new TextBox();
        String passwordVal = localStorage.getItem("password");
        password.setValue(passwordVal);
        Label passwordLabel = new Label("Password:");
        grid.setWidget(2, 0, passwordLabel);
        grid.setWidget(2, 1, password);
        grid.setWidget(3, 0, saveSettingsButton());
    }

    Button saveSettingsButton() {
        return new Button("Save", saveSettingsHandler);
    }

    ClickHandler saveSettingsHandler = new ClickHandler() {
        public void onClick(ClickEvent event) {
            //mywebapp.toggleConsole();
            if (Storage.isSupported()) {
                // Interact with the storage...
                localStorage.setItem("key", "value");
            }
        }
    };
}
