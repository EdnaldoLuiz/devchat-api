package com.ednaldoluiz.websocket.web.websocket.strategy;

import java.util.Optional;

import com.ednaldoluiz.websocket.web.websocket.store.OnlineUserStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectCommandStrategy implements StompCommandStrategy {

    private final OnlineUserStore onlineUserStore;

    @Override
    public void handle(StompHeaderAccessor accessor) {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) accessor.getUser();

        if (auth != null && auth.getPrincipal() instanceof UserDetails user) {
            String userId = user.getUsername();
            String sessionId = accessor.getSessionId();
            onlineUserStore.add(userId, sessionId);
            log.info("Usuário {} conectado na sessão {}", userId, sessionId);
        } else {
            log.warn("Falha ao recuperar UserDetails após CONNECT");
        }
    }
}
