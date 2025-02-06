package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO de requisição para a autenticação de usuário.")
public record LoginRequest(

    @NotBlank(message = "{register.email.notBlank}")
    @Email(message = "{register.email.invalid}")
    @Schema(
        description = "Email do usuário. Deve ser válido.",
        example = "usuario@email.com"
    )
    String email,
    
    @NotBlank(message = "{register.password.notBlank}")
    @Size(min = 8, max = 20, message = "{register.password.size}")
    @Schema(
        description = "Senha do usuário. Deve conter pelo menos 8 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais.",
        example = "SenhaForte123!"
    )
    String password

) {}
