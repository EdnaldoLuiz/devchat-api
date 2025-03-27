package com.ednaldoluiz.websocket.infra.security.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler responsável por capturar autenticações bem-sucedidas.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String correlationId = UUID.randomUUID().toString();
        log.info("Autenticação bem-sucedida para usuário: {} - ID: {}", authentication.getName(), correlationId);

        ErrorResponse successResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Success",
            "Usuário autenticado com sucesso.",
            request.getServletPath(),
            null,
            "Login realizado com sucesso.",
            null
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getOutputStream(), successResponse);
    }
}