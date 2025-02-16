package com.ednaldoluiz.websocket.infra.security.ratelimit;

public record RateLimitRule(
        String path,
        int capacity,
        int refillInterval
) implements RateLimitPolicy {}