package com.spotmouth.gwt.client.common;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TextArea;

/**
 * A {@link TextArea} that automatically grows and shrinks depending on its content.
 *
 * @author zubi
 *
 */
public class AutoGrowTextArea extends TextArea {

    private class SizeHandler implements KeyUpHandler, KeyPressHandler, BlurHandler {

        @Override
        public void onKeyUp(KeyUpEvent event) {
            size();
        }

        public void size() {
            shrink();
            grow();
        }

        private void grow() {
            while (getElement().getScrollHeight() > getElement().getClientHeight()) {
                setVisibleLines(getVisibleLines() + fGrowLines);
            }
        }

        private void shrink() {
            int rows = getVisibleLines();
            while (rows > fInitialLines) {
                setVisibleLines(--rows);
            }
        }

        @Override
        public void onKeyPress(KeyPressEvent event) {
            size();
        }

        @Override
        public void onBlur(BlurEvent event) {
            size();
        }
    }

    private int fInitialLines;
    private int fGrowLines;
    private SizeHandler fSizeHandler;

    /**
     * Creates new text area. Initial number of lines is set to 2, grow lines is set to 1 (see
     * {@link #AutoGrowTextArea(int, int)}.
     */
    public AutoGrowTextArea() {
        this(1, 1);
    }

    /**
     * Creates new text area.
     *
     * @param initialLines
     *            how high in terms of visible lines the initial text box is (the height will never go below this
     *            number)
     * @param growLines
     *            how many lines the text box grows when content reaches the current last line
     */
    public AutoGrowTextArea(int initialLines, int growLines) {
        super();
        registerHandlers();
        adjustStyle();
        setVisibleLines(initialLines);
        fInitialLines = initialLines;
        fGrowLines = growLines;
    }

    private void adjustStyle() {
        getElement().getStyle().setOverflow(Overflow.HIDDEN);
        getElement().getStyle().setProperty("resize", "none");
    }

    private void registerHandlers() {
        fSizeHandler = new SizeHandler();
        addKeyUpHandler(fSizeHandler);
        addKeyPressHandler(fSizeHandler);
        addBlurHandler(fSizeHandler);
        sinkEvents(Event.ONPASTE);
        registerOnCut(getElement());
    }

    private native void registerOnCut(Element element) /*-{
        var that = this.@com.spotmouth.gwt.client.common.AutoGrowTextArea::fSizeHandler;
        element.oncut = $entry(function() {
        that.@com.spotmouth.gwt.client.common.AutoGrowTextArea.SizeHandler::shrink()();
        return false;
        });
    }-*/;

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        switch (DOM.eventGetType(event)) {
            case Event.ONPASTE:
                Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                    @Override
                    public void execute() {
                        fSizeHandler.size();
                    }

                });
                break;
        }
    }
}