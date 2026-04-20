package com.keax.project.application.usecases;

import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import java.util.List;

@Service
@Transactional(readOnly = true)
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
