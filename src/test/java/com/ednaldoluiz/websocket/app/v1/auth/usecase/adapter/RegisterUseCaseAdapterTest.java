package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Sql(scripts = "/db/migration/schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class RegisterUseCaseAdapterTest {

    @Autowired
    private RegisterUserUseCasePort registerUserUseCasePort;

    @Test
    @DisplayName("Deve registrar um usuário com sucesso.")
    public void testRegister_Success() {
        RegisterRequest request = new RegisterRequest(
            "usuario@teste.com",
            "SenhaForte123!",
            "SenhaForte123!",
            "João Silva",
            "11987654321",
            false
        );

        RegisterResponse response = assertDoesNotThrow(() -> registerUserUseCasePort.register(request));

        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("usuario@teste.com");
        assertThat(response.tokens()).isNotEmpty();
        assertThat(response.tokens().size()).isEqualTo(2);
    }
}
