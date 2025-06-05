package com.ednaldoluiz.websocket.web.websocket.strategy;

import com.ednaldoluiz.websocket.web.websocket.store.OnlineUserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectCommandStrategy implements StompCommandStrategy {

    private final OnlineUserStore onlineUserStore;

    @Override
    public void handle(StompHeaderAccessor accessor) {
        var auth = (UsernamePasswordAuthenticationToken) accessor.getUser();
        if (auth == null)
            return;

        String userId = auth.getName(); // agora já é o id
        String sessionId = accessor.getSessionId();
        onlineUserStore.add(userId, sessionId);
        log.info("Usuário {} conectado na sessão {}", userId, sessionId);
    }
}
