package com.keax.application.services.Implementation;

import com.keax.application.services.Interfaces.IEducationService;
import com.keax.domain.models.Education;
import com.keax.domain.ports.in.Education.CreateEducationUseCase;
import com.keax.domain.ports.in.Education.DeleteEducationUseCase;
import com.keax.domain.ports.in.Education.RetrieveEducationUseCase;
import com.keax.domain.ports.in.Education.UpdateEducationUseCase;

import java.util.List;

public class EducationServiceImpl implements IEducationService {

    private final CreateEducationUseCase createEducationUseCase;
    private final UpdateEducationUseCase updateEducationUseCase;
    private final RetrieveEducationUseCase retrieveEducationUseCase;
    private  final DeleteEducationUseCase deleteEducationUseCase;

    public EducationServiceImpl(CreateEducationUseCase createEducationUseCase, UpdateEducationUseCase updateEducationUseCase, RetrieveEducationUseCase retrieveEducationUseCase, DeleteEducationUseCase deleteEducationUseCase) {
        this.createEducationUseCase = createEducationUseCase;
        this.updateEducationUseCase = updateEducationUseCase;
        this.retrieveEducationUseCase = retrieveEducationUseCase;
        this.deleteEducationUseCase = deleteEducationUseCase;
    }

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
    public Boolean deleteEducation(Long education_id) {
        return deleteEducationUseCase.deleteEducation(education_id);
    }

}
