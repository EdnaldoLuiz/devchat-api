package com.ednaldoluiz.websocket.v1.shared.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.handler.logging.LogLevel;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

public abstract class AbstractApiTest extends AbstractTestBase {

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
}