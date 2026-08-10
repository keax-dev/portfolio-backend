package com.keax.experience.application.usecases;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.in.CreateExperienceUseCase;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.text.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateExperienceUseCaseImpl implements CreateExperienceUseCase {

    private final ExperienceRepositoryPort repository;

    @Override
    public Experience createExperience(Experience experience) {
        repository.findByPosition(experience.getExperiencePosition()).ifPresent(existing -> {
            throw new ResourceConflictException("There is already an experience with this position");
        });

        normalize(experience);
        experience.setExperienceId(null);
        experience.setExperienceVisible(Objects.requireNonNullElse(
                experience.getExperienceVisible(),
                true
        ));
        return repository.save(experience);
    }

    static void normalize(Experience experience) {
        experience.setExperienceRole(TextNormalizer.uppercase(experience.getExperienceRole()));
        experience.setExperienceRoleEs(TextNormalizer.uppercase(experience.getExperienceRoleEs()));
        experience.setExperienceCompany(TextNormalizer.uppercase(experience.getExperienceCompany()));
        experience.setExperienceDescription(TextNormalizer.trimToNull(
                experience.getExperienceDescription()
        ));
        experience.setExperienceDescriptionEs(TextNormalizer.trimToNull(
                experience.getExperienceDescriptionEs()
        ));
        experience.setExperienceStart(TextNormalizer.trimToNull(experience.getExperienceStart()));
        experience.setExperienceStartEs(TextNormalizer.trimToNull(experience.getExperienceStartEs()));
        experience.setExperienceEnd(TextNormalizer.trimToNull(experience.getExperienceEnd()));
        experience.setExperienceEndEs(TextNormalizer.trimToNull(experience.getExperienceEndEs()));
    }
}
