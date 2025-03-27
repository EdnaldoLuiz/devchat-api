package com.ednaldoluiz.websocket.web.websocket.strategy;

import java.util.Optional;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.infra.security.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConnectCommandStrategy implements StompCommandStrategy {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void handle(StompHeaderAccessor accessor) {
        String token = extractToken(accessor);
        if (token == null) {
            log.warn("Cabeçalho de autorização não encontrado ou mal formatado no CONNECT");
            return;
        }
        processToken(token, accessor);
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization" do STOMP.
     *
     * @param accessor o accessor do cabeçalho STOMP
     * @return o token JWT sem o prefixo "Bearer " ou null se não existir ou estiver
     *         mal formatado
     */
    private String extractToken(StompHeaderAccessor accessor) {
        return Optional.ofNullable(accessor.getFirstNativeHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .orElse(null);
    }

    /**
     * Processa o token extraído, validando e autenticando o usuário se possível.
     *
     * @param token    o token JWT extraído
     * @param accessor o accessor do cabeçalho STOMP
     */
    private void processToken(String token, StompHeaderAccessor accessor) {
        try {
            String username = jwtService.extractUsername(token);
            if (username == null) {
                log.warn("Falha ao extrair o username do token");
                return;
            }
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.info("Usuário já autenticado, pulando reautenticação");
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (!jwtService.isTokenValid(token, userDetails)) {
                log.warn("Token inválido para o usuário: {}", username);
                return;
            }
            authenticateUser(userDetails, accessor);
        } catch (Exception e) {
            log.error("Erro na autenticação via STOMP CONNECT: {}", e.getMessage());
        }
    }

    /**
     * Cria um objeto de autenticação com os detalhes do usuário e o define no
     * accessor.
     *
     * @param userDetails os detalhes do usuário autenticado
     * @param accessor    o accessor do cabeçalho STOMP
     */
    private void authenticateUser(UserDetails userDetails, StompHeaderAccessor accessor) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(accessor.getSessionAttributes());
        accessor.setUser(authToken);
        log.info("Usuário autenticado via STOMP CONNECT: {}", userDetails.getUsername());
    }
}
