package com.ednaldoluiz.websocket.app.v1.auth.validator.chain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.validator.AuthValidator;
import com.ednaldoluiz.websocket.infra.security.policy.PasswordPolicy;
import com.ednaldoluiz.websocket.web.handler.exception.MismatchedPasswordsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class PasswordValidator implements AuthValidator<RegisterRequest> {

    private final PasswordPolicy passwordPolicy;

    @Override
    public void validate(RegisterRequest request) {
        log.info("Validando senha do usuário: {}", request.email());
        boolean isPasswordValid = request.password().equals(request.confirmPassword());
        if (!isPasswordValid) {
            throw new MismatchedPasswordsException();
        }
        passwordPolicy.validatePassword(request.password());
    }
}
