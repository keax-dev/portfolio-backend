package com.keax.domain.ports.in.Project;

import com.keax.domain.models.Project;

public interface DeleteProjectUseCase {

    Project deleteProject(Long projectId);

}
