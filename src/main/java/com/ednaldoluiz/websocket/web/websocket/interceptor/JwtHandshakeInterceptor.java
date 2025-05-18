package com.ednaldoluiz.websocket.web.websocket.interceptor;

import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService uds;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attrs
    ) {

        String auth = request.getHeaders().getFirst("Authorization");
        log.info("JWT auth: {}", auth);
        if (auth == null) {
            String raw = UriComponentsBuilder.fromUri(request.getURI())
                    .build()
                    .getQueryParams()
                    .getFirst("access_token");
            log.info("JWT raw: {}", raw);
            if (raw != null) auth = "Bearer " + raw;
        }

        if (auth == null || !auth.startsWith("Bearer ")) return false;

        String token = auth.substring(7);
        String username = jwtService.extractUsername(token);
        log.info("Username: {}", username);
        if (username == null) return false;

        UserDetails user = uds.loadUserByUsername(username);
        log.info("User: {}", user);
        if (!jwtService.isTokenValid(token, user)) return false;

        // salva no contexto + attrs (usado depois no Principal)
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        attrs.put("user", authentication);               // importante para convertAndSendToUser
        log.info("Autenticação definida no handshake: {}", user.getUsername());
        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception ex)
    {
        log.info("Handshake completed");
    }
}
