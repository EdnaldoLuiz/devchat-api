package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record CipherPayload(
        @JsonProperty("type") int type,
        @JsonProperty("body") String body
) {
    private static final Logger log = LoggerFactory.getLogger(CipherPayload.class);

    /** Decodifica o body aceitando Base64 (padrão) ou "0x..." (HEX) */
    public byte[] decodeBodyBytes() {
        if (body == null) return new byte[0];
        try {
            if (isHex(body)) {
                byte[] out = hexToBytes(body);
                log.info("[CipherPayload] decode HEX -> {} bytes firstByte=0x{}",
                        out.length, out.length == 0 ? "??" : String.format("%02X", out[0]));
                return out;
            }
            byte[] out = java.util.Base64.getDecoder().decode(body);
            log.info("[CipherPayload] decode Base64 -> {} bytes firstByte=0x{}",
                    out.length, out.length == 0 ? "??" : String.format("%02X", out[0]));
            return out;
        } catch (IllegalArgumentException e) {
            log.warn("[CipherPayload] decode FAIL (len={} head={})",
                    body.length(), body.substring(0, Math.min(10, body.length())));
            throw e;
        }
    }

    // Mantém compat (se alguém ainda chama)
    public byte[] decodeBody() {
        return decodeBodyBytes();
    }

    private static boolean isHex(String s) {
        return s.startsWith("0x") || s.startsWith("0X");
    }

    private static byte[] hexToBytes(String s) {
        String hex = (s.startsWith("0x") || s.startsWith("0X")) ? s.substring(2) : s;
        if ((hex.length() & 1) != 0) hex = "0" + hex;
        int n = hex.length() / 2;
        byte[] out = new byte[n];
        for (int i = 0; i < n; i++) {
            int hi = Character.digit(hex.charAt(i * 2), 16);
            int lo = Character.digit(hex.charAt(i * 2 + 1), 16);
            out[i] = (byte) ((hi << 4) | lo);
        }
        return out;
    }

    @JsonCreator
    public static CipherPayload of(
            @JsonProperty("type") int type,
            @JsonProperty("body") String body
    ) {
        return new CipherPayload(type, body);
    }
}
