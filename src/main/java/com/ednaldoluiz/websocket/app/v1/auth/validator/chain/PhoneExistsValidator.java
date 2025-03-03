package com.ednaldoluiz.websocket.app.v1.auth.validator.chain;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.validator.AuthValidator;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.web.handler.exception.RegisterValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order(3)
@Component
@RequiredArgsConstructor
public class PhoneExistsValidator implements AuthValidator<RegisterRequest> {

    private final UserRepository userRepository;

    @Override
    public void validate(RegisterRequest request) {
        log.info("Validando o telefone do request: {}", request.phone());
        if (userRepository.existsByPhone(request.phone())) {
            throw new RegisterValidationException("Já existe um usuário com este telefone.");
        }
    }
}