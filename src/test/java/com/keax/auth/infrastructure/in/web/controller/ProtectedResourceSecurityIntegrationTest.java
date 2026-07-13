package com.keax.auth.infrastructure.in.web.controller;

import com.keax.auth.infrastructure.out.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica la matriz negativa de seguridad sobre recursos protegidos:
 * sin token, con token mal formado y con un subject no existente.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ProtectedResourceSecurityIntegrationTest {

    private final MockMvc mockMvc;
    private final JwtUtil jwtUtil;

    ProtectedResourceSecurityIntegrationTest(MockMvc mockMvc, JwtUtil jwtUtil) {
        this.mockMvc = mockMvc;
        this.jwtUtil = jwtUtil;
    }

    @Test
    void rejectsProtectedManagementEndpointWithoutJwt() throws Exception {
        // Act y Assert: un recurso administrativo no debe aceptar llamadas anonimas.
        mockMvc.perform(get("/api/skill"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsProtectedManagementEndpointWithMalformedJwt() throws Exception {
        // Act y Assert: un bearer con formato incorrecto no debe autenticar la peticion.
        mockMvc.perform(get("/api/skill")
                        .header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void rejectsProtectedManagementEndpointWhenJwtSubjectDoesNotExist() throws Exception {
        // Arrange: se genera un JWT valido criptograficamente, pero su usuario no existe en BD.
        String tokenForUnknownUser = jwtUtil.generateToken("ghost-admin");

        // Act y Assert: el filtro limpia el contexto y Spring Security devuelve 401.
        mockMvc.perform(get("/api/skill")
                        .header("Authorization", "Bearer " + tokenForUnknownUser))
                .andExpect(status().isUnauthorized());
    }

}
