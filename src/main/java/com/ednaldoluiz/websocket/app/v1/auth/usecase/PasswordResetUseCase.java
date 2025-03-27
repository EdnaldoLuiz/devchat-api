package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.web.web.handler.exception.InvalidTokenException;
import com.ednaldoluiz.websocket.web.web.handler.exception.MismatchedPasswordsException;
import com.ednaldoluiz.websocket.web.web.handler.exception.ResetPasswordUserNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordResetUseCase {

    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public void execute(ResetPasswordRequest request) {
        log.info("Redefinindo senha para algum usuário.");

        if (!request.password().equals(request.confirmPassword())) {
            throw new MismatchedPasswordsException();
        }
        
        PasswordResetToken resetToken = tokenRepository.findByKeyId(request.key())
            .orElseThrow(() -> new InvalidTokenException("Token inválido ou expirado."));

        User user = userRepository.findById(resetToken.getUser().getId())
            .orElseThrow(ResetPasswordUserNotFoundException::new);

        if (resetToken.isExpired()) {
            throw new InvalidTokenException("Token expirado ou já utilizado.");
        }

        if (!passwordEncoder.matches(request.token(), resetToken.getHashedToken())) {
            throw new InvalidTokenException("Token inválido.");
        }

        user.setPassword(passwordEncoder.encode(request.password()).toCharArray());
        userRepository.save(user);

        resetToken.markAsUsed();
        tokenRepository.save(resetToken);
        log.info("Senha redefinida com sucesso para o usuário: {}", user.getEmail());
    }
}