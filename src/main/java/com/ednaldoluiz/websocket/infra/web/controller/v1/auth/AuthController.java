package com.ednaldoluiz.websocket.infra.web.controller.v1.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LogoutResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.GeneratePasswordUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.LoginUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.PasswordResetUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final RegisterUserUseCasePort registerPort;
    private final LoginUseCasePort loginPort;
    private final GeneratePasswordUseCasePort generatePasswordUseCase;
    private final PasswordResetUseCasePort passwordResetUseCasePort;

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {
        RegisterResponse response = registerPort.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        LoginResponse response = loginPort.login(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GeneratedPasswordResponse> generateStrongPassword() {
        GeneratedPasswordResponse password = generatePasswordUseCase.generateStrongPassword();
        return ResponseEntity.ok(password);
    }

    @Override
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(LogoutResponse.success());
    }

    @PostMapping
    public ResponseEntity<String> testEmail(@RequestParam String email, @RequestParam String token) {
        passwordResetUseCasePort.sendPasswordResetEmail(email, token);
        return ResponseEntity.ok("E-mail de teste enviado para " + email);
    }
}