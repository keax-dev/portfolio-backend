package com.keax.application.services.Interfaces;

import com.keax.domain.models.Technology;

import java.util.List;

public interface ITechnologyService {

    Technology createTechnology(Technology technology);
    Technology updateTechnology(Long technologyId, Technology technology);
    Technology deleteTechnology(Long technologyId);
    List<Technology> findByTechnologyDeleted(Boolean deleted);
    List<Technology> getListTechnology();

}
