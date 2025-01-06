package com.ednaldoluiz.websocket.domain.model.base;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Classe base especializada para entidades que 
 * requerem auditoria temporal completa.
 * 
 * Esta classe adiciona suporte para:
 * 
 * - Rastrear a data de criação do registro (`createdAt`).
 * - Rastrear a data da última modificação do registro (`updatedAt`).
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class TimestampedEntityBase extends EntityBase {
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public TimestampedEntityBase(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}
