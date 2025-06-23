package com.ednaldoluiz.websocket.app.v1.signal.dto.request;

import com.ednaldoluiz.websocket.app.v1.signal.validator.ByteArraySize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Objects;

@Schema(description = "DTO de requisição para uma pre-key do Signal.")
public record PreKeyRequestDTO(

        @Min(1)
        int keyId,

        @NotNull
        @ByteArraySize(32)
        byte[] publicKey

) {
        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof PreKeyRequestDTO(int id, byte[] key))) return false;
                return keyId == id && Arrays.equals(publicKey, key);
        }

        @Override
        public int hashCode() {
                return Objects.hash(keyId, Arrays.hashCode(publicKey));
        }

        @Override
        public String toString() {
                return "PreKeyRequestDTO[keyId=%d, publicKey=%s]".formatted(
                        keyId, Arrays.toString(publicKey)
                );
        }
}