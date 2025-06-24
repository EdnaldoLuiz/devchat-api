package com.ednaldoluiz.websocket.app.v1.signal.facade;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.request.RotateSignedPreKeyRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.PreKeyBundleResponseDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.SignalKeyStatusResponseDTO;
import com.ednaldoluiz.websocket.app.v1.signal.usecase.FetchPreKeyBundleUseCase;
import com.ednaldoluiz.websocket.app.v1.signal.usecase.GetSignalKeyStatusUseCase;
import com.ednaldoluiz.websocket.app.v1.signal.usecase.RotateSignedPreKeyUseCase;
import com.ednaldoluiz.websocket.app.v1.signal.usecase.SaveKeyBundleUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignalFacade {

    private final SaveKeyBundleUseCase saveKeyBundleUseCase;
    private final FetchPreKeyBundleUseCase fetchPreKeyBundleUseCase;
    private final RotateSignedPreKeyUseCase rotateSignedPreKeyUseCase;
    private final GetSignalKeyStatusUseCase getSignalKeyStatusUseCase;
    
    public void saveKeyBundle(Long userId, KeyBundleRequestDTO dto) {
        log.info("Facade: salvando key bundle para userId={}", userId);
        saveKeyBundleUseCase.execute(userId, dto);
    }

    public PreKeyBundleResponseDTO fetchKeyBundle(Long requesterId, Long targetId) {
        log.info("Facade: buscando  key bundle para requesterId={} e targetId={}", requesterId, targetId);
        return fetchPreKeyBundleUseCase.execute(requesterId, targetId);
    }

    public void rotateSignedPreKey(Long userId, RotateSignedPreKeyRequestDTO dto) {
        log.info("Facade: rotacionando signed pre-key para userId={}", userId);
        rotateSignedPreKeyUseCase.execute(userId, dto);
    }

    public SignalKeyStatusResponseDTO getSignalKeyStatus(Long userId) {
        log.info("Facade: buscando status de chaves para userId={}", userId);
        return getSignalKeyStatusUseCase.execute(userId);
    }
}
