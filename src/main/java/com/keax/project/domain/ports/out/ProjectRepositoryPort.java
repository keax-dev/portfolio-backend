package com.keax.project.domain.ports.out;

import com.keax.project.domain.model.Project;
import java.util.Optional;
import java.util.List;

public interface ProjectRepositoryPort {

    Project createProject(Project project);
    Project updateProject(Project project);
    Project deleteProject(Project project);
    List<Project> findPublished();
    List<Project> findAll();
    Optional<Project> findByTitle(String projectTitle);
    Optional<Project> findById(Long projectId);
    Optional<Project> findByPosition(int position);

}
