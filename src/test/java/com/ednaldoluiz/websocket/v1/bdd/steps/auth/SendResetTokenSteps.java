package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.springframework.http.ResponseEntity;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.web.controller.common.GenericApiResponse;
import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;

public class SendResetTokenSteps extends BaseSteps {

    private ResponseEntity<?> response;
    private String recoveryEmail;

    @Given("que existe um payload de recuperação para o email {string}")
    public void payloadRecuperacao(String email) {
        this.recoveryEmail = email;
    }

    @Given("que existe um usuário cadastrado para recuperação de senha com email {string} e senha {string}")
    public void usuarioCadastradoParaResetDeSenha(String email, String senha) {
        var registerRequest = new RegisterRequest(
                email,
                senha,
                senha,
                "Usuário Teste",
                true);
        insertUserIntoDatabase(registerRequest);
    }

    @When("eu envio uma requisição de redefinição de senha com um email válido")
    public void envioRequisicaoRedefinicaoValida() {
        String uri = Paths.V1.Auth.AUTH + Paths.Auth.FORGOT_PASSWORD + "?email=" + recoveryEmail;
        log.info("🚀 Enviando requisição para: {}", uri);

        response = ApiRequestHelper.doPost(uri, webClient, "", GenericApiResponse.class);
    }

    @When("eu envio uma requisição de redefinição de senha com um email inválido")
    public void envioRequisicaoRedefinicaoInvalida() {
        String uri = Paths.V1.Auth.AUTH + Paths.Auth.FORGOT_PASSWORD + "?email=" + recoveryEmail;
        log.info("🚀 Enviando requisição para: {}", uri);

        response = ApiRequestHelper.doPost(uri, webClient, "", ErrorResponse.class);
    }

    @Then("o status da resposta da API de recuperação deve ser sucesso {int}")
    public void respostaStatusSucesso(int status) {
        assertNotNull(response, "A resposta não pode ser nula");
        assertNotNull(response.getBody(), "O corpo da resposta não pode ser nulo");
        assertEquals(status, response.getStatusCode().value(), "O status da resposta não é o esperado");

        assertTrue(response.getBody() instanceof GenericApiResponse,
                "O corpo da resposta deveria ser um GenericApiResponse");
    }

    @Then("o status da resposta da API de recuperação deve ser um erro {int}")
    public void respostaStatusErro(int status) {
        assertNotNull(response, "A resposta não pode ser nula");
        assertNotNull(response.getBody(), "O corpo da resposta não pode ser nulo");
        assertEquals(status, response.getStatusCode().value(), "O status da resposta não é o esperado");

        assertTrue(response.getBody() instanceof ErrorResponse,
                "O corpo da resposta deveria ser um ErrorResponse");
    }

    @Then("a resposta de sucesso deve conter a mensagem {string}")
    public void verificarMensagemSucesso(String mensagemEsperada) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula");

        GenericApiResponse sucessResponse = (GenericApiResponse) response.getBody();
        assertNotNull(sucessResponse, "A mensagem de sucesso não pode ser nula");
        log.info("Mensagem da API (sucesso): {}", sucessResponse.getMessage());

        assertTrue(sucessResponse.getMessage().contains(mensagemEsperada),
                "A resposta não contém a mensagem esperada: " + mensagemEsperada);
    }

    @Then("a resposta de erro deve conter a mensagem {string}")
    public void verificarMensagemErro(String mensagemEsperada) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula");

        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse, "A mensagem de erro não pode ser nula");
        log.info("Mensagem da API (erro): {}", errorResponse.error());

        assertTrue(errorResponse.error().contains(mensagemEsperada),
                "A resposta não contém a mensagem esperada: " + mensagemEsperada);
    }
}