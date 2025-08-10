package com.sccon.archtecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(packages = "com.sccon", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    @ArchTest
    static final ArchRule layers_should_respect_dependencies = layeredArchitecture()
        .consideringAllDependencies()
        .layer("presentation").definedBy("..presentation..")
        .layer("service").definedBy("..service..")
        .layer("repository").definedBy("..repository..")
        .layer("domain").definedBy("..domain..")
        .layer("infrastructure").definedBy("..infrastructure..")

        .whereLayer("presentation").mayNotBeAccessedByAnyLayer()
        .whereLayer("service").mayOnlyBeAccessedByLayers("presentation", "infrastructure")
        .whereLayer("repository").mayOnlyBeAccessedByLayers("service", "infrastructure")
        .whereLayer("domain").mayOnlyBeAccessedByLayers("presentation", "repository");

    @ArchTest
    static final ArchRule services_should_have_right_sufix =
        classes()
            .that().resideInAPackage("..service..")
            .and().areNotAnonymousClasses()
            .should().haveSimpleNameEndingWith("Service")
            .orShould().haveSimpleNameContaining("Port");

    @ArchTest
    static final ArchRule repositories_should_have_right_sufix =
        classes()
            .that().resideInAPackage("..repository..")
            .should().haveSimpleNameEndingWith("Repository");

    @ArchTest
    static final ArchRule domain_should_not_depends_on_frameworks =
        noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("jakarta.persistence..", "javax.persistence..")
            .because("Domain entities should not depends on frameworks");
}