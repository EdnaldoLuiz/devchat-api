package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import reactor.core.publisher.Mono;

import org.springframework.http.ResponseEntity;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;

public class SendResetTokenSteps extends BaseSteps {

    private ResponseEntity<?> response;
    private String recoveryEmail;

    @Dado("que eu limpei o banco de dados para garantir um estado inicial para os testes de envio de token de recuperação")
    public void limparBanco() {
        limparBancoDeDados();
    }

    @Dado("que existe um payload de recuperação para o email {string}")
    public void payloadRecuperacao(String email) {
        this.recoveryEmail = email;
    }

    @Dado("que existe um usuário cadastrado para recuperação de senha com email {string} e senha {string}")
    public void usuarioCadastradoParaResetDeSenha(String email, String senha) {
        var registerRequest = new RegisterRequest(
            email,
            senha,
            senha,
            "Usuário Teste",
            true
        );
        insertUserIntoDatabase(registerRequest);
    }

    @Quando("eu envio uma requisição de redefinição de senha")
    public void envioRequisicaoRedefinicao() {
        String uri = Paths.V1.Auth.AUTH + Paths.Auth.FORGOT_PASSWORD + "?email=" + recoveryEmail;
        Mono<ResponseEntity<String>> responseMono = webClient.post()
            .uri(uri)
            .retrieve()
            .toEntity(String.class)
            .onErrorResume(e -> {
                log.error("Erro ao solicitar redefinição de senha: {}", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body("Erro: " + e.getMessage()));
            });

        response = responseMono.block();
    }

    @Então("a resposta deve ser de sucesso com status {int}")
    public void respostaSucesso(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Então("a resposta deve retornar status {int}")
    public void respostaStatus(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Então("deve conter a mensagem {string}")
    public void respostaContemMensagem(String mensagem) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula");
        String responseAsString = Objects.requireNonNull(response.getBody()).toString();
        assertTrue(responseAsString.contains(mensagem),
            "A resposta não contém a mensagem esperada: " + mensagem);
    }
}