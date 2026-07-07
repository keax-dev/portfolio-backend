package com.keax.socialnetwork.application.usecases;

import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.socialnetwork.domain.ports.out.SocialNetworkRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
 * Verifica las reglas de creación, actualización, consulta y borrado lógico de
 * redes sociales, incluyendo sus restricciones de nombre y posición.
 */
class SocialNetworkUseCasesTest {

    private SocialNetworkRepositoryPort repository;

    @BeforeEach
    void setUp() {
        // El mock evita que estas reglas dependan de Spring Data.
        repository = mock(SocialNetworkRepositoryPort.class);
    }

    @Test
    void createsNormalizedSocialNetwork() {
        // Arrange: nombre y posición están libres.
        SocialNetwork input = social(null, "Github", 1, null);
        when(repository.findBySocialNetworkNameAndSocialNetworkDeleted("GITHUB", false))
                .thenReturn(Optional.empty());
        when(repository.findBySocialNetworkPositionAndSocialNetworkDeleted(1, false))
                .thenReturn(Optional.empty());
        when(repository.createSocialNetwork(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se crea la red social.
        SocialNetwork result = inject(new CreateSocialNetworkUseCaseImpl()).createSocialNetwork(input);

        // Assert: el nombre y el estado quedan controlados por aplicación.
        assertEquals("GITHUB", result.getSocialNetworkName());
        assertFalse(result.getSocialNetworkDeleted());
    }

    @Test
    void rejectsDuplicatedSocialNetworkName() {
        // Arrange: ya existe el mismo nombre activo.
        SocialNetwork input = social(null, "Github", 1, null);
        when(repository.findBySocialNetworkNameAndSocialNetworkDeleted("GITHUB", false))
                .thenReturn(Optional.of(social(2L, "GITHUB", 2, false)));

        // Act y Assert: se evita crear el duplicado.
        assertThrows(
                ResourceConflictException.class,
                () -> inject(new CreateSocialNetworkUseCaseImpl()).createSocialNetwork(input)
        );
    }

    @Test
    void updatesAllEditableSocialNetworkFields() {
        // Arrange: existe el registro y no hay conflictos ajenos.
        SocialNetwork stored = social(1L, "OLD", 1, false);
        SocialNetwork changes = new SocialNetwork(
                null, "Github", "github-icon", "#000000", 2, "https://github.com/keax", null
        );
        when(repository.findBySocialNetworkIdAndSocialNetworkDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(repository.findBySocialNetworkNameAndSocialNetworkDeleted("GITHUB", false))
                .thenReturn(Optional.empty());
        when(repository.findBySocialNetworkPositionAndSocialNetworkDeleted(2, false))
                .thenReturn(Optional.empty());
        when(repository.updateSocialNetwork(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act: se actualiza la red.
        SocialNetwork result = inject(new UpdateSocialNetworkUseCaseImpl())
                .updateSocialNetwork(1L, changes);

        // Assert: se aplican todos los campos y se conserva el id.
        assertEquals(1L, result.getSocialNetworkId());
        assertEquals("github-icon", result.getSocialNetworkIcon());
        assertEquals("#000000", result.getSocialNetworkColor());
        assertEquals(2, result.getSocialNetworkPosition());
    }

    @Test
    void reportsMissingSocialNetworkOnDelete() {
        // Arrange: no existe el identificador activo.
        when(repository.findBySocialNetworkIdAndSocialNetworkDeleted(99L, false))
                .thenReturn(Optional.empty());

        // Act y Assert: se comunica recurso no encontrado.
        assertThrows(
                ResourceNotFoundException.class,
                () -> inject(new DeleteSocialNetworkUseCaseImpl()).deleteSocialNetwork(99L)
        );
    }

    @Test
    void logicallyDeletesSocialNetwork() {
        // Arrange: existe una red activa.
        SocialNetwork stored = social(1L, "GITHUB", 1, false);
        when(repository.findBySocialNetworkIdAndSocialNetworkDeleted(1L, false))
                .thenReturn(Optional.of(stored));
        when(repository.deleteSocialNetwork(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act y Assert: el registro queda marcado como eliminado.
        assertTrue(inject(new DeleteSocialNetworkUseCaseImpl())
                .deleteSocialNetwork(1L)
                .getSocialNetworkDeleted());
    }

    @Test
    void rejectsEmptySocialNetworkList() {
        // Arrange: no existen redes para el filtro solicitado.
        when(repository.findBySocialNetworkDeleted(false)).thenReturn(List.of());

        // Act y Assert: se mantiene la alerta funcional existente.
        assertThrows(
                ExceptionAlert.class,
                () -> inject(new RetrieveSocialNetworkUseCaseImpl()).findBySocialNetworkDeleted(false)
        );
    }

    private <T> T inject(T useCase) {
        // Inyecta el puerto en la implementación concreta bajo prueba.
        ReflectionTestUtils.setField(useCase, "socialNetworkRepositoryPort", repository);
        return useCase;
    }

    private SocialNetwork social(Long id, String name, int position, Boolean deleted) {
        // Construye un modelo válido con valores técnicos mínimos.
        return new SocialNetwork(id, name, "icon", "#ffffff", position, "https://example.com", deleted);
    }

}
