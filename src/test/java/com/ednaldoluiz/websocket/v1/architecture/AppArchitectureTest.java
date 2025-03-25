package com.ednaldoluiz.websocket.v1.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AppArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .importPackages("com.ednaldoluiz.websocket");
    }

    @Test
    void appLayerShouldOnlyBeAccessedByWebAndInfra() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("App").definedBy("..app..")
                .whereLayer("App").mayOnlyBeAccessedByLayers("Web", "Infra")
                .check(importedClasses);
    }
}