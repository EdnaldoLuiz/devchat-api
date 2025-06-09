package com.ednaldoluiz.websocket;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories(
    basePackages = {
        "com.ednaldoluiz.websocket.infra.persistence.repository",
        "com.ednaldoluiz.websocket.infra.persistence.viewrepository"
    }
)
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

    @PostConstruct
    public void onInit() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
    }
}