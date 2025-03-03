package com.ednaldoluiz.websocket.infra.web.websocket.strategy;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class UnsubscribeCommandStrategy implements StompCommandStrategy {

    @Override
    public void handle(StompHeaderAccessor accessor) {
        System.out.println("Handling UNSUBSCRIBE command");
    }
}
