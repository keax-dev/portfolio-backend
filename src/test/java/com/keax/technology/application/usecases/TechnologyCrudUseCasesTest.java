package com.keax.technology.application.usecases;

import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.ports.out.ProjectTechnologyReferencePort;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica creación, actualización, consulta y borrado lógico de tecnologías,
 * incluida la protección cuando existen proyectos activos asociados.
 */
class TechnologyCrudUseCasesTest {

    private TechnologyRepositoryPort technologyRepository;
    private ProjectTechnologyReferencePort projectTechnologyReferencePort;

    @BeforeEach
    void setUp() {
        // Se aíslan ambos módulos mediante sus puertos públicos.
        technologyRepository = mock(TechnologyRepositoryPort.class);
        projectTechnologyReferencePort = mock(ProjectTechnologyReferencePort.class);
    }

    @Test
    void createsNormalizedTechnology() {
        // Arrange: el nombre está disponible.
        Technology input = technology(null, "Java", null);
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.empty());
        when(technologyRepository.createTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea la tecnología.
        Technology result = new CreateTechnologyUseCaseImpl(technologyRepository).createTechnology(input);

        // Assert: se normaliza y queda activa.
        assertEquals("JAVA", result.getTechnologyName());
        assertFalse(result.getTechnologyDeleted());
    }

    @Test
    void rejectsDuplicatedTechnologyName() {
        // Arrange: el nombre ya pertenece a una tecnología activa.
        Technology input = technology(null, "Java", null);
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.of(technology(2L, "JAVA", false)));

        // Act y Assert: se rechaza el nombre duplicado.
        assertThrows(
                ResourceConflictException.class,
                () -> new CreateTechnologyUseCaseImpl(technologyRepository).createTechnology(input)
        );
    }

    @Test
    void updatesTechnologyWithoutSelfConflict() {
        // Arrange: los resultados de unicidad apuntan al mismo registro.
        Technology stored = technology(1L, "OLD", false);
        Technology changes = technology(null, "Java", null);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.of(technology(1L, "JAVA", false)));
        when(technologyRepository.updateTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el registro.
        Technology result = new UpdateTechnologyUseCaseImpl(technologyRepository)
                .updateTechnology(1L, changes);

        // Assert: cambia el nombre y conserva la identidad.
        assertEquals(1L, result.getTechnologyId());
        assertEquals("JAVA", result.getTechnologyName());
    }

    @Test
    void preventsTechnologyDeletionWithActiveProjects() {
        // Arrange: la tecnología existe y un proyecto activo la referencia.
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(technology(1L, "JAVA", false)));
        when(projectTechnologyReferencePort.existsActiveProjectForTechnology(1L)).thenReturn(true);

        // Act y Assert: la relación impide el borrado lógico.
        assertThrows(
                ResourceConflictException.class,
                () -> new DeleteTechnologyUseCaseImpl(technologyRepository, projectTechnologyReferencePort)
                        .deleteTechnology(1L)
        );
    }

    @Test
    void logicallyDeletesTechnologyWithoutProjects() {
        // Arrange: no existen proyectos activos asociados.
        Technology stored = technology(1L, "JAVA", false);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(projectTechnologyReferencePort.existsActiveProjectForTechnology(1L)).thenReturn(false);
        when(technologyRepository.deleteTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act y Assert: se activa el borrado lógico.
        assertTrue(new DeleteTechnologyUseCaseImpl(technologyRepository, projectTechnologyReferencePort)
                .deleteTechnology(1L)
                .getTechnologyDeleted());
    }

    @Test
    void returnsEmptyTechnologyList() {
        // Arrange: el repositorio no devuelve tecnologías.
        when(technologyRepository.getListTechnology()).thenReturn(List.of());
        RetrieveTechnologyUseCaseImpl useCase = new RetrieveTechnologyUseCaseImpl(technologyRepository);

        // Act y Assert: una colección vacía sigue siendo una respuesta exitosa.
        assertTrue(useCase.getListTechnology().isEmpty());
    }

    @Test
    void reportsMissingTechnologyOnUpdate() {
        // Arrange: el id editado no existe.
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(99L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: se detiene el flujo antes de validar duplicados.
        assertThrows(
                ResourceNotFoundException.class,
                () -> new UpdateTechnologyUseCaseImpl(technologyRepository)
                        .updateTechnology(99L, technology(null, "Java", null))
        );
    }

    private Technology technology(Long id, String name, Boolean deleted) {
        // Crea un modelo sin proyectos para probar únicamente reglas CRUD.
        return new Technology(id, name, deleted);
    }

}
