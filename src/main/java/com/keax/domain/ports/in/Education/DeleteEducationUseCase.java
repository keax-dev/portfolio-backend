package com.keax.domain.ports.in.Education;

import com.keax.domain.models.Education;

public interface DeleteEducationUseCase {

    Education deleteEducation(Long education_id);

}
