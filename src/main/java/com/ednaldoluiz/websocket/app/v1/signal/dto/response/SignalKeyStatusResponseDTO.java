package com.ednaldoluiz.websocket.app.v1.signal.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Situação atual do estoque de chaves do usuário.")
public record SignalKeyStatusResponseDTO(

        int remainingPreKeys,
        Instant spkValidUntil
        
) {}