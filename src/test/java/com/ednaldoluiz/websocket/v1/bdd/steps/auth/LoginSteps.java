package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;

public class LoginSteps extends BaseSteps {

    private LoginRequest loginRequest;

    @Given("que existe um usuário pré-cadastrado com email {string} e senha {string}")
    public void usuarioPreCadastrado(String email, String senha) {
        insertUserIntoDatabase(new RegisterRequest(email, senha, senha, "Usuário Teste", true));
    }

    @Given("que existe um payload de login com email {string} e senha {string}")
    public void payloadLoginCustom(String email, String senha) {
        loginRequest = new LoginRequest(email, senha);
    }

    @Given("que existe um payload de login válido")
    public void payloadLoginValido() {
        loginRequest = new LoginRequest(
                "user@example.com",
                "SenhaForte123!");
    }

    @When("eu envio uma requisição de login")
    public void envioRequisicaoLogin() {
        String loginPath = Paths.V1.Auth.AUTH + Paths.Auth.LOGIN;
        response = ApiRequestHelper.doPost(loginPath, webClient, loginRequest, LoginResponse.class);
        log.info("Resposta da requisição de login: {}", response);
    }

    @Then("o status da resposta da API de login deve ser {int}")
    public void verificarStatusLogin(int status) {
        verificarStatus(status, LoginResponse.class);
    }

    @Then("deve retornar o email do usuário e seus tokens")
    public void verificarEmailETokens() {
        LoginResponse body = (LoginResponse) response.getBody();
        assertNotNull(Objects.requireNonNull(body).tokens(), "Os tokens não podem ser nulos");
        assertEquals(2, body.tokens().size(), "Deve haver exatamente dois tokens retornados");
    }
}