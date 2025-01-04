package com.ednaldoluiz.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class WebsocketApplication {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

    @PostConstruct
    public void logDatabaseConnection() {
        System.out.println("Conectando ao banco de dados em: " + env.getProperty("spring.datasource.url"));
    }
}
