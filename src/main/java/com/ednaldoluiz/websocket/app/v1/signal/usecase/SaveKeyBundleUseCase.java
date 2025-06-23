package com.ednaldoluiz.websocket.app.v1.signal.usecase;

import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveKeyBundleUseCase {

    private final UserSignalKeysRepository userSignalKeysRepository;

    public void execute(Long userId, KeyBundleRequestDTO dto) {
        log.info("Salvando key bundle para userId={}", userId);
        userSignalKeysRepository.saveKeyBundle(userId, dto);
        log.info("Key bundle salvo com sucesso para userId={}", userId);
    }
}
