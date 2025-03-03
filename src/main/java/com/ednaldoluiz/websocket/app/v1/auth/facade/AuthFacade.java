package com.ednaldoluiz.websocket.app.v1.auth.facade;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.GeneratePasswordUseCase;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.LoginUseCase;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.PasswordResetUseCase;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.RegisterUseCase;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.SendResetTokenUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFacade {
    
    private final LoginUseCase loginUseCase;
    private final RegisterUseCase registerUseCase;
    private final GeneratePasswordUseCase generatePasswordUseCase;
    private final SendResetTokenUseCase sendResetTokenUseCase;
    private final PasswordResetUseCase passwordResetUseCase;

    public LoginResponse login(LoginRequest request) {
        log.info("Facade: login do usuário {}", request.email());
        return loginUseCase.execute(request);
    }

    public RegisterResponse register(RegisterRequest request) {
        log.info("Facade: registrando usuário {}", request.email());
        return registerUseCase.execute(request);
    }

    public GeneratedPasswordResponse generateStrongPassword() {
        log.info("Facade: gerando senha forte");
        return generatePasswordUseCase.execute();
    }

    public void forgotPassword(String email) {
        log.info("Facade: enviando e-mail de redefinição de senha para {}", email);
        sendResetTokenUseCase.execute(email);
    }

    public void resetPassword(ResetPasswordRequest request) {
        log.info("Facade: resetando senha do token {}", request.token());
        passwordResetUseCase.execute(request);
    }
}