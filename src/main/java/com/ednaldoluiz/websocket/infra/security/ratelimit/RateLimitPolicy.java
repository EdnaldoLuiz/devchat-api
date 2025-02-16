package com.ednaldoluiz.websocket.infra.security.ratelimit;

public interface RateLimitPolicy {

    String path();

    int capacity();

    int refillInterval();
    
}