package com.ednaldoluiz.websocket.web.websocket.interceptor;

import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String SESSION_USER_KEY = "user";

    private final JwtService jwtService;
    private final CustomUserDetailsService uds;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = getAccessor(message);
        if (accessor == null) {
            log.debug("Sem accessor STOMP — pulando autenticação");
            return message;
        }
        log.debug("Interceptando STOMP command={}", accessor.getCommand());

        if (isConnectOrSend(accessor)) {
            tryAuthenticateViaToken(accessor);
        }

        if (accessor.getUser() == null) {
            tryAuthenticateViaSession(accessor);
        }

        if (accessor.getUser() == null) {
            log.warn("Nenhum principal associado à mensagem STOMP (command={})", accessor.getCommand());
        } else {
            log.debug("Principal final associado: {}", accessor.getUser().getName());
        }

        return message;
    }

    private StompHeaderAccessor getAccessor(Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }

    private boolean isConnectOrSend(StompHeaderAccessor accessor) {
        StompCommand cmd = accessor.getCommand();
        return StompCommand.CONNECT.equals(cmd) || StompCommand.SEND.equals(cmd);
    }

    private void tryAuthenticateViaToken(StompHeaderAccessor accessor) {
        extractBearerToken(accessor)
                .flatMap(this::extractUsername)
                .flatMap(this::loadUserDetails)
                .filter(user -> validateToken(accessor, user))
                .ifPresent(user -> setAuthentication(accessor, user));
    }

    private Optional<String> extractBearerToken(StompHeaderAccessor accessor) {
        return Optional.ofNullable(accessor.getFirstNativeHeader("Authorization"))
                .or(() -> Optional.ofNullable(accessor.getFirstNativeHeader("access_token"))
                        .map(token -> BEARER_PREFIX + token))
                .filter(token -> token.startsWith(BEARER_PREFIX))
                .map(token -> token.substring(BEARER_PREFIX.length()));
    }

    private Optional<String> extractUsername(String rawToken) {
        String username = jwtService.extractUsername(rawToken);
        log.info("Username do token: {}", username);
        return Optional.ofNullable(username);
    }

    private Optional<AuthUser> loadUserDetails(String username) {
        try {
            AuthUser user = (AuthUser) uds.loadUserByUsername(username);
            log.debug("UserDetails carregado: id={}, email={}", user.id(), user.email());
            return Optional.of(user);
        } catch (Exception e) {
            log.warn("Falha ao carregar UserDetails para '{}': {}", username, e.getMessage());
            return Optional.empty();
        }
    }

    private boolean validateToken(StompHeaderAccessor accessor, AuthUser user) {
        String rawToken = Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")).substring(BEARER_PREFIX.length());
        boolean valid = jwtService.isTokenValid(rawToken, user);
        if (!valid) {
            log.warn("Token inválido para user {}", user.email());
        }
        return valid;
    }

    private void setAuthentication(StompHeaderAccessor accessor, AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        accessor.setUser(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("Autenticação via token aplicada — user={}", user.email());
    }

    private void tryAuthenticateViaSession(StompHeaderAccessor accessor) {
        Optional.ofNullable(accessor.getSessionAttributes())
                .map(session -> session.get(SESSION_USER_KEY))
                .filter(Authentication.class::isInstance)
                .map(Authentication.class::cast)
                .ifPresent(auth -> {
                    accessor.setUser(auth);
                    log.info("Autenticação recuperada da sessão — user={}", auth.getName());
                });
    }
}
