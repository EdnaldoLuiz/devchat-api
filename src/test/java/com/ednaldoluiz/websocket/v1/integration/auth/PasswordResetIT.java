package com.ednaldoluiz.websocket.v1.integration.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.integration.base.AbstractAuthIT;
import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;

@Tag("auth")
@SuppressWarnings("null")
@ExtendWith({ TestContainerDatabaseConfig.class })
class PasswordResetIT extends AbstractAuthIT {

    private final String RESET_PASSWORD_URI = Paths.V1.Auth.AUTH + Paths.Auth.RESET_PASSWORD;
    
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Test
    @DisplayName("Deve redefinir senha com token válido")
    void shouldResetPasswordWithValidToken() {

        RegisterRequest request = new RegisterRequest(
            "validuser@example.com", 
            "SenhaAntiga123!", 
            "SenhaAntiga123!", 
            "Valid User", 
            true);

        User user = insertUserIntoDatabase(request);

        String keyId = UUID.randomUUID().toString();
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(rawToken);
        PasswordResetToken token = new PasswordResetToken(user, keyId, hashedToken);
        tokenRepository.save(token);

        ResetPasswordRequest resetRequest = new ResetPasswordRequest(keyId, rawToken, "NovaSenha123!", "NovaSenha123!");

        ResponseEntity<String> response = doPost(RESET_PASSWORD_URI, webClient, resetRequest, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "A senha deveria ser redefinida com sucesso");
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertTrue(passwordEncoder.matches("NovaSenha123!", updatedUser.getPassword()), "A senha deveria ter sido atualizada");
        
        PasswordResetToken updatedToken = tokenRepository.findById(token.getId()).orElseThrow();
        assertTrue(updatedToken.isUsed(), "O token deveria estar marcado como usado");
    }

    @Test
    @DisplayName("Não deve redefinir senha com token inválido")
    void shouldNotResetPasswordWithInvalidToken() {
        ResetPasswordRequest resetRequest = new ResetPasswordRequest(UUID.randomUUID().toString(), UUID.randomUUID().toString(), "NovaSenha123!", "NovaSenha123!");

        ResponseEntity<String> response = doPost(RESET_PASSWORD_URI, webClient, resetRequest, String.class);

        assertNotNull(response, "Deveria retornar uma resposta");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Deveria retornar erro ao usar um token inválido");
        assertTrue(response.getBody().contains("Token inválido ou expirado."), "O erro deveria indicar token inválido");
    }

    @Test
    @DisplayName("Não deve redefinir senha com token expirado")
    void shouldNotResetPasswordWithExpiredToken() {
        RegisterRequest request = new RegisterRequest("expireduser@example.com", "SenhaAntiga123!", "SenhaAntiga123!", "Expired User", true);
        User user = insertUserIntoDatabase(request);

        String keyId = UUID.randomUUID().toString();
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(rawToken);
        PasswordResetToken token = new PasswordResetToken(user, keyId, hashedToken);
        ReflectionTestUtils.setField(token, "expiresAt", LocalDateTime.now().minus(31, ChronoUnit.MINUTES));
        tokenRepository.save(token);

        ResetPasswordRequest resetRequest = new ResetPasswordRequest(keyId, rawToken, "NovaSenha123!", "NovaSenha123!");

        ResponseEntity<String> response = doPost(RESET_PASSWORD_URI, webClient, resetRequest, String.class);

        assertNotNull(response, "Deveria retornar uma resposta");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Deveria retornar erro ao usar um token expirado");
        assertTrue(response.getBody().contains("Token expirado ou já utilizado."), "O erro deveria indicar token expirado");
    }

    @Test
    @DisplayName("Não deve redefinir senha com token que não corresponde ao keyId")
    void shouldNotResetPasswordWithMismatchedToken() {
        RegisterRequest request = new RegisterRequest("mismatch@example.com", "SenhaAntiga123!", "SenhaAntiga123!", "Mismatch User", true);
        User user = insertUserIntoDatabase(request);

        String keyId = UUID.randomUUID().toString();
        String rawToken = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(rawToken);
        PasswordResetToken token = new PasswordResetToken(user, keyId, hashedToken);
        tokenRepository.save(token);

        ResetPasswordRequest resetRequest = new ResetPasswordRequest(keyId, UUID.randomUUID().toString(), "NovaSenha123!", "NovaSenha123!");

        ResponseEntity<String> response = doPost(RESET_PASSWORD_URI, webClient, resetRequest, String.class);

        assertNotNull(response, "Deveria retornar uma resposta");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Deveria retornar erro ao usar token incorreto");
        assertTrue(response.getBody().contains("Token inválido."), "O erro deveria indicar token incorreto");
    }

    @Test
    @DisplayName("Não deve redefinir senha se as senhas não forem idênticas")
    void shouldNotResetPasswordWithMismatchedPasswords() {
        String keyId = UUID.randomUUID().toString();
        String rawToken = UUID.randomUUID().toString();

        ResetPasswordRequest resetRequest = new ResetPasswordRequest(keyId, rawToken, "NovaSenha123!", "OutraSenha123!");

        ResponseEntity<String> response = doPost(RESET_PASSWORD_URI, webClient, resetRequest, String.class);

        assertNotNull(response, "Deveria retornar uma resposta");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Deveria retornar erro ao usar senhas diferentes");
    }
}
