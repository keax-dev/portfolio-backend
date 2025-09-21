package com.keax.application.usecases.Project;

import com.keax.domain.ports.in.Project.RetrieveProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Project;
import java.util.List;

@Component
public class RetrieveProjectUseCaseImpl implements RetrieveProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Override
    public List<Project> findByProjectDeleted(Boolean deleted) {

        List<Project> projectList = projectRepositoryPort.findByProjectDeleted(deleted);

        return validateNotEmpty(projectList);
    }

    @Override
    public List<Project> getListProject() {

        List<Project> projectList = projectRepositoryPort.getListProject();

        return validateNotEmpty(projectList);
    }

    private List<Project> validateNotEmpty(List<Project> projects) {

        if (projects.isEmpty()) {
            throw new ExceptionAlert("No projects have been created");
        }

        return projects;
    }

}
