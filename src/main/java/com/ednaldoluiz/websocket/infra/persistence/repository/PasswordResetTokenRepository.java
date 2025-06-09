package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.time.LocalDateTime;
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
    int deleteExpiredUnusedTokens(LocalDateTime now);
    
    Optional<PasswordResetToken> findByKeyId(String key);

    @Query("SELECT t FROM PasswordResetToken t JOIN FETCH t.user WHERE t.id = :id")
    Optional<PasswordResetToken> findByIdWithUser(Long id);

}