package com.keax.application.usecases.Project;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Project.UpdateProjectUseCase;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Project;
import java.util.Objects;

@Component
public class UpdateProjectUseCaseImpl implements UpdateProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Project updateProject(Long projectId, Project project) {

        Project projectUpdate = projectRepositoryPort.findByProjectIdAndProjectDeleted(projectId, false).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(project.getTechnologyId(), false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        projectUpdate.setProjectTittle(project.getProjectTittle().toUpperCase());

        projectRepositoryPort.findByProjectTittleAndProjectDeleted(projectUpdate.getProjectTittle(), false).ifPresent(
                e ->{
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ExceptionAlert("The tittle of the project to be updated is already registered");
                    }
                }
        );

        projectRepositoryPort.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(project.getProjectPosition(), false, project.getTechnologyId()).ifPresent(
                e -> {
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ExceptionAlert("The project position is already filled");
                    }
                }
        );

        projectUpdate.setProjectDescription(project.getProjectDescription().toUpperCase());
        projectUpdate.setProjectDeploy(project.getProjectDeploy());
        projectUpdate.setProjectGithub(project.getProjectGithub());
        projectUpdate.setProjectDeleted(false);

        return projectRepositoryPort.updateProject(projectUpdate);
    }

}
