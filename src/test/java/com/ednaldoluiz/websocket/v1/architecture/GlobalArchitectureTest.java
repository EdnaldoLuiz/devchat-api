package com.ednaldoluiz.websocket.v1.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
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
}