package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InfraArchitectureTestTest extends BaseArchitectureTest {

    @Test
    void controllersShouldBeInWebControllerPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..web.controller..")
                .check(importedClasses);
    }

    @Test
    void repositoriesShouldBeInInfraPersistencePackage() {
        classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..infra.persistence..")
                .check(importedClasses);
    }
}