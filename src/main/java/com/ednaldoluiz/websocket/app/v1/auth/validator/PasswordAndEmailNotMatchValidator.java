package com.ednaldoluiz.websocket.app.v1.auth.validator;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.web.handler.exception.LoginValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordAndEmailNotMatchValidator implements LoginValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void validate(LoginRequest request) {
        log.info("Validando credenciais de login para: {}", request.email());

        userRepository.findByEmail(request.email()).ifPresentOrElse(user -> {
            if (!passwordEncoder.matches(request.password(), new String(user.getPassword()))) {
                throw new LoginValidationException("Email ou senha inválidos.");
            }
        }, () -> {
            throw new LoginValidationException("Email ou senha inválidos.");
        });
    }
}