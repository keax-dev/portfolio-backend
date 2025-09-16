package com.keax.application.services.Implementation;

import com.keax.domain.ports.in.Technology.RetrieveTechnologyUseCase;
import com.keax.application.services.Interfaces.ITechnologyService;
import com.keax.domain.ports.in.Technology.CreateTechnologyUseCase;
import com.keax.domain.ports.in.Technology.DeleteTechnologyUseCase;
import com.keax.domain.ports.in.Technology.UpdateTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Technology;
import java.util.List;

@Service
public class TechnologyServiceImpl implements ITechnologyService {

    @Autowired
    private CreateTechnologyUseCase createTechnologyUseCase;

    @Autowired
    private UpdateTechnologyUseCase updateTechnologyUseCase;

    @Autowired
    private DeleteTechnologyUseCase deleteTechnologyUseCase;

    @Autowired
    private RetrieveTechnologyUseCase retrieveTechnologyUseCase;

    @Override
    public Technology createTechnology(Technology technology) {
        return createTechnologyUseCase.createTechnology(technology);
    }

    @Override
    public Technology updateTechnology(Long technologyId, Technology technology) {
        return updateTechnologyUseCase.updateTechnology(technologyId, technology);
    }

    @Override
    public Technology deleteTechnology(Long technologyId) {
        return deleteTechnologyUseCase.deleteTechnology(technologyId);
    }

    @Override
    public List<Technology> findByTechnologyDeleted(Boolean deleted) {
        return retrieveTechnologyUseCase.findByTechnologyDeleted(deleted);
    }

    @Override
    public List<Technology> getListTechnology() {
        return retrieveTechnologyUseCase.getListTechnology();
    }

}
