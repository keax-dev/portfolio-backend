package com.keax.application.services.Interfaces;

import com.keax.domain.models.Project;
import java.util.List;

public interface IProjectService {

    Project createProject(Project project);
    Project updateProject(Long projectId, Project project);
    Project deleteProject(Long projectId);
    List<Project> findByProjectDeleted(Boolean deleted);
    List<Project> getListProject();

}
