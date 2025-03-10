package com.ednaldoluiz.websocket.domain.model.user;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "password_reset_tokens", schema = "websocket")
@NoArgsConstructor
public class PasswordResetToken extends EntityBase {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "key_id", nullable = false, unique = true)
    private String keyId;

    @Column(name = "hashed_token", nullable = false)
    private String hashedToken;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name="created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean used = false;
    
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now()) || used;
    }

    public void markAsUsed() {
        this.used = true;
    }

    public PasswordResetToken(User user, String keyId, String tokenValue) {
        this.user = user;
        this.keyId = keyId;
        this.hashedToken = tokenValue;
        this.expiresAt = LocalDateTime.now().plusMinutes(30);
    }
}