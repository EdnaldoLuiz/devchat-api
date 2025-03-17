package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;

public class RegisterSteps extends BaseSteps {

    private RegisterRequest registerRequest;

    @Given("que eu limpei o banco de dados para garantir um estado inicial para os testes de registro")
    public void limparBanco() {
        limparBancoDeDados();
    }

    @Given("que existe um payload de registro válido")
    public void payloadValido() {
        registerRequest = new RegisterRequest("test@example.com", "SenhaForte123!", "SenhaForte123!", "Teste da Silva", true);
    }

    @Given("que existe um usuario pré-cadastrado com email {string}")
    public void usuarioPreCadastrado(String email) {
        insertUserIntoDatabase(new RegisterRequest(email, "SenhaForte123!", "SenhaForte123!", "Usuário Existente", true));
    }

    @Given("que existe um payload de registro com senhas diferentes")
    public void payloadSenhasDiferentes() {
        registerRequest = new RegisterRequest(
                "luiz@gmail.com",
                "abcdefgh123+",
                "abcdefgh124+",
                "Abc 123",
                true);
    }

    @Given("que existe um payload de registro com a senha {string}")
    public void payloadSenhaFracaComExemplos(String senha) {
        registerRequest = new RegisterRequest(
                "usuario@exemplo.com",
                senha,
                senha,
                "Usuario Exemplo",
                true);
    }

    @Given("que existe um payload de registro com o email {string}")
    public void payloadComEmailExistente(String email) {
        registerRequest = new RegisterRequest(
                email,
                "NovaSenhaForte123!",
                "NovaSenhaForte123!",
                "Novo Usuário",
                true);
    }

    @When("eu envio uma requisição de registro")
    public void envioRequisicaoRegistro() {
        String registerPath = Paths.V1.Auth.AUTH + Paths.Auth.REGISTER;
        response = ApiRequestHelper.doPost(registerPath, webClient, registerRequest, RegisterResponse.class);
        log.info("Resposta da requisição de registro: {}", response);
    }

    @Then("o status da resposta da API de registro deve ser {int}")
    public void verificarStatusRegistro(int status) {
        verificarStatus(status, RegisterResponse.class);
    }

    @Then("deve retornar o email do novo usuário e seus tokens")
    public void verificarEmailETokens() {
        RegisterResponse body = (RegisterResponse) response.getBody();

        assertNotNull(body, "O corpo da resposta não pode ser nulo.");
        assertEquals(registerRequest.email(), body.email(), "O email retornado não corresponde ao esperado");
        assertNotNull(body.tokens(), "Os tokens não podem ser nulos");
        assertEquals(2, body.tokens().size(), "Deve haver exatamente dois tokens retornados");
    }

    @Then("deve conter a mensagem {string}")
    public void verificarMensagem(String mensagem) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula.");

        String responseAsString = Optional.ofNullable(response.getBody())
                                        .map(Object::toString)
                                        .orElse("");

        assertTrue(responseAsString.contains(mensagem), "A resposta não contém a mensagem esperada: " + mensagem);
    }
}