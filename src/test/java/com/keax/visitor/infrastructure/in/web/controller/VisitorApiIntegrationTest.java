package com.keax.visitor.infrastructure.in.web.controller;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.auth.infrastructure.out.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integra HTTP, seguridad JWT, controlador, casos de uso y persistencia para
 * comprobar el contrato publico y administrativo del modulo visitor.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VisitorApiIntegrationTest {

    private final MockMvc mockMvc;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    VisitorApiIntegrationTest(
            MockMvc mockMvc,
            JpaUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Test
    void allowsAnonymousVisitorRegistration() throws Exception {
        // Arrange: se crea una peticion anonima similar a la enviada por el frontend.
        String body = """
                {
                  "path": "/portfolio"
                }
                """;

        // Act y Assert: el endpoint publico persiste la visita y devuelve su contrato.
        mockMvc.perform(post("/api/visitor")
                        .with(request -> {
                            request.setRemoteAddr("203.0.113.10");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", "JUnit")
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.ip").value(
                        org.hamcrest.Matchers.matchesPattern("[0-9a-f]{12}\\.\\.\\.")
                ))
                .andExpect(jsonPath("$.data.country").doesNotExist())
                .andExpect(jsonPath("$.data.city").doesNotExist())
                .andExpect(jsonPath("$.data.path").value("/portfolio"));
    }

    @Test
    void rejectsAnonymousAccessToVisitorDashboard() throws Exception {
        // Act y Assert: las estadisticas con PII continúan protegidas.
        mockMvc.perform(get("/api/visitor/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsAuthenticatedVisitorListWithValidJwt() throws Exception {
        // Arrange: se persiste el usuario que el filtro cargara desde el subject del JWT.
        String token = createAuthenticatedUserToken();

        // Act y Assert: la cadena completa autentica y permite el recurso protegido.
        mockMvc.perform(get("/api/visitor")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void rejectsInvertedVisitorDateRange() throws Exception {
        // Arrange: un administrador autenticado envia un rango cronologicamente invalido.
        String token = createAuthenticatedUserToken();

        // Act y Assert: el error de negocio se expresa como una solicitud invalida.
        mockMvc.perform(get("/api/visitor")
                        .param("startAt", "2026-07-08T00:00:00Z")
                        .param("endAt", "2026-07-01T00:00:00Z")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(false));
    }

    private String createAuthenticatedUserToken() {
        // Persiste credenciales BCrypt porque UserDetailsService consulta la BD real.
        String username = "integration-admin-" + System.nanoTime();
        userRepository.save(new UserEntity(
                null,
                username,
                passwordEncoder.encode("irrelevant-password")
        ));
        return jwtUtil.generateToken(username);
    }

}
