package com.keax.domain.ports.in.Education;

import com.keax.domain.models.Education;

public interface UpdateEducationUseCase {

    Education updateEducation(Long educationId, Education education);

}
