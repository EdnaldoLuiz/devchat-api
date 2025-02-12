package com.ednaldoluiz.websocket.app.v1.auth.validator;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.web.handler.exception.RegisterValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class EmailExistsValidator implements RegisterValidator {

    private final UserRepository userRepository;

    @Override
    public void validate(RegisterRequest request) {
        log.info("Validando o email do request: " + request.email());
        if (userRepository.existsByEmail(request.email())) {
            throw new RegisterValidationException("Já existe um usuário com este email.");
        }
    }
}