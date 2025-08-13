package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.port.EmailPort;
import com.ednaldoluiz.websocket.infra.aws.ses.EmailTemplateService;
import com.ednaldoluiz.websocket.infra.persistence.repository.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.web.handler.exception.EmailNotRegisteredException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendResetTokenUseCase {

    private final UserRepository userRepository;
    private final EmailPort emailSender;
    private final EmailTemplateService emailTemplateService;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void execute(String recipientEmail) {
        log.info("Solicitação de redefinição de senha para o e-mail: {}", recipientEmail);
        User user = userRepository.findByEmail(recipientEmail)
                .orElseThrow(() -> new EmailNotRegisteredException("O e-mail informado não está cadastrado."));

        String rawToken = UUID.randomUUID().toString();
        PasswordResetToken tokenEntity = generatePasswordResetToken(user, rawToken);
        tokenRepository.persist(tokenEntity);

        String resetLink = generateResetLink(tokenEntity.getKeyId(), rawToken);
        sendResetEmail(user.getEmail(), user.getName(), resetLink);
        log.info("Token de redefinição gerado e enviado para {}", recipientEmail);
    }

    private PasswordResetToken generatePasswordResetToken(User user, String rawToken) {
        String keyId = UUID.randomUUID().toString();
        String hashedToken = passwordEncoder.encode(rawToken);

        return new PasswordResetToken(user, keyId, hashedToken);
    }

    private String generateResetLink(String keyId, String rawToken) {
        String encodedKey = URLEncoder.encode(keyId, StandardCharsets.UTF_8);
        String encodedToken = URLEncoder.encode(rawToken, StandardCharsets.UTF_8);

        return String.format("http://localhost:3000/reset-password?k=%s&t=%s", encodedKey, encodedToken);
    }

    private void sendResetEmail(String email, String name, String resetLink) {
        String bodyHtml = emailTemplateService.buildPasswordResetEmail(resetLink, name);
        emailSender.sendEmail(null, email, "Recuperação de senha", bodyHtml);
    }
}