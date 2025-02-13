package com.ednaldoluiz.websocket.infra.websocket.strategy;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface StompCommandStrategy {

    void handle(StompHeaderAccessor accessor);

}
