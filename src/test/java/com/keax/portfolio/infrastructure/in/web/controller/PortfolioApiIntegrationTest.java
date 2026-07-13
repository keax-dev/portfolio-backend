package com.keax.portfolio.infrastructure.in.web.controller;

import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import com.keax.profile.infrastructure.out.persistence.entity.ProfileEntity;
import com.keax.profile.infrastructure.out.persistence.repository.JpaProfileRepository;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.skill.infrastructure.out.persistence.entity.SkillEntity;
import com.keax.skill.infrastructure.out.persistence.repository.JpaSkillRepository;
import com.keax.socialnetwork.infrastructure.out.persistence.entity.SocialNetworkEntity;
import com.keax.socialnetwork.infrastructure.out.persistence.repository.JpaSocialNetworkRepository;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integra el portafolio publico contra el stack real del backend: MVC,
 * validacion, casos de uso, mapeos y persistencia en H2.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PortfolioApiIntegrationTest {

    private final MockMvc mockMvc;
    private final JpaProfileRepository profileRepository;
    private final JpaInstitutionRepository institutionRepository;
    private final JpaEducationRepository educationRepository;
    private final JpaSkillRepository skillRepository;
    private final JpaTechnologyRepository technologyRepository;
    private final JpaProjectRepository projectRepository;
    private final JpaSocialNetworkRepository socialNetworkRepository;
    private final EntityManager entityManager;

    PortfolioApiIntegrationTest(
            MockMvc mockMvc,
            JpaProfileRepository profileRepository,
            JpaInstitutionRepository institutionRepository,
            JpaEducationRepository educationRepository,
            JpaSkillRepository skillRepository,
            JpaTechnologyRepository technologyRepository,
            JpaProjectRepository projectRepository,
            JpaSocialNetworkRepository socialNetworkRepository,
            EntityManager entityManager
    ) {
        this.mockMvc = mockMvc;
        this.profileRepository = profileRepository;
        this.institutionRepository = institutionRepository;
        this.educationRepository = educationRepository;
        this.skillRepository = skillRepository;
        this.technologyRepository = technologyRepository;
        this.projectRepository = projectRepository;
        this.socialNetworkRepository = socialNetworkRepository;
        this.entityManager = entityManager;
    }

    @Test
    void servesOnlyActivePortfolioDataThroughRealPersistence() throws Exception {
        // Arrange: se persiste un conjunto mixto de registros activos y eliminados logicamente.
        profileRepository.saveAndFlush(new ProfileEntity(
                null,
                "KEAX",
                "JIMENEZ",
                "DEVELOPER",
                "DESARROLLADOR",
                "https://example.com/cv",
                "profile.png"
        ));

        InstitutionEntity activeInstitution = institutionRepository.saveAndFlush(new InstitutionEntity(
                null,
                "UNIVERSITY",
                "UNIVERSIDAD",
                "https://university.test",
                false
        ));
        institutionRepository.saveAndFlush(new InstitutionEntity(
                null,
                "DELETED UNIVERSITY",
                "UNIVERSIDAD ELIMINADA",
                "https://deleted.test",
                true
        ));

        educationRepository.saveAndFlush(new EducationEntity(
                null,
                "DEGREE",
                "TITLE",
                "UNIVERSITY",
                "2020",
                "2020",
                "2024",
                "2024",
                1,
                false,
                activeInstitution
        ));
        educationRepository.saveAndFlush(new EducationEntity(
                null,
                "DELETED DEGREE",
                "DELETED TITLE",
                "UNIVERSITY",
                "2018",
                "2018",
                "2019",
                "2019",
                2,
                true,
                activeInstitution
        ));

        skillRepository.saveAndFlush(new SkillEntity(null, "SPRING", "skill.png", 1, false));
        skillRepository.saveAndFlush(new SkillEntity(null, "DELETED SKILL", "deleted-skill.png", 2, true));

        TechnologyEntity activeTechnology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "JAVA",
                1,
                false,
                new ArrayList<>()
        ));
        TechnologyEntity deletedTechnology = technologyRepository.saveAndFlush(new TechnologyEntity(
                null,
                "LEGACY",
                2,
                true,
                new ArrayList<>()
        ));

        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "PORTFOLIO",
                "PORTAFOLIO",
                "Backend portfolio",
                "Portafolio backend",
                "project.png",
                "https://deploy.test",
                "https://github.test",
                1,
                false,
                activeTechnology
        ));
        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "DELETED PROJECT",
                "PROYECTO ELIMINADO",
                "Deleted backend portfolio",
                "Portafolio eliminado",
                "deleted-project.png",
                "https://deleted-deploy.test",
                "https://deleted-github.test",
                2,
                true,
                activeTechnology
        ));
        projectRepository.saveAndFlush(new ProjectEntity(
                null,
                "HIDDEN PROJECT",
                "PROYECTO OCULTO",
                "Should not appear because parent technology is deleted",
                "No debe aparecer",
                "hidden-project.png",
                "https://hidden.test",
                "https://hidden-github.test",
                1,
                false,
                deletedTechnology
        ));

        socialNetworkRepository.saveAndFlush(new SocialNetworkEntity(
                null,
                "GITHUB",
                "github",
                "#ffffff",
                1,
                "https://github.com/keax",
                false
        ));
        socialNetworkRepository.saveAndFlush(new SocialNetworkEntity(
                null,
                "DELETED NETWORK",
                "ban",
                "#000000",
                2,
                "https://deleted.test",
                true
        ));

        // Se limpia el primer nivel de cache para que las lecturas HTTP usen consultas reales a BD.
        entityManager.flush();
        entityManager.clear();

        // Act y Assert: cada endpoint publico debe exponer unicamente datos activos.
        mockMvc.perform(get("/api/portfolio/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.name").value("KEAX"));

        mockMvc.perform(get("/api/portfolio/education"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].title").value("DEGREE"));

        mockMvc.perform(get("/api/portfolio/skill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("SPRING"));

        mockMvc.perform(get("/api/portfolio/technology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("JAVA"))
                .andExpect(jsonPath("$.data[0].projects.length()").value(1))
                .andExpect(jsonPath("$.data[0].projects[0].title").value("PORTFOLIO"));

        mockMvc.perform(get("/api/portfolio/socialNetwork"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].name").value("GITHUB"));
    }

    @Test
    void returnsNotFoundWhenThePublicProfileHasNotBeenCreatedYet() throws Exception {
        // Act y Assert: el endpoint publico debe traducir el dominio vacio al contrato global de error.
        mockMvc.perform(get("/api/portfolio/profile"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.alert").value("The profile is not created"));
    }

    @Test
    void rejectsInvalidPublicContactPayloadBeforeReachingEmailSending() throws Exception {
        // Arrange: se envia un contacto estructuralmente invalido para activar Bean Validation real.
        String invalidContact = """
                {"name":"","email":"invalid-email","message":""}
                """;

        // Act y Assert: la API publica responde 400 con el formato global de errores.
        mockMvc.perform(post("/api/portfolio/contact")
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.40");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContact))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.alert").value("Validation error"))
                .andExpect(jsonPath("$.messages").isArray());
    }

}
