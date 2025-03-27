package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.web.controller.common.GenericApiResponse;
import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;

import java.time.LocalDateTime;

public class ResetPasswordSteps extends BaseSteps {

    private static final String RESET_PASSWORD_URI = Paths.V1.Auth.AUTH + Paths.Auth.RESET_PASSWORD;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    private ResetPasswordRequest requestPayload;
    private ResponseEntity<?> response;

    @Given("que existe um usuário cadastrado com email {string}")
    public void criarUsuario(String email) {
        var user = new User(email, passwordEncoder.encode("SenhaForte123!"), "Usuário Teste");
        userRepository.save(user);
    }

    @Given("que existe um token válido com key {string} e token {string} vinculado ao usuário com email {string}")
    public void criarTokenValido(String key, String token, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado com email: " + email));

        String hashedToken = passwordEncoder.encode(token);
        var resetToken = new PasswordResetToken(user, key, hashedToken);
        tokenRepository.save(resetToken);
    }

    @Given("que existe um token expirado com key {string} vinculado ao usuário")
    public void criarTokenExpirado(String key) {
        User user = userRepository.findByEmail("user2@example.com")
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        String hashedToken = passwordEncoder.encode("qualquer-token");

        PasswordResetToken token = new PasswordResetToken(user, key, hashedToken);
        ReflectionTestUtils.setField(token, "expiresAt", LocalDateTime.now().minusMinutes(10));
        tokenRepository.save(token);
    }

    @Given("que existe um token válido com key {string} e token {string} vinculado a um usuário removido do banco")
    public void criarTokenValidoUsuarioRemovido(String key, String token) {
        // Primeiro cria o usuário normalmente
        User usuarioRemovido = new User("removido@example.com", passwordEncoder.encode("SenhaTeste123!"),
                "Usuário Removido");
        userRepository.save(usuarioRemovido);

        String hashedToken = passwordEncoder.encode(token);
        var resetToken = new PasswordResetToken(usuarioRemovido, key, hashedToken);
        tokenRepository.save(resetToken);

        userRepository.delete(usuarioRemovido);
    }

    @Given("que existe um payload de reset com key {string}, token {string}, senha {string} e confirmPassword {string}")
    public void criarPayload(String key, String token, String senha, String confirmSenha) {
        requestPayload = new ResetPasswordRequest(key, token, senha, confirmSenha);
    }

    @When("eu envio uma requisição de reset de senha")
    public void enviarRequisicaoReset() {
        response = ApiRequestHelper.doPost(RESET_PASSWORD_URI, webClient, requestPayload, GenericApiResponse.class);
    }

    @Then("o status da resposta da API de reset deve ser {int}")
    public void validarStatusDaResposta(int statusEsperado) {
        assertNotNull(response, "A resposta da requisição não pode ser nula.");
        assertEquals(statusEsperado, response.getStatusCode().value(),
                "O status retornado não corresponde ao esperado.");
        log.info("Status da resposta: {}", response.getStatusCode());
    }

    @Then("deve conter a mensagem de reset {string}")
    public void validarMensagemDeResposta(String mensagemEsperada) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula.");

        if (response.getBody() instanceof GenericApiResponse genericResp) {
            assertTrue(genericResp.getMessage().contains(mensagemEsperada),
                    "A resposta de sucesso não contém a mensagem esperada: " + mensagemEsperada);
        }
        if (response.getBody() instanceof ErrorResponse errResp) {
            assertTrue(errResp.error().contains(mensagemEsperada),
                    "A resposta de erro não contém a mensagem esperada: " + mensagemEsperada);
        }
    }
}