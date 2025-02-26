package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request;

import com.ednaldoluiz.websocket.infra.annotation.phone.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO de requisição para registro de usuário.")
public record RegisterRequest(

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
    String password,

    @NotBlank(message = "{register.password.confirmation.notBlank}")
    @Size(min = 8, max = 20, message = "{register.password.size}")
    @Schema(
        description = "Confirmação da senha do usuário. Deve ser igual à senha.",
        example = "SenhaForte123!"
    )
    String confirmPassword,

    @NotBlank(message = "{register.name.notBlank}")
    @Size(min = 3, max = 50, message = "{register.name.size}")
    @Schema(
        description = "Nome completo do usuário.",
        example = "João Silva"
    )
    String name,

    @NotBlank(message = "{register.phone.notBlank}")
    @Phone(message = "{register.phone.invalid}")
    @Schema(
        description = "Número de telefone do usuário no formato internacional. Deve conter DDD e ter entre 10 e 11 dígitos.",
        example = "11912345678"
    )
    String phone,

    @AssertTrue(message = "{register.terms.isTrue}")
    @Schema(
        description = "Confirmação da aceitação dos termos de uso.",
        example = "true"
    )
    Boolean terms
) {}
