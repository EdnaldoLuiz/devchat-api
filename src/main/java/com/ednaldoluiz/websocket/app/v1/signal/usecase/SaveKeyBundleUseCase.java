package com.ednaldoluiz.websocket.app.v1.signal.usecase;

import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;
import org.whispersystems.curve25519.Curve25519;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveKeyBundleUseCase {

    private final UserSignalKeysRepository userSignalKeysRepository;

    public void execute(Long userId, KeyBundleRequestDTO dto) {

        log.info("[DEBUG Signal] identityKey(base64)={}", Base64.getEncoder().encodeToString(dto.identityKey()));
        log.info("[DEBUG Signal] signedPreKey(base64)={}", Base64.getEncoder().encodeToString(dto.signedPreKey()));
        log.info("[DEBUG Signal] signedPreKeySig(base64)={}", Base64.getEncoder().encodeToString(dto.signedPreKeySig()));

        log.info("Salvando key bundle para userId={}", userId);

        try {
            validarSignedPreKey(dto.identityKey(), dto.signedPreKey(), dto.signedPreKeySig());
        } catch (Exception ex) {
            log.error("SignedPreKey inválida! Não será salvo.", ex);
            throw new IllegalArgumentException("SignedPreKey signature verification failed", ex);
        }
        // --- FIM DA VALIDAÇÃO ---

        userSignalKeysRepository.saveKeyBundle(userId, dto);
        log.info("Key bundle salvo com sucesso para userId={}", userId);
    }

    public static void validarSignedPreKey(byte[] ik, byte[] spk, byte[] sig) {
        if (ik.length != 33 || spk.length != 33 || sig.length != 64)
            throw new IllegalArgumentException("Tamanhos inválidos");

        Curve25519 curve = Curve25519.getInstance(Curve25519.BEST);

        // XEdDSA: publicKey, 1 skip, message, 1 skip, signature
        boolean ok = curve.verifySignature(
                Arrays.copyOfRange(ik, 1, 33),    // 32 bytes pub
                spk,   // mensagem de 32 bytes
                sig
        );
        if (!ok) throw new IllegalArgumentException("Assinatura SPK inválida!");
    }
}
