package com.ednaldoluiz.websocket.app.v1.auth.validator.chain;

import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.validator.AuthValidator;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.web.handler.exception.LoginValidationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(1)
@Component
@RequiredArgsConstructor
public class PasswordAndEmailNotMatchValidator implements AuthValidator<LoginRequest> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void validate(LoginRequest request) {
        log.info("Validando credenciais de login para: {}", request.email());

        userRepository.findByEmail(request.email()).ifPresentOrElse(user -> {
            String password = new String(user.getPassword()).trim();

            if (password.isEmpty()) {
                throw new LoginValidationException(
                    String.format("A conta associada a %s foi criada via %s. Faça login usando esse provedor ou redefina sua senha.",
                        request.email(), user.getAuthProvider().name())
                );
            }

            if (!passwordEncoder.matches(request.password(), password)) {
                throw new LoginValidationException("Email ou senha inválidos.");
            }
        }, () -> {
            throw new LoginValidationException("Email ou senha inválidos.");
        });
    }
}