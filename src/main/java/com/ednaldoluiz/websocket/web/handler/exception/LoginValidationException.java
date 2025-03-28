package com.ednaldoluiz.websocket.web.handler.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LoginValidationException extends RuntimeException {

    public LoginValidationException(String message) {
        super(message);
    }
}
