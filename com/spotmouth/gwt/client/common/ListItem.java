package com.spotmouth.gwt.client.common;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class ListItem extends ComplexPanel implements HasText , HasClickHandlers {
    public ListItem() {
        setElement(DOM.createElement("LI"));
    }

    public void add(Widget w) {
        super.add(w, getElement());
    }

    public void insert(Widget w, int beforeIndex) {
        super.insert(w, getElement(), beforeIndex, true);
    }

    public String getText() {
        return DOM.getInnerText(getElement());
    }

    public ListItem(String text) {
        this();
        setText(text);
    }

    public void setText(String text) {
        DOM.setInnerText(getElement(), (text == null) ? "" : text);
    }

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }
}
