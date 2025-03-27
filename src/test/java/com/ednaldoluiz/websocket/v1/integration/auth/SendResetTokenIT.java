package com.ednaldoluiz.websocket.v1.integration.auth;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.port.EmailPort;
import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.web.route.Paths;
import com.ednaldoluiz.websocket.v1.config.TestContainerDatabaseConfig;
import com.ednaldoluiz.websocket.v1.shared.base.AbstractAuthTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("auth")
@SuppressWarnings("null")
@ExtendWith({ TestContainerDatabaseConfig.class })
class SendResetTokenIT extends AbstractAuthTest {

    private final String URI = Paths.V1.Auth.AUTH + Paths.Auth.FORGOT_PASSWORD;

    @MockitoBean
    private EmailPort emailPort;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @BeforeEach
    void cleanDatabase() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar e enviar token de reset para usuário existente e validar formato")
    void shouldCreateAndSendTokenForExistingUserWithValidFormat() {
        RegisterRequest request = new RegisterRequest(
                "teste@exemplo.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        insertUserIntoDatabase(request);

        String uri = UriComponentsBuilder.fromUriString(URI)
                .queryParam("email", request.email())
                .build(true)
                .toUriString();

        ResponseEntity<String> response = new ResponseEntity<>(webClient.post()
                .uri(uri)
                .exchange()
                .expectBody(String.class)
                .returnResult().getResponseBody(), HttpStatus.OK);

        List<PasswordResetToken> tokens = tokenRepository.findAll();
        assertEquals(1, tokens.size(), "Deveria ter um token no banco");

        PasswordResetToken token = tokenRepository.findByIdWithUser(tokens.get(0).getId())
                .orElseThrow(() -> new AssertionError("Token não encontrado"));

        assertNotNull(response);
        assertTrue(response.getBody().contains("E-mail de redefinição de senha enviado com sucesso"),
                "Deveria retornar mensagem de sucesso");
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Deveria retornar 200 OK");
        assertEquals(request.email(), token.getUser().getEmail(), "Deveria ter o e-mail do usuário");

        assertEquals(60, token.getHashedToken().length(), "O hashed_token deveria ter 60 caracteres (Bcrypt)");
        assertDoesNotThrow(() -> UUID.fromString(token.getKeyId()), "O keyId deveria ser um UUID válido");

        verify(emailPort, times(1)).sendEmail(any(), eq("teste@exemplo.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("Não deve criar token nem enviar e-mail para usuário inexistente")
    void shouldNotCreateTokenForNonExistentUser() {
        String emailNaoCadastrado = "naoexiste@exemplo.com";

        String uri = UriComponentsBuilder.fromUriString(URI)
                .queryParam("email", emailNaoCadastrado)
                .build(true)
                .toUriString();

        String responseBody = webClient.post()
                .uri(uri)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);
        assertTrue(responseBody.contains("O e-mail informado não está cadastrado."),
                "O corpo da resposta deve conter o erro de e-mail não cadastrado");

        List<PasswordResetToken> tokens = tokenRepository.findAll();
        assertTrue(tokens.isEmpty(), "Nenhum token deveria ser criado para usuário inexistente");

        verify(emailPort, never()).sendEmail(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve marcar token como expirado após o tempo limite")
    void shouldMarkTokenAsExpiredAfterExpirationTime() {
        RegisterRequest request = new RegisterRequest(
                "teste@exemplo.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        User user = insertUserIntoDatabase(request);

        PasswordResetToken token = new PasswordResetToken(user, UUID.randomUUID().toString(),
                passwordEncoder.encode("fakeHashedToken"));
        ReflectionTestUtils.setField(token, "expiresAt", LocalDateTime.now().minusMinutes(1));

        tokenRepository.save(token);

        PasswordResetToken fetchedToken = tokenRepository.findById(token.getId())
                .orElseThrow(() -> new AssertionError("Token não encontrado"));

        assertTrue(fetchedToken.isExpired(), "O token deveria estar marcado como expirado");
    }

    @Test
    @DisplayName("Deve marcar token como usado após ser utilizado")
    void shouldMarkTokenAsUsedAfterPasswordReset() {
        RegisterRequest request = new RegisterRequest(
                "teste@exemplo.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        User user = insertUserIntoDatabase(request);

        PasswordResetToken token = new PasswordResetToken(user, UUID.randomUUID().toString(),
                passwordEncoder.encode("fakeHashedToken"));

        tokenRepository.save(token);

        token.markAsUsed();
        tokenRepository.save(token);

        PasswordResetToken fetchedToken = tokenRepository.findById(token.getId())
                .orElseThrow(() -> new AssertionError("Token não encontrado"));

        assertTrue(fetchedToken.isUsed(), "O token deveria estar marcado como usado");
    }
}