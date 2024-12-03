package dev.team.systers.tools;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTest {

    @Test
    void service() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("dev.team.systers");

        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(org.springframework.stereotype.Service.class);

        rule.check(importedClasses);
    }

    @Test
    void repository() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("dev.team.systers");

        ArchRule rule = classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(org.springframework.stereotype.Repository.class);

        rule.check(importedClasses);
    }

    @Test
    void controller() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("dev.team.systers");

        ArchRule rule = classes()
                .that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(org.springframework.stereotype.Controller.class);

        rule.check(importedClasses);
    }
}