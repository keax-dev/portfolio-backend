package com.keax.shared.infrastructure.out.persistence;

import com.keax.shared.domain.ports.out.EducationInstitutionReferencePort;
import com.keax.shared.domain.ports.out.ProjectTechnologyReferencePort;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SharedReferencePersistenceAdapter implements
        ProjectTechnologyReferencePort,
        EducationInstitutionReferencePort {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

    @Override
    public Set<Long> findActiveTechnologyIds(Set<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return Set.of();
        }

        flushPendingChanges();
        String placeholders = technologyIds.stream().map(id -> "?").collect(Collectors.joining(","));
        List<Long> ids = jdbcTemplate.queryForList(
                "select technology_id from technology "
                        + "where technology_deleted = false and technology_id in (" + placeholders + ")",
                Long.class,
                technologyIds.toArray()
        );
        return new HashSet<>(ids);
    }

    @Override
    public boolean existsActiveProjectForTechnology(Long technologyId) {
        flushPendingChanges();
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from project p "
                        + "join project_technology pt on pt.project_id = p.project_id "
                        + "where p.project_deleted = false and pt.technology_id = ?",
                Long.class,
                technologyId
        );
        return count != null && count > 0;
    }

    @Override
    public boolean existsActiveInstitution(Long institutionId) {
        flushPendingChanges();
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from institution where institution_id = ? and institution_deleted = false",
                Long.class,
                institutionId
        );
        return count != null && count > 0;
    }

    @Override
    public boolean existsActiveEducationForInstitution(Long institutionId) {
        flushPendingChanges();
        Long count = jdbcTemplate.queryForObject(
                "select count(*) from education where institution_id = ? and education_deleted = false",
                Long.class,
                institutionId
        );
        return count != null && count > 0;
    }

    private void flushPendingChanges() {
        if (entityManager.isJoinedToTransaction()) {
            entityManager.flush();
        }
    }
}
