package com.ednaldoluiz.websocket.infra.web.handler.exception;

public class MismatchedPasswordsException extends RuntimeException {
    public MismatchedPasswordsException() {
        super("Senhas não conferem.");
    }
}