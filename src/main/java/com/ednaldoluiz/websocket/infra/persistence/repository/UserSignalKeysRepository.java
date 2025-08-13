package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.SignalHandshakeBundleProjection;

public interface UserSignalKeysRepository {

    Optional<SignalHandshakeBundleProjection> findKeysByUserIdForHandshake(Long userId);

    void saveKeyBundle(Long userId, KeyBundleRequestDTO dto);

    Stream<Long> streamAllUserIds(int batchSize);

    Stream<Long> streamExpiredSignedPreKeyUserIds(int spkTtlDays, int batchSize);

    void deleteByUserId(Long userId);

    Optional<SignalPreKey> fetchAndConsumePreKey(Long userId);

    void updateSignedPreKey(Long userId, int id, byte[] key, byte[] sig);

    int countAvailablePreKeys(Long userId);

    Instant getSignedPreKeyCreatedAt(Long userId);
}
