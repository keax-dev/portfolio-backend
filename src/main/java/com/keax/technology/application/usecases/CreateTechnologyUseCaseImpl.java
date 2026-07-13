package com.keax.technology.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.ports.in.CreateTechnologyUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.technology.domain.model.Technology;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTechnologyUseCaseImpl implements CreateTechnologyUseCase {
    private final TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology createTechnology(Technology technology) {

        technology.setTechnologyName(technology.getTechnologyName().toUpperCase());
        technologyRepositoryPort.findByTechnologyNameAndTechnologyDeleted(
                technology.getTechnologyName(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a technology with this name");
                }
        );

        technologyRepositoryPort.findByTechnologyPositionAndTechnologyDeleted(
                technology.getTechnologyPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a technology with this position");
                }
        );

        technology.setTechnologyId(null);
        technology.setTechnologyDeleted(false);

        return technologyRepositoryPort.createTechnology(technology);
    }

}
