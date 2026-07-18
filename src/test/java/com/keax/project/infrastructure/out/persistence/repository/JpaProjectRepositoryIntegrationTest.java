package com.keax.project.infrastructure.out.persistence.repository;

import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.infrastructure.out.persistence.entity.ProjectTechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Verifies global project ordering and technology guards through the join table. */
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
    void resolvesProjectPositionGlobally() {
        TechnologyEntity java = saveTechnology("JAVA", 1);
        TechnologyEntity spring = saveTechnology("SPRING", 2);
        projectRepository.saveAndFlush(project("PORTFOLIO", 1, false, java));
        projectRepository.saveAndFlush(project("API", 2, false, spring));

        var first = projectRepository.findByProjectPositionAndProjectDeleted(1, false);
        var second = projectRepository.findByProjectPositionAndProjectDeleted(2, false);

        assertEquals("PORTFOLIO", first.orElseThrow().getProjectTitle());
        assertEquals("API", second.orElseThrow().getProjectTitle());
    }

    @Test
    void reportsProjectsRelatedThroughAnyTechnology() {
        TechnologyEntity kotlin = saveTechnology("KOTLIN", 1);
        TechnologyEntity angular = saveTechnology("ANGULAR", 2);
        projectRepository.saveAndFlush(project("ACTIVE PROJECT", 1, false, angular, kotlin));
        projectRepository.saveAndFlush(project("DELETED PROJECT", 2, true, kotlin));

        assertTrue(projectRepository.existsByTechnologyIdAndProjectDeleted(kotlin.getTechnologyId(), false));
        assertTrue(projectRepository.existsByTechnologyIdAndProjectDeleted(kotlin.getTechnologyId(), true));

        var activeProjects = projectRepository.findByProjectDeletedOrderByProjectPosition(false);
        assertEquals(1, activeProjects.size());
        assertEquals(2, activeProjects.getFirst().getProjectTechnologies().size());
    }

    @Test
    void returnsFalseWhenATechnologyHasNoActiveProjects() {
        TechnologyEntity rust = saveTechnology("RUST", 1);
        projectRepository.saveAndFlush(project("OLD PROJECT", 1, true, rust));

        assertFalse(projectRepository.existsByTechnologyIdAndProjectDeleted(rust.getTechnologyId(), false));
    }

    private TechnologyEntity saveTechnology(String name, int position) {
        return technologyRepository.saveAndFlush(new TechnologyEntity(null, name, position, false));
    }

    private ProjectEntity project(
            String title,
            int position,
            boolean deleted,
            TechnologyEntity... technologies
    ) {
        ProjectEntity project = new ProjectEntity(
                null,
                title,
                title,
                "Description",
                "Descripción",
                null,
                position,
                deleted,
                new LinkedHashSet<>(),
                new LinkedHashSet<>()
        );
        for (int index = 0; index < technologies.length; index++) {
            project.getProjectTechnologies().add(new ProjectTechnologyEntity(
                    null,
                    project,
                    technologies[index],
                    index + 1
            ));
        }
        return project;
    }
}
