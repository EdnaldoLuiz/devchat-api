package com.ednaldoluiz.websocket.infra.web.route;

public interface Channels {

    String CHANNELS = "/channels";
    String CHANNELS_ID = CHANNELS + "/{id}";

    interface V1 {
        interface Message {
            String MESSAGE = CHANNELS_ID + "/message";
        }
    }
}