package com.ednaldoluiz.websocket.web.controller.v1.signal;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.request.RotateSignedPreKeyRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.PreKeyBundleResponseDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.SignalKeyStatusResponseDTO;
import com.ednaldoluiz.websocket.app.v1.signal.facade.SignalFacade;
import com.ednaldoluiz.websocket.web.controller.common.GenericApiResponse;

import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SignalKeyController implements SignalKeyApi {

    private final SignalFacade signalFacade;

    @Override
    public ResponseEntity<GenericApiResponse> uploadKeyBundle(KeyBundleRequestDTO dto, AuthUser user) {
        signalFacade.saveKeyBundle(user.id(), dto);
        return ResponseEntity.ok(
            new GenericApiResponse("Key bundle salvo com sucesso")
        );
    }

    @Override
    public ResponseEntity<PreKeyBundleResponseDTO> fetchPreKeyBundle(Long targetId, AuthUser user) {
        return ResponseEntity.ok(signalFacade.fetchKeyBundle(user.id(), targetId));
    }

    @Override
    public ResponseEntity<GenericApiResponse> rotateSignedPreKey(RotateSignedPreKeyRequestDTO dto, AuthUser user) {
        signalFacade.rotateSignedPreKey(user.id(), dto);
        return ResponseEntity.ok(new GenericApiResponse("SignedPreKey rotacionada"));
    }

    @Override
    public ResponseEntity<SignalKeyStatusResponseDTO> getKeyStatus(AuthUser user) {
        return ResponseEntity.ok(signalFacade.getSignalKeyStatus(user.id()));
    }
}
