package com.ednaldoluiz.websocket.domain.model.base;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Classe base especializada para entidades que requerem auditoria completa.
 * 
 * Além dos campos de auditoria temporal herdados de {@link TimestampedEntityBase}, 
 * esta classe adiciona suporte para:
 * 
 * - Rastrear o usuário que criou o registro (`createdBy`).
 * - Rastrear o usuário que modificou o registro (`updatedBy`).
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntityBase extends TimestampedEntityBase {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

}