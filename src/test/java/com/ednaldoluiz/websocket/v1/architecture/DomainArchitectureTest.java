package com.ednaldoluiz.websocket.v1.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DomainArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.ednaldoluiz.websocket");
    }

    @Test
    void domainLayerShouldBeIndependent() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat()
                .resideOutsideOfPackages(
                        "..domain..", "java..", "jakarta.persistence..", "org.springframework..",
                        "lombok..", "com.ednaldoluiz.websocket.shared.generator..", "io.hypersistence.utils.hibernate.."
                )
                .check(importedClasses);
    }
}
