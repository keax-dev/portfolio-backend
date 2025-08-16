package com.keax.domain.ports.in.Education;

import com.keax.domain.models.Education;

public interface UpdateEducationUseCase {
    Education updateEducation(Long education_id, Education education);
}
