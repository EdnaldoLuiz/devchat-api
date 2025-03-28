package com.ednaldoluiz.websocket;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ednaldoluiz.websocket.v1.config.TestSecurityConfig;

@SpringBootTest
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class WebsocketApplicationTests {

    @Autowired
    private WebsocketApplication application;

	@Test
    void contextLoads() {
        assertThat(application).isNotNull();
    }
}