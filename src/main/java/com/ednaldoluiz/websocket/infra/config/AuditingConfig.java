package com.ednaldoluiz.websocket.infra.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing()
public class AuditingConfig {

    // /**
    //  * Fornece o ID do usuário autenticado.
    //  *
    //  * @return Optional contendo o ID do usuário.
    //  */
    // public AuditorAware<Long> auditorProvider() {
    //     return () -> {
    //         return Optional.of(1L);
    //     };
    // }
}
