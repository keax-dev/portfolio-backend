package com.keax.portfolio.infrastructure.in.web.controller;

import com.keax.course.domain.model.Course;
import com.keax.education.domain.model.Education;
import com.keax.email.domain.model.Contact;
import com.keax.email.domain.ports.in.ContactEmailUseCase;
import com.keax.email.infrastructure.in.web.ratelimit.ContactRateLimiter;
import com.keax.profile.domain.model.Profile;
import com.keax.portfolio.domain.ports.in.PortfolioQueryUseCase;
import com.keax.project.domain.model.Project;
import com.keax.project.domain.model.ProjectLink;
import com.keax.project.domain.model.ProjectImage;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.shared.infrastructure.in.web.exception.GlobalExceptionHandler;
import com.keax.skill.domain.model.Skill;
import com.keax.socialnetwork.domain.model.SocialNetwork;
import com.keax.technology.domain.model.Technology;
import com.keax.shared.infrastructure.in.web.client.ClientIdentityHasher;
import com.keax.shared.infrastructure.in.web.client.ClientIpResolver;
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
    private PortfolioQueryUseCase portfolioQuery;
    @Mock
    private ContactEmailUseCase contactEmailUseCase;
    @Mock
    private ContactRateLimiter contactRateLimiter;
    @Mock
    private ClientIpResolver clientIpResolver;
    @Mock
    private ClientIdentityHasher clientIdentityHasher;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Arrange común: se conectan los puertos simulados al controlador.
        PortfolioController controller = new PortfolioController(
                portfolioQuery,
                contactEmailUseCase,
                contactRateLimiter,
                clientIpResolver,
                clientIdentityHasher
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
        when(portfolioQuery.getProfile()).thenReturn(new Profile(
                1L, "KEAX", "JIMENEZ", "DEVELOPER", "DESARROLLADOR", "cv", "cv-es", "profile.png"
        ));
        when(portfolioQuery.getEducation()).thenReturn(List.of(new Education(
                11L, "DEGREE", "TÍTULO", "UNIVERSITY", "2020", "2020",
                "2024", "2024", 1, 10L, "UNIVERSITY", "UNIVERSIDAD", "url", null
        )));
        when(portfolioQuery.getCourses()).thenReturn(List.of(new Course(
                12L,
                "SPRING BOOT DESDE CERO",
                "SPRING BOOT FROM SCRATCH",
                "certificate.png",
                "https://udemy.test/certificate/12",
                2,
                10L,
                "UDEMY",
                "UDEMY",
                null
        )));
        when(portfolioQuery.getSkills())
                .thenReturn(List.of(new Skill(21L, "SPRING", "skill.png", 1, null)));
        when(portfolioQuery.getTechnologies())
                .thenReturn(List.of(new Technology(31L, "JAVA", null)));
        when(portfolioQuery.getPublishedProjects()).thenReturn(List.of(project));
        when(portfolioQuery.getSocialNetworks()).thenReturn(List.of(
                new SocialNetwork(51L, "GITHUB", "github", "#fff", 1, "https://github.test", null)
        ));

        // Act y Assert: cada ruta conserva el envelope y los nombres JSON públicos.
        mockMvc.perform(get("/api/portfolio/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("KEAX"));
        mockMvc.perform(get("/api/portfolio/education"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].institution_name").value("UNIVERSITY"));
        mockMvc.perform(get("/api/portfolio/course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("SPRING BOOT DESDE CERO"))
                .andExpect(jsonPath("$.data[0].name_en").value("SPRING BOOT FROM SCRATCH"))
                .andExpect(jsonPath("$.data[0].certificate_img").value("certificate.png"))
                .andExpect(jsonPath("$.data[0].certificate_url")
                        .value("https://udemy.test/certificate/12"))
                .andExpect(jsonPath("$.data[0].position").value(2));
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
        verify(portfolioQuery).getEducation();
        verify(portfolioQuery).getCourses();
        verify(portfolioQuery).getSkills();
        verify(portfolioQuery).getTechnologies();
        verify(portfolioQuery).getPublishedProjects();
        verify(portfolioQuery).getSocialNetworks();
    }

    @Test
    void acceptsAValidContactAndUsesTheRemoteAddressForRateLimiting() throws Exception {
        // Arrange: el caso de uso devuelve el contacto que logró enviar.
        when(contactEmailUseCase.sendContactEmail(org.mockito.ArgumentMatchers.any(Contact.class)))
                .thenReturn(new Contact("Ana", "ana@example.com", "Hello"));
        when(clientIpResolver.resolve(org.mockito.ArgumentMatchers.any())).thenReturn("198.51.100.25");
        when(clientIdentityHasher.hash("198.51.100.25")).thenReturn("anonymous-client-key");

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
        verify(contactRateLimiter).assertAllowed("anonymous-client-key");
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
