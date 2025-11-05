package com.keax.domain.ports.in.Technology;

import com.keax.domain.models.Technology;
import java.util.List;

public interface RetrieveTechnologyUseCase {

    List<Technology> findByTechnologyDeleted(Boolean deleted);
    List<Technology> findByTechnologyDeletedWithProjects(Boolean deleted, Boolean projectDeleted);
    List<Technology> getListTechnology();

}
