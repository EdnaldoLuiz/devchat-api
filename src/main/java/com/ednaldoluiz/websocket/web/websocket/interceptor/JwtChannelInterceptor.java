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

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService uds;
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;

        log.info("Interceptando STOMP: {}", accessor.getCommand());
        log.info("Headers: {}", accessor.toNativeHeaderMap());

        // Tenta autenticar via header (funciona para websocket puro)
        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token == null) {
                token = accessor.getFirstNativeHeader("access_token");
                if (token != null && !token.startsWith(BEARER_PREFIX)) token = BEARER_PREFIX + token;
            }

            if (token != null && token.startsWith(BEARER_PREFIX)) {
                token = token.substring(BEARER_PREFIX.length());
                String username = jwtService.extractUsername(token);
                log.info("Token extraído: {}", token);
                log.info("Usuário do token: {}", username);

                if (username != null) {
                    AuthUser user = (AuthUser) uds.loadUserByUsername(username);
                    if (jwtService.isTokenValid(token, user)) {
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        log.info("Auth: {}", auth);
                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth); // Define no contexto

                        log.info("Authentication setado no accessor com sucesso: {}", user.getUsername());
                    } else {
                        log.warn("Token inválido para user {}", username);
                    }
                } else {
                    log.warn("Username extraído foi null");
                }
            }
        }

        // Se não conseguiu pelo header, tenta pegar dos session attributes (gambiarra obrigatória pro SockJS)
        if (accessor.getUser() == null && accessor.getSessionAttributes() != null) {
            Object obj = accessor.getSessionAttributes().get("user");
            if (obj instanceof Authentication) {
                accessor.setUser((Authentication) obj);
                log.info("Authentication recuperado dos session attributes: {}", ((Authentication) obj).getName());
            } else {
                log.warn("Session attribute 'user' não encontrado ou não é Authentication. Valor: {}", obj);
            }
        }

        // Última checagem pra garantir
        if (accessor.getUser() == null) {
            log.warn("Nenhum principal foi associado à mensagem STOMP. Assegure-se de que o handshake salvou 'user' na sessão!");
        }

        return message;
    }

}
