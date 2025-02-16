package com.ednaldoluiz.websocket.infra.security.ratelimit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateLimitRouter {

    private final RateLimitProperties properties;

    /**
     * Dado um path, retorna a política de rate-limit (ou a default).
     */
    public RateLimitPolicy resolvePolicy(String path) {
        Optional<RateLimitRule> matched = properties.getRoutes().stream()
            .filter(Objects::nonNull)
            .filter(r -> path.equals(r.path()))
            .findFirst();

        return matched.orElse(properties.getDefaultRule());
    }
}