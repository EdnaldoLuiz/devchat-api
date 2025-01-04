package com.ednaldoluiz.websocket.domain.model.base;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Classe base genérica para todas as entidades do sistema.
 * 
 * Contém os campos e lógica comum para:
 * - Identificação única com ID gerado pelo Snowflake.
 * - Auditoria de criação e modificação de registros (datas).
 * 
 * Todas as entidades do sistema podem herdar desta classe para reutilizar
 * a lógica de geração de ID e auditoria temporal.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntityBase {

    @Id
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected EntityBase(SnowflakeIdGenerator idGenerator) {
        this.id = idGenerator.generateId();
    }

    protected EntityBase() {}
    
}
