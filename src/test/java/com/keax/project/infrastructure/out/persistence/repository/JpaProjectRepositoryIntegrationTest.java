package com.keax.project.infrastructure.out.persistence.repository;

import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifica consultas derivadas criticas del repositorio de proyectos que
 * sostienen la posicion por tecnologia y el borrado logico dependiente.
 */
@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JpaProjectRepositoryIntegrationTest {

    private final JpaProjectRepository projectRepository;
    private final JpaTechnologyRepository technologyRepository;

    JpaProjectRepositoryIntegrationTest(
            JpaProjectRepository projectRepository,
            JpaTechnologyRepository technologyRepository
    ) {
        this.projectRepository = projectRepository;
        this.technologyRepository = technologyRepository;
    }

    @Test
    void resolvesPositionQueryInsideTheRequestedTechnologyOnly() {
        // Arrange: se crean dos tecnologias para comprobar que la posicion se evalua por padre.
        TechnologyEntity javaTechnology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "JAVA",
                1,
                false,
                new ArrayList<>()
        ));
        TechnologyEntity springTechnology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "SPRING",
                2,
                false,
                new ArrayList<>()
        ));

        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "PORTFOLIO",
                "PORTAFOLIO",
                "Java portfolio",
                "Portafolio Java",
                null,
                null,
                null,
                1,
                false,
                javaTechnology
        ));
        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "API",
                "API",
                "Spring API",
                "API Spring",
                null,
                null,
                null,
                1,
                false,
                springTechnology
        ));

        // Act y Assert: la consulta por posicion debe respetar la tecnologia solicitada.
        var foundInJava = projectRepository.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(
                1,
                false,
                javaTechnology.getTechnologyId()
        );
        var foundInSpring = projectRepository.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(
                1,
                false,
                springTechnology.getTechnologyId()
        );

        assertTrue(foundInJava.isPresent());
        assertTrue(foundInSpring.isPresent());
        assertEquals("PORTFOLIO", foundInJava.orElseThrow().getProjectTitle());
        assertEquals("API", foundInSpring.orElseThrow().getProjectTitle());
    }

    @Test
    void reportsOnlyActiveChildProjectsInTheSoftDeleteGuardQuery() {
        // Arrange: una tecnologia tiene un proyecto activo y otro eliminado logicamente.
        TechnologyEntity technology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "KOTLIN",
                1,
                false,
                new ArrayList<>()
        ));

        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "ACTIVE PROJECT",
                "PROYECTO ACTIVO",
                "Active child",
                "Hijo activo",
                null,
                null,
                null,
                1,
                false,
                technology
        ));
        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "DELETED PROJECT",
                "PROYECTO ELIMINADO",
                "Deleted child",
                "Hijo eliminado",
                null,
                null,
                null,
                2,
                true,
                technology
        ));

        // Act: se ejecutan las consultas que usan los casos de uso de borrado logico.
        boolean hasActiveProjects = projectRepository.existsByTechnology_technologyIdAndProjectDeleted(
                technology.getTechnologyId(),
                false
        );
        boolean hasDeletedProjects = projectRepository.existsByTechnology_technologyIdAndProjectDeleted(
                technology.getTechnologyId(),
                true
        );
        var activeProjects = projectRepository.findByProjectDeleted(false);

        // Assert: el guardia distingue correctamente entre hijos activos y eliminados.
        assertTrue(hasActiveProjects);
        assertTrue(hasDeletedProjects);
        assertEquals(1, activeProjects.size());
        assertEquals("ACTIVE PROJECT", activeProjects.getFirst().getProjectTitle());
    }

    @Test
    void returnsFalseWhenATechnologyHasNoActiveProjects() {
        // Arrange: la tecnologia solo conserva hijos eliminados logicamente.
        TechnologyEntity technology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "RUST",
                1,
                false,
                new ArrayList<>()
        ));

        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "OLD PROJECT",
                "PROYECTO ANTIGUO",
                "Only deleted child",
                "Solo hijo eliminado",
                null,
                null,
                null,
                1,
                true,
                technology
        ));

        // Act y Assert: la proteccion de borrado no debe bloquear si no hay hijos activos.
        boolean hasActiveProjects = projectRepository.existsByTechnology_technologyIdAndProjectDeleted(
                technology.getTechnologyId(),
                false
        );

        assertFalse(hasActiveProjects);
    }

}
