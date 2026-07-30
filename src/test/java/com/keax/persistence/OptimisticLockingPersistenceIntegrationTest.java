package com.keax.persistence;

import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import com.keax.technology.infrastructure.out.persistence.repository.JpaTechnologyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(showSql = false)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OptimisticLockingPersistenceIntegrationTest {

    private final JpaTechnologyRepository repository;
    private final JdbcTemplate jdbcTemplate;

    OptimisticLockingPersistenceIntegrationTest(
            JpaTechnologyRepository repository,
            JdbcTemplate jdbcTemplate
    ) {
        this.repository = repository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    void rejectsAnUpdateWhenThePersistedVersionChanged() {
        TechnologyEntity entity = repository.saveAndFlush(
                new TechnologyEntity(null, "JAVA", false)
        );
        jdbcTemplate.update(
                "update technology set version = version + 1 where technology_id = ?",
                entity.getTechnologyId()
        );

        entity.setTechnologyName("KOTLIN");

        assertThrows(ObjectOptimisticLockingFailureException.class, repository::flush);
    }

    @Test
    void logicalDeletionIncrementsVersionAndHidesTheEntity() {
        TechnologyEntity entity = repository.saveAndFlush(
                new TechnologyEntity(null, "JAVA", false)
        );
        Long id = entity.getTechnologyId();
        Long version = entity.getVersion();

        repository.delete(entity);
        repository.flush();

        assertTrue(repository.findById(id).isEmpty());
        assertEquals(Boolean.TRUE, jdbcTemplate.queryForObject(
                "select technology_deleted from technology where technology_id = ?",
                Boolean.class,
                id
        ));
        assertEquals(version + 1, jdbcTemplate.queryForObject(
                "select version from technology where technology_id = ?",
                Long.class,
                id
        ));
    }
}
