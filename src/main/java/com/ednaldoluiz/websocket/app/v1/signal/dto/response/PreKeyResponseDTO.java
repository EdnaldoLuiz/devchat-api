package com.ednaldoluiz.websocket.app.v1.signal.dto.response;

import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import java.util.Arrays;
import java.util.Objects;

public record PreKeyResponseDTO(

        int keyId,
        byte[] publicKey

) {
    public static PreKeyResponseDTO from(SignalPreKey entity) {
        return new PreKeyResponseDTO(
                entity.getKeyId(),
                entity.getPreKey()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PreKeyResponseDTO(int id, byte[] key))) return false;
        return keyId == id &&
                Arrays.equals(publicKey, key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyId, Arrays.hashCode(publicKey));
    }

    @Override
    public String toString() {
        return "PreKeyResponseDTO[keyId=%d, publicKey=%s]"
                .formatted(keyId, Arrays.toString(publicKey));
    }
}
