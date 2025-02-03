package com.ednaldoluiz.websocket.app.v1.auth.validator;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.infra.security.policy.PasswordPolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordValidator implements RegisterValidator {

    private final PasswordPolicy passwordPolicy;

    @Override
    public void validate(RegisterRequest request) {
        log.info("Validando senha do usuário: {}", request.email());
        passwordPolicy.validatePassword(request.password());
    }
}
