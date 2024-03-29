package com.spotmouth.gwt.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class ULPanel extends ComplexPanel {
    private UListElement list;

    public ULPanel() {
        list = Document.get().createULElement();
        setElement(list);
    }

    @Override
    public void add(Widget w) {
        super.add(w, getElement());
        // Element li = Document.get().createLIElement().cast();
        // list.appendChild(li);
        //super.add(child, li);
    }
}
