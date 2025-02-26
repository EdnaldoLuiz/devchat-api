package com.ednaldoluiz.websocket.infra.persistence;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.ednaldoluiz.websocket.domain.model.user.PasswordResetToken;

import jakarta.transaction.Transactional;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetToken> {

    @Transactional
    @Modifying
    @Query("""
        DELETE FROM PasswordResetToken t 
        WHERE t.expiresAt < :now 
        AND t.used = false
    """)
    int deleteExpiredUnusedTokens(Instant now);
    
    Optional<PasswordResetToken> findByToken(String tokenValue);

}