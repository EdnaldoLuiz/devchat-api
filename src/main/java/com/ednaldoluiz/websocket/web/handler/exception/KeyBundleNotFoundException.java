package com.ednaldoluiz.websocket.web.handler.exception;

public class KeyBundleNotFoundException extends RuntimeException {
    public KeyBundleNotFoundException(Long userId) {
        super("Key bundle não encontrado para user " + userId);
    }
}