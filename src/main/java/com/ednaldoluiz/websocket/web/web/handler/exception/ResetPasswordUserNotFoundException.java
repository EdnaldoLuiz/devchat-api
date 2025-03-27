package com.ednaldoluiz.websocket.web.web.handler.exception;

public class ResetPasswordUserNotFoundException extends RuntimeException {
    public ResetPasswordUserNotFoundException() {
        super("Usuário não encontrado para reset de senha.");
    }
}