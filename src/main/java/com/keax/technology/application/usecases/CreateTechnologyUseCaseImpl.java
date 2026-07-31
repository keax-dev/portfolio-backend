package com.keax.technology.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.technology.domain.ports.out.TechnologyRepositoryPort;
import com.keax.technology.domain.ports.in.CreateTechnologyUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.technology.domain.model.Technology;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateTechnologyUseCaseImpl implements CreateTechnologyUseCase {
    private final TechnologyRepositoryPort technologyRepositoryPort;

    @Override
    public Technology createTechnology(Technology technology) {

        technology.setTechnologyName(TextNormalizer.uppercase(technology.getTechnologyName()));
        technologyRepositoryPort.findByName(
                technology.getTechnologyName()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already a technology with this name");
                }
        );

        technology.setTechnologyId(null);
        return technologyRepositoryPort.createTechnology(technology);
    }

}
