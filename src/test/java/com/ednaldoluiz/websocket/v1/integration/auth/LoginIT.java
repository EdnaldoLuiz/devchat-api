package com.ednaldoluiz.websocket.v1.integration.auth;

import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.v1.integration.base.AbstractAuthIT;

import static org.assertj.core.api.Assertions.assertThat;
import com.ednaldoluiz.websocket.infra.web.route.Paths;

@Tag("auth")
@SuppressWarnings("null")
@ExtendWith({TestContainerDatabaseConfig.class})
public class LoginIT extends AbstractAuthIT {

    private final String URI = Paths.V1.Auth.AUTH + Paths.Auth.LOGIN;

    @Test
    @Order(1)
    @DisplayName("Deve logar com sucesso e retornar um token de acesso e um token de refresh")
    void testLoginSuccessful() {
        RegisterRequest request = new RegisterRequest(
                "testuser@example.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        insertUserIntoDatabase(request);

        LoginRequest loginRequest = new LoginRequest(
                "testuser@example.com",
                "SenhaForte123!");

        ResponseEntity<LoginResponse> response = doPost(URI, webClient, loginRequest, LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().tokens()).hasSize(2);
    }

    @Test
    @Order(2)
    @DisplayName("Deve falhar ao logar com email inválido")
    void testLoginInvalidEmail() {

        LoginRequest loginRequest = new LoginRequest(
                "testuser@gmail.com",
                "SenhaForte123!");

        ResponseEntity<String> response = doPost(URI, webClient, loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email ou senha inválidos.");
    }

    @Test
    @Order(3)
    @DisplayName("Deve falhar ao logar com senha inválida")
    void testLoginInvalidPassword() {

        LoginRequest loginRequest = new LoginRequest(
                "testuser@example.com",
                "SenhaErrada456!");

        ResponseEntity<String> response = doPost(URI, webClient, loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Email ou senha inválidos.");
    }
}