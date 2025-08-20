// src/main/java/com/ednaldoluiz/websocket/app/v1/chat/dto/response/HistoryPayload.java
package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.util.Base64;

public record HistoryPayload(
    String algorithm,
    int    version,
    String initializationVectorBase64,
    String ciphertextBase64
) {
  /** Construtor de conveniência p/ quando você tem bytes no backend. */
  public static HistoryPayload fromRaw(
      String algorithm,
      Integer version,
      byte[] initializationVector,
      byte[] ciphertext
  ) {
    final int ver = (version != null) ? version.intValue() : 1;

    String ivB64 = null;
    if (initializationVector != null && initializationVector.length > 0) {
      ivB64 = Base64.getEncoder().encodeToString(initializationVector);
    }

    String ctB64 = null;
    if (ciphertext != null && ciphertext.length > 0) {
      ctB64 = Base64.getEncoder().encodeToString(ciphertext);
    }

    return new HistoryPayload(algorithm, ver, ivB64, ctB64);
  }

  /** Retorna o IV em bytes (ou null se não houver). */
  public byte[] decodeIvBytes() {
    if (initializationVectorBase64 == null || initializationVectorBase64.isBlank()) {
      return null;
    }
    return Base64.getDecoder().decode(initializationVectorBase64);
  }

  /** Retorna o ciphertext em bytes (ou null se não houver). */
  public byte[] decodeCiphertextBytes() {
    if (ciphertextBase64 == null || ciphertextBase64.isBlank()) {
      return null;
    }
    return Base64.getDecoder().decode(ciphertextBase64);
  }

  /** Conveniências (caso queira checar presença sem decodificar). */
  public boolean hasInitializationVector() {
    return initializationVectorBase64 != null && !initializationVectorBase64.isBlank();
  }

  public boolean hasCiphertext() {
    return ciphertextBase64 != null && !ciphertextBase64.isBlank();
  }
}
