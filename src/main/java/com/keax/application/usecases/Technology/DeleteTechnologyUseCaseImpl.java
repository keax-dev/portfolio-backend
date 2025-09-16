package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.DeleteTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;
import java.util.Optional;

@Component
public class DeleteTechnologyUseCaseImpl implements DeleteTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology deleteTechnology(Long technologyId) {

        Optional<Technology> technologyFind = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(technologyId, false);

        if (technologyFind.isEmpty()){
            throw new ExceptionAlert("The technology entered was not found");
        }

        Technology technology = technologyFind.get();
        technology.setTechnologyDeleted(true);

        return technologyRepositoryPort.deleteTechnology(technology);
    }

}
