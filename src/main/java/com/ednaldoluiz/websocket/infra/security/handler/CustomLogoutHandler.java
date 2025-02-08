package com.ednaldoluiz.websocket.infra.security.handler;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.infra.security.service.TokenService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Iniciando processo de logout.");

        String authHeader = request.getHeader("Authorization");
        log.info("Valor do header Authorization: {}", authHeader);

        if (!handleLogout(request)) {
            log.warn("Falha ao processar o logout. Token inválido ou ausente.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        log.info("Logout realizado com sucesso.");
    }

    private boolean handleLogout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Header Authorization está ausente ou não começa com 'Bearer '.");
            return false;
        }

        String token = authHeader.substring(7);
        log.info("Token extraído: {}", token);
        String jti = jwtService.extractClaim(token, Claims::getId);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        log.info("Invalidando token JWT: {}", jti);
        tokenService.invalidateToken(jti, expiration);
        log.info("Token invalidado com sucesso.");
        return true;
    }
}