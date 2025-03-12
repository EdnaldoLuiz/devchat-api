package com.ednaldoluiz.websocket.v1.acceptance.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;

import com.ednaldoluiz.websocket.v1.integration.base.AbstractAuthIT;
import com.ednaldoluiz.websocket.v1.integration.config.TestContainerDatabaseConfig;

@CucumberContextConfiguration
@Import({ TestContainerDatabaseConfig.class})
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = { "pretty", "json:target/cucumber-report.json" },
        features = "src/test/resources/features/auth",
        glue = {
                "com.ednaldoluiz.websocket.v1.acceptance.cucumber.auth.steps",
        },
        tags = "@registro_sucesso or @registro_email_duplicado or @registro_senhas_nao_coincidem or @registro_senha_fraca"
)
public class AuthCucumberIT extends AbstractAuthIT { }