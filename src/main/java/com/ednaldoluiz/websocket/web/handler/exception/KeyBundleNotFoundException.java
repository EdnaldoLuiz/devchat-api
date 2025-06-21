package com.ednaldoluiz.websocket.web.handler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class KeyBundleNotFoundException extends RuntimeException {

    public KeyBundleNotFoundException(Long userId) {
        super("Key bundle não encontrado para user " + userId);
    }
}