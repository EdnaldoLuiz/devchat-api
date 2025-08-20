package com.ednaldoluiz.websocket.domain.message.valueObject;

import java.util.Arrays;

/**
 * VO que descreve o histórico criptográfico associado à mensagem,
 * fixo no algoritmo AES-256-GCM.
 */
public record HistoryContext(
        int version,
        byte[] initializationVector,
        byte[] ciphertext
) {
    public static final String ALGORITHM   = "AES-256-GCM";
    public static final int    DEFAULT_VER = 1;
    public static final int    IV_LEN      = 12;

    public HistoryContext {
        version = (version < 1) ? DEFAULT_VER : version;
        initializationVector = normalize(initializationVector);
        ciphertext           = normalize(ciphertext);

        if (initializationVector == null || initializationVector.length != IV_LEN) {
            throw new IllegalArgumentException("Invalid IV length (expected 12 bytes for AES-256-GCM)");
        }
        if (ciphertext == null || ciphertext.length == 0) {
            throw new IllegalArgumentException("Ciphertext is required for AES-256-GCM");
        }
    }

    private static byte[] normalize(byte[] a) {
        if (a == null || a.length == 0) return null;
        return Arrays.copyOf(a, a.length); // defensive copy
    }

    public static HistoryContext ofNullable(Integer version, byte[] iv, byte[] ct) {
        return new HistoryContext(
                (version == null ? DEFAULT_VER : version),
                iv,
                ct
        );
    }

    public String algorithm() {
        return ALGORITHM;
    }
}
