package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;

@NoRepositoryBean
public interface BaseRepository<T extends EntityBase> extends JpaRepository<T, Long> {}