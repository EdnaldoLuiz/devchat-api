package com.ednaldoluiz.websocket.v1.config;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@TestConfiguration
public class TestContainerDatabaseConfig implements BeforeAllCallback {

    private static boolean started = false;

    @SuppressWarnings("resource")
    private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.30"))
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("test")
            .withUrlParam("useTimezone", "true")
            .withUrlParam("serverTimezone", "America/Sao_Paulo")
            .withUrlParam("useSSL", "false")
            .withUrlParam("allowPublicKeyRetrieval", "true")
            .withReuse(true)
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("mysql-container")))
            .waitingFor(Wait.forLogMessage(".* ------------- ready for connections ------------- *", 2))
            .withCommand("mysqld --log-bin-trust-function-creators=1")
            .withStartupAttempts(3)
            .withStartupTimeout(Duration.ofMinutes(5))
            .withInitScript("db/sql/init.sql");

    @Override
    public void beforeAll(ExtensionContext context) {
        if (!started) {
            MYSQL_CONTAINER.start();
            System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
            System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
            System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
            System.setProperty("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");

            Flyway.configure()
                    .dataSource(MYSQL_CONTAINER.getJdbcUrl(), MYSQL_CONTAINER.getUsername(),
                            MYSQL_CONTAINER.getPassword())
                    .load()
                    .migrate();

            started = true;
        }
    }
}
