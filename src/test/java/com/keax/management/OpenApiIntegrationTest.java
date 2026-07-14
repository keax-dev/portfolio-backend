package com.keax.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica que la documentación OpenAPI y la interfaz Swagger UI queden
 * expuestas públicamente y describan correctamente los endpoints con JWT.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OpenApiIntegrationTest {

    private final MockMvc mockMvc;

    OpenApiIntegrationTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void exposesSwaggerUiWithoutAuthentication() throws Exception {
        // Act y Assert: la interfaz HTML de Swagger UI debe estar pública.
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Swagger UI")));
    }

    @Test
    void exposesOpenApiSpecAndDocumentsJwtSecurity() throws Exception {
        // Act y Assert: el documento OpenAPI debe estar disponible sin autenticación.
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title").value("Keax Portfolio API"))
                .andExpect(jsonPath("$.components.securitySchemes.BearerAuth.scheme").value("bearer"))
                .andExpect(jsonPath("$.paths['/api/skill'].get.security[0].BearerAuth").exists())
                .andExpect(jsonPath("$.paths['/api/portfolio/profile'].get.security").doesNotExist())
                .andExpect(jsonPath("$.paths['/api/visitor'].post.security").doesNotExist())
                .andExpect(jsonPath("$.paths['/api/visitor'].get.security[0].BearerAuth").exists());
    }

}
