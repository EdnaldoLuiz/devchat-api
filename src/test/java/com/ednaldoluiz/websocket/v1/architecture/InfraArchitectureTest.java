package com.ednaldoluiz.websocket.v1.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InfraArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.ednaldoluiz.websocket");
    }

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