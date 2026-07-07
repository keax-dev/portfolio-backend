package com.keax.education.application.usecases;

import com.keax.education.domain.model.Education;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.institution.domain.model.Institution;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
 * Verifica las reglas CRUD de educación: institución obligatoria, unicidad de
 * título/posición, normalización, campos opcionales y borrado lógico.
 */
class EducationUseCasesTest {

    private EducationRepositoryPort educationRepository;
    private InstitutionRepositoryPort institutionRepository;

    @BeforeEach
    void setUp() {
        // Cada caso se ejecuta contra contratos simulados.
        educationRepository = mock(EducationRepositoryPort.class);
        institutionRepository = mock(InstitutionRepositoryPort.class);
    }

    @Test
    void createsEducationWithNormalizedOptionalFields() {
        // Arrange: no hay conflictos y la institución relacionada existe.
        Education input = education(null, "Degree", "Título", 1, null, 10L);
        input.setEducationStart(" ");
        input.setEducationStartEs(null);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L))
                .thenReturn(Optional.empty());
        when(educationRepository.findByEducationPositionAndEducationDeleted(1, false))
                .thenReturn(Optional.empty());
        when(institutionRepository.findByInstitutionIdAndInstitutionDeleted(10L, false))
                .thenReturn(Optional.of(new Institution(10L, "UNIVERSITY", "UNIVERSIDAD", null, false)));
        when(educationRepository.createEducation(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea el registro educativo.
        Education result = inject(new CreateEducationUseCaseImpl()).createEducation(input);

        // Assert: los obligatorios se normalizan y los opcionales vacíos quedan nulos.
        assertEquals("DEGREE", result.getEducationTitle());
        assertEquals("TÍTULO", result.getEducationTitleEs());
        assertNull(result.getEducationStart());
        assertNull(result.getEducationStartEs());
        assertFalse(result.getEducationDeleted());
    }

    @Test
    void rejectsEducationForMissingInstitution() {
        // Arrange: título y posición están disponibles, pero la relación no existe.
        Education input = education(null, "Degree", "Título", 1, null, 10L);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L))
                .thenReturn(Optional.empty());
        when(educationRepository.findByEducationPositionAndEducationDeleted(1, false))
                .thenReturn(Optional.empty());
        when(institutionRepository.findByInstitutionIdAndInstitutionDeleted(10L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: no se permite una referencia inexistente.
        assertThrows(
                ResourceNotFoundException.class,
                () -> inject(new CreateEducationUseCaseImpl()).createEducation(input)
        );
    }

    @Test
    void rejectsDuplicatedEducationTitleInInstitution() {
        // Arrange: existe el mismo título activo dentro de la institución.
        Education input = education(null, "Degree", "Título", 1, null, 10L);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L))
                .thenReturn(Optional.of(education(2L, "DEGREE", "TÍTULO", 2, false, 10L)));

        // Act y Assert: se detiene antes de validar el resto de datos.
        assertThrows(
                ResourceConflictException.class,
                () -> inject(new CreateEducationUseCaseImpl()).createEducation(input)
        );
    }

    @Test
    void updatesEducationWithoutSelfConflict() {
        // Arrange: título y posición encontrados pertenecen al mismo id.
        Education stored = education(1L, "OLD", "ANTIGUO", 1, false, 10L);
        Education changes = education(null, "Degree", "Título", 2, null, 10L);
        changes.setEducationStart("January");
        changes.setEducationStartEs("Enero");
        when(educationRepository.findByEducationIdAndEducationDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(institutionRepository.findByInstitutionIdAndInstitutionDeleted(10L, false))
                .thenReturn(Optional.of(new Institution(10L, "UNI", "UNI", null, false)));
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L))
                .thenReturn(Optional.of(education(1L, "DEGREE", "TÍTULO", 2, false, 10L)));
        when(educationRepository.findByEducationPositionAndEducationDeleted(2, false))
                .thenReturn(Optional.of(education(1L, "DEGREE", "TÍTULO", 2, false, 10L)));
        when(educationRepository.updateEducation(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el registro.
        Education result = inject(new UpdateEducationUseCaseImpl()).updateEducation(1L, changes);

        // Assert: los campos temporales y descriptivos se aplican normalizados.
        assertEquals("DEGREE", result.getEducationTitle());
        assertEquals("JANUARY", result.getEducationStart());
        assertEquals("ENERO", result.getEducationStartEs());
        assertEquals(2, result.getEducationPosition());
    }

    @Test
    void logicallyDeletesExistingEducation() {
        // Arrange: existe una educación activa.
        Education stored = education(1L, "DEGREE", "TÍTULO", 1, false, 10L);
        when(educationRepository.findByEducationIdAndEducationDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(educationRepository.deleteEducation(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act y Assert: la operación marca el registro sin eliminarlo físicamente.
        assertTrue(inject(new DeleteEducationUseCaseImpl())
                .deleteEducation(1L)
                .getEducationDeleted());
    }

    @Test
    void reportsMissingEducationOnDelete() {
        // Arrange: el identificador activo no existe.
        when(educationRepository.findByEducationIdAndEducationDeleted(99L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: se informa recurso no encontrado.
        assertThrows(
                ResourceNotFoundException.class,
                () -> inject(new DeleteEducationUseCaseImpl()).deleteEducation(99L)
        );
    }

    @Test
    void rejectsEmptyEducationList() {
        // Arrange: la consulta filtrada no devuelve registros.
        when(educationRepository.findByEducationDeleted(false)).thenReturn(List.of());

        // Act y Assert: se mantiene el contrato de alerta.
        assertThrows(
                ExceptionAlert.class,
                () -> inject(new RetrieveEducationUseCaseImpl()).findByEducationDeleted(false)
        );
    }

    private <T> T inject(T useCase) {
        // Inyecta solo los puertos declarados por la implementación concreta.
        if (org.springframework.util.ReflectionUtils.findField(
                useCase.getClass(), "educationRepositoryPort") != null) {
            ReflectionTestUtils.setField(useCase, "educationRepositoryPort", educationRepository);
        }
        if (org.springframework.util.ReflectionUtils.findField(
                useCase.getClass(), "institutionRepositoryPort") != null) {
            ReflectionTestUtils.setField(useCase, "institutionRepositoryPort", institutionRepository);
        }
        return useCase;
    }

    private Education education(
            Long id,
            String title,
            String titleEs,
            int position,
            Boolean deleted,
            Long institutionId
    ) {
        // Construye una educación válida para concentrar la prueba en cada regla.
        return new Education(
                id, title, titleEs, "Place", null, null, "Present", "Actualidad",
                position, deleted, institutionId, "University", "Universidad", null
        );
    }

}
