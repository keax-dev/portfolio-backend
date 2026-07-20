package com.keax.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Protege la direccion de dependencias de la arquitectura hexagonal para impedir
 * que dominio o aplicacion se acoplen accidentalmente a detalles de infraestructura.
 */
@AnalyzeClasses(packages = "com.keax", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    // El dominio debe seguir siendo Java puro, independiente de Spring, JPA y adaptadores.
    @ArchTest
    static final ArchRule DOMAIN_MUST_NOT_DEPEND_ON_OUTER_LAYERS = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "..application..",
                    "..infrastructure..",
                    "org.springframework..",
                    "jakarta.persistence..",
                    "jakarta.servlet.."
            );

    // Los casos de uso pueden consumir puertos, pero nunca implementaciones externas.
    @ArchTest
    static final ArchRule APPLICATION_MUST_NOT_DEPEND_ON_INFRASTRUCTURE = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..");

    @ArchTest
    static final ArchRule FEATURES_MUST_NOT_FORM_DEPENDENCY_CYCLES = slices()
            .matching("com.keax.(*)..")
            .should().beFreeOfCycles();

}
