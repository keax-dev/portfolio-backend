package com.keax.domain.ports.in.Education;

import com.keax.domain.models.Education;

import java.util.List;

public interface RetrieveEducationUseCase {
    List<Education> getListEducation();
    List<Education> findByEducationDeleted(Boolean deleted);
}
