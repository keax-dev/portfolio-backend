package com.keax.education.domain.ports.in;

import com.keax.education.domain.model.Education;

public interface CreateEducationUseCase {

    Education createEducation(Education education);

}
