package com.keax.project.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.project.domain.ports.in.RetrieveProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
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

        return projectRepositoryPort.findByProjectDeleted(deleted);
    }

    @Override
    public List<Project> getListProject() {

        return projectRepositoryPort.getListProject();
    }

}
