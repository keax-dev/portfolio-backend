package com.keax.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.skill.infrastructure.out.persistence.repository.JpaSkillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejecuta un smoke E2E sobre un servidor HTTP real: autentica al administrador,
 * crea una habilidad protegida y comprueba su publicación anónima y persistencia.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BackendEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private JpaSkillRepository skillRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Arrange común: se garantiza aislamiento y se crea una credencial real con BCrypt.
        skillRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.save(new UserEntity(
                null,
                "e2e-admin",
                passwordEncoder.encode("StrongPassword123!")
        ));
    }

    @AfterEach
    void cleanUp() {
        // Limpia los datos escritos por peticiones que corren en transacciones del servidor.
        skillRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void publishesAnAuthenticatedChangeThroughTheRealHttpServer() {
        // Act: se autentica por HTTP y se obtiene el JWT emitido por la aplicación.
        ResponseEntity<JsonNode> loginResponse = restTemplate.postForEntity(
                url("/api/auth/login"),
                Map.of("username", "e2e-admin", "password", "StrongPassword123!"),
                JsonNode.class
        );

        // Assert intermedio: login y token deben ser válidos antes del comando protegido.
        assertTrue(loginResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().path("data").path("token").asText();
        assertTrue(!token.isBlank());

        // Act: se crea la habilidad atravesando socket, seguridad, MVC, aplicación y JPA.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        ResponseEntity<JsonNode> createResponse = restTemplate.exchange(
                url("/api/skill"),
                HttpMethod.POST,
                new HttpEntity<>(Map.of("name", "Spring Boot", "position", 1), headers),
                JsonNode.class
        );

        // Assert: la API administrativa confirma el recurso y este quedó en H2.
        assertTrue(createResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(createResponse.getBody());
        assertEquals("SPRING BOOT", createResponse.getBody().path("data").path("name").asText());
        assertEquals(1, skillRepository.count());

        // Act y Assert final: el contenido nuevo ya es visible desde el endpoint público anónimo.
        ResponseEntity<JsonNode> publicResponse = restTemplate.getForEntity(
                url("/api/portfolio/skill"),
                JsonNode.class
        );
        assertTrue(publicResponse.getStatusCode().is2xxSuccessful());
        assertNotNull(publicResponse.getBody());
        assertEquals("SPRING BOOT", publicResponse.getBody().path("data").get(0).path("name").asText());
    }

    private String url(String path) {
        // Construye URLs locales sobre el puerto efímero asignado por Spring Boot.
        return "http://127.0.0.1:" + port + path;
    }
}
