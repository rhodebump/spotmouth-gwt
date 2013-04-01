package com.spotmouth.gwt.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.modernizr.client.Modernizr;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created with IntelliJ IDEA.
 * User: prhodes
 * Date: 2/13/13
 * Time: 9:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class TextField extends TextBox {
    //_done

    private boolean placeholderSupported = true;

    /**
     * Creates an empty text box.
     */
    public TextField() {
        placeholderSupported =  Modernizr.inputAttribute(Modernizr.InputAttribute.PLACEHOLDER);

        if (! placeholderSupported) {

            this.addBlurHandler(new BlurHandler() {
                @Override
                public void onBlur(BlurEvent event) {
                    if (getValue().isEmpty()) {
                        TextField.this.setValue(placeholder);
                    }
                    checkDone();
                }
            });
            this.addFocusHandler(new FocusHandler() {
                @Override
                public void onFocus(FocusEvent event) {
                    if (TextField.this.getValue().equals(placeholder)) {
                        TextField.this.setValue("smclear");
                    }
                }
            });
        }

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
        if (getValue().isEmpty()) {
            this.removeStyleName("_done");
        } else if (getValue().equals(placeholder)) {
            this.removeStyleName("_done");
        } else {
            this.addStyleName("_done");
        }
    }

    protected boolean isEmpty(String val) {
        if (val == null || val.length() == 0) {
            return true;
        } else if (val.trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
//
//    public void setValue(String value) {
//        setValue(value,true);
//    }

    //emptyCheck
    public void setValue(String value) {
        if (isEmpty(value)) {
            return;
        }
        //oh so dirty...
        if (value.equals("smclear")) {
            value = "";
        }
        GWT.log("setValue " + value);
        super.setValue(value);
        checkDone();
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
        doPlaceHolder();
    }

    private void doPlaceHolder() {
        if (placeholderSupported) {
            return;
        }
        if (getValue().isEmpty()) {
            TextField.this.setValue(placeholder);
        }
    }

    String required = "";

    /**
     * Gets the current placeholder text for the text box.
     *
     * @return the current placeholder text
     */
    public String getRequired() {
        return required;
    }

    /**
     * Sets the placeholder text displayed in the text box.
     *
     * @param placeholder the placeholder text
     */
    public void setRequired(String text) {
        required = (text != null ? text : "");
        getElement().setPropertyString("required", required);
    }

    String type = "";

    /**
     * Gets the current placeholder text for the text box.
     *
     * @return the current placeholder text
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the placeholder text displayed in the text box.
     *
     * @param placeholder the placeholder text
     */
    public void setType(String type) {
        getElement().setAttribute("type", type);
    }
}