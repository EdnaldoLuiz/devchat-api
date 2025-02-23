package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.GeneratePasswordUseCasePort;
import com.ednaldoluiz.websocket.infra.security.policy.PasswordPolicy;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneratePasswordUseCaseAdapter implements GeneratePasswordUseCasePort {

    private final PasswordPolicy passwordPolicy;

    @Override
    @RateLimiter(name = "generatePasswordRateLimiter")
    public GeneratedPasswordResponse generateStrongPassword() {
        return passwordPolicy.generateSecurePassword();
    }
}