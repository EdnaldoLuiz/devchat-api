package com.ednaldoluiz.websocket.v1.bdd;

import io.cucumber.spring.CucumberContextConfiguration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;
import com.ednaldoluiz.websocket.v1.integration.config.TestSecurityConfig;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import({
    TestSecurityConfig.class,
    TestContainerDatabaseConfig.class
})
@ActiveProfiles("test")
public class CucumberSpringConfiguration {}