package com.keax.uploadimage.infrastructure.in.web.controller;

import com.keax.institution.domain.model.Institution;
import com.keax.profile.domain.model.Profile;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectImage;
import com.keax.skill.domain.model.Skill;
import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.uploadimage.domain.ports.in.UploadImageInstitutionUseCase;
import com.keax.uploadimage.domain.ports.in.UploadImageProfileUseCase;
import com.keax.uploadimage.domain.ports.in.UploadImageProjectUseCase;
import com.keax.uploadimage.domain.ports.in.UploadImageSkillUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica las cuatro rutas multipart de imágenes y que el adaptador HTTP
 * convierta nombre, MIME y bytes antes de invocar cada caso de uso.
 */
@ExtendWith(MockitoExtension.class)
class UploadImageControllerTest {

    @Mock
    private UploadImageInstitutionUseCase uploadImageInstitutionUseCase;
    @Mock
    private UploadImageProfileUseCase uploadImageProfileUseCase;
    @Mock
    private UploadImageSkillUseCase uploadImageSkillUseCase;
    @Mock
    private UploadImageProjectUseCase uploadImageProjectUseCase;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Arrange común: se inyectan los cuatro puertos sin cargar el contexto completo.
        UploadImageController controller = new UploadImageController(
                uploadImageInstitutionUseCase,
                uploadImageProfileUseCase,
                uploadImageSkillUseCase,
                uploadImageProjectUseCase
        );
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void uploadsInstitutionImageThroughMultipartContract() throws Exception {
        // Arrange: el puerto devuelve la institución con su nueva URL.
        when(uploadImageInstitutionUseCase.uploadImageInstitution(
                org.mockito.ArgumentMatchers.eq(10L), any(ImageFile.class)
        )).thenReturn(new Institution(10L, "UNIVERSITY", "UNIVERSIDAD", "image-url", false));

        // Act y Assert: se conserva el id de ruta y la respuesta pública.
        mockMvc.perform(multipart("/api/image/institution/{id}", 10L).file(image()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.url").value("image-url"));

        // Assert adicional: se valida la conversión del archivo recibida por el dominio.
        verifyImagePassedToInstitution();
    }

    @Test
    void uploadsProfileImageThroughMultipartContract() throws Exception {
        // Arrange: el puerto devuelve el perfil actualizado.
        when(uploadImageProfileUseCase.uploadImageProfile(any(ImageFile.class))).thenReturn(
                new Profile(1L, "KEAX", "JIMENEZ", "DEV", "DESARROLLADOR", "cv", "image-url")
        );

        // Act y Assert: la ruta sin id serializa la imagen actualizada.
        mockMvc.perform(multipart("/api/image/profile").file(image()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.image").value("image-url"));

        // Assert adicional: se llamó exactamente al puerto correspondiente.
        verify(uploadImageProfileUseCase).uploadImageProfile(any(ImageFile.class));
    }

    @Test
    void uploadsSkillImageThroughMultipartContract() throws Exception {
        // Arrange: la habilidad contiene la URL resultante.
        when(uploadImageSkillUseCase.uploadImageSkill(
                org.mockito.ArgumentMatchers.eq(20L), any(ImageFile.class)
        )).thenReturn(new Skill(20L, "JAVA", "image-url", 1, false));

        // Act y Assert: el path variable y la imagen llegan al endpoint correcto.
        mockMvc.perform(multipart("/api/image/skill/{id}", 20L).file(image()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.picture").value("image-url"));

        // Assert adicional: se delegó usando el identificador solicitado.
        verify(uploadImageSkillUseCase).uploadImageSkill(
                org.mockito.ArgumentMatchers.eq(20L), any(ImageFile.class)
        );
    }

    @Test
    void uploadsProjectImageThroughMultipartContract() throws Exception {
        // Arrange: el proyecto simula una imagen persistida por el proveedor externo.
        when(uploadImageProjectUseCase.uploadProjectImages(
                org.mockito.ArgumentMatchers.eq(30L), anyList()
        )).thenReturn(new Project(
                30L, "PORTFOLIO", "PORTAFOLIO", "Description", "Descripción",
                1, false, List.of(), List.of(), List.of(
                        new ProjectImage(1L, "image-url", 1)
                )
        ));

        // Act y Assert: el controlador responde con el DTO de proyecto.
        mockMvc.perform(multipart("/api/image/project/{id}", 30L).file(projectImage()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.images[0].url").value("image-url"));

        // Assert adicional: se delegó al caso de uso de proyecto.
        verify(uploadImageProjectUseCase).uploadProjectImages(
                org.mockito.ArgumentMatchers.eq(30L), anyList()
        );
    }

    @Test
    void deletesAProjectImageThroughItsDedicatedContract() throws Exception {
        when(uploadImageProjectUseCase.deleteProjectImage(30L, 8L)).thenReturn(new Project(
                30L, "PORTFOLIO", "PORTAFOLIO", "Description", "DescripciÃ³n",
                1, false, List.of(), List.of(), List.of(
                        new ProjectImage(9L, "remaining-url", 1)
                )
        ));

        mockMvc.perform(delete("/api/image/project/{projectId}/{imageId}", 30L, 8L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.images[0].url").value("remaining-url"));

        verify(uploadImageProjectUseCase).deleteProjectImage(30L, 8L);
    }

    private MockMultipartFile image() {
        // Crea una imagen mínima y determinista para todas las solicitudes multipart.
        return new MockMultipartFile("image", "avatar.png", "image/png", new byte[]{1, 2, 3});
    }

    private MockMultipartFile projectImage() {
        return new MockMultipartFile("images", "project.png", "image/png", new byte[]{1, 2, 3});
    }

    private void verifyImagePassedToInstitution() {
        // Captura el value object para probar el mapper MultipartFile -> ImageFile.
        ArgumentCaptor<ImageFile> imageCaptor = ArgumentCaptor.forClass(ImageFile.class);
        verify(uploadImageInstitutionUseCase).uploadImageInstitution(
                org.mockito.ArgumentMatchers.eq(10L), imageCaptor.capture()
        );
        assertEquals("avatar.png", imageCaptor.getValue().getOriginalName());
        assertEquals("image/png", imageCaptor.getValue().getContentType());
        assertArrayEquals(new byte[]{1, 2, 3}, imageCaptor.getValue().getBytes());
    }
}
