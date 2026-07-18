package com.keax.persistence;

import com.keax.auth.infrastructure.out.persistence.entity.UserEntity;
import com.keax.auth.infrastructure.out.persistence.repository.JpaUserRepository;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import com.keax.project.infrastructure.out.persistence.entity.ProjectLinkEntity;
import com.keax.project.infrastructure.out.persistence.entity.ProjectTechnologyEntity;
import com.keax.project.domain.model.ProjectLinkType;
import com.keax.project.domain.model.ProjectTechnology;
import com.keax.project.infrastructure.out.persistence.adapter.ProjectPersistenceAdapter;
import com.keax.project.infrastructure.out.persistence.mapper.ProjectPersistenceMapper;
import com.keax.project.infrastructure.out.persistence.repository.JpaProjectRepository;
import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;

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
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
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

    private final JpaInstitutionRepository institutionRepository;
    private final JpaEducationRepository educationRepository;
    private final JpaTechnologyRepository technologyRepository;
    private final JpaProjectRepository projectRepository;
    private final JpaUserRepository userRepository;
    private final TestEntityManager entityManager;

    PostgreSqlPersistenceIntegrationTest(
            JpaInstitutionRepository institutionRepository,
            JpaEducationRepository educationRepository,
            JpaTechnologyRepository technologyRepository,
            JpaProjectRepository projectRepository,
            JpaUserRepository userRepository,
            TestEntityManager entityManager
    ) {
        this.institutionRepository = institutionRepository;
        this.educationRepository = educationRepository;
        this.technologyRepository = technologyRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

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
    void fetchesProjectTechnologiesAndLinksWithEntityGraphOnPostgreSql() {
        // Arrange: se persiste tecnología y un proyecto hijo.
        TechnologyEntity technology = technologyRepository.save(
                new TechnologyEntity(null, "JAVA", false)
        );
        ProjectEntity project = new ProjectEntity(
                null, "PORTFOLIO", "PORTAFOLIO", "Description", "Descripción",
                1, false, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>()
        );
        project.getProjectTechnologies().add(
                new ProjectTechnologyEntity(null, project, technology, 1)
        );
        project.getProjectLinks().add(new ProjectLinkEntity(
                null, project, ProjectLinkType.DEPLOY, "https://example.com", 1
        ));
        projectRepository.saveAndFlush(project);
        // Se limpia el primer nivel de caché para obligar a validar el EntityGraph contra PostgreSQL.
        entityManager.clear();

        // Act: se usa la consulta optimizada que alimenta el portafolio.
        var projects = projectRepository.findByProjectDeletedOrderByProjectPosition(false);

        // Assert: el EntityGraph entrega el hijo y respeta el orden.
        assertEquals(1, projects.size());
        assertEquals("JAVA", projects.getFirst().getProjectTechnologies().iterator().next()
                .getTechnology().getTechnologyName());
        assertEquals(1, projects.getFirst().getProjectTechnologies().iterator().next().getPosition());
        assertEquals(
                ProjectLinkType.DEPLOY,
                projects.getFirst().getProjectLinks().iterator().next().getType()
        );
    }

    @Test
    void removesAndReordersProjectTechnologiesOnPostgreSql() {
        TechnologyEntity java = technologyRepository.save(new TechnologyEntity(null, "JAVA", false));
        TechnologyEntity angular = technologyRepository.save(new TechnologyEntity(null, "ANGULAR", false));
        TechnologyEntity mysql = technologyRepository.save(new TechnologyEntity(null, "MYSQL", false));
        ProjectEntity entity = new ProjectEntity(
                null, "PORTFOLIO", "PORTAFOLIO", "Description", "DescripciÃ³n",
                1, false, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>()
        );
        entity.getProjectTechnologies().add(new ProjectTechnologyEntity(null, entity, java, 1));
        entity.getProjectTechnologies().add(new ProjectTechnologyEntity(null, entity, angular, 2));
        entity.getProjectTechnologies().add(new ProjectTechnologyEntity(null, entity, mysql, 3));
        ProjectEntity saved = projectRepository.saveAndFlush(entity);
        entityManager.clear();

        var project = ProjectPersistenceMapper.toDomain(
                projectRepository.findByProjectIdAndProjectDeleted(saved.getProjectId(), false).orElseThrow()
        );
        Long javaRelationId = project.getProjectTechnologies().stream()
                .filter(relation -> relation.getTechnologyId().equals(java.getTechnologyId()))
                .findFirst().orElseThrow().getProjectTechnologyId();
        Long mysqlRelationId = project.getProjectTechnologies().stream()
                .filter(relation -> relation.getTechnologyId().equals(mysql.getTechnologyId()))
                .findFirst().orElseThrow().getProjectTechnologyId();
        project.setProjectTechnologies(new ArrayList<>(List.of(
                new ProjectTechnology(mysqlRelationId, mysql.getTechnologyId(), "MYSQL", 1),
                new ProjectTechnology(javaRelationId, java.getTechnologyId(), "JAVA", 2)
        )));

        var updated = new ProjectPersistenceAdapter(projectRepository).updateProject(project);

        assertEquals(2, updated.getProjectTechnologies().size());
        assertEquals(mysql.getTechnologyId(), updated.getProjectTechnologies().getFirst().getTechnologyId());
        assertEquals(1, updated.getProjectTechnologies().getFirst().getPosition());
        assertEquals(java.getTechnologyId(), updated.getProjectTechnologies().get(1).getTechnologyId());
        assertEquals(2, updated.getProjectTechnologies().get(1).getPosition());
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
