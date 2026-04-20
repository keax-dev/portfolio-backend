package com.keax.project.domain.ports.out;

import com.keax.project.domain.model.Project;
import java.util.Optional;
import java.util.List;

public interface ProjectRepositoryPort {

    Project createProject(Project project);
    Project updateProject(Project project);
    Project deleteProject(Project project);
    List<Project> findByProjectDeleted(Boolean deleted);
    List<Project> getListProject();
    Optional<Project> findByProjectTitleAndProjectDeleted(String projectTitle, Boolean deleted);
    Optional<Project> findByProjectIdAndProjectDeleted(Long projectId, Boolean deleted);
    Optional<Project> findByProjectPositionAndProjectDeletedAndTechnology_technologyId(int position, Boolean deleted, Long technologyId);
    Boolean existsByTechnology_technologyIdAndProjectDeleted(Long technologyId, Boolean deleted);

}
