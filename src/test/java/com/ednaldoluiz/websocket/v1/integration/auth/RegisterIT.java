package com.ednaldoluiz.websocket.v1.integration.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.ednaldoluiz.websocket.v1.shared.base.AbstractAuthTest;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.*;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.infra.web.handler.ErrorResponse;
import com.ednaldoluiz.websocket.infra.web.handler.FieldErrorResponse;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.config.TestContainerDatabaseConfig;

@Tag("auth")
@SuppressWarnings({"null", "unchecked", "rawtypes"})
@ExtendWith({TestContainerDatabaseConfig.class})
class RegisterIT extends AbstractAuthTest {

    private final String URI = Paths.V1.Auth.AUTH + Paths.Auth.REGISTER;

    @Test
    @Order(1)
    @DisplayName("Deve registrar um usuário com sucesso")
    void testRegisterUser_Successful() {

        RegisterRequest request = new RegisterRequest(
                "test@example.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        ResponseEntity<RegisterResponse> response = (ResponseEntity) ApiRequestHelper.doPost(URI, webClient, request, RegisterResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("test@example.com");
        assertThat(response.getBody().tokens()).hasSize(2);
    }

    @Test
    @Order(2)
    @DisplayName("Deve falhar ao registrar um usuário com email já cadastrado")
    void testRegisterUser_EmailAlreadyExists() {

        RegisterRequest existingUser = new RegisterRequest(
                "existinguser@example.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Existing User",
                true);

        insertUserIntoDatabase(existingUser);

        RegisterRequest request = new RegisterRequest(
                "existinguser@example.com",
                "NovaSenhaForte123!",
                "NovaSenhaForte123!",
                "Novo Usuário",
                true);

        ResponseEntity<ErrorResponse> response = (ResponseEntity) ApiRequestHelper.doPost(URI, webClient, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().error()).contains("Já existe um usuário com este email.");
    }

    @Test
    @Order(3)
    @DisplayName("Deve falhar ao registrar um usuário com vários erros simultâneos")
    void testRegisterUser_MultipleErrors_01() {
        RegisterRequest request = new RegisterRequest(
                "inv",
                "",
                "abc",
                "A",
                false);

        ResponseEntity<ErrorResponse> response = (ResponseEntity) ApiRequestHelper.doPost(URI, webClient, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().fieldErrors())
            .extracting(FieldErrorResponse::message)
            .contains(
                "O email informado é inválido.",
                "A senha não pode estar em branco.",
                "O nome deve ter entre 3 e 50 caracteres.",
                "Os termos de uso devem ser aceitos para prosseguir."
            );
    }

    @Test
    @Order(4)
    @DisplayName("Deve falhar ao registrar um usuário com senhas diferentes")
    void testRegisterUser_MultipleErrors_02() {
        RegisterRequest request = new RegisterRequest(
                "luiz@gmail.com",
                "abcdefgh123+",
                "abcdefgh124+",
                "Abc 123",
                true);

        ResponseEntity<ErrorResponse> response = (ResponseEntity) ApiRequestHelper.doPost(URI, webClient, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().error()).contains("Senhas não conferem.");
    }

    @Test
    @Order(5)
    @DisplayName("Deve falhar ao registrar um usuário com senha fraca, sem condições do PasswordPolicy")
    void testRegisterUser_MultipleErrors_03() {
        RegisterRequest request = new RegisterRequest(
                "weakuser@gmail.com",
                "abcdefgh",
                "abcdefgh",
                "Weak User",
                true);

        ResponseEntity<ErrorResponse> response = (ResponseEntity) ApiRequestHelper.doPost(URI, webClient, request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().fieldErrors())
        .extracting(FieldErrorResponse::message)
                .contains("Deve conter pelo menos 1 caractere maiúsculo.")
                .contains("Deve conter pelo menos 1 dígito.")
                .contains("Deve conter pelo menos 1 caractere especial.");
    }
}