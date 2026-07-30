package com.keax.skill.application.usecases;

import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Verifica normalización, unicidad por nombre/posición, consulta y borrado
 * lógico de las habilidades.
 */
class SkillUseCasesTest {

    private SkillRepositoryPort repository;

    @BeforeEach
    void setUp() {
        // Crea un puerto nuevo para cada regla bajo prueba.
        repository = mock(SkillRepositoryPort.class);
    }

    @Test
    void createsSkillWithControlledDefaults() {
        // Arrange: nombre y posición están disponibles.
        Skill input = skill(null, "Java", "untrusted-picture", 1, null);
        when(repository.findByName("JAVA")).thenReturn(Optional.empty());
        when(repository.findByPosition(1)).thenReturn(Optional.empty());
        when(repository.createSkill(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se ejecuta la creación.
        Skill result = new CreateSkillUseCaseImpl(repository).createSkill(input);

        // Assert: el caso de uso controla id, imagen y borrado.
        assertEquals("JAVA", result.getSkillName());
        assertNull(result.getSkillId());
        assertNull(result.getSkillPicture());
    }

    @Test
    void rejectsDuplicatedSkillPosition() {
        // Arrange: otro registro activo ocupa la posición solicitada.
        Skill input = skill(null, "Java", null, 1, null);
        when(repository.findByName("JAVA")).thenReturn(Optional.empty());
        when(repository.findByPosition(1))
                .thenReturn(Optional.of(skill(2L, "SPRING", null, 1, false)));

        // Act y Assert: la posición duplicada se rechaza.
        assertThrows(
                ResourceConflictException.class,
                () -> new CreateSkillUseCaseImpl(repository).createSkill(input)
        );
    }

    @Test
    void updatesSkillWithoutConflictingWithItself() {
        // Arrange: nombre y posición encontrados pertenecen al mismo id.
        Skill stored = skill(1L, "OLD", "picture", 1, false);
        Skill changes = skill(null, "Java", null, 2, null);
        when(repository.findById(1L)).thenReturn(Optional.of(stored));
        when(repository.findByName("JAVA"))
                .thenReturn(Optional.of(skill(1L, "JAVA", null, 2, false)));
        when(repository.findByPosition(2))
                .thenReturn(Optional.of(skill(1L, "JAVA", null, 2, false)));
        when(repository.updateSkill(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el registro.
        Skill result = new UpdateSkillUseCaseImpl(repository).updateSkill(1L, changes);

        // Assert: la imagen existente se conserva y cambian nombre/posición.
        assertEquals("JAVA", result.getSkillName());
        assertEquals(2, result.getSkillPosition());
        assertEquals("picture", result.getSkillPicture());
    }

    @Test
    void reportsMissingSkillOnDelete() {
        // Arrange: el id solicitado no existe como registro activo.
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act y Assert: no se inventa un borrado sobre un recurso ausente.
        assertThrows(
                ResourceNotFoundException.class,
                () -> new DeleteSkillUseCaseImpl(repository).deleteSkill(99L)
        );
    }

    @Test
    void logicallyDeletesExistingSkill() {
        // Arrange: existe una habilidad activa.
        Skill stored = skill(1L, "JAVA", null, 1, false);
        when(repository.findById(1L)).thenReturn(Optional.of(stored));
        when(repository.deleteSkill(stored)).thenReturn(stored);

        // Act y Assert: la eliminación activa únicamente el indicador lógico.
        assertEquals(stored, new DeleteSkillUseCaseImpl(repository).deleteSkill(1L));
    }

    @Test
    void returnsEmptySkillList() {
        // Arrange: la consulta no encuentra habilidades.
        when(repository.findAll()).thenReturn(List.of());

        // Act y Assert: una colección vacía sigue siendo una respuesta exitosa.
        assertTrue(new RetrieveSkillUseCaseImpl(repository)
                .getListSkill()
                .isEmpty());
    }

    private Skill skill(Long id, String name, String picture, int position, Boolean ignoredDeleted) {
        // Crea una habilidad de dominio reutilizable.
        return new Skill(id, name, picture, position, null);
    }

}
