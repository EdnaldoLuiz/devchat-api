package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.PasswordResetUseCasePort;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.port.EmailSenderPort;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordResetUseCaseAdapter implements PasswordResetUseCasePort {

    private final EmailSenderPort emailSender;
    //private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // Construtor ...

    @Override
    public void sendPasswordResetEmail(String recipientEmail, String resetToken) {
        // User user = userRepository.findByEmail(recipientEmail)
        //     .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // // Gera token
        // String tokenValue = UUID.randomUUID().toString();
        // Instant expires = Instant.now().plus(30, ChronoUnit.MINUTES);

        // // Salva o token
        // PasswordResetToken token = new PasswordResetToken(tokenValue, user.getId(), expires);
        // tokenRepository.save(token);

        // // Monta link
        // String resetLink = "http://localhost:8080/reset-password?token=" + tokenValue;

        // // Monta email (pode usar template etc.)
        // String subject = "Recuperação de senha";
        // String bodyHtml = """
        //     <p>Olá, clique no link abaixo para redefinir sua senha. Este link expira em 30 minutos:</p>
        //     <p><a href="%s">Redefinir senha</a></p>
        // """.formatted(resetLink);

        // emailSender.sendHtmlEmail(null, recipientEmail, subject, bodyHtml);
    }

    @Override
    public void resetPassword(String tokenValue, String newPassword) {
        // PasswordResetToken token = tokenRepository.findByToken(tokenValue)
        //     .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        // if (token.isExpired()) {
        //     throw new IllegalStateException("Token expirado");
        // }

        // User user = userRepository.findById(token.getUserId())
        //     .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        // user.setPassword(passwordEncoder.encode(newPassword));
        // userRepository.save(user);

        // // Pode invalidar o token
        // tokenRepository.delete(token);
    }
}