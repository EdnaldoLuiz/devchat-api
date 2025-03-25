package com.ednaldoluiz.websocket.v1.shared.base;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ednaldoluiz.websocket.v1.config.TestSecurityConfig;

@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTestBase {
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @BeforeEach
    void setUp() {
        log.info("Iniciando teste: {}", this.getClass().getSimpleName());
    }
}