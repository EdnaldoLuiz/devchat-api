package com.ednaldoluiz.websocket.domain.model.base;

import java.io.Serializable;

import com.ednaldoluiz.websocket.shared.generator.CustomTsidGenerator;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe base genérica para todas as entidades do sistema.
 * 
 * Contém a Identificação única com ID gerado pelo Snowflake.
 * 
 * Todas as entidades do sistema podem herdar desta classe 
 * para reutilizar a lógica de geração de ID.
 */
@Setter
@Getter
@MappedSuperclass
public abstract class EntityBase implements Serializable {

    @Id
    @Tsid(CustomTsidGenerator.class)
    private Long id;

    protected EntityBase() {}

}