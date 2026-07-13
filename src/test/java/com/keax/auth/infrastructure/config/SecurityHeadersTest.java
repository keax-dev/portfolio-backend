package com.keax.auth.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * Integra la cadena de Spring Security para comprobar que las respuestas HTTP
 * incluyen las cabeceras defensivas configuradas por defecto.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class SecurityHeadersTest {

    private final MockMvc mockMvc;

    SecurityHeadersTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void sendsDenyFrameOptionsHeader() throws Exception {
        // Act y Assert: una ruta publica no puede ser embebida en un frame.
        mockMvc.perform(get("/api/portfolio/skill"))
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

}
