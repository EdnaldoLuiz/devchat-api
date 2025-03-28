package com.ednaldoluiz.websocket.web.handler.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {

    private long retryAfterSeconds;
    private String path;

    public RateLimitException(String message, long retryAfterSeconds, String path) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
        this.path = path;
    }

    public RateLimitException(String message) {
        super(message);
    }
}
