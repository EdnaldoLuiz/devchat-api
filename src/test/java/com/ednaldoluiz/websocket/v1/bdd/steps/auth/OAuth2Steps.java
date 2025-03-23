package com.ednaldoluiz.websocket.v1.bdd.steps.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import com.ednaldoluiz.websocket.app.v1.auth.dto.response.OAuth2LoginResponse;
import com.ednaldoluiz.websocket.domain.model.user.AuthProvider;
import com.ednaldoluiz.websocket.infra.web.handler.ErrorResponse;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.bdd.steps.BaseSteps;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class OAuth2Steps extends BaseSteps {

    private static final String OAUTH2_LOGIN_URI = Paths.V1.Auth.AUTH + "/oauth2/login";
    private static final Map<String, Map<String, String>> DEFAULT_ATTRIBUTES = new HashMap<>();

    private Map<String, Object> userAttributes = new HashMap<>();
    private AuthProvider provider;

    static {
        Map<String, String> githubAttributes = Map.of(
                "email", "githubuser@example.com",
                "login", "githubUser",
                "avatar_url", "https://github.com/images/123.png",
                "bio", "Dev at GitHub");

        Map<String, String> googleAttributes = Map.of(
                "email", "googleuser@example.com",
                "name", "GoogleUser",
                "picture", "https://google.com/avatar/456.png");

        DEFAULT_ATTRIBUTES.put("GITHUB", githubAttributes);
        DEFAULT_ATTRIBUTES.put("GOOGLE", googleAttributes);
    }

    @Given("que possuo atributos válidos do {string}")
    public void possuoAtributosValidos(String providerStr) {
        userAttributes.clear();
        Map<String, String> attributes = DEFAULT_ATTRIBUTES.get(providerStr.toUpperCase());
        if (attributes != null) {
            userAttributes.putAll(attributes);
        } else {
            throw new IllegalArgumentException("Provider não suportado: " + providerStr);
        }
    }

    @Given("que possuo atributos inválidos do {string} com o campo {string} ausente")
    public void possuoAtributosInvalidos(String providerStr, String campo) {
        possuoAtributosValidos(providerStr);
        userAttributes.remove(campo);

        if (!userAttributes.containsKey("email") && !userAttributes.containsKey("name")) {
            userAttributes.put("name", "valorDummy");
        }
    }

    @Given("que vou usar o provider {string}")
    public void vouUsarProvider(String providerStr) {
        provider = AuthProvider.valueOf(providerStr.toUpperCase());
    }

    @Given("o usuário não está cadastrado no sistema")
    public void usuarioNaoEstaCadastrado() {
        limparBancoDeDados();
        log.info("Usuário removido do banco para teste de registro via OAuth2.");
    }

    @Given("o usuário {string} agora existe no banco de dados")
    public void usuarioExisteNoBanco(String email) {
        var userOpt = userRepository.findByEmail(email);
        assertTrue(userOpt.isPresent(), "O usuário " + email + " não foi criado.");
        log.info("Usuário {} verificado no banco de dados.", email);
    }

    @When("eu envio uma requisição de login OAuth2")
    public void enviarRequisicaoOAuth2() {
        String successUri = Paths.V1.Auth.AUTH + "/oauth2/success";

        response = ApiRequestHelper.doGet(successUri, webClient, OAuth2LoginResponse.class);

        log.info("Requisição de login OAuth2 enviada (GET /success). Provider: {}", provider);
    }

    @Then("o status da resposta da API de OAuth2 deve ser {int}")
    public void validarStatusOAuth2(int statusEsperado) {
        verificarStatus(statusEsperado, OAuth2LoginResponse.class);
    }

    @Then("deve conter a mensagem de OAuth2 {string}")
    public void deveConterMensagemOAuth2(String mensagemEsperada) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula.");
        if (response.getBody() instanceof OAuth2LoginResponse oAuth2Resp) {
            assertTrue(oAuth2Resp.message().contains(mensagemEsperada),
                    "A resposta não contém a mensagem esperada: " + mensagemEsperada);
        } else if (response.getBody() instanceof ErrorResponse errResp) {
            fail("Recebemos um ErrorResponse ao invés de OAuth2LoginResponse! Mensagem: " + errResp.message());
        }
    }

    @Then("deve conter um token de acesso")
    public void deveConterUmToken() {
        assertNotNull(response.getBody(), "A resposta não pode ser nula.");
        assertTrue(response.getBody() instanceof OAuth2LoginResponse, "O corpo não é do tipo OAuth2LoginResponse.");
        OAuth2LoginResponse oAuth2Resp = (OAuth2LoginResponse) response.getBody();

        assertNotNull(oAuth2Resp, "A resposta não pode ser nula.");
        assertFalse(oAuth2Resp.token().isBlank(), "Token não pode ser vazio.");
        log.info("Token OAuth2 validado com sucesso: {}", oAuth2Resp.token());
    }
}