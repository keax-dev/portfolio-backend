package com.keax.experience.domain.ports.in;

import com.keax.experience.domain.model.Experience;

public interface CreateExperienceUseCase {

    Experience createExperience(Experience experience);
}
