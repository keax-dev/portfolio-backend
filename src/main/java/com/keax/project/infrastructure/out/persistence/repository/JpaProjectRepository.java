package com.keax.project.infrastructure.out.persistence.repository;

import com.keax.project.infrastructure.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findAllByOrderByProjectPosition();
    List<ProjectEntity> findByProjectPublishedTrueOrderByProjectPosition();

    Optional<ProjectEntity> findByProjectTitle(String projectTitle);

    Optional<ProjectEntity> findByProjectPosition(int position);

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

}
