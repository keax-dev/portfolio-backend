package com.keax.application.services.Interfaces;

import com.keax.domain.models.Education;

import java.util.List;

public interface IEducationService {
    Education createEducation(Education education);
    Education updateEducation(Long education_id, Education education);
    List<Education> getListEducation();
    Education deleteEducation(Long education_id);
    List<Education> findByEducationDeleted(Boolean deleted);
}
