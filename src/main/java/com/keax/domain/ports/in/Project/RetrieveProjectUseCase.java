package com.keax.domain.ports.in.Project;

import com.keax.domain.models.Project;
import java.util.List;

public interface RetrieveProjectUseCase {

    List<Project> findByProjectDeleted(Boolean deleted);
    List<Project> getListProject();

}
