package com.keax.domain.ports.in.Education;

import com.keax.domain.models.Education;

public interface DeleteEducationUseCase {
    Boolean deleteEducation(Long education_id);
}
