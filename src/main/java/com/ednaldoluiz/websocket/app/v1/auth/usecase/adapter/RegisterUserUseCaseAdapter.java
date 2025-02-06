package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.validator.RegisterValidator;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUserUseCaseAdapter implements RegisterUserUseCasePort {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final SnowflakeIdGenerator idGenerator;
    private final List<RegisterValidator> validators;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Registering user: {} ........", request.email());
        validators.forEach(validator -> validator.validate(request));

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(idGenerator, request.email(), hashedPassword, request.name(), request.phone());

        userRepository.save(user);
        user.clearPassword();
        
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("User registered: {} :)", user.getEmail());
        return RegisterResponse.from(user.getId(), user.getEmail(), token, refreshToken);
    }
}