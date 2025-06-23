package com.ednaldoluiz.websocket.app.v1.signal.usecase;

import com.ednaldoluiz.websocket.app.v1.signal.dto.response.PreKeyBundleResponseDTO;
import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.SignalHandshakeBundleProjection;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;
import com.ednaldoluiz.websocket.shared.config.SignalKeyProperties;
import com.ednaldoluiz.websocket.web.handler.exception.KeyBundleNotFoundException;
import com.ednaldoluiz.websocket.web.handler.exception.OutOfPreKeysException;
import com.ednaldoluiz.websocket.web.websocket.event.SignalEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchPreKeyBundleUseCase {

    private final UserSignalKeysRepository userSignalKeysRepository;
    private final SignalEventPublisher signalEventPublisher;
    private final SignalKeyProperties signalProperties;

    @Transactional
    public PreKeyBundleResponseDTO execute(Long requesterId, Long targetId) {
        SignalHandshakeBundleProjection keys = userSignalKeysRepository.findKeysByUserIdForHandshake(targetId)
                .orElseThrow(KeyBundleNotFoundException::new);

        SignalPreKey preKey = userSignalKeysRepository.fetchAndConsumePreKey(targetId)
                .orElseThrow(OutOfPreKeysException::new);

        int remaining = userSignalKeysRepository.countAvailablePreKeys(targetId);
        log.info("Pre-key bundle encontrado para userId={}, targetId={}, remaining={}", requesterId, targetId, remaining);
        if (remaining <= signalProperties.lowPreKeyThreshold()) {
            signalEventPublisher.notifyLowPreKey(targetId, remaining);
        }
        log.info("Publicando evento de pre-key bundle para userId={} e targetId={}", requesterId, targetId);

        return PreKeyBundleResponseDTO.of(keys, preKey);
    }
}
