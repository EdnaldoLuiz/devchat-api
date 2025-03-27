package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import org.junit.jupiter.api.Test;

public class AppArchitectureTest extends BaseArchitectureTest {

    @Test
    void useCasesShouldResideInAppPackage() {
        classes()
                .that().haveSimpleNameEndingWith("UseCase")
                .or().haveSimpleNameEndingWith("Facade")
                .should().resideInAPackage("..app..")
                .check(importedClasses);
    }
}