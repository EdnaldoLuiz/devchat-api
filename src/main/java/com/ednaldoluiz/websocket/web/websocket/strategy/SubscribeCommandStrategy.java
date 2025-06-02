package com.ednaldoluiz.websocket.web.websocket.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

// src/.../websocket/strategy/SubscribeCommandStrategy.java
@Slf4j
@Component
public class SubscribeCommandStrategy implements StompCommandStrategy {

    private static final Set<String> ALLOWED_QUEUES = Set.of(
            "/user/queue/messages",
            "/user/queue/typing",
            "/user/queue/read",
            "/user/queue/files",
            "/user/queue/notify",
            "/user/queue/presence"
    );

    @Override
    public void handle(StompHeaderAccessor accessor) {

        /* ----------------------------------------------------------------
         * 1)  Pode acontecer de o Spring ainda não ter ligado o principal
         *     à sessão quando o primeiro SUBSCRIBE chega.
         *     Se não houver usuário, apenas faz log e deixa passar.
         * ---------------------------------------------------------------- */
        if (accessor.getUser() == null) {
            log.debug("SUBSCRIBE sem principal ainda. Destino={}", accessor.getDestination());
            return;
        }

        String principal = accessor.getUser().getName();
        String dest      = accessor.getDestination();
        log.info("Subscribed {} -> {}", principal, dest);

        /* ----------------------------------------------------------------
         * 2)  Validação do destino: só /user/queue/* é permitido.
         * ---------------------------------------------------------------- */
        if (dest != null && dest.startsWith("/user/") && !dest.startsWith("/user/queue/")) {
            throw new IllegalArgumentException("Somente /user/queue/* é permitido");
        }

        /* ----------------------------------------------------------------
         * 3)  (Opcional)  Whitelist específica – descomente se quiser bloquear
         *     quaisquer filas não listadas.
         *
         * if (!ALLOWED_QUEUES.contains(dest)) {
         *     throw new IllegalArgumentException("Queue não autorizada: " + dest);
         * }
         * ---------------------------------------------------------------- */
    }
}
