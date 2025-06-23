package com.ednaldoluiz.websocket.app.v1.signal.dto.request;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.ednaldoluiz.websocket.app.v1.signal.validator.ByteArraySize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO de requisição para o pacote de chaves do Signal.")
public record KeyBundleRequestDTO(

        @Min(1)
        int registrationId,

        @NotNull
        @ByteArraySize(32)
        byte[] identityKey,
        @Min(1)
        int signedPreKeyId,

        @NotNull
        @ByteArraySize(32)
        byte[] signedPreKey,

        @NotNull
        @ByteArraySize(64)
        byte[] signedPreKeySig,

        @NotNull
        @Size(min = 1, max = 100, message = "Deve conter de 1 até 100 pre-keys")
        List<@Valid PreKeyRequestDTO> oneTimePreKeys
        
) {
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof KeyBundleRequestDTO(
                        int id, byte[] key, int preKeyId, byte[] preKey, byte[] preKeySig,
                        List<PreKeyRequestDTO> timePreKeys
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
                return "KeyBundleRequestDTO[" +
                        "registrationId=" + registrationId +
                        ", identityKey=" + Arrays.toString(identityKey) +
                        ", signedPreKeyId=" + signedPreKeyId +
                        ", signedPreKey=" + Arrays.toString(signedPreKey) +
                        ", signedPreKeySig=" + Arrays.toString(signedPreKeySig) +
                        ", oneTimePreKeys=" + oneTimePreKeys +
                        ']';
        }
}