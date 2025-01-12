package com.ednaldoluiz.websocket.app.v1.usecase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(
    
    @NotBlank
    String message,
    
    Long userId,
    Long chatId

) {}