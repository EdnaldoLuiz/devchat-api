package com.ednaldoluiz.websocket.v1.architecture;

import com.ednaldoluiz.websocket.v1.shared.base.AbstractTestBase;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;

public abstract class BaseArchitectureTest extends AbstractTestBase {

    protected static JavaClasses importedClasses;

    @BeforeAll
    static void init() {
        importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.ednaldoluiz.websocket");
    }
}
