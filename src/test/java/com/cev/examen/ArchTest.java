package com.cev.examen;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.cev.examen");

        noClasses()
            .that()
            .resideInAnyPackage("com.cev.examen.service..")
            .or()
            .resideInAnyPackage("com.cev.examen.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.cev.examen.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
