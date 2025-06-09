package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.infra.persistence.repository.BaseRepository;
import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InfraArchitectureTest extends BaseArchitectureTest {

    @Test
    void repositoriesShouldBeInInfraPersistencePackage() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..infra.persistence..")
                .check(importedClasses);
    }

    @Test
    void allRepositoriesShouldImplementBaseRepository() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().beAssignableTo(BaseRepository.class)
                .check(importedClasses);
    }
}