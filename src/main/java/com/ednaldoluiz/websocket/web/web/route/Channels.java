package com.ednaldoluiz.websocket.web.web.route;

public interface Channels {

    String CHANNELS = "/channels";
    String CHANNELS_ID = CHANNELS + "/{id}";

    interface V1 {
        interface Message {
            String MESSAGE = CHANNELS_ID + "/message";
        }
    }
}