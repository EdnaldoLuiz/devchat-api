package com.ednaldoluiz.websocket.infra.web.handler.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {

    private final long retryAfterSeconds;
    private final String path;

    public RateLimitException(String message, long retryAfterSeconds, String path) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
        this.path = path;
    }
}
