package com.ednaldoluiz.websocket.web.web.handler.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}