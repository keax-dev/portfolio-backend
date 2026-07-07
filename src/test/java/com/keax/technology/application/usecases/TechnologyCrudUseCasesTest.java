package com.keax.technology.application.usecases;

import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
    private ProjectRepositoryPort projectRepository;

    @BeforeEach
    void setUp() {
        // Se aíslan ambos módulos mediante sus puertos públicos.
        technologyRepository = mock(TechnologyRepositoryPort.class);
        projectRepository = mock(ProjectRepositoryPort.class);
    }

    @Test
    void createsNormalizedTechnology() {
        // Arrange: nombre y posición están disponibles.
        Technology input = technology(null, "Java", 1, null);
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.empty());
        when(technologyRepository.findByTechnologyPositionAndTechnologyDeleted(1, false))
                .thenReturn(Optional.empty());
        when(technologyRepository.createTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea la tecnología.
        Technology result = inject(new CreateTechnologyUseCaseImpl()).createTechnology(input);

        // Assert: se normaliza y queda activa.
        assertEquals("JAVA", result.getTechnologyName());
        assertFalse(result.getTechnologyDeleted());
    }

    @Test
    void rejectsDuplicatedTechnologyName() {
        // Arrange: el nombre ya pertenece a una tecnología activa.
        Technology input = technology(null, "Java", 1, null);
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.of(technology(2L, "JAVA", 2, false)));

        // Act y Assert: se rechaza el nombre duplicado.
        assertThrows(
                ResourceConflictException.class,
                () -> inject(new CreateTechnologyUseCaseImpl()).createTechnology(input)
        );
    }

    @Test
    void updatesTechnologyWithoutSelfConflict() {
        // Arrange: los resultados de unicidad apuntan al mismo registro.
        Technology stored = technology(1L, "OLD", 1, false);
        Technology changes = technology(null, "Java", 2, null);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(technologyRepository.findByTechnologyNameAndTechnologyDeleted("JAVA", false))
                .thenReturn(Optional.of(technology(1L, "JAVA", 2, false)));
        when(technologyRepository.findByTechnologyPositionAndTechnologyDeleted(2, false))
                .thenReturn(Optional.of(technology(1L, "JAVA", 2, false)));
        when(technologyRepository.updateTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el registro.
        Technology result = inject(new UpdateTechnologyUseCaseImpl()).updateTechnology(1L, changes);

        // Assert: cambia nombre/posición y conserva identidad.
        assertEquals(1L, result.getTechnologyId());
        assertEquals("JAVA", result.getTechnologyName());
        assertEquals(2, result.getTechnologyPosition());
    }

    @Test
    void preventsTechnologyDeletionWithActiveProjects() {
        // Arrange: la tecnología existe y un proyecto activo la referencia.
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(technology(1L, "JAVA", 1, false)));
        when(projectRepository.existsByTechnology_technologyIdAndProjectDeleted(1L, false))
                .thenReturn(true);

        // Act y Assert: la relación impide el borrado lógico.
        assertThrows(
                ResourceConflictException.class,
                () -> inject(new DeleteTechnologyUseCaseImpl()).deleteTechnology(1L)
        );
    }

    @Test
    void logicallyDeletesTechnologyWithoutProjects() {
        // Arrange: no existen proyectos activos asociados.
        Technology stored = technology(1L, "JAVA", 1, false);
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(projectRepository.existsByTechnology_technologyIdAndProjectDeleted(1L, false))
                .thenReturn(false);
        when(technologyRepository.deleteTechnology(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act y Assert: se activa el borrado lógico.
        assertTrue(inject(new DeleteTechnologyUseCaseImpl())
                .deleteTechnology(1L)
                .getTechnologyDeleted());
    }

    @Test
    void rejectsEmptyTechnologyList() {
        // Arrange: el repositorio no devuelve tecnologías.
        when(technologyRepository.getListTechnology()).thenReturn(List.of());
        RetrieveTechnologyUseCaseImpl useCase = new RetrieveTechnologyUseCaseImpl(technologyRepository);

        // Act y Assert: se conserva el contrato de alerta.
        assertThrows(ExceptionAlert.class, useCase::getListTechnology);
    }

    @Test
    void reportsMissingTechnologyOnUpdate() {
        // Arrange: el id editado no existe.
        when(technologyRepository.findByTechnologyIdAndTechnologyDeleted(99L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: se detiene el flujo antes de validar duplicados.
        assertThrows(
                ResourceNotFoundException.class,
                () -> inject(new UpdateTechnologyUseCaseImpl())
                        .updateTechnology(99L, technology(null, "Java", 1, null))
        );
    }

    private <T> T inject(T useCase) {
        // Inyecta únicamente los puertos presentes en cada implementación.
        if (org.springframework.util.ReflectionUtils.findField(
                useCase.getClass(), "technologyRepositoryPort") != null) {
            ReflectionTestUtils.setField(useCase, "technologyRepositoryPort", technologyRepository);
        }
        if (org.springframework.util.ReflectionUtils.findField(
                useCase.getClass(), "projectRepositoryPort") != null) {
            ReflectionTestUtils.setField(useCase, "projectRepositoryPort", projectRepository);
        }
        return useCase;
    }

    private Technology technology(Long id, String name, int position, Boolean deleted) {
        // Crea un modelo sin proyectos para probar únicamente reglas CRUD.
        return new Technology(id, name, position, deleted, new ArrayList<>());
    }

}
