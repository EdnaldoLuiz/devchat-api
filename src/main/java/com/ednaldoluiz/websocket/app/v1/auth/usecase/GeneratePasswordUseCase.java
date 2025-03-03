package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import com.ednaldoluiz.websocket.app.v1.auth.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.infra.security.policy.PasswordPolicy;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeneratePasswordUseCase {

    private final PasswordPolicy passwordPolicy;

    @RateLimiter(name = "generatePasswordRateLimiter")
    public GeneratedPasswordResponse execute() {
        return passwordPolicy.generateSecurePassword();
    }
}