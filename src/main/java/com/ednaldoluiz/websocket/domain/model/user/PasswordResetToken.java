package com.ednaldoluiz.websocket.domain.model.user;

import java.time.Instant;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

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

    @Column(name="token", nullable = false, unique = true, length = 64)
    private String token;

    @Column(name="expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name="created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private boolean used = false;

    public PasswordResetToken(SnowflakeIdGenerator snowflakeId, User user, String token, Instant expiresAt) {
        super(snowflakeId);
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }
    
    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}