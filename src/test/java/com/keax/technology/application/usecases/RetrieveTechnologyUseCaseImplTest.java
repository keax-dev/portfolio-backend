package com.keax.technology.application.usecases;

import com.keax.project.domain.model.Project;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica el filtrado de proyectos por borrado logico al recuperar tecnologias
 * para el portafolio publico.
 */
class RetrieveTechnologyUseCaseImplTest {

    @Test
    void keepsOnlyProjectsWithRequestedDeletionState() {
        // Arrange: una tecnologia contiene proyectos activos, eliminados y sin estado.
        TechnologyRepositoryPort repository = mock(TechnologyRepositoryPort.class);
        Technology technology = new Technology(
                1L,
                "JAVA",
                1,
                false,
                List.of(project(1L, false), project(2L, true), project(3L, null))
        );
        when(repository.findByTechnologyDeletedWithProjects(false))
                .thenReturn(List.of(technology));
        RetrieveTechnologyUseCaseImpl useCase = new RetrieveTechnologyUseCaseImpl(repository);

        // Act: se solicitan solo proyectos activos.
        List<Technology> result = useCase.findByTechnologyDeletedWithProjects(false, false);

        // Assert: el agregado conserva exclusivamente el estado solicitado.
        assertEquals(1, result.getFirst().getProjectList().size());
        assertEquals(1L, result.getFirst().getProjectList().getFirst().getProjectId());
    }

    @Test
    void canFilterProjectsWithNullDeletionStateSafely() {
        // Arrange: se incluye un registro legado cuyo estado de borrado es nulo.
        TechnologyRepositoryPort repository = mock(TechnologyRepositoryPort.class);
        Technology technology = new Technology(
                1L,
                "JAVA",
                1,
                false,
                List.of(project(3L, null))
        );
        when(repository.findByTechnologyDeletedWithProjects(false))
                .thenReturn(List.of(technology));
        RetrieveTechnologyUseCaseImpl useCase = new RetrieveTechnologyUseCaseImpl(repository);

        // Act: se filtra explicitamente por el estado nulo.
        List<Technology> result = useCase.findByTechnologyDeletedWithProjects(false, null);

        // Assert: el filtrado no lanza excepciones y conserva el dato legado.
        assertNull(result.getFirst().getProjectList().getFirst().getProjectDeleted());
    }

    private Project project(Long id, Boolean deleted) {
        // Crea un proyecto minimo; solo el id y el estado participan en esta regla.
        return new Project(
                id,
                "Project",
                "Proyecto",
                "Description",
                "Descripcion",
                null,
                null,
                null,
                1,
                1L,
                "JAVA",
                deleted
        );
    }

}
