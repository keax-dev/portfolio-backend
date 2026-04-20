package com.keax.project.domain.ports.in;

import com.keax.project.domain.model.Project;

public interface DeleteProjectUseCase {

    Project deleteProject(Long projectId);

}
