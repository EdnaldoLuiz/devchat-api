package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record OAuth2LoginResponse(
        String message,
        String token,
        Map<String, Object> userAttributes
    ) {}