package com.keax.application.services.Implementation;

import com.keax.domain.ports.in.Education.RetrieveEducationUseCase;
import com.keax.application.services.Interfaces.IEducationService;
import com.keax.domain.ports.in.Education.CreateEducationUseCase;
import com.keax.domain.ports.in.Education.DeleteEducationUseCase;
import com.keax.domain.ports.in.Education.UpdateEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.keax.domain.models.Education;
import java.util.List;

@Service
public class EducationServiceImpl implements IEducationService {

    @Autowired
    private CreateEducationUseCase createEducationUseCase;

    @Autowired
    private UpdateEducationUseCase updateEducationUseCase;

    @Autowired
    private RetrieveEducationUseCase retrieveEducationUseCase;

    @Autowired
    private DeleteEducationUseCase deleteEducationUseCase;

    @Override
    public Education createEducation(Education education) {
        return createEducationUseCase.createEducation(education);
    }

    @Override
    public Education updateEducation(Long education_id, Education education) {
        return updateEducationUseCase.updateEducation(education_id, education);
    }

    @Override
    public List<Education> getListEducation() {
        return retrieveEducationUseCase.getListEducation();
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {
        return retrieveEducationUseCase.findByEducationDeleted(deleted);
    }

    @Override
    public Education deleteEducation(Long education_id) {
        return deleteEducationUseCase.deleteEducation(education_id);
    }

}
