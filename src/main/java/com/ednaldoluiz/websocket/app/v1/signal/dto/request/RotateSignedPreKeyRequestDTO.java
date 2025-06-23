package com.ednaldoluiz.websocket.app.v1.signal.dto.request;

import com.ednaldoluiz.websocket.app.v1.signal.validator.ByteArraySize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitação de rotação de signedPreKey.")
public record RotateSignedPreKeyRequestDTO(
    
        @Min(1)
        int signedPreKeyId,

        @NotNull 
        @ByteArraySize(32)
        byte[] signedPreKey,

        @NotNull 
        @ByteArraySize(64)
        byte[] signedPreKeySig
) {}