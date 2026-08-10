package com.keax.experience.application.usecases;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.in.UpdateExperienceUseCase;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateExperienceUseCaseImpl implements UpdateExperienceUseCase {

    private final ExperienceRepositoryPort repository;

    @Override
    public Experience updateExperience(Long experienceId, Experience changes) {
        Experience stored = repository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The experience to be updated was not found"
                ));

        repository.findByPosition(changes.getExperiencePosition()).ifPresent(existing -> {
            if (!Objects.equals(existing.getExperienceId(), stored.getExperienceId())) {
                throw new ResourceConflictException(
                        "There is already an experience with this position"
                );
            }
        });

        CreateExperienceUseCaseImpl.normalize(changes);
        stored.setExperienceRole(changes.getExperienceRole());
        stored.setExperienceRoleEs(changes.getExperienceRoleEs());
        stored.setExperienceCompany(changes.getExperienceCompany());
        stored.setExperienceDescription(changes.getExperienceDescription());
        stored.setExperienceDescriptionEs(changes.getExperienceDescriptionEs());
        stored.setExperienceStart(changes.getExperienceStart());
        stored.setExperienceStartEs(changes.getExperienceStartEs());
        stored.setExperienceEnd(changes.getExperienceEnd());
        stored.setExperienceEndEs(changes.getExperienceEndEs());
        stored.setExperiencePosition(changes.getExperiencePosition());
        stored.setExperienceVisible(Objects.requireNonNullElse(
                changes.getExperienceVisible(),
                true
        ));
        return repository.save(stored);
    }
}
