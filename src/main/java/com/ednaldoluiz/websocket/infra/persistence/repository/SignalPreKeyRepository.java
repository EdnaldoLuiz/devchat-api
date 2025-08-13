package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKeyId;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;

@Repository
public interface SignalPreKeyRepository extends BaseJpaRepository<SignalPreKey, SignalPreKeyId> {

    Optional<SignalPreKey> findFirstByUserIdAndConsumedFalse(Long userId);

    int countByUserIdAndConsumedFalse(Long userId);

}