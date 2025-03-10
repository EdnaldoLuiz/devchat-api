package com.ednaldoluiz.websocket.v1.integration.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.*;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.infra.web.route.Paths;
import com.ednaldoluiz.websocket.v1.integration.base.AbstractAuthIT;

@Tag("auth")
@SuppressWarnings("null")
@ExtendWith({TestContainerDatabaseConfig.class})
class RegisterUserIT extends AbstractAuthIT {

    private final String URI = Paths.V1.Auth.AUTH + Paths.Auth.REGISTER;

    @Test
    @DisplayName("Deve registrar um usuário com sucesso")
    void testRegisterUser_Successful() {

        RegisterRequest request = new RegisterRequest(
                "test@example.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Test User",
                true);

        ResponseEntity<RegisterResponse> response = doPost(URI, webClient, request, RegisterResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("test@example.com");
        assertThat(response.getBody().tokens()).hasSize(2);
    }

    @Test
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

        ResponseEntity<String> response = doPost(URI, webClient, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("Já existe um usuário com este email.");
    }

    @Test
    @DisplayName("Deve falhar ao registrar um usuário com vários erros simultâneos")
    void testRegisterUser_MultipleErrors_01() {
        RegisterRequest request = new RegisterRequest(
                "inv",
                "",
                "abc",
                "A",
                false);

        ResponseEntity<String> response = doPost(URI, webClient, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("O email informado é inválido.")
                .contains("A senha não pode estar em branco.")
                .contains("O nome deve ter entre 3 e 50 caracteres.")
                .contains("Os termos de uso devem ser aceitos para prosseguir.");
    }

    @Test
    @DisplayName("Deve falhar ao registrar um usuário com senhas diferentes")
    void testRegisterUser_MultipleErrors_02() {
        RegisterRequest request = new RegisterRequest(
                "luiz@gmail.com",
                "abcdefgh123+",
                "abcdefgh124+",
                "Abc 123",
                true);

        ResponseEntity<String> response = doPost(URI, webClient, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("As senhas não coincidem.");
    }

    @Test
    @DisplayName("Deve falhar ao registrar um usuário com senha fraca, sem condições do PasswordPolicy")
    void testRegisterUser_MultipleErrors_03() {
        RegisterRequest request = new RegisterRequest(
                "weakuser@gmail.com",
                "abcdefgh",
                "abcdefgh",
                "Weak User",
                true);

        ResponseEntity<String> response = doPost(URI, webClient, request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody())
                .contains("Deve conter pelo menos 1 caractere maiúsculo.")
                .contains("Deve conter pelo menos 1 dígito.")
                .contains("Deve conter pelo menos 1 caractere especial.");
    }
}