package com.keax.project.application.usecases;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.project.domain.ports.in.UpdateProjectUseCase;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import java.util.Objects;

@Service
@Transactional
public class UpdateProjectUseCaseImpl implements UpdateProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Project updateProject(Long projectId, Project project) {

        Project projectUpdate = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(
                project.getTechnologyId(),
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        projectUpdate.setProjectTitle(project.getProjectTitle().toUpperCase());
        projectRepositoryPort.findByProjectTitleAndProjectDeleted(
                projectUpdate.getProjectTitle(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ExceptionAlert("The title of the project to be updated is already registered");
                    }
                }
        );

        projectUpdate.setProjectPosition(project.getProjectPosition());
        projectUpdate.setTechnologyId(project.getTechnologyId());
        projectRepositoryPort.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(
                projectUpdate.getProjectPosition(),
                false,
                projectUpdate.getTechnologyId()
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getProjectId(), projectUpdate.getProjectId())){
                        throw new ExceptionAlert("The project position is already filled");
                    }
                }
        );

        projectUpdate.setProjectTitleEs(project.getProjectTitleEs().toUpperCase());
        projectUpdate.setProjectDescription(project.getProjectDescription());
        projectUpdate.setProjectDescriptionEs(project.getProjectDescriptionEs());
        projectUpdate.setProjectDeploy(project.getProjectDeploy());
        projectUpdate.setProjectGithub(project.getProjectGithub());
        projectUpdate.setProjectDeleted(false);

        return projectRepositoryPort.updateProject(projectUpdate);
    }

}
