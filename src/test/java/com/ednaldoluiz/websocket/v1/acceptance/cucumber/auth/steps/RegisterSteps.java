package com.ednaldoluiz.websocket.v1.acceptance.cucumber.auth.steps;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.extension.ExtendWith;

import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Quando;
import io.cucumber.java.pt.Então;
import io.cucumber.spring.CucumberContextConfiguration;
import reactor.core.publisher.Mono;

import org.springframework.http.*;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.v1.acceptance.cucumber.AuthCucumberIT;
import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;
import com.ednaldoluiz.websocket.infra.web.route.Paths;

@CucumberContextConfiguration
@ExtendWith({TestContainerDatabaseConfig.class})
public class RegisterSteps extends AuthCucumberIT {

    private ResponseEntity<?> response;
    private RegisterRequest registerRequest;

    @Dado("que eu limpei o banco de dados para garantir um estado inicial")
    public void limparBanco() {
        userRepository.deleteAll();
    }

    @Dado("que existe um payload de registro válido")
    public void payloadValido() {
        registerRequest = new RegisterRequest(
                "test@example.com",
                "SenhaForte123!",
                "SenhaForte123!",
                "Teste da Silva",
                true
        );
    }

    @Dado("que existe um usuario pré-cadastrado com email {string}")
    public void usuarioPreCadastrado(String email) {
        var existingRequest = new RegisterRequest(
                email,
                "SenhaForte123!",
                "SenhaForte123!",
                "Usuário Existente",
                true
        );
        insertUserIntoDatabase(existingRequest);
    }

    @Dado("que existe um payload de registro com o mesmo email")
    public void payloadComEmailExistente() {
        registerRequest = new RegisterRequest(
                "existinguser@example.com",
                "NovaSenhaForte123!",
                "NovaSenhaForte123!",
                "Novo Usuário",
                true
        );
    }

    @Dado("que existe um payload de registro com senhas diferentes")
    public void payloadSenhasDiferentes() {
        registerRequest = new RegisterRequest(
                "luiz@gmail.com",
                "abcdefgh123+",
                "abcdefgh124+",
                "Abc 123",
                true
        );
    }

    @Dado("que existe um payload de registro com a senha {string}")
    public void payloadSenhaFracaComExemplos(String senha) {
        registerRequest = new RegisterRequest(
            "usuario@exemplo.com",
            senha,
            senha,
            "Usuario Exemplo",
            true
        );
    }

    @Quando("eu envio uma requisição de registro")
    public void envioRequisicaoRegistro() {
        String uri = Paths.V1.Auth.AUTH + Paths.Auth.REGISTER;
        Mono<ResponseEntity<RegisterResponse>> responseMono = webClient.post()
            .uri(uri)
            .bodyValue(registerRequest)
            .retrieve()
            .toEntity(RegisterResponse.class)
            .onErrorResume(e -> {
                log.error("Erro ao registrar usuário: {}", e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(null));
            });

        response = responseMono.block(); 
    }

    @Então("a resposta deve ser de sucesso com status {int}")
    public void respostaSucesso(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Então("deve retornar o email do novo usuário e seus tokens")
    public void verificarEmailETokens() {
        RegisterResponse body = (RegisterResponse) response.getBody();
        assertNotNull(body, "O corpo da resposta não pode ser nulo");
        assertEquals(registerRequest.email(), body.email(), "O email retornado não corresponde ao esperado");
        assertNotNull(body.tokens(), "Os tokens não podem ser nulos");
        assertEquals(2, body.tokens().size(), "Deve haver exatamente dois tokens retornados");
    }

    @Então("a resposta deve retornar status {int}")
    public void respostaStatus(int status) {
        assertEquals(status, response.getStatusCode());
    }

    @Então("deve conter a mensagem {string}")
    public void verificarMensagem(String mensagem) {
        assertNotNull(response.getBody(), "A resposta não pode ser nula");
        String responseAsString = Objects.requireNonNull(response.getBody()).toString();
        assertTrue(responseAsString.contains(mensagem), "A resposta não contém a mensagem esperada: " + mensagem);
    }
}