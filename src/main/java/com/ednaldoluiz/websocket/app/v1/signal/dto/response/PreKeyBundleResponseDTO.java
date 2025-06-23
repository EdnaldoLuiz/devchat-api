package com.ednaldoluiz.websocket.app.v1.signal.dto.response;

import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.SignalHandshakeBundleProjection;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Bundle mínimo para handshake: 1 pre-key já consumida.")
public record PreKeyBundleResponseDTO(

        int registrationId,
        byte[] identityKey,
        int signedPreKeyId,
        byte[] signedPreKey,
        byte[] signedPreKeySig,
        int oneTimePreKeyId,
        byte[] oneTimePreKey

) {
    public static PreKeyBundleResponseDTO of(
            SignalHandshakeBundleProjection keys,
            SignalPreKey preKey
    ) {
        return new PreKeyBundleResponseDTO(
                keys.registrationId(),
                keys.identityKey(),
                keys.signedPreKeyId(),
                keys.signedPreKey(),
                keys.signedPreKeySig(),
                preKey.getKeyId(),
                preKey.getPreKey()
        );
    }
}