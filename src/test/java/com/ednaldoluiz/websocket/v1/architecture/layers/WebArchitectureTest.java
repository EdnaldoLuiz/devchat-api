package com.ednaldoluiz.websocket.v1.architecture.layers;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class WebArchitectureTest extends BaseArchitectureTest {

    @Test
    void controllersShouldResideInWebControllerPackage() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..web.controller..")
                .check(importedClasses);
    }

    @Test
    void allControllersShouldBeAnnotatedWithRestController() {
        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().beAnnotatedWith(RestController.class)
                .orShould().beAnnotatedWith(Controller.class)
                .check(importedClasses);
    }

    @Test
    void allControllersShouldImplementApi() {
        var importedClasses = new ClassFileImporter()
                .importPackages("com.ednaldoluiz.websocket");

        classes()
                .that().haveSimpleNameEndingWith("Controller")
                .and().doNotHaveSimpleName("OAuth2AuthController")
                .should(implementInterfaceEndingWithApi())
                .check(importedClasses);
    }

    /**
     * Retorna a condição ArchUnit que checa se a classe
     * implementa pelo menos uma interface que termine em "Api".
     */
    private ArchCondition<JavaClass> implementInterfaceEndingWithApi() {
        return new ArchCondition<>("implementar alguma interface cujo nome termine em 'Api'") {
            @Override
            public void check(JavaClass controllerClass, ConditionEvents events) {

                // Pega as interfaces que a classe implementa diretamente
                // (em algumas versões do ArchUnit, pode ser getRawInterfaces(), getInterfaces(), etc.)
                var interfaces = controllerClass.getRawInterfaces();

                boolean foundApi = interfaces.stream()
                        .anyMatch(iface -> iface.getName().endsWith("Api"));

                if (!foundApi) {
                    String msg = "A classe '%s' não implementa nenhuma interface que termine em 'Api'"
                            .formatted(controllerClass.getName());
                    events.add(SimpleConditionEvent.violated(controllerClass, msg));
                }
            }
        };
    }
}
