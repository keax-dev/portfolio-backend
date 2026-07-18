package com.keax.project.application.usecases;

import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.project.application.validation.ProjectStructureValidator;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica las reglas CRUD de proyectos, su relación obligatoria con tecnología,
 * la unicidad de título/posición y el borrado lógico.
 */
class ProjectUseCasesTest {

    private ProjectRepositoryPort projectRepository;
    private TechnologyRepositoryPort technologyRepository;
    private ProjectStructureValidator structureValidator;

    @BeforeEach
    void setUp() {
        // Los repositorios se sustituyen por puertos simulados.
        projectRepository = mock(ProjectRepositoryPort.class);
        technologyRepository = mock(TechnologyRepositoryPort.class);
        structureValidator = new ProjectStructureValidator(technologyRepository);
    }

    @Test
    void createsProjectForExistingTechnology() {
        // Arrange: la tecnología existe y no hay conflictos de título o posición.
        Project input = project(null, "Portfolio", "Portafolio", 1, null, 10L);
        input.setProjectPicture("untrusted-picture");
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(10L, false))
                .thenReturn(Optional.of(new Technology(10L, "JAVA", 1, false)));
        when(projectRepository.findByProjectTitleAndProjectDeleted("PORTFOLIO", false))
                .thenReturn(Optional.empty());
        when(projectRepository.findByProjectPositionAndProjectDeleted(1, false))
                .thenReturn(Optional.empty());
        when(projectRepository.createProject(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea el proyecto.
        Project result = new CreateProjectUseCaseImpl(projectRepository, structureValidator)
                .createProject(input);

        // Assert: se normalizan títulos y la imagen solo puede venir del upload.
        assertEquals("PORTFOLIO", result.getProjectTitle());
        assertEquals("PORTAFOLIO", result.getProjectTitleEs());
        assertNull(result.getProjectPicture());
        assertFalse(result.getProjectDeleted());
    }

    @Test
    void rejectsProjectForMissingTechnology() {
        // Arrange: la tecnología relacionada no existe.
        Project input = project(null, "Portfolio", "Portafolio", 1, null, 10L);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(10L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: la relación se valida antes de persistir.
        assertThrows(
                ResourceNotFoundException.class,
                () -> new CreateProjectUseCaseImpl(projectRepository, structureValidator)
                        .createProject(input)
        );
    }

    @Test
    void rejectsDuplicatedProjectPositionGlobally() {
        // Arrange: la posición ya está ocupada dentro de la misma tecnología.
        Project input = project(null, "Portfolio", "Portafolio", 1, null, 10L);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(10L, false))
                .thenReturn(Optional.of(new Technology(10L, "JAVA", 1, false)));
        when(projectRepository.findByProjectTitleAndProjectDeleted("PORTFOLIO", false))
                .thenReturn(Optional.empty());
        when(projectRepository.findByProjectPositionAndProjectDeleted(1, false))
                .thenReturn(Optional.of(project(2L, "OTHER", "OTRO", 1, false, 10L)));

        // Act y Assert: se rechaza la posición duplicada.
        assertThrows(
                ResourceConflictException.class,
                () -> new CreateProjectUseCaseImpl(projectRepository, structureValidator)
                        .createProject(input)
        );
    }

    @Test
    void updatesProjectWithoutSelfConflict() {
        // Arrange: los resultados de unicidad corresponden al mismo proyecto.
        Project stored = project(1L, "OLD", "ANTIGUO", 1, false, 10L);
        stored.setProjectPicture("https://cdn/picture.jpg");
        Project changes = project(null, "Portfolio", "Portafolio", 2, null, 10L);
        changes.setProjectDescription("Updated description");
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(10L, false))
                .thenReturn(Optional.of(new Technology(10L, "JAVA", 1, false)));
        when(projectRepository.findByProjectTitleAndProjectDeleted("PORTFOLIO", false))
                .thenReturn(Optional.of(project(1L, "PORTFOLIO", "PORTAFOLIO", 2, false, 10L)));
        when(projectRepository.findByProjectPositionAndProjectDeleted(2, false))
                .thenReturn(Optional.of(project(1L, "PORTFOLIO", "PORTAFOLIO", 2, false, 10L)));
        when(projectRepository.updateProject(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el proyecto.
        Project result = new UpdateProjectUseCaseImpl(projectRepository, structureValidator)
                .updateProject(1L, changes);

        // Assert: se preserva la imagen y cambian los datos editables.
        assertEquals(1L, result.getProjectId());
        assertEquals("PORTFOLIO", result.getProjectTitle());
        assertEquals(2, result.getProjectPosition());
        assertEquals("https://cdn/picture.jpg", result.getProjectPicture());
        assertEquals("Updated description", result.getProjectDescription());
        assertEquals(101L, result.getProjectTechnologies().getFirst().getProjectTechnologyId());
    }

    @Test
    void logicallyDeletesProject() {
        // Arrange: existe un proyecto activo.
        Project stored = project(1L, "PORTFOLIO", "PORTAFOLIO", 1, false, 10L);
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(projectRepository.deleteProject(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act y Assert: se marca como eliminado sin borrado físico.
        assertTrue(new DeleteProjectUseCaseImpl(projectRepository)
                .deleteProject(1L)
                .getProjectDeleted());
    }

    @Test
    void reportsMissingProjectOnDelete() {
        // Arrange: no existe un proyecto activo con el id.
        when(projectRepository.findByProjectIdAndProjectDeleted(99L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: se informa recurso no encontrado.
        assertThrows(
                ResourceNotFoundException.class,
                () -> new DeleteProjectUseCaseImpl(projectRepository).deleteProject(99L)
        );
    }

    @Test
    void rejectsEmptyProjectList() {
        // Arrange: el filtro no devuelve proyectos.
        when(projectRepository.findByProjectDeleted(false)).thenReturn(List.of());

        // Act y Assert: se conserva la alerta funcional existente.
        assertThrows(
                ExceptionAlert.class,
                () -> new RetrieveProjectUseCaseImpl(projectRepository).findByProjectDeleted(false)
        );
    }

    private Project project(
            Long id,
            String title,
            String titleEs,
            int position,
            Boolean deleted,
            Long technologyId
    ) {
        // Construye un proyecto válido para concentrar cada prueba en una regla.
        Long relationId = id == null ? null : id + 100;
        return new Project(
                id, title, titleEs, "Description", "Descripción", null,
                position, deleted,
                List.of(new ProjectTechnology(relationId, technologyId, "JAVA", 1)),
                List.of(new ProjectLink(
                        relationId,
                        ProjectLinkType.DEPLOY,
                        "https://deploy.example",
                        1
                ))
        );
    }

}
