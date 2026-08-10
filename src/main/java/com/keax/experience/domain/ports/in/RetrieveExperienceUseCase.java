package com.keax.experience.domain.ports.in;

import com.keax.experience.domain.model.Experience;

import java.util.List;

public interface RetrieveExperienceUseCase {

    List<Experience> getExperiences();

    List<Experience> getVisibleExperiences();
}
