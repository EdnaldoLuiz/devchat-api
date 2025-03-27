package com.ednaldoluiz.websocket.infra.web.controller.v1.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.facade.AuthFacade;
import com.ednaldoluiz.websocket.infra.web.controller.common.GenericApiResponse;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthFacade authFacade;

    @Override
    public ResponseEntity<RegisterResponse> register(RegisterRequest request) {
        RegisterResponse response = authFacade.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        LoginResponse response = authFacade.login(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GeneratedPasswordResponse> generateStrongPassword() {
        GeneratedPasswordResponse password = authFacade.generateStrongPassword();
        return ResponseEntity.ok(password);
    }

    @Override
    public ResponseEntity<GenericApiResponse> logout() {
        return ResponseEntity.ok(new GenericApiResponse("Logout realizado com sucesso."));
    }

    @Override
    public ResponseEntity<GenericApiResponse> forgotPassword(String email) {
        authFacade.forgotPassword(email);
        return ResponseEntity.ok(new GenericApiResponse("E-mail de redefinição de senha enviado com sucesso"));
    }

    @Override
    public ResponseEntity<GenericApiResponse> resetPassword(ResetPasswordRequest request) {
        authFacade.resetPassword(request);
        return ResponseEntity.ok(new GenericApiResponse("Senha redefinida com sucesso"));
    }
}