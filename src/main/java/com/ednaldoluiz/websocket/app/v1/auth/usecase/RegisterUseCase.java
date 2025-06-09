package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.validator.AuthValidator;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final List<AuthValidator<RegisterRequest>> validators;

    public RegisterResponse execute(RegisterRequest request) {
        log.info("Registering user: {} ........", request.email());
        validators.forEach(validator -> validator.validate(request));

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(request.email(), hashedPassword, request.name());

        userRepository.save(user);
        user.clearPassword();
        
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        log.info("User registered: {} :)", user.getEmail());
        return RegisterResponse.from(user.getId(), user.getEmail(), token, refreshToken);
    }
}