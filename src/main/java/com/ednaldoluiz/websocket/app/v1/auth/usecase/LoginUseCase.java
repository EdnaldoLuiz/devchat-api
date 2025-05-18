package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.validator.AuthValidator;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;
import com.ednaldoluiz.websocket.web.handler.exception.LoginValidationException;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUseCase {
    
    private final List<AuthValidator<LoginRequest>> validators;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @RateLimiter(name = "loginRateLimiter")
    public LoginResponse execute(LoginRequest request) {
        log.info("Logging in user: {} ........", request.email());
        validators.forEach(validator -> validator.validate(request));
        
        User user = userRepository
            .findByEmail(request.email())
            .orElseThrow(LoginValidationException::new);

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        log.info("User logged in: {} :)", user.getEmail());
        return LoginResponse.from(user.getId(), user.getEmail(), user.getName(), user.getAvatar(), token, refreshToken);
    }
}