package com.keax.portfolio.infrastructure.in.web.controller;

import com.keax.education.domain.model.Education;
import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.email.domain.model.Contact;
import com.keax.email.domain.ports.in.ContactEmailUseCase;
import com.keax.email.infrastructure.in.web.ratelimit.ContactRateLimiter;
import com.keax.profile.domain.model.Profile;
import com.keax.profile.domain.ports.in.RetrieveProfileUseCase;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectImage;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.shared.infrastructure.in.web.exception.GlobalExceptionHandler;
import com.keax.skill.domain.model.Skill;
import com.keax.skill.domain.ports.in.RetrieveSkillUseCase;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.socialnetwork.domain.ports.in.RetrieveSocialNetworkUseCase;
import com.keax.technology.domain.model.Technology;
import com.keax.technology.domain.ports.in.RetrieveTechnologyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica el contrato HTTP público del portafolio: composición de sus cinco
 * secciones, envío de contacto, validación y delegación al rate limiter.
 */
@ExtendWith(MockitoExtension.class)
class PortfolioControllerTest {

    @Mock
    private RetrieveProfileUseCase retrieveProfileUseCase;
    @Mock
    private RetrieveEducationUseCase retrieveEducationUseCase;
    @Mock
    private RetrieveSkillUseCase retrieveSkillUseCase;
    @Mock
    private RetrieveTechnologyUseCase retrieveTechnologyUseCase;
    @Mock
    private RetrieveProjectUseCase retrieveProjectUseCase;
    @Mock
    private RetrieveSocialNetworkUseCase retrieveSocialNetworkUseCase;
    @Mock
    private ContactEmailUseCase contactEmailUseCase;
    @Mock
    private ContactRateLimiter contactRateLimiter;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Arrange común: se conectan los puertos simulados al controlador.
        PortfolioController controller = new PortfolioController(
                retrieveProfileUseCase,
                retrieveEducationUseCase,
                retrieveSkillUseCase,
                retrieveTechnologyUseCase,
                retrieveProjectUseCase,
                retrieveSocialNetworkUseCase,
                contactEmailUseCase,
                contactRateLimiter
        );

        // Se levanta únicamente Spring MVC para probar rutas, JSON y Bean Validation.
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void exposesEveryPublicPortfolioSection() throws Exception {
        // Arrange: cada puerto entrega un modelo representativo, incluidas relaciones anidadas.
        Project project = new Project(
                41L, "PORTFOLIO", "PORTAFOLIO", "Description", "Descripción",
                1, false,
                List.of(new ProjectTechnology(61L, 31L, "JAVA", 1)),
                List.of(new ProjectLink(71L, ProjectLinkType.DEPLOY, "https://deploy.test", 1)),
                List.of(new ProjectImage(81L, "project.png", 1))
        );
        when(retrieveProfileUseCase.getProfile()).thenReturn(new Profile(
                1L, "KEAX", "JIMENEZ", "DEVELOPER", "DESARROLLADOR", "cv", "profile.png"
        ));
        when(retrieveEducationUseCase.findByEducationDeleted(false)).thenReturn(List.of(new Education(
                11L, "DEGREE", "TÍTULO", "UNIVERSITY", "2020", "2020",
                "2024", "2024", 1, false, 10L, "UNIVERSITY", "UNIVERSIDAD", "url"
        )));
        when(retrieveSkillUseCase.findBySkillDeleted(false))
                .thenReturn(List.of(new Skill(21L, "SPRING", "skill.png", 1, false)));
        when(retrieveTechnologyUseCase.findByTechnologyDeleted(false))
                .thenReturn(List.of(new Technology(31L, "JAVA", false)));
        when(retrieveProjectUseCase.findByProjectDeleted(false)).thenReturn(List.of(project));
        when(retrieveSocialNetworkUseCase.findBySocialNetworkDeleted(false)).thenReturn(List.of(
                new SocialNetwork(51L, "GITHUB", "github", "#fff", 1, "https://github.test", false)
        ));

        // Act y Assert: cada ruta conserva el envelope y los nombres JSON públicos.
        mockMvc.perform(get("/api/portfolio/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("KEAX"));
        mockMvc.perform(get("/api/portfolio/education"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].institution_name").value("UNIVERSITY"));
        mockMvc.perform(get("/api/portfolio/skill"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].picture").value("skill.png"));
        mockMvc.perform(get("/api/portfolio/technology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("JAVA"))
                .andExpect(jsonPath("$.data[0].position").doesNotExist());
        mockMvc.perform(get("/api/portfolio/project"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value("PORTFOLIO"))
                .andExpect(jsonPath("$.data[0].technologies[0].name").value("JAVA"))
                .andExpect(jsonPath("$.data[0].links[0].type").value("DEPLOY"))
                .andExpect(jsonPath("$.data[0].picture").doesNotExist())
                .andExpect(jsonPath("$.data[0].images[0].url").value("project.png"));
        mockMvc.perform(get("/api/portfolio/socialNetwork"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].url").value("https://github.test"));

        // Assert adicional: los filtros de borrado lógico siempre se aplican al contenido público.
        verify(retrieveEducationUseCase).findByEducationDeleted(false);
        verify(retrieveSkillUseCase).findBySkillDeleted(false);
        verify(retrieveTechnologyUseCase).findByTechnologyDeleted(false);
        verify(retrieveProjectUseCase).findByProjectDeleted(false);
        verify(retrieveSocialNetworkUseCase).findBySocialNetworkDeleted(false);
    }

    @Test
    void acceptsAValidContactAndUsesTheRemoteAddressForRateLimiting() throws Exception {
        // Arrange: el caso de uso devuelve el contacto que logró enviar.
        when(contactEmailUseCase.sendContactEmail(org.mockito.ArgumentMatchers.any(Contact.class)))
                .thenReturn(new Contact("Ana", "ana@example.com", "Hello"));

        // Act: se envía JSON válido desde una IP conocida.
        mockMvc.perform(post("/api/portfolio/contact")
                        .with(request -> {
                            request.setRemoteAddr("198.51.100.25");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Ana","email":"ana@example.com","message":"Hello"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("ana@example.com"));

        // Assert: primero se limita por IP y luego se entrega el dominio correctamente mapeado.
        verify(contactRateLimiter).assertAllowed("198.51.100.25");
        ArgumentCaptor<Contact> contactCaptor = ArgumentCaptor.forClass(Contact.class);
        verify(contactEmailUseCase).sendContactEmail(contactCaptor.capture());
        assertEquals("Hello", contactCaptor.getValue().getMessage());
    }

    @Test
    void rejectsAnInvalidContactBeforeInvokingApplicationServices() throws Exception {
        // Arrange: el payload tiene email inválido y campos obligatorios vacíos.
        String invalidContact = """
                {"name":"","email":"not-an-email","message":""}
                """;

        // Act y Assert: Bean Validation traduce el error al contrato global.
        mockMvc.perform(post("/api/portfolio/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidContact))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.alert").value("Validation error"));

        // Assert adicional: una solicitud inválida nunca consume cuota ni intenta enviar correo.
        verifyNoInteractions(contactRateLimiter);
        verify(contactEmailUseCase, never()).sendContactEmail(org.mockito.ArgumentMatchers.any());
    }
}
