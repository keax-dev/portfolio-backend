package com.keax.profile.application.usecases;

import com.keax.profile.domain.model.Profile;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica creación, consulta y actualización del perfil único, incluyendo
 * normalización y errores cuando el perfil ya existe o todavía no fue creado.
 */
class ProfileUseCasesTest {

    private ProfileRepositoryPort repository;

    @BeforeEach
    void setUp() {
        // Cada prueba parte de un puerto aislado para no compartir interacciones.
        repository = mock(ProfileRepositoryPort.class);
    }

    @Test
    void createsAndNormalizesFirstProfile() {
        // Arrange: no existe un perfil y el request contiene texto sin normalizar.
        Profile input = profile(null, "Keax", "Jimenez", "Developer", "Desarrollador");
        when(repository.getListProfile()).thenReturn(List.of());
        when(repository.saveProfile(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        CreateProfileUseCaseImpl useCase = new CreateProfileUseCaseImpl(repository);

        // Act: se crea el único perfil permitido.
        Profile result = useCase.createProfile(input);

        // Assert: se normalizan los campos y el identificador queda a cargo de JPA.
        assertEquals("KEAX", result.getProfileName());
        assertEquals("JIMENEZ", result.getProfileLastName());
        assertEquals("DEVELOPER", result.getProfileTitle());
        assertNull(result.getProfileId());
        verify(repository).saveProfile(input);
    }

    @Test
    void rejectsSecondProfile() {
        // Arrange: el repositorio ya contiene el perfil único.
        when(repository.getListProfile()).thenReturn(List.of(profile(1L, "KEAX", "JIMENEZ", "DEV", "DEV")));
        CreateProfileUseCaseImpl useCase = new CreateProfileUseCaseImpl(repository);

        // Act y Assert: la regla de unicidad se expresa como conflicto.
        assertThrows(
                ResourceConflictException.class,
                () -> useCase.createProfile(profile(null, "Other", "User", "Title", "Titulo"))
        );
    }

    @Test
    void returnsExistingProfile() {
        // Arrange: existe un perfil persistido.
        Profile stored = profile(1L, "KEAX", "JIMENEZ", "DEV", "DEV");
        when(repository.getListProfile()).thenReturn(List.of(stored));
        RetrieveProfileUseCaseImpl useCase = new RetrieveProfileUseCaseImpl(repository);

        // Act y Assert: se retorna exactamente el agregado encontrado.
        assertSame(stored, useCase.getProfile());
    }

    @Test
    void reportsMissingProfile() {
        // Arrange: el repositorio no contiene registros.
        when(repository.getListProfile()).thenReturn(List.of());
        RetrieveProfileUseCaseImpl useCase = new RetrieveProfileUseCaseImpl(repository);

        // Act y Assert: la ausencia se traduce en una excepción de recurso.
        assertThrows(ResourceNotFoundException.class, useCase::getProfile);
    }

    @Test
    void updatesEditableFieldsAndPreservesPicture() {
        // Arrange: el perfil almacenado ya posee una imagen.
        Profile stored = profile(1L, "OLD", "NAME", "OLD", "ANTIGUO");
        stored.setProfilePicture("https://cdn/picture.jpg");
        Profile changes = profile(null, "Keax", "Jimenez", "Developer", "Desarrollador");
        changes.setProfileCv("https://cdn/cv.pdf");
        changes.setProfileCvEs("https://cdn/cv-es.pdf");
        when(repository.getListProfile()).thenReturn(List.of(stored));
        when(repository.saveProfile(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        UpdateProfileUseCaseImpl useCase = new UpdateProfileUseCaseImpl(repository);

        // Act: se aplican los campos editables.
        Profile result = useCase.updateProfile(changes);

        // Assert: identidad e imagen se conservan y el contenido se normaliza.
        assertEquals(1L, result.getProfileId());
        assertEquals("KEAX", result.getProfileName());
        assertEquals("https://cdn/picture.jpg", result.getProfilePicture());
        assertEquals("https://cdn/cv.pdf", result.getProfileCv());
        assertEquals("https://cdn/cv-es.pdf", result.getProfileCvEs());
    }

    private Profile profile(Long id, String name, String lastName, String title, String titleEs) {
        // Crea el modelo mínimo reutilizado por los escenarios.
        return new Profile(id, name, lastName, title, titleEs, null, null, null);
    }

}
