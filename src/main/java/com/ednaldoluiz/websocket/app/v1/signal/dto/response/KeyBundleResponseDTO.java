package com.ednaldoluiz.websocket.app.v1.signal.dto.response;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.domain.model.signal.UserSignalKeys;

public record KeyBundleResponseDTO(

        int registrationId,
        byte[] identityKey,
        int signedPreKeyId,
        byte[] signedPreKey,
        byte[] signedPreKeySig,
        List<PreKeyResponseDTO> oneTimePreKeys

) {
    public static KeyBundleResponseDTO from(UserSignalKeys entity, List<SignalPreKey> preKeys) {
        return new KeyBundleResponseDTO(
                entity.getRegistrationId(),
                entity.getIdentityKey(),
                entity.getSignedPreKeyId(),
                entity.getSignedPreKey(),
                entity.getSignedPreSig(),
                preKeys.stream().map(PreKeyResponseDTO::from).toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyBundleResponseDTO(
                int id, byte[] key, int preKeyId, byte[] preKey, byte[] preKeySig, List<PreKeyResponseDTO> timePreKeys
        ))) return false;
        return registrationId == id
                && signedPreKeyId == preKeyId
                && Arrays.equals(identityKey, key)
                && Arrays.equals(signedPreKey, preKey)
                && Arrays.equals(signedPreKeySig, preKeySig)
                && Objects.equals(oneTimePreKeys, timePreKeys);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(registrationId, signedPreKeyId, oneTimePreKeys);
        result = 31 * result + Arrays.hashCode(identityKey);
        result = 31 * result + Arrays.hashCode(signedPreKey);
        result = 31 * result + Arrays.hashCode(signedPreKeySig);
        return result;
    }

    @Override
    public String toString() {
        return "KeyBundleResponseDTO[" +
                "registrationId=" + registrationId +
                ", identityKey=" + Arrays.toString(identityKey) +
                ", signedPreKeyId=" + signedPreKeyId +
                ", signedPreKey=" + Arrays.toString(signedPreKey) +
                ", signedPreKeySig=" + Arrays.toString(signedPreKeySig) +
                ", oneTimePreKeys=" + oneTimePreKeys +
                ']';
    }
}
