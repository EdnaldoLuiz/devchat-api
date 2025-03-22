package com.ednaldoluiz.websocket.infra.web.handler.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}