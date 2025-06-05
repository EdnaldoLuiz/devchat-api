package com.ednaldoluiz.websocket.web.websocket.interceptor;

import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtService jwtService;
    private final CustomUserDetailsService uds;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attrs) {
        return extractUserFromRequest(request)
                .filter(user -> jwtService.isTokenValid(getRawToken(request), user))
                .map(user -> createAuthentication(user, attrs))
                .isPresent();
    }

    private Optional<UserDetails> extractUserFromRequest(ServerHttpRequest request) {
        return extractUsername(getRawToken(request))
                .map(uds::loadUserByUsername);
    }

    private String getRawToken(ServerHttpRequest request) {
        return extractTokenHeader(request)
                .or(() -> extractTokenQueryParam(request))
                .map(this::stripBearerPrefix)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Token inválido"));
    }

    private Optional<String> extractTokenHeader(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().getFirst("Authorization"))
                .filter(auth -> !auth.isBlank());
    }

    private Optional<String> extractTokenQueryParam(ServerHttpRequest request) {
        return Optional.ofNullable(
                UriComponentsBuilder.fromUri(request.getURI())
                        .build()
                        .getQueryParams()
                        .getFirst("access_token"));
    }

    private String stripBearerPrefix(String token) {
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    private Optional<String> extractUsername(String token) {
        try {
            return Optional.ofNullable(jwtService.extractUsername(token));
        } catch (Exception e) {
            log.warn("Falha ao extrair username do token", e);
            return Optional.empty();
        }
    }

    private Optional<UserDetails> createAuthentication(UserDetails userDetails, Map<String, Object> attrs) {
        // 1) cast para AuthUser para termos acesso ao método id()
        AuthUser authUser = (AuthUser) userDetails;

        // 2) usamos o próprio ID como principal (string)
        String userId = authUser.id().toString();

        // 3) mantemos as authorities vindas do AuthUser
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, // principal = id do usuário
                null, // credentials (não precisamos aqui)
                authUser.getAuthorities());

        // 4) armazenamos o AuthUser completo em detalhes, caso precisemos dele mais
        // tarde
        auth.setDetails(authUser);

        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("Autenticação definida no handshake: userId={}", userId);
        return Optional.of(userDetails);
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception ex) {
        log.info("Handshake completed");
    }
}
