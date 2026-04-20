package com.keax.education.domain.ports.in;

import com.keax.education.domain.model.Education;
import java.util.List;

public interface RetrieveEducationUseCase {

    List<Education> findByEducationDeleted(Boolean deleted);
    List<Education> getListEducation();

}
