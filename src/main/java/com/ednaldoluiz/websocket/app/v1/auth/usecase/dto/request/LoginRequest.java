package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @Email
    String email,
    
    @NotBlank
    String password

) {}
