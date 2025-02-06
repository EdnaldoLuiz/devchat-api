package com.ednaldoluiz.websocket.infra.security.handler;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.infra.security.service.TokenService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
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
        if (authentication == null || authentication.getName() == null) {
            log.warn("Tentativa de logout sem usuário autenticado. Provavelmente o token já foi invalidado.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!handleLogout(request)) {
            log.warn("Falha ao processar o logout. Token inválido ou ausente.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        clearCookies(response);
        SecurityContextHolder.clearContext();
        response.setStatus(HttpServletResponse.SC_OK);
        log.info("Usuário {} deslogado com sucesso.", authentication.getName());
    }

    private boolean handleLogout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        String token = authHeader.substring(7);
        String jti = jwtService.extractClaim(token, Claims::getId);
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

        log.info("Invalidando token JWT: {}", jti);
        tokenService.invalidateToken(jti, expiration);
        return true;
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}