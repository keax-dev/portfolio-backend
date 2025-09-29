package com.keax.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.keax.infrastructure.entities.ProjectEntity;
import java.util.Optional;
import java.util.List;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByProjectDeleted(Boolean deleted);
    Optional<ProjectEntity> findByProjectTitleAndProjectDeleted(String projectTitle, Boolean deleted);
    Optional<ProjectEntity> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted);
    Optional<ProjectEntity> findByProjectPositionAndProjectDeletedAndTechnology_technologyId(int position, Boolean deleted, Long technologyId);
    Boolean existsByTechnology_technologyIdAndProjectDeleted(Long technologyId, Boolean deleted);

}
