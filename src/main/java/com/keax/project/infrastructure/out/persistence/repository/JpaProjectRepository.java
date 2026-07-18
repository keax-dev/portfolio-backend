package com.keax.project.infrastructure.out.persistence.repository;

import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @EntityGraph(attributePaths = {"projectTechnologies", "projectTechnologies.technology", "projectLinks"})
    List<ProjectEntity> findByProjectDeletedOrderByProjectPosition(Boolean deleted);

    @EntityGraph(attributePaths = {"projectTechnologies", "projectTechnologies.technology", "projectLinks"})
    Optional<ProjectEntity> findByProjectTitleAndProjectDeleted(String projectTitle, Boolean deleted);

    @EntityGraph(attributePaths = {"projectTechnologies", "projectTechnologies.technology", "projectLinks"})
    Optional<ProjectEntity> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted);

    Optional<ProjectEntity> findByProjectPositionAndProjectDeleted(int position, Boolean deleted);

    @Query("""
            select case when count(project) > 0 then true else false end
            from ProjectEntity project
            join project.projectTechnologies relation
            where relation.technology.technologyId = :technologyId
              and project.projectDeleted = :deleted
            """)
    Boolean existsByTechnologyIdAndProjectDeleted(
            @Param("technologyId") Long technologyId,
            @Param("deleted") Boolean deleted
    );

}
