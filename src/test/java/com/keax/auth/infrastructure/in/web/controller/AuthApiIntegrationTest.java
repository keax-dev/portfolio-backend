package com.keax.auth.infrastructure.in.web.controller;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integra login, BCrypt, AuthenticationManager, emisión JWT, validación web y
 * preflight CORS sobre el endpoint público de autenticación.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JpaUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void authenticatesValidCredentialsAndReturnsTokenWithoutPassword() throws Exception {
        // Arrange: se almacena el usuario con BCrypt como en producción.
        userRepository.save(new UserEntity(
                null,
                "admin",
                passwordEncoder.encode("correct-password")
        ));

        // Act y Assert: el flujo completo devuelve JWT y oculta la contraseña.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin","password":"correct-password"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void rejectsInvalidCredentials() throws Exception {
        // Arrange: el usuario existe, pero la contraseña enviada es incorrecta.
        userRepository.save(new UserEntity(
                null,
                "admin",
                passwordEncoder.encode("correct-password")
        ));

        // Act y Assert: el backend responde 401 sin emitir token.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin","password":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(false))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void rejectsStructurallyInvalidLoginRequest() throws Exception {
        // Act y Assert: Bean Validation detiene credenciales vacías con HTTP 400.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"","password":""}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.alert").value("Validation error"))
                .andExpect(jsonPath("$.messages").isArray());
    }

    @Test
    void permitsConfiguredCorsPreflight() throws Exception {
        // Act: el frontend configurado consulta permiso para ejecutar POST.
        mockMvc.perform(options("/api/auth/login")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST"))
                // Assert: Spring Security permite el origen exacto configurado.
                .andExpect(status().isOk())
                .andExpect(header().string(
                        "Access-Control-Allow-Origin",
                        "http://localhost:5173"
                ));
    }

}
