package com.keax.application.usecases.Project;

import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.ports.in.Project.RetrieveProjectUseCase;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
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
            throw new ExceptionAlert("There are no created projects");
        }

        return projects;
    }

}
