package com.keax.experience.domain.ports.in;

import com.keax.experience.domain.model.Experience;

public interface UpdateExperienceUseCase {

    Experience updateExperience(Long experienceId, Experience experience);
}
