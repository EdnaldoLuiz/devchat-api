package com.ednaldoluiz.websocket.web.web.websocket.strategy;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandStrategy {

    void handle(StompHeaderAccessor accessor);

}
