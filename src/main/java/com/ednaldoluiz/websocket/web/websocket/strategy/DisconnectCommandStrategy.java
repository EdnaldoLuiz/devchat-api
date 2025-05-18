package com.ednaldoluiz.websocket.web.websocket.strategy;

import com.ednaldoluiz.websocket.web.websocket.store.OnlineUserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisconnectCommandStrategy implements StompCommandStrategy {

    private final OnlineUserStore onlineUserStore;

    @Override
    public void handle(StompHeaderAccessor accessor) {
        String userName = Optional.ofNullable(accessor.getUser())
                .map(Principal::getName)
                .orElseThrow(() -> new IllegalArgumentException("Username is required"));

        String sessionId = accessor.getSessionId();
        onlineUserStore.remove(userName, sessionId);
        log.info("Usuário {} desconectou da sessão {}", userName, sessionId);
    }
}
