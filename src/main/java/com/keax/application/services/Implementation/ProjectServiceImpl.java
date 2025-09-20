package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IProjectService;
import com.keax.domain.ports.in.Project.RetrieveProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Project.CreateProjectUseCase;
import com.keax.domain.ports.in.Project.DeleteProjectUseCase;
import com.keax.domain.ports.in.Project.UpdateProjectUseCase;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Project;
import java.util.List;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    private CreateProjectUseCase createProjectUseCase;

    @Autowired
    private UpdateProjectUseCase updateProjectUseCase;

    @Autowired
    private DeleteProjectUseCase deleteProjectUseCase;

    @Autowired
    private RetrieveProjectUseCase retrieveProjectUseCase;

    @Override
    public Project createProject(Project project) {
        return createProjectUseCase.createProject(project);
    }

    @Override
    public Project updateProject(Long projectId, Project project) {
        return updateProjectUseCase.updateProject(projectId, project);
    }

    @Override
    public Project deleteProject(Long projectId) {
        return deleteProjectUseCase.deleteProject(projectId);
    }

    @Override
    public List<Project> findByProjectDeleted(Boolean deleted) {
        return retrieveProjectUseCase.findByProjectDeleted(deleted);
    }

    @Override
    public List<Project> getListProject() {
        return retrieveProjectUseCase.getListProject();
    }

}
