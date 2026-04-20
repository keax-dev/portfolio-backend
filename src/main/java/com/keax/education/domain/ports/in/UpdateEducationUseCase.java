package com.keax.education.domain.ports.in;

import com.keax.education.domain.model.Education;

public interface UpdateEducationUseCase {

    Education updateEducation(Long educationId, Education education);

}
