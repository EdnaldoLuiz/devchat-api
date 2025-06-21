package com.ednaldoluiz.websocket.web.handler.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class OutOfPreKeysException extends RuntimeException {

    public OutOfPreKeysException() {
        super("Usuário está sem pre-keys disponíveis");
    }
}