package com.ednaldoluiz.websocket.infra.security.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OAuth2AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
                log.info("Path: {}", request.getRequestURI());

        if (request.getRequestURI().equals("/api/v1/auth/oauth2/success")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            log.info("Auth: {}", auth);
            if (!(auth instanceof OAuth2AuthenticationToken)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falha no login via GitHub");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}