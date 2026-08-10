package com.keax.experience.application.usecases;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.in.DeleteExperienceUseCase;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteExperienceUseCaseImpl implements DeleteExperienceUseCase {

    private final ExperienceRepositoryPort repository;

    @Override
    public Experience deleteExperience(Long experienceId) {
        Experience stored = repository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The experience to be deleted was not found"
                ));
        return repository.delete(stored);
    }
}
