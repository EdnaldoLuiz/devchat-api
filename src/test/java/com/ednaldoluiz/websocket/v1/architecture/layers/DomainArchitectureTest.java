package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import org.junit.jupiter.api.Test;

public class DomainArchitectureTest extends BaseArchitectureTest {

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
