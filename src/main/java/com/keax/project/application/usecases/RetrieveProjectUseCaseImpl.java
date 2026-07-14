package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveProjectUseCaseImpl implements RetrieveProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;

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
