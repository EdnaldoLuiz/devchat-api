package com.ednaldoluiz.websocket.web.web.handler.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotRegisteredException extends RuntimeException {

    public EmailNotRegisteredException(String message) {
        super(message);
    }
}