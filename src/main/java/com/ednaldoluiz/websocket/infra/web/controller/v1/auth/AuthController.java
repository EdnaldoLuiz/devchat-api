package com.ednaldoluiz.websocket.infra.web.controller.v1.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LogoutResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.GeneratePasswordUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.LoginUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.PasswordResetUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;
import com.ednaldoluiz.websocket.infra.web.controller.common.GenericApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final RegisterUserUseCasePort registerPort;
    private final LoginUseCasePort loginPort;
    private final GeneratePasswordUseCasePort generatePasswordUseCasePort;
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
        GeneratedPasswordResponse password = generatePasswordUseCasePort.generateStrongPassword();
        return ResponseEntity.ok(password);
    }

    @Override
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(LogoutResponse.success());
    }

    @Override
    public ResponseEntity<GenericApiResponse> forgotPassword(String email) {
        passwordResetUseCasePort.sendPasswordResetEmail(email);
        return ResponseEntity.ok(new GenericApiResponse("E-mail de redefinição de senha enviado com sucesso"));
    }

    @Override
    public ResponseEntity<GenericApiResponse> resetPassword(ResetPasswordRequest request) {
        passwordResetUseCasePort.resetPassword(request);
        return ResponseEntity.ok(new GenericApiResponse("Senha redefinida com sucesso"));
    }
}