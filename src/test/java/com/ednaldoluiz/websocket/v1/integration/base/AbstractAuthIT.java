package com.ednaldoluiz.websocket.v1.integration.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.v1.integration.config.TestSecurityConfig;

import io.netty.handler.logging.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAuthIT {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected WebClient webClient;

    @Autowired
    public void setWebClient(@LocalServerPort int port) {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .wiretap("reactor.netty.http.client.HttpClient",
                                        LogLevel.INFO,
                                        AdvancedByteBufFormat.TEXTUAL)))
                .build();
    }

    protected User insertUserIntoDatabase(RegisterRequest request) {
        log.info("Inserindo usuário no banco: {} pela classe {}", request.email(), this.getClass().getSimpleName());
        User userEntity = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name());
        userRepository.save(userEntity);
        return userEntity;
    }

    protected <T, R> ResponseEntity<T> doPost(String uri, WebClient webClient, R requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .body(Mono.just(requestBody), (Class<R>) requestBody.getClass())
                .exchangeToMono(response -> response.bodyToMono(responseType)
                        .map(body -> new ResponseEntity<>(body, response.statusCode())))
                .block();
    }
}