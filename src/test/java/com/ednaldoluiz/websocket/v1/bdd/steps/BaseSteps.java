package com.ednaldoluiz.websocket.v1.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.http.ResponseEntity;

import com.ednaldoluiz.websocket.v1.shared.base.AbstractAuthTest;

public abstract class BaseSteps extends AbstractAuthTest {

    protected ResponseEntity<?> response;

    protected void limparBancoDeDados() {
        log.info("Limpando banco de dados...");
        userRepository.deleteAll();
        log.info("Banco de dados limpo na classe: {}.", this.getClass().getSimpleName());
    }

    protected <T> void verificarStatus(int statusEsperado, Class<T> responseType) {
        assertNotNull(response, "A resposta da requisição não pode ser nula.");
        assertEquals(statusEsperado, response.getStatusCode().value(), "O status retornado não corresponde ao esperado.");
        log.info("Status da resposta: {}", response.getStatusCode());
    }
}