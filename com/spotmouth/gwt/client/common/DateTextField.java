package com.spotmouth.gwt.client.common;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.modernizr.client.Modernizr;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/14/13
 * Time: 7:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DateTextField extends DateBox {
    /**
     * Creates an empty text box.
     */
    public DateTextField() {
    }

    String placeholder = "";

    /**
     * Gets the current placeholder text for the text box.
     *
     * @return the current placeholder text
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Sets the placeholder text displayed in the text box.
     *
     * @param placeholder the placeholder text
     */
    public void setPlaceholder(String text) {
        placeholder = (text != null ? text : "");
        getElement().setPropertyString("placeholder", placeholder);
        doPlaceHolder();
    }

    private void checkDone() {
        if (getTextBox().getValue().isEmpty()) {
            this.removeStyleName("_done");
        } else if (getTextBox().getValue().equals(placeholder)) {
            this.removeStyleName("_done");
        } else {
            this.addStyleName("_done");
        }
    }

    public void setValue2(String value) {
        getTextBox().setValue(value);
        checkDone();
    }

    private void doPlaceHolder() {
        if (Modernizr.inputAttribute(Modernizr.InputAttribute.PLACEHOLDER)) {
            return;
        }
        getTextBox().addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (getTextBox().getValue().isEmpty()) {
                    getTextBox().setValue(placeholder);
                }
                checkDone();
            }
        });
        getTextBox().addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                if (getTextBox().getValue().equals(placeholder)) {
                    getTextBox().setValue("");
                }
            }
        });
    }
}
