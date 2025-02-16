package com.ednaldoluiz.websocket.infra.security.ratelimit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Configuration
@PropertySource("classpath:rate-limit.properties") // Lê o arquivo rate-limit.properties
@ConfigurationProperties(prefix = "rate-limits")  // Mapeia as propriedades com prefixo "rate-limits"
public class RateLimitProperties {

    private RateLimitRule defaultRule;  // Regra padrão
    private List<RateLimitRule> routes; // Lista de regras personalizadas

}