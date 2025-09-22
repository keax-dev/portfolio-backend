package com.keax.application.usecases.Technology;

import com.keax.domain.ports.in.Technology.UpdateTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.TechnologyRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Technology;
import java.util.Objects;

@Component
public class UpdateTechnologyUseCaseImpl implements UpdateTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology updateTechnology(Long technologyId, Technology technology) {

        Technology technologyUpdate = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(technologyId, false).orElseThrow(
                () -> new ExceptionAlert("The technology entered was not found")
        );

        technologyUpdate.setTechnologyName(technology.getTechnologyName().toUpperCase());

        technologyRepositoryPort.findByTechnologyNameAndTechnologyDeleted(technologyUpdate.getTechnologyName(), false).ifPresent(
                e ->{
                    if (!Objects.equals(e.getTechnologyId(), technologyUpdate.getTechnologyId())){
                        throw new ExceptionAlert("The name of the technology to be updated is already registered");
                    }
                }
        );

        technologyUpdate.setTechnologyPosition(technology.getTechnologyPosition());

        technologyRepositoryPort.findByTechnologyPositionAndTechnologyDeleted(technologyUpdate.getTechnologyPosition(), false).ifPresent(
                e ->{
                    if (!Objects.equals(e.getTechnologyId(), technologyUpdate.getTechnologyId())){
                        throw new ExceptionAlert("The position of the technology to be updated is already registered");
                    }
                }
        );

        technologyUpdate.setTechnologyDeleted(false);

        return technologyRepositoryPort.updateTechnology(technologyUpdate);
    }

}
