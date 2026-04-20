package com.keax.project.domain.ports.in;

import com.keax.project.domain.model.Project;
import java.util.List;

public interface RetrieveProjectUseCase {

    List<Project> findByProjectDeleted(Boolean deleted);
    List<Project> getListProject();

}
