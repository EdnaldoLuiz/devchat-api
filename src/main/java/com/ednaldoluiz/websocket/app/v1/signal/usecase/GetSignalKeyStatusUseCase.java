package com.ednaldoluiz.websocket.app.v1.signal.usecase;

import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.signal.dto.response.SignalKeyStatusResponseDTO;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;
import com.ednaldoluiz.websocket.shared.config.SignalKeyProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetSignalKeyStatusUseCase {

    private final UserSignalKeysRepository repo;
    private final SignalKeyProperties cfg;

    @Transactional(readOnly = true)
    public SignalKeyStatusResponseDTO execute(Long userId) {
        int remaining = repo.countAvailablePreKeys(userId);
        var created   = repo.getSignedPreKeyCreatedAt(userId);
        return new SignalKeyStatusResponseDTO(
                remaining,
                created.plus(cfg.signedPreKeyTtlDays(), ChronoUnit.DAYS)
        );
    }
}
