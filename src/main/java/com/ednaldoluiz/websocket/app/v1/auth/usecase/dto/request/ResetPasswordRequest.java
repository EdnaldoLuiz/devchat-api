package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO de requisição para reset de senha.")
public record ResetPasswordRequest(

    @NotBlank(message = "{reset-password.token.notBlank}")
    @Schema(
        description = "Token de redefinição de senha.",
        example = "c7b3b3b3-4b3b-4b3b-4b3b-4b3b3b3b3b3b"
    )
    String token,

    @NotBlank(message = "{reset-password.notBlank}")
    @Size(min = 8, max = 20, message = "{reset-password.size}")
    @Schema(
        description = "Senha do usuário. Deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.",
        example = "SenhaForte123!"
    )
    String password,

    @NotBlank(message = "{reset-password.confirmation.notBlank}")
    @Size(min = 8, max = 20, message = "{reset-password.size}")
    @Schema(
        description = "Confirmação da senha do usuário. Deve ser igual à senha.",
        example = "SenhaForte123!"
    )
    String confirmPassword
) {}