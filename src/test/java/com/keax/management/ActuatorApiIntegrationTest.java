package com.keax.management;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.auth.infrastructure.out.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica que Actuator exponga healthchecks operativos sin autenticación y
 * proteja las métricas detalladas detrás del mismo flujo JWT administrativo.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ActuatorApiIntegrationTest {

    private final MockMvc mockMvc;
    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    ActuatorApiIntegrationTest(
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
    void exposesPublicHealthProbes() throws Exception {
        // Act y Assert: el estado general de salud está disponible para chequeos externos.
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        // Act y Assert: liveness confirma que el proceso está vivo.
        mockMvc.perform(get("/actuator/health/liveness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        // Act y Assert: readiness confirma que la app está lista para recibir tráfico.
        mockMvc.perform(get("/actuator/health/readiness"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void protectsMetricsAndPrometheusEndpoints() throws Exception {
        // Assert: sin autenticación las métricas operativas no deben exponerse.
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/prometheus"))
                .andExpect(status().isUnauthorized());

        // Arrange: se obtiene un JWT válido para consultar observabilidad detallada.
        String token = token();

        // Act y Assert: el listado de métricas se expone solo a clientes autenticados.
        mockMvc.perform(get("/actuator/metrics")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names", hasItem("jvm.memory.used")));

        // Act y Assert: Prometheus devuelve el formato de scraping esperado.
        mockMvc.perform(get("/actuator/prometheus")
                        .header("Authorization", bearer(token)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("# HELP")));
    }

    private String token() {
        // Persiste un usuario técnico para autenticarse contra los endpoints protegidos.
        String username = "actuator-" + System.nanoTime();
        userRepository.save(new UserEntity(
                null,
                username,
                passwordEncoder.encode("irrelevant-password")
        ));
        return jwtUtil.generateToken(username);
    }

    private String bearer(String token) {
        // Centraliza el formato de la cabecera Authorization.
        return "Bearer " + token;
    }

}
