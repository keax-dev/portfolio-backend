package com.keax.experience.application.usecases;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ExperienceUseCasesTest {

    private final ExperienceRepositoryPort repository = mock(ExperienceRepositoryPort.class);

    @Test
    void createsNormalizedVisibleExperience() {
        Experience input = experience(null, 1);
        input.setExperienceVisible(null);
        when(repository.findByPosition(1)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Experience result = new CreateExperienceUseCaseImpl(repository).createExperience(input);

        assertEquals("FULL STACK DEVELOPER", result.getExperienceRole());
        assertEquals("KEAX", result.getExperienceCompany());
        assertTrue(result.getExperienceVisible());
    }

    @Test
    void rejectsDuplicatedPositionOnCreate() {
        when(repository.findByPosition(1)).thenReturn(Optional.of(experience(2L, 1)));

        assertThrows(
                ResourceConflictException.class,
                () -> new CreateExperienceUseCaseImpl(repository)
                        .createExperience(experience(null, 1))
        );
    }

    @Test
    void updatesVisibilityAndContent() {
        Experience stored = experience(1L, 1);
        Experience changes = experience(null, 2);
        changes.setExperienceVisible(false);
        when(repository.findById(1L)).thenReturn(Optional.of(stored));
        when(repository.findByPosition(2)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Experience result = new UpdateExperienceUseCaseImpl(repository)
                .updateExperience(1L, changes);

        assertEquals(2, result.getExperiencePosition());
        assertEquals(false, result.getExperienceVisible());
    }

    @Test
    void logicallyDeletesExistingExperience() {
        Experience stored = experience(1L, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(stored));
        when(repository.delete(stored)).thenReturn(stored);

        new DeleteExperienceUseCaseImpl(repository).deleteExperience(1L);

        verify(repository).delete(stored);
    }

    @Test
    void reportsMissingExperienceOnDelete() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> new DeleteExperienceUseCaseImpl(repository).deleteExperience(99L)
        );
    }

    @Test
    void retrievesAdministrativeAndVisibleCollectionsSeparately() {
        List<Experience> all = List.of(experience(1L, 1), experience(2L, 2));
        List<Experience> visible = List.of(all.getFirst());
        when(repository.findAll()).thenReturn(all);
        when(repository.findVisible()).thenReturn(visible);
        RetrieveExperienceUseCaseImpl useCase = new RetrieveExperienceUseCaseImpl(repository);

        assertEquals(all, useCase.getExperiences());
        assertEquals(visible, useCase.getVisibleExperiences());
    }

    private static Experience experience(Long id, int position) {
        return new Experience(
                id,
                " Full Stack Developer ",
                " Desarrollador Full Stack ",
                " Keax ",
                "Enterprise applications",
                "Aplicaciones empresariales",
                "2022",
                "2022",
                null,
                null,
                position,
                true,
                null
        );
    }
}
