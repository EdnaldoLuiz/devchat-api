package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class WebArchitectureTestTest extends BaseArchitectureTest {

    @Test
    void controllersShouldResideInWebControllerPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..web.controller..")
                .check(importedClasses);
    }
}
