package com.keax.uploadimage.application.usecases;

import com.keax.institution.domain.model.Institution;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.profile.domain.model.Profile;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectImage;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.domain.ports.out.SkillRepositoryPort;
import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica los cuatro flujos de subida y su compensación: validar recurso,
 * almacenar la nueva imagen, persistir URL, eliminar la anterior y limpiar
 * la nueva cuando la actualización falla.
 */
class UploadImageUseCasesTest {

    private ImageStoragePort storage;
    private ProfileRepositoryPort profileRepository;
    private SkillRepositoryPort skillRepository;
    private ProjectRepositoryPort projectRepository;
    private InstitutionRepositoryPort institutionRepository;
    private ImageFile image;

    @BeforeEach
    void setUp() {
        // Se recrean todos los puertos y una imagen JPEG válida.
        storage = mock(ImageStoragePort.class);
        profileRepository = mock(ProfileRepositoryPort.class);
        skillRepository = mock(SkillRepositoryPort.class);
        projectRepository = mock(ProjectRepositoryPort.class);
        institutionRepository = mock(InstitutionRepositoryPort.class);
        image = new ImageFile(
                "photo.jpg",
                "image/jpeg",
                new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00}
        );
    }

    @Test
    void uploadsProfilePictureAndDeletesPreviousImage() {
        // Arrange: existe perfil con una imagen anterior.
        Profile profile = new Profile(1L, "KEAX", "JIMENEZ", "DEV", "DEV", null, "old-url");
        when(profileRepository.getListProfile()).thenReturn(List.of(profile));
        when(storage.upload(image, "Profile")).thenReturn("new-url");
        when(profileRepository.saveProfile(any())).thenAnswer(invocation -> invocation.getArgument(0));
        UploadImageProfileUseCaseImpl useCase = new UploadImageProfileUseCaseImpl(profileRepository, storage);

        // Act: se reemplaza la imagen.
        Profile result = useCase.uploadImageProfile(image);

        // Assert: primero se persiste la URL nueva y después se elimina la anterior.
        assertEquals("new-url", result.getProfilePicture());
        InOrder order = inOrder(profileRepository, storage);
        order.verify(storage).upload(image, "Profile");
        order.verify(profileRepository).saveProfile(profile);
        order.verify(storage).delete("old-url");
    }

    @Test
    void rejectsProfileUploadWhenProfileDoesNotExist() {
        // Arrange: el portafolio todavía no tiene perfil.
        when(profileRepository.getListProfile()).thenReturn(List.of());
        UploadImageProfileUseCaseImpl useCase = new UploadImageProfileUseCaseImpl(profileRepository, storage);

        // Act y Assert: no se consume Cloudinary para un recurso inexistente.
        assertThrows(ResourceNotFoundException.class, () -> useCase.uploadImageProfile(image));
        verify(storage, never()).upload(any(), any());
    }

    @Test
    void uploadsSkillImage() {
        // Arrange: existe la habilidad y Cloudinary devuelve una URL segura.
        Skill skill = new Skill(1L, "JAVA", "old-url", 1, false);
        when(skillRepository.findBySkillIdAndSkillDeleted(1L, false)).thenReturn(Optional.of(skill));
        when(storage.upload(image, "Skills")).thenReturn("new-url");
        when(skillRepository.updateSkill(any())).thenAnswer(invocation -> invocation.getArgument(0));
        UploadImageSkillUseCaseImpl useCase = new UploadImageSkillUseCaseImpl(skillRepository, storage);

        // Act: se reemplaza la imagen.
        Skill result = useCase.uploadImageSkill(1L, image);

        // Assert: se actualiza el recurso y se limpia la imagen anterior.
        assertEquals("new-url", result.getSkillPicture());
        verify(storage).delete("old-url");
    }

    @Test
    void deletesNewProjectImageWhenDatabaseUpdateFails() {
        // Arrange: la carga remota funciona, pero persistir el proyecto falla.
        Project project = new Project(
                1L, "PROJECT", "PROYECTO", "Description", "Descripción",
                1, false, List.of(), List.of(), new java.util.ArrayList<>(List.of(
                        new ProjectImage(1L, "old-url", 1)
                ))
        );
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(project));
        when(storage.upload(image, "Projects")).thenReturn("new-url");
        when(projectRepository.updateProject(any())).thenThrow(new IllegalStateException("database down"));
        UploadImageProjectUseCaseImpl useCase = new UploadImageProjectUseCaseImpl(projectRepository, storage);

        // Act: se produce el fallo posterior a la carga.
        ExternalServiceException exception = assertThrows(
                ExternalServiceException.class,
                () -> useCase.uploadProjectImages(1L, List.of(image))
        );

        // Assert: la imagen huérfana se elimina y se conserva la causa.
        verify(storage).delete("new-url");
        assertEquals("database down", exception.getCause().getMessage());
    }

    @Test
    void appendsProjectImagesUpToTheMaximum() {
        Project project = new Project(
                1L, "PROJECT", "PROYECTO", "Description", "DescripciÃ³n",
                1, false, List.of(), List.of(), new java.util.ArrayList<>(List.of(
                        new ProjectImage(1L, "first-url", 1)
                ))
        );
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(project));
        when(storage.upload(image, "Projects")).thenReturn("second-url");
        when(projectRepository.updateProject(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = new UploadImageProjectUseCaseImpl(projectRepository, storage)
                .uploadProjectImages(1L, List.of(image));

        assertEquals(2, result.getProjectImages().size());
        assertEquals("second-url", result.getProjectImages().get(1).getUrl());
    }

    @Test
    void rejectsMoreThanThreeProjectImagesAndProtectsTheLastImage() {
        Project project = new Project(
                1L, "PROJECT", "PROYECTO", "Description", "DescripciÃ³n",
                1, false, List.of(), List.of(), new java.util.ArrayList<>(List.of(
                        new ProjectImage(1L, "first-url", 1)
                ))
        );
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(project));
        UploadImageProjectUseCaseImpl useCase = new UploadImageProjectUseCaseImpl(projectRepository, storage);

        assertThrows(
                ResourceConflictException.class,
                () -> useCase.uploadProjectImages(1L, List.of(image, image, image))
        );
        assertThrows(ResourceConflictException.class, () -> useCase.deleteProjectImage(1L, 1L));
        verify(storage, never()).upload(any(), any());
    }

    @Test
    void deletesAProjectImageAndKeepsTheRemainingOrder() {
        Project project = new Project(
                1L, "PROJECT", "PROYECTO", "Description", "DescripciÃ³n",
                1, false, List.of(), List.of(), new java.util.ArrayList<>(List.of(
                        new ProjectImage(10L, "first-url", 1),
                        new ProjectImage(11L, "second-url", 2)
                ))
        );
        when(projectRepository.findByProjectIdAndProjectDeleted(1L, false))
                .thenReturn(Optional.of(project));
        when(projectRepository.updateProject(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Project result = new UploadImageProjectUseCaseImpl(projectRepository, storage)
                .deleteProjectImage(1L, 10L);

        assertEquals(1, result.getProjectImages().size());
        assertEquals(2, result.getProjectImages().getFirst().getPosition());
        verify(storage).delete("first-url");
    }

    @Test
    void uploadsInstitutionImage() {
        // Arrange: existe la institución y su imagen previa.
        Institution institution = new Institution(1L, "UNI", "UNI", "old-url", false);
        when(institutionRepository.findByInstitutionIdAndInstitutionDeleted(1L, false))
                .thenReturn(Optional.of(institution));
        when(storage.upload(image, "Institutions")).thenReturn("new-url");
        when(institutionRepository.updateInstitution(any())).thenAnswer(invocation -> invocation.getArgument(0));
        UploadImageInstitutionUseCaseImpl useCase = new UploadImageInstitutionUseCaseImpl(
                institutionRepository,
                storage
        );

        // Act: se actualiza la imagen institucional.
        Institution result = useCase.uploadImageInstitution(1L, image);

        // Assert: la URL nueva queda persistida y la anterior se limpia.
        assertEquals("new-url", result.getInstitutionUrl());
        verify(storage).delete("old-url");
    }

}
