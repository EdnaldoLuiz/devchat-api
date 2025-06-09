package com.ednaldoluiz.websocket.infra.schedule;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.infra.persistence.repository.PasswordResetTokenRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenExpiredCleanupJob {

    private final PasswordResetTokenRepository tokenRepository;
    private static final int ONE_HOUR = 3600000;

    @Scheduled(fixedDelay = ONE_HOUR)
    public void cleanExpiredTokens() {
        log.info("Iniciando limpeza de tokens expirados...");
        int deletedCount = tokenRepository.deleteExpiredUnusedTokens(LocalDateTime.now());
        log.info("Tokens expirados removidos: {}", deletedCount);
    }
}
