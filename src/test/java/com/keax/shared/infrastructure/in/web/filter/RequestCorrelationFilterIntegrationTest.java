package com.keax.shared.infrastructure.in.web.filter;

import com.keax.profile.infrastructure.out.persistence.entity.ProfileEntity;
import com.keax.profile.infrastructure.out.persistence.repository.JpaProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Comprueba que el filtro de correlacion agregue o propague X-Request-Id
 * tanto en respuestas exitosas como en respuestas de error/autenticacion.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RequestCorrelationFilterIntegrationTest {

    private final MockMvc mockMvc;
    private final JpaProfileRepository profileRepository;

    RequestCorrelationFilterIntegrationTest(MockMvc mockMvc, JpaProfileRepository profileRepository) {
        this.mockMvc = mockMvc;
        this.profileRepository = profileRepository;
    }

    @Test
    void generatesRequestIdWhenTheClientDoesNotProvideOne() throws Exception {
        // Arrange: se crea un perfil para garantizar una respuesta 200 del endpoint publico.
        profileRepository.saveAndFlush(new ProfileEntity(
                null,
                "KEAX",
                "JIMENEZ",
                "DEVELOPER",
                "DESARROLLADOR",
                null,
                null,
                null
        ));

        // Act: se llama al endpoint sin encabezado de correlacion entrante.
        MvcResult result = mockMvc.perform(get("/api/portfolio/profile"))
                .andExpect(status().isOk())
                .andExpect(header().exists(RequestCorrelationFilter.REQUEST_ID_HEADER))
                .andReturn();

        // Assert: el backend debe generar un identificador no vacio para trazabilidad.
        String generatedRequestId = result.getResponse().getHeader(RequestCorrelationFilter.REQUEST_ID_HEADER);
        assertFalse(generatedRequestId == null || generatedRequestId.isBlank());
    }

    @Test
    void preservesIncomingRequestIdEvenWhenTheRequestEndsAsUnauthorized() throws Exception {
        // Arrange: se usa un valor conocido para comprobar propagacion exacta.
        String incomingRequestId = "request-id-from-client";

        // Act y Assert: aunque la seguridad rechace la peticion, el encabezado debe sobrevivir.
        mockMvc.perform(get("/api/skill")
                        .header(RequestCorrelationFilter.REQUEST_ID_HEADER, incomingRequestId))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(
                        RequestCorrelationFilter.REQUEST_ID_HEADER,
                        incomingRequestId
                ));
    }

}
