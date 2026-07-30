package com.keax.technology.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.ports.in.UpdateTechnologyUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.technology.domain.model.Technology;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateTechnologyUseCaseImpl implements UpdateTechnologyUseCase {
    private final TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology updateTechnology(Long technologyId, Technology technology) {

        Technology technologyUpdate = technologyRepositoryPort.findById(technologyId).orElseThrow(
                () -> new ResourceNotFoundException("The technology entered was not found")
        );

        technologyUpdate.setTechnologyName(TextNormalizer.uppercase(technology.getTechnologyName()));
        technologyRepositoryPort.findByName(
                technologyUpdate.getTechnologyName()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getTechnologyId(), technologyUpdate.getTechnologyId())){
                        throw new ResourceConflictException("The name of the technology to be updated is already registered");
                    }
                }
        );

        return technologyRepositoryPort.updateTechnology(technologyUpdate);
    }

}
