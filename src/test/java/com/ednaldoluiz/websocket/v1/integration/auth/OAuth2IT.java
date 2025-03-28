package com.ednaldoluiz.websocket.v1.integration.auth;

import com.ednaldoluiz.websocket.v1.config.TestContainerDatabaseConfig;
import com.ednaldoluiz.websocket.v1.shared.base.AbstractAuthTest;
import com.ednaldoluiz.websocket.v1.shared.helpers.ApiRequestHelper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.domain.model.user.AuthProvider;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.route.Paths;

import reactor.netty.http.client.HttpClient;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Objects;

@Tag("auth")
@SuppressWarnings({"null", "unchecked"})
@ExtendWith({TestContainerDatabaseConfig.class})
public class OAuth2IT extends AbstractAuthTest {

    @Order(1)
    @ParameterizedTest(name = "[{index}] Redirecionamento para provider {0}")
    @EnumSource(value = AuthProvider.class, names = { "GITHUB", "GOOGLE" })
    @DisplayName("Deve redirecionar para /oauth2/authorization/{provider} ao chamar /login/{provider}")
    void testRedirectToProvider(AuthProvider provider) {

        WebTestClient clientNoRedirect = webClient.mutate()
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create().followRedirect(false)))
                .build();

        String url = Paths.V1.Auth.AUTH + "/oauth2/login/" + provider.name().toLowerCase();

        ResponseEntity<?> response = ApiRequestHelper.doGet(url, clientNoRedirect, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);

        String location = Objects.requireNonNull(response.getHeaders().getLocation()).toString();
        URI uri = java.net.URI.create(location);

        assertThat(uri.getPath()).isEqualTo("/oauth2/authorization/" + provider.name().toLowerCase());
    }

    @Order(2)
    @ParameterizedTest(name = "[{index}] Testando OAuth2 com Provider = {0}")
    @EnumSource(value = AuthProvider.class, names = { "GITHUB", "GOOGLE" })
    @DisplayName("Deve falhar ao tentar fazer login normal (email/senha) em conta criada via OAuth2 (sem senha)")
    void testFailEmailPasswordLoginWithOAuthAccount(AuthProvider provider) {

        User user = new User(
                provider.name().toLowerCase() + "_user@example.coms",
                "OAuth User",
                "",
                "",
                provider);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "sbduidlivei123+");
        String URI = Paths.V1.Auth.AUTH + "/oauth2";

        ResponseEntity<ErrorResponse> response = (ResponseEntity<ErrorResponse>) 
        ApiRequestHelper.doPost(URI, webClient, loginRequest, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}