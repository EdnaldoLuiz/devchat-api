package com.ednaldoluiz.websocket.app.v1.signal.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.RotateSignedPreKeyRequestDTO;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RotateSignedPreKeyUseCase {
    
    private final UserSignalKeysRepository repo;

    @Transactional
    public void execute(Long userId, RotateSignedPreKeyRequestDTO dto) {
        repo.updateSignedPreKey(userId,
                dto.signedPreKeyId(),
                dto.signedPreKey(),
                dto.signedPreKeySig());
    }
}
