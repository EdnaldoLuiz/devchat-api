package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.ResetPasswordRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.PasswordResetUseCasePort;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.port.EmailSenderPort;
import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

@Component
@RequiredArgsConstructor
public class PasswordResetUseCaseAdapter implements PasswordResetUseCasePort {

    private final EmailSenderPort emailSender;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SnowflakeIdGenerator snowflakeId;

    @Override
    @Transactional
    public void sendPasswordResetEmail(String recipientEmail) {

        userRepository.findByEmail(recipientEmail)
                .ifPresent(user -> {
                    String tokenValue = UUID.randomUUID().toString();
                    Instant expires = Instant.now().plus(30, ChronoUnit.MINUTES);

                    PasswordResetToken tokenEntity = new PasswordResetToken(
                            snowflakeId, user, tokenValue, expires
                    );

                    tokenRepository.save(tokenEntity);

                    String encodedToken = URLEncoder.encode(tokenValue, StandardCharsets.UTF_8);
                    String resetLink = "http://localhost:3000/reset-password?token=" + encodedToken;
                    String subject = "Recuperação de senha";
                    String bodyHtml = "<p>Olá, clique no link abaixo para redefinir sua senha. Ele expira em 30 minutos:</p>\n"
                            + "<p><a href=\"" + resetLink + "\">Redefinir senha</a></p>"
                            + "<p>Se o link acima não funcionar, copie e cole este URL no seu navegador:</p>"
                            + "<p>" + resetLink + "</p>";

                    emailSender.sendEmail(null, recipientEmail, subject, bodyHtml);
                });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalStateException("Token expirado");
        }

        if (token.isUsed()) {
            throw new IllegalStateException("Token já utilizado");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Senhas não conferem");
        }

        User user = userRepository.findById(token.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        user.setPassword(passwordEncoder.encode(request.password()).toCharArray());
        userRepository.save(user);
        tokenRepository.delete(token);
    }
}