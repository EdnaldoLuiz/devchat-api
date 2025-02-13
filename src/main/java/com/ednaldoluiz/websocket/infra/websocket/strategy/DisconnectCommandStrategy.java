package com.ednaldoluiz.websocket.infra.websocket.strategy;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class DisconnectCommandStrategy implements StompCommandStrategy {

    @Override
    public void handle(StompHeaderAccessor accessor) {
        System.out.println("Handling DISCONNECT command");
    }
}
