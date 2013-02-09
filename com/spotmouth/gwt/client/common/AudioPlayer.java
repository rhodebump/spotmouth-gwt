package com.spotmouth.gwt.client.common;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.phonegap.gwt.media.client.Media;
import com.phonegap.gwt.media.client.Media.MediaError;

public class AudioPlayer extends HorizontalPanel {
    private String url = null;
    Media media = null;

    public AudioPlayer(String url) {
        this.url = url;
        class MediaCallback implements Media.Callback {
            public void onSuccess() {
            }

            public void onError(MediaError error) {
            }
        }
        media = Media.newInstance(url, new MediaCallback());
        class PlayerHander implements ClickHandler {
            public void onClick(ClickEvent event) {
                media.play();
            }
        }
        // navigator.device.capture.captureAudio(captureSuccess, captureError,
        // {limit:2});
        Button play = new Button("Play", new PlayerHander());
        add(play);
        class PauseHander implements ClickHandler {
            public void onClick(ClickEvent event) {
                media.pause();
            }
        }
        Button pause = new Button("Pause", new PauseHander());
        add(pause);
        class StopHander implements ClickHandler {
            public void onClick(ClickEvent event) {
                media.stop();
            }
        }
        Button stop = new Button("Stop", new StopHander());
        add(stop);
        // play,pause,stop
        // release?
    }
}
