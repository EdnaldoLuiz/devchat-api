package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;

/**
 * Repositório base para TODAS as entidades que herdam de EntityBase.
 * Aqui declaramos métodos comuns como findAll e deleteAll.
 */
@NoRepositoryBean
public interface BaseRepository<T extends EntityBase> extends BaseJpaRepository<T, Long> {

    /**
     * Busca todas as entidades do tipo T.
     */
    @Query("SELECT e FROM #{#entityName} e")
    List<T> findAll();

    /**
     * Remove todos os registros da entidade T.
     * É @Modifying + @Transactional porque altera o banco.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM #{#entityName} e")
    void deleteAll();

}
