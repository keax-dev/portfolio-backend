package com.keax.technology.application.usecases;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.ports.in.UpdateTechnologyUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.model.Technology;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
public class UpdateTechnologyUseCaseImpl implements UpdateTechnologyUseCase {

    @Autowired
    private TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology updateTechnology(Long technologyId, Technology technology) {

        Technology technologyUpdate = technologyRepositoryPort.findByTechnologyIdAndTechnologyDeleted(
                technologyId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The technology entered was not found")
        );

        technologyUpdate.setTechnologyName(technology.getTechnologyName().toUpperCase());
        technologyRepositoryPort.findByTechnologyNameAndTechnologyDeleted(
                technologyUpdate.getTechnologyName(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getTechnologyId(), technologyUpdate.getTechnologyId())){
                        throw new ResourceConflictException("The name of the technology to be updated is already registered");
                    }
                }
        );

        technologyUpdate.setTechnologyPosition(technology.getTechnologyPosition());
        technologyRepositoryPort.findByTechnologyPositionAndTechnologyDeleted(
                technologyUpdate.getTechnologyPosition(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getTechnologyId(), technologyUpdate.getTechnologyId())){
                        throw new ResourceConflictException("The position of the technology to be updated is already registered");
                    }
                }
        );

        technologyUpdate.setTechnologyDeleted(false);

        return technologyRepositoryPort.updateTechnology(technologyUpdate);
    }

}
