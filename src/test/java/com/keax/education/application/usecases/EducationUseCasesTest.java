package com.keax.education.application.usecases;

import com.keax.education.domain.model.Education;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.ports.out.EducationInstitutionReferencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private EducationInstitutionReferencePort institutionReferencePort;

    @BeforeEach
    void setUp() {
        // Cada caso se ejecuta contra contratos simulados.
        educationRepository = mock(EducationRepositoryPort.class);
        institutionReferencePort = mock(EducationInstitutionReferencePort.class);
    }

    @Test
    void createsEducationWithNormalizedOptionalFields() {
        // Arrange: no hay conflictos y la institución relacionada existe.
        Education input = education(null, "Degree", "Título", 1, null, 10L);
        input.setEducationStart(" ");
        input.setEducationStartEs(null);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L
                ))
                .thenReturn(Optional.empty());
        when(educationRepository.findByEducationPositionAndEducationDeleted(1, false))
                .thenReturn(Optional.empty());
        when(institutionReferencePort.existsActiveInstitution(10L)).thenReturn(true);
        when(educationRepository.createEducation(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea el registro educativo.
        Education result = new CreateEducationUseCaseImpl(educationRepository, institutionReferencePort)
                .createEducation(input);

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
                        "DEGREE", false, 10L
                ))
                .thenReturn(Optional.empty());
        when(educationRepository.findByEducationPositionAndEducationDeleted(1, false))
                .thenReturn(Optional.empty());
        when(institutionReferencePort.existsActiveInstitution(10L)).thenReturn(false);

        // Act y Assert: no se permite una referencia inexistente.
        assertThrows(
                ResourceNotFoundException.class,
                () -> new CreateEducationUseCaseImpl(educationRepository, institutionReferencePort)
                        .createEducation(input)
        );
    }

    @Test
    void rejectsDuplicatedEducationTitleInInstitution() {
        // Arrange: existe el mismo título activo dentro de la institución.
        Education input = education(null, "Degree", "Título", 1, null, 10L);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L
                ))
                .thenReturn(Optional.of(education(2L, "DEGREE", "TÍTULO", 2, false, 10L)));

        // Act y Assert: se detiene antes de validar el resto de datos.
        assertThrows(
                ResourceConflictException.class,
                () -> new CreateEducationUseCaseImpl(educationRepository, institutionReferencePort)
                        .createEducation(input)
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
        when(institutionReferencePort.existsActiveInstitution(10L)).thenReturn(true);
        when(educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, 10L
                ))
                .thenReturn(Optional.of(education(1L, "DEGREE", "TÍTULO", 2, false, 10L)));
        when(educationRepository.findByEducationPositionAndEducationDeleted(2, false))
                .thenReturn(Optional.of(education(1L, "DEGREE", "TÍTULO", 2, false, 10L)));
        when(educationRepository.updateEducation(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza el registro.
        Education result = new UpdateEducationUseCaseImpl(educationRepository, institutionReferencePort)
                .updateEducation(1L, changes);

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
        assertTrue(new DeleteEducationUseCaseImpl(educationRepository)
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
                () -> new DeleteEducationUseCaseImpl(educationRepository).deleteEducation(99L)
        );
    }

    @Test
    void returnsEmptyEducationList() {
        // Arrange: la consulta filtrada no devuelve registros.
        when(educationRepository.findByEducationDeleted(false)).thenReturn(List.of());

        // Act y Assert: una colección vacía sigue siendo una respuesta exitosa.
        assertTrue(new RetrieveEducationUseCaseImpl(educationRepository)
                .findByEducationDeleted(false)
                .isEmpty());
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
