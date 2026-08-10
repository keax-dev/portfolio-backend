package com.keax.experience.application.usecases;

import com.keax.experience.domain.model.Experience;
import com.keax.experience.domain.ports.in.RetrieveExperienceUseCase;
import com.keax.experience.domain.ports.out.ExperienceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveExperienceUseCaseImpl implements RetrieveExperienceUseCase {

    private final ExperienceRepositoryPort repository;

    @Override
    public List<Experience> getExperiences() {
        return repository.findAll();
    }

    @Override
    public List<Experience> getVisibleExperiences() {
        return repository.findVisible();
    }
}
