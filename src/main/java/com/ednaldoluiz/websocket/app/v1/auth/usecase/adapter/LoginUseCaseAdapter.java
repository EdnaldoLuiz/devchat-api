package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.LoginUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.validator.LoginValidator;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUseCaseAdapter implements LoginUseCasePort {
    
    private final List<LoginValidator> validators;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Logging in user: {} ........", request.email());
        validators.forEach(validator -> validator.validate(request));

        User user = userRepository.findByEmailAndDeleted(request.email()).orElseThrow();

        return new LoginResponse(null, null);
    }
}
