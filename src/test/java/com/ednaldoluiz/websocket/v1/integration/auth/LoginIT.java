package com.ednaldoluiz.websocket.v1.integration.auth;

import com.ednaldoluiz.websocket.v1.config.TestContainerDatabaseConfig;
import com.ednaldoluiz.websocket.v1.shared.base.AbstractAuthTest;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;

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

import static org.assertj.core.api.Assertions.assertThat;

import com.ednaldoluiz.websocket.web.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.web.route.Paths;

@Tag("auth")
@SuppressWarnings({"null", "unchecked"})
@ExtendWith({TestContainerDatabaseConfig.class})
public class LoginIT extends AbstractAuthTest {

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

        ResponseEntity<LoginResponse> response = (ResponseEntity<LoginResponse>) 
        ApiRequestHelper.doPost(URI, webClient, loginRequest, LoginResponse.class);

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

        ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>) 
        ApiRequestHelper.doPost(URI, webClient, loginRequest, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).contains("Erro de validação com as credenciais do usuário");
    }

    @Test
    @Order(3)
    @DisplayName("Deve falhar ao logar com senha inválida")
    void testLoginInvalidPassword() {

        LoginRequest loginRequest = new LoginRequest(
                "testuser@example.com",
                "SenhaErrada456!");

        ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>)
        ApiRequestHelper.doPost(URI, webClient, loginRequest, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().message()).contains("Erro de validação com as credenciais do usuário");
    }
}