package com.keax.institution.application.usecases;

import com.keax.institution.domain.model.Institution;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.ports.out.InstitutionReferencePort;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica las reglas CRUD de institución, en especial nombres duplicados,
 * borrado lógico y protección frente a registros educativos asociados.
 */
class InstitutionUseCasesTest {

    private InstitutionRepositoryPort institutionRepository;
    private InstitutionReferencePort institutionReferencePort;

    @BeforeEach
    void setUp() {
        // Los puertos simulados aíslan las reglas de negocio de JPA.
        institutionRepository = mock(InstitutionRepositoryPort.class);
        institutionReferencePort = mock(InstitutionReferencePort.class);
    }

    @Test
    void createsNormalizedInstitution() {
        // Arrange: nombre disponible y repositorio preparado para devolver el argumento.
        Institution input = institution(null, "University", "Universidad", null, null);
        when(institutionRepository.findByName("UNIVERSITY"))
                .thenReturn(Optional.empty());
        when(institutionRepository.saveInstitution(any())).thenAnswer(invocation -> invocation.getArgument(0));
        CreateInstitutionUseCaseImpl useCase = new CreateInstitutionUseCaseImpl(institutionRepository);

        // Act: se crea la institución.
        Institution result = useCase.createInstitution(input);

        // Assert: se normaliza y se inicializa el borrado lógico.
        assertEquals("UNIVERSITY", result.getInstitutionName());
        assertEquals("UNIVERSIDAD", result.getInstitutionNameEs());
        assertNull(result.getInstitutionId());
    }

    @Test
    void rejectsDuplicatedInstitutionName() {
        // Arrange: existe otra institución activa con el nombre normalizado.
        Institution input = institution(null, "University", "Universidad", null, null);
        when(institutionRepository.findByName("UNIVERSITY"))
                .thenReturn(Optional.of(institution(1L, "UNIVERSITY", "UNIVERSIDAD", null, false)));
        CreateInstitutionUseCaseImpl useCase = new CreateInstitutionUseCaseImpl(institutionRepository);

        // Act y Assert: no se intenta guardar un duplicado.
        assertThrows(ResourceConflictException.class, () -> useCase.createInstitution(input));
    }

    @Test
    void updatesInstitutionWhenNameBelongsToSameRecord() {
        // Arrange: la búsqueda de duplicado retorna el mismo id del registro editado.
        Institution stored = institution(1L, "OLD", "ANTIGUA", null, false);
        Institution changes = institution(null, "University", "Universidad", null, null);
        when(institutionRepository.findById(1L))
                .thenReturn(Optional.of(stored));
        when(institutionRepository.findByName("UNIVERSITY"))
                .thenReturn(Optional.of(institution(1L, "UNIVERSITY", "UNIVERSIDAD", null, false)));
        when(institutionRepository.updateInstitution(any())).thenAnswer(invocation -> invocation.getArgument(0));
        UpdateInstitutionUseCaseImpl useCase = new UpdateInstitutionUseCaseImpl(institutionRepository);

        // Act: se actualiza sin producir un falso conflicto consigo misma.
        Institution result = useCase.updateInstitution(1L, changes);

        // Assert: conserva identidad y aplica el nombre normalizado.
        assertEquals(1L, result.getInstitutionId());
        assertEquals("UNIVERSITY", result.getInstitutionName());
    }

    @Test
    void preventsDeletionWhenActiveEducationExists() {
        // Arrange: la institución existe y tiene educación activa asociada.
        Institution stored = institution(1L, "UNIVERSITY", "UNIVERSIDAD", null, false);
        when(institutionRepository.findById(1L))
                .thenReturn(Optional.of(stored));
        when(institutionReferencePort.existsActiveEducationForInstitution(1L)).thenReturn(true);
        DeleteInstitutionUseCaseImpl useCase = new DeleteInstitutionUseCaseImpl(
                institutionRepository,
                institutionReferencePort
        );

        // Act y Assert: se protege la relación antes del borrado lógico.
        assertThrows(ResourceConflictException.class, () -> useCase.deleteInstitution(1L));
    }

    @Test
    void preventsDeletionWhenActiveCourseExists() {
        Institution stored = institution(1L, "UDEMY", "UDEMY", null, false);
        when(institutionRepository.findById(1L))
                .thenReturn(Optional.of(stored));
        when(institutionReferencePort.existsActiveEducationForInstitution(1L)).thenReturn(false);
        when(institutionReferencePort.existsActiveCourseForInstitution(1L)).thenReturn(true);
        DeleteInstitutionUseCaseImpl useCase = new DeleteInstitutionUseCaseImpl(
                institutionRepository,
                institutionReferencePort
        );

        assertThrows(ResourceConflictException.class, () -> useCase.deleteInstitution(1L));
    }

    @Test
    void logicallyDeletesInstitutionWithoutAssociations() {
        // Arrange: existe una institución sin educación activa.
        Institution stored = institution(1L, "UNIVERSITY", "UNIVERSIDAD", null, false);
        when(institutionRepository.findById(1L))
                .thenReturn(Optional.of(stored));
        when(institutionReferencePort.existsActiveEducationForInstitution(1L)).thenReturn(false);
        when(institutionRepository.deleteInstitution(stored)).thenReturn(stored);
        DeleteInstitutionUseCaseImpl useCase = new DeleteInstitutionUseCaseImpl(
                institutionRepository,
                institutionReferencePort
        );

        // Act: se elimina lógicamente.
        Institution result = useCase.deleteInstitution(1L);

        // Assert: el registro persiste con el indicador activado.
        assertEquals(stored, result);
        verify(institutionRepository).deleteInstitution(stored);
    }

    @Test
    void returnsEmptyInstitutionList() {
        // Arrange: una consulta administrativa no devuelve instituciones.
        when(institutionRepository.findAll()).thenReturn(List.of());
        RetrieveInstitutionUseCaseImpl useCase = new RetrieveInstitutionUseCaseImpl(institutionRepository);

        // Act y Assert: una colección vacía sigue siendo una respuesta exitosa.
        assertTrue(useCase.getListInstitution().isEmpty());
    }

    private Institution institution(Long id, String name, String nameEs, String url, Boolean ignoredDeleted) {
        // Construye una institución de dominio sin involucrar entidades JPA.
        return new Institution(id, name, nameEs, url, null);
    }

}
