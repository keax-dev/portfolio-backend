package com.keax.project.application.validation;

import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectStructureValidatorTest {

    private TechnologyRepositoryPort technologyRepository;
    private ProjectStructureValidator validator;

    @BeforeEach
    void setUp() {
        technologyRepository = mock(TechnologyRepositoryPort.class);
        validator = new ProjectStructureValidator(technologyRepository);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(new Technology(1L, "ANGULAR", 1, false)));
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(2L, false))
                .thenReturn(Optional.of(new Technology(2L, "SPRING BOOT", 2, false)));
    }

    @Test
    void acceptsOrderedTechnologiesAndLinksAndTrimsUrls() {
        Project project = project(
                List.of(
                        new ProjectTechnology(null, 1L, null, 1),
                        new ProjectTechnology(null, 2L, null, 2)
                ),
                List.of(
                        new ProjectLink(null, ProjectLinkType.DEPLOY, " https://example.com ", 1),
                        new ProjectLink(null, ProjectLinkType.GITHUB_BACKEND, "https://github.com/api", 2)
                )
        );

        validator.validate(project);

        assertEquals("https://example.com", project.getProjectLinks().getFirst().getUrl());
    }

    @Test
    void rejectsProjectsWithoutTechnologies() {
        assertThrows(ExceptionAlert.class, () -> validator.validate(project(List.of(), List.of())));
    }

    @Test
    void treatsMissingLinksAsAnEmptyOptionalCollection() {
        Project project = project(
                List.of(new ProjectTechnology(null, 1L, null, 1)),
                List.of()
        );
        project.setProjectLinks(null);

        validator.validate(project);

        assertTrue(project.getProjectLinks().isEmpty());
    }

    @Test
    void rejectsDuplicateTechnologiesAndTechnologyPositions() {
        Project repeatedTechnology = project(
                List.of(
                        new ProjectTechnology(null, 1L, null, 1),
                        new ProjectTechnology(null, 1L, null, 2)
                ),
                List.of()
        );
        Project repeatedPosition = project(
                List.of(
                        new ProjectTechnology(null, 1L, null, 1),
                        new ProjectTechnology(null, 2L, null, 1)
                ),
                List.of()
        );

        assertThrows(ResourceConflictException.class, () -> validator.validate(repeatedTechnology));
        assertThrows(ResourceConflictException.class, () -> validator.validate(repeatedPosition));
    }

    @Test
    void rejectsDuplicateLinkTypesAndLinkPositions() {
        Project repeatedType = project(
                List.of(new ProjectTechnology(null, 1L, null, 1)),
                List.of(
                        new ProjectLink(null, ProjectLinkType.GITHUB, "https://github.com/a", 1),
                        new ProjectLink(null, ProjectLinkType.GITHUB, "https://github.com/b", 2)
                )
        );
        Project repeatedPosition = project(
                List.of(new ProjectTechnology(null, 1L, null, 1)),
                List.of(
                        new ProjectLink(null, ProjectLinkType.DEPLOY, "https://example.com", 1),
                        new ProjectLink(null, ProjectLinkType.GITHUB, "https://github.com/a", 1)
                )
        );

        assertThrows(ResourceConflictException.class, () -> validator.validate(repeatedType));
        assertThrows(ResourceConflictException.class, () -> validator.validate(repeatedPosition));
    }

    private Project project(List<ProjectTechnology> technologies, List<ProjectLink> links) {
        return new Project(
                null,
                "PROJECT",
                "PROYECTO",
                "Description",
                "Descripción",
                null,
                1,
                false,
                new ArrayList<>(technologies),
                new ArrayList<>(links)
        );
    }
}
