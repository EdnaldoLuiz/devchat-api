package com.ednaldoluiz.websocket.web.handler.exception;

public class MismatchedPasswordsException extends RuntimeException {
    public MismatchedPasswordsException() {
        super("Senhas não conferem.");
    }
}