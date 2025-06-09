package com.ednaldoluiz.websocket.app.v1.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para iniciar um chat privado com outro usuário.")
public record StartChatRequest(
    
        @NotNull
        @Schema(description = "ID do participante com quem quero conversar", example = "42")
        Long participantId
) {}