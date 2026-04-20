package com.keax.technology.domain.ports.in;

import com.keax.technology.domain.model.Technology;
import java.util.List;

public interface RetrieveTechnologyUseCase {

    List<Technology> findByTechnologyDeleted(Boolean deleted);
    List<Technology> findByTechnologyDeletedWithProjects(Boolean deleted, Boolean projectDeleted);
    List<Technology> getListTechnology();

}
