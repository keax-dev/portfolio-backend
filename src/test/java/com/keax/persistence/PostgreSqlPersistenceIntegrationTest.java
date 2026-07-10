package com.keax.persistence;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ejecuta consultas y restricciones críticas contra PostgreSQL real mediante
 * Testcontainers para cubrir diferencias que H2 no puede representar.
 */
@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers(disabledWithoutDocker = true)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostgreSqlPersistenceIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17-alpine");

    @DynamicPropertySource
    static void configurePostgreSql(DynamicPropertyRegistry registry) {
        // Conecta el contexto JPA al contenedor efímero.
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.flyway.baseline-on-migrate", () -> "false");
    }

    @Autowired
    private JpaInstitutionRepository institutionRepository;
    @Autowired
    private JpaEducationRepository educationRepository;
    @Autowired
    private JpaTechnologyRepository technologyRepository;
    @Autowired
    private JpaProjectRepository projectRepository;
    @Autowired
    private JpaUserRepository userRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void resolvesEducationRelationshipQueriesOnPostgreSql() {
        // Arrange: se persisten institución y educación relacionadas.
        InstitutionEntity institution = institutionRepository.save(new InstitutionEntity(
                null, "UNIVERSITY", "UNIVERSIDAD", null, false
        ));
        educationRepository.saveAndFlush(new EducationEntity(
                null, "DEGREE", "TÍTULO", "UNIVERSITY", "2020", "2020",
                "2024", "2024", 1, false, institution
        ));

        // Act: se ejecutan nombres derivados que navegan la asociación JPA.
        var found = educationRepository
                .findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                        "DEGREE", false, institution.getInstitutionId()
                );
        boolean associated = educationRepository
                .existsByInstitution_InstitutionIdAndEducationDeleted(
                        institution.getInstitutionId(), false
                );

        // Assert: PostgreSQL resuelve correctamente relación y predicados.
        assertTrue(found.isPresent());
        assertTrue(associated);
        assertEquals(institution.getInstitutionId(), found.orElseThrow().getInstitution().getInstitutionId());
    }

    @Test
    void fetchesTechnologyProjectsWithEntityGraphOnPostgreSql() {
        // Arrange: se persiste tecnología y un proyecto hijo.
        TechnologyEntity technology = technologyRepository.save(new TechnologyEntity(
                null, "JAVA", 1, false, new ArrayList<>()
        ));
        projectRepository.saveAndFlush(new ProjectEntity(
                null, "PORTFOLIO", "PORTAFOLIO", "Description", "Descripción",
                null, null, null, 1, false, technology
        ));
        // Se limpia el primer nivel de caché para obligar a validar el EntityGraph contra PostgreSQL.
        entityManager.clear();

        // Act: se usa la consulta optimizada que alimenta el portafolio.
        var technologies = technologyRepository.findWithProjectsByTechnologyDeleted(false);

        // Assert: el EntityGraph entrega el hijo y respeta el orden.
        assertEquals(1, technologies.size());
        assertEquals(1, technologies.getFirst().getProjectEntityList().size());
        assertEquals("PORTFOLIO", technologies.getFirst().getProjectEntityList().getFirst().getProjectTitle());
    }

    @Test
    void enforcesUniqueUsernameAtDatabaseLevel() {
        // Arrange: se persiste el primer usuario.
        userRepository.saveAndFlush(new UserEntity(null, "admin", "hash"));

        // Act y Assert: PostgreSQL protege la unicidad incluso ante concurrencia.
        assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.saveAndFlush(new UserEntity(null, "admin", "other-hash"))
        );
    }

}
