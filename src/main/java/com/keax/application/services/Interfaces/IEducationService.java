package com.keax.application.services.Interfaces;

import com.keax.domain.models.Education;

import java.util.List;

public interface IEducationService {
    Education createEducation(Education education);
    Education updateEducation(Long educationId, Education education);
    List<Education> getListEducation();
    Education deleteEducation(Long educationId);
    List<Education> findByEducationDeleted(Boolean deleted);
}
