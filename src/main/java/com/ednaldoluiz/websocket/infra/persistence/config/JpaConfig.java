package com.ednaldoluiz.websocket.infra.persistence.config;

import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.ednaldoluiz.websocket.infra.persistence.repository",
        "com.ednaldoluiz.websocket.infra.persistence.viewrepository"
    },
    repositoryBaseClass = BaseJpaRepositoryImpl.class
)
public class JpaConfig {}