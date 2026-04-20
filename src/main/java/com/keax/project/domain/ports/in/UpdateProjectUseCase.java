package com.keax.project.domain.ports.in;

import com.keax.project.domain.model.Project;

public interface UpdateProjectUseCase {

    Project updateProject(Long projectId, Project project);

}
