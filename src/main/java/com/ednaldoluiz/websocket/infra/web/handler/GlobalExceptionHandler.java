package com.ednaldoluiz.websocket.infra.web.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ednaldoluiz.websocket.infra.web.handler.exception.LoginValidationException;
import com.ednaldoluiz.websocket.infra.web.handler.exception.PasswordValidationException;
import com.ednaldoluiz.websocket.infra.web.handler.exception.RateLimitException;
import com.ednaldoluiz.websocket.infra.web.handler.exception.RegisterValidationException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RegisterValidationException.class, LoginValidationException.class})
    public ResponseEntity<ErrorResponse> handleRegisterValidationException(
            RuntimeException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação com as credenciais do usuário",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

     @ExceptionHandler(PasswordValidationException.class)
    public ResponseEntity<ErrorResponse> handlePasswordValidationException(
            PasswordValidationException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação da senha",
                HttpStatus.BAD_REQUEST.value(),
                "Erros de validação da senha encontrados.",
                request.getRequestURI(),
                ex.getErrors());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitException(
            RateLimitException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage() + ". Tente novamente em " + ex.getRetryAfterSeconds() + " segundos.",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Limite de requisições excedido",
                ex.getPath()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.TOO_MANY_REQUESTS);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        List<FieldErrorResponse> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Error",
                HttpStatus.BAD_REQUEST.value(),
                "Erros de validação encontrados.",
                request.getDescription(false),
                fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}