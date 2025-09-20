package com.keax.application.usecases.Project;

import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.in.Project.CreateProjectUseCase;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Project;

@Component
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Project createProject(Project project) {

        technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(project.getTechnologyId(), false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        project.setProjectTittle(project.getProjectTittle().toUpperCase());

        projectRepositoryPort.findByProjectTittleAndProjectDeleted(project.getProjectTittle(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already a project with this tittle");
                }
        );

        projectRepositoryPort.findByProjectPositionAndProjectDeletedAndTechnology_technologyId(project.getProjectPosition(), false, project.getTechnologyId()).ifPresent(
                e -> {
                    throw new ExceptionAlert("The project position is already filled");
                }
        );

        project.setProjectId(null);
        project.setProjectPicture(null);
        project.setProjectDescription(project.getProjectDescription().toUpperCase());

        return projectRepositoryPort.createProject(project);
    }

}
