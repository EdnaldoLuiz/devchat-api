package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request;

import com.ednaldoluiz.websocket.infra.annotation.phone.Phone;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "{register.email.notBlank}")
    @Email(message = "{register.email.invalid}")
    String email,

    @NotBlank(message = "{register.password.notBlank}")
    @Size(min = 8, max = 20, message = "{register.password.size}")
    String password,

    @NotBlank(message = "{register.name.notBlank}")
    @Size(min = 3, max = 50, message = "{register.name.size}")
    String name,

    @NotBlank(message = "{register.phone.notBlank}")
    @Phone(message = "{register.phone.invalid}")
    String phone,

    String avatar,

    @AssertTrue(message = "{register.terms.isTrue}")
    Boolean terms
) {}
