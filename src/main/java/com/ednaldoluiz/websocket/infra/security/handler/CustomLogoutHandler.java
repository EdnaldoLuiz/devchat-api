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

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenService tokenService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            String jti = jwtService.extractClaim(token, Claims::getId);
            Date expiration = jwtService.extractClaim(token, Claims::getExpiration);

            tokenService.invalidateToken(jti, expiration);
        }

        clearCookies(request, response);
        SecurityContextHolder.clearContext();
    }

    private void clearCookies(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        response.addCookie(cookie);
    }
}
