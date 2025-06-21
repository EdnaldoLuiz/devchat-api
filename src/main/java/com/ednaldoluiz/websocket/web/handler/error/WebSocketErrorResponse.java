package com.ednaldoluiz.websocket.web.handler.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record WebSocketErrorResponse(
        String type,
        String message,
        String correlationId
) {}