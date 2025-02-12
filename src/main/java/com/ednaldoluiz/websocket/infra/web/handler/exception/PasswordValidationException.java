package com.ednaldoluiz.websocket.infra.web.handler.exception;

import com.ednaldoluiz.websocket.infra.web.handler.FieldErrorResponse;

import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordValidationException extends RuntimeException {

    private final List<FieldErrorResponse> errors;

    public PasswordValidationException(List<FieldErrorResponse> errors) {
        super("Erro de validação da senha");
        this.errors = errors;
    }

    public PasswordValidationException(String error) {
        super("As senhas não coincidem.");
        this.errors = List.of(new FieldErrorResponse("password", error));
    }
}
