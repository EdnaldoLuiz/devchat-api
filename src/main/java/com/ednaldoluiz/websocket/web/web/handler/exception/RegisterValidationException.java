package com.ednaldoluiz.websocket.web.web.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RegisterValidationException extends RuntimeException {

    public RegisterValidationException(String message) {
        super(message);
    }
}
