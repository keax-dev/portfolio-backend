package com.keax.application.usecases.Education;

import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.models.Education;
import com.keax.domain.ports.in.Education.DeleteEducationUseCase;
import com.keax.domain.ports.out.EducationRepositoryPort;

import java.util.Optional;

public class DeleteEducationUseCaseImpl implements DeleteEducationUseCase {

    private final EducationRepositoryPort educationRepositoryPort;

    public DeleteEducationUseCaseImpl(EducationRepositoryPort educationRepositoryPort) {
        this.educationRepositoryPort = educationRepositoryPort;
    }

    @Override
    public Boolean deleteEducation(Long education_id) {

        Optional<Education> educationFind = educationRepositoryPort.findByEducationIdAndEducationDeleted(education_id, false);

        if (educationFind.isEmpty()){
            throw new ExceptionAlert("The institution to delete was not found");
        }

        educationRepositoryPort.deleteEducation(educationFind.get());

        return true;
    }
}
