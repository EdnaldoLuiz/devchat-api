package com.ednaldoluiz.websocket.v1.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GlobalArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.ednaldoluiz.websocket");
    }

    @Test
    void shouldNotHaveCycles() {
        SlicesRuleDefinition.slices()
                .matching("com.ednaldoluiz.websocket.(**)..")
                .should().beFreeOfCycles()
                .check(importedClasses);
    }

    @Test
    void shouldRespectLayers() {
        Architectures.layeredArchitecture()
            .consideringAllDependencies()

            .layer("Web").definedBy("..web..")
            .layer("App").definedBy("..app..")
            .layer("Domain").definedBy("..domain..")
            .layer("Infra").definedBy("..infra..")

            .whereLayer("Web").mayNotBeAccessedByAnyLayer()
            .whereLayer("App").mayOnlyBeAccessedByLayers("Web")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("App", "Domain")
            .whereLayer("Infra").mayOnlyBeAccessedByLayers("App", "Infra")

            .check(importedClasses);
    }
}