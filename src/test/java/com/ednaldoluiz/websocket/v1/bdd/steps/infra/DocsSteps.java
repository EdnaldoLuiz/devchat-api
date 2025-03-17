package com.ednaldoluiz.websocket.v1.bdd.steps.infra;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DocsSteps {

    private final WebClient webClient = WebClient.create();

    @Value("${test.server.base-url:http://localhost:8080}")
    private String baseUrl;

    private String swaggerUrl;

    @Given("que eu sei a URL do Swagger")
    public void definirSwaggerUrl() {
        swaggerUrl = baseUrl + "/swagger-ui/index.html";
        log.info("Swagger URL definida como: {}", swaggerUrl);
    }

    @Then("o Swagger deve estar acessível")
    public void verificarSwagger() {
        log.info("Verificando se o Swagger está acessível em: {}", swaggerUrl);

        ResponseEntity<String> response = webClient.get()
                .uri(swaggerUrl)
                .retrieve()
                .toEntity(String.class)
                .block();

        assertNotNull(response, "A resposta da requisição não pode ser nula.");
        assertEquals(200, response.getStatusCode().value(), "O Swagger não está acessível.");

        log.info("Swagger encontrado e acessível com status {}", response.getStatusCode().value());
    }
}