package com.keax.project.infrastructure.out.persistence.repository;

import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByProjectDeletedOrderByProjectPosition(Boolean deleted);

    List<ProjectEntity> findByProjectDeletedAndProjectPublishedOrderByProjectPosition(
            Boolean deleted,
            Boolean published
    );

    Optional<ProjectEntity> findByProjectTitleAndProjectDeleted(String projectTitle, Boolean deleted);

    Optional<ProjectEntity> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted);

    Optional<ProjectEntity> findByProjectPositionAndProjectDeleted(int position, Boolean deleted);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update ProjectTechnologyEntity relation
            set relation.position = -relation.position
            where relation.project.projectId = :projectId
            """)
    int stageProjectTechnologyPositions(@Param("projectId") Long projectId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update ProjectLinkEntity link
            set link.position = -link.position
            where link.project.projectId = :projectId
            """)
    int stageProjectLinkPositions(@Param("projectId") Long projectId);

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
