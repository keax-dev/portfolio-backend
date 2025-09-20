package com.keax.domain.ports.in.Project;

import com.keax.domain.models.Project;

public interface UpdateProjectUseCase {

    Project updateProject(Long projectId, Project project);

}
