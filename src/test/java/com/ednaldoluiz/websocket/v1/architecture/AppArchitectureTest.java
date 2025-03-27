package com.ednaldoluiz.websocket.v1.architecture;

import com.ednaldoluiz.websocket.v1.shared.base.AbstractTestBase;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppArchitectureTest extends AbstractTestBase {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.ednaldoluiz.websocket");
    }

    @Test
    void appLayerShouldOnlyBeAccessedByWebAndInfra() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("App").definedBy("..app..")
                .layer("Infra").definedBy("..infra..")
                .whereLayer("App").mayOnlyBeAccessedByLayers("Infra")
                .check(importedClasses);
    }
}