package com.ednaldoluiz.websocket.v1.architecture.global;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

public class GlobalArchitectureTest extends BaseArchitectureTest {

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
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("App", "Domain", "Infra")
            .whereLayer("Infra").mayOnlyBeAccessedByLayers("App", "Infra")

            .check(importedClasses);
    }

    @Test
    void testClassesShouldOnlyExistInTestPackage() {
        ArchRuleDefinition.noClasses()
                .that().haveSimpleNameContaining("Test")
                .should().resideOutsideOfPackage("..test..")
                .allowEmptyShould(true)
                .check(importedClasses);
    }
}