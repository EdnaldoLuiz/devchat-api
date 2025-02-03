package com.ednaldoluiz.websocket.infra.web.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldErrorResponse> fieldErrors, // Lista de erros detalhados
    String cause,
    StackTraceElement[] stackTrace
) {
    public ErrorResponse(String message, int status, String error, String path) {
        this(LocalDateTime.now(), status, error, message, path, null, null, null);
    }

    public ErrorResponse(String message, int status, String error, String path, Throwable ex) {
        this(LocalDateTime.now(), status, error, message, path, null, ex != null ? ex.getMessage() : null, ex != null ? ex.getStackTrace() : null);
    }

    public ErrorResponse(String message, int status, String error, String path, List<FieldErrorResponse> fieldErrors) {
        this(LocalDateTime.now(), status, error, message, path, fieldErrors, null, null);
    }
}
