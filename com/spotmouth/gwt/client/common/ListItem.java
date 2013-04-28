package com.spotmouth.gwt.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.*;

public class ListItem extends HTMLPanel implements   HasClickHandlers {
    public ListItem() {
        //setElement(DOM.createElement("LI"));
        super("li", "");
    }

//    public void add(Widget w) {
//        super.add(w, getElement());
//    }
//
//    public void insert(Widget w, int beforeIndex) {
//        super.insert(w, getElement(), beforeIndex, true);
//    }
//
//    public String getText() {
//        return DOM.getInnerText(getElement());
//    }
//
//    public String getHTML() {
//        return DOM.getInnerText(getElement());
//    }
//
//
//
    public ListItem(String text) {
        super("li", text);
    }
//
//
//    public void setHTML(String html) {
//        //
//        // DOM.setInnerText(getElement(), (text == null) ? "" : text);
//        DOM.setInnerHTML(getElement(),html);
//        //HTML h;
//        //hasText = hasHtml = h = new HTML(html);
////
////        contents.clear();
////        contents.add(h);
//
//
//    }
//
//    public void setText(String text) {
//        DOM.setInnerText(getElement(), (text == null) ? "" : text);
//    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
