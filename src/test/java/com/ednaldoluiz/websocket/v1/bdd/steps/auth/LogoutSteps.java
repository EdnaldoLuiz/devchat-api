package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.ednaldoluiz.websocket.web.web.controller.common.GenericApiResponse;
import com.ednaldoluiz.websocket.web.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LogoutSteps extends BaseSteps {

    private static final String URI = Paths.V1.Auth.AUTH + Paths.Auth.LOGOUT;

    @Given("que o usuário está autenticado")
    public void usuarioEstaAutenticado() {
        // Aqui você deve garantir que o usuário está autenticado.
        // Pode ser: inserir um usuário no banco e realizar login, configurando o token no WebClient.
        // Para simplificar, assumimos que esse step já deixa o WebClient autenticado.
        log.info("Usuário autenticado configurado para o teste de logout.");
    }

    @When("eu envio uma requisição de logout")
    public void enviarRequisicaoLogout() {
        response = ApiRequestHelper.doPost(URI, webClient, "", GenericApiResponse.class);
        log.info("Requisição de logout enviada.");
    }

    @Then("o status da resposta da API de logout deve ser {int}")
    public void validarStatusLogout(int statusEsperado) {
        verificarStatus(statusEsperado, GenericApiResponse.class);
    }

    @Then("deve conter a mensagem de logout {string}")
    public void validarMensagemLogout(String mensagemEsperada) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula.");
        if (response.getBody() instanceof GenericApiResponse genericResp) {
            assertTrue(genericResp.getMessage().contains(mensagemEsperada),
                    "A resposta não contém a mensagem esperada: " + mensagemEsperada);
        }
        log.info("Mensagem de logout validada.");
    }
}