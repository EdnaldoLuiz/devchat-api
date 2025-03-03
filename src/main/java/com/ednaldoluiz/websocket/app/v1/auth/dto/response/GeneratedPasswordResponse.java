package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GeneratedPasswordResponse(

    @Schema(description = "Senha forte gerada automaticamente.", example = "Abc@12345")
    String password,

    @Schema(description = "Pontuação de força da senha (de 1 a 100).", example = "85")
    int strengthScore

) {
    public static GeneratedPasswordResponse from(String password, int strengthScore) {
        return new GeneratedPasswordResponse(
            password,
            strengthScore
        );
    }
}
