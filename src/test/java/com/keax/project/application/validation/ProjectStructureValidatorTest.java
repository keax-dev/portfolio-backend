package com.keax.project.application.validation;

import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.ports.out.ProjectTechnologyReferencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectStructureValidatorTest {

    private ProjectTechnologyReferencePort technologyReferencePort;
    private ProjectStructureValidator validator;

    @BeforeEach
    void setUp() {
        technologyReferencePort = mock(ProjectTechnologyReferencePort.class);
        validator = new ProjectStructureValidator(technologyReferencePort);
        when(technologyReferencePort.findActiveTechnologyIds(Set.of(1L)))
                .thenReturn(Set.of(1L));
        when(technologyReferencePort.findActiveTechnologyIds(Set.of(1L, 2L)))
                .thenReturn(Set.of(1L, 2L));
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
                1,
                false,
                new ArrayList<>(technologies),
                new ArrayList<>(links),
                new ArrayList<>()
        );
    }
}
