package com.ednaldoluiz.websocket.web.handler;

import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.handler.error.FieldErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

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

import com.ednaldoluiz.websocket.web.handler.exception.BusinessException;
import com.ednaldoluiz.websocket.web.handler.exception.EmailNotRegisteredException;
import com.ednaldoluiz.websocket.web.handler.exception.InvalidTokenException;
import com.ednaldoluiz.websocket.web.handler.exception.LoginValidationException;
import com.ednaldoluiz.websocket.web.handler.exception.MismatchedPasswordsException;
import com.ednaldoluiz.websocket.web.handler.exception.PasswordValidationException;
import com.ednaldoluiz.websocket.web.handler.exception.RegisterValidationException;
import com.ednaldoluiz.websocket.web.handler.exception.ResetPasswordUserNotFoundException;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred", ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de negócio",
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                request.getRequestURI());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler({RegisterValidationException.class, LoginValidationException.class, InvalidTokenException.class, 
        MismatchedPasswordsException.class, ResetPasswordUserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleRegisterValidationException(
            RuntimeException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação com as credenciais do usuário",
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailNotRegisteredException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            RuntimeException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de validação com as credenciais do usuário",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
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

    @ExceptionHandler({RequestNotPermitted.class})
    public ResponseEntity<ErrorResponse> handleRateLimitException(
        RequestNotPermitted ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                "Erro de limite de requisições",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                "Muitas requisições. Tente mais tarde.",
                request.getRequestURI());

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
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Error",
                HttpStatus.BAD_REQUEST.value(),
                "Erros de validação encontrados.",
                request.getDescription(false),
                fieldErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}