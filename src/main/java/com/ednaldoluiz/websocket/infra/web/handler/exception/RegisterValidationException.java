package com.ednaldoluiz.websocket.infra.web.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterValidationException extends RuntimeException {

    public RegisterValidationException(String message) {
        super(message);
    }
}
