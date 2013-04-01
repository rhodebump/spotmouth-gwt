package com.spotmouth.gwt.client;

public class DataOperationDialog {
    public DataOperationDialog(String message) {
        super();
        //var showPop = function(){
        //    	document.getElementById('popup').style.zIndex = "9999999";
        // 	document.getElementById('popup').style.opacity = "1";
        // }
    }

    public void show() {
        MyWebApp.showPopup();
    }

    public void hide() {
        MyWebApp.hidePopup();
    }

    public void center() {
    }
}
