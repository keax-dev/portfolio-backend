package com.keax.application.usecases.Education;

import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.models.Education;
import com.keax.domain.ports.in.Education.RetrieveEducationUseCase;
import com.keax.domain.ports.out.EducationRepositoryPort;

import java.util.List;

public class RetrieveEducationUseCaseImpl implements RetrieveEducationUseCase {

    private final EducationRepositoryPort educationRepositoryPort;

    public RetrieveEducationUseCaseImpl(EducationRepositoryPort educationRepositoryPort) {
        this.educationRepositoryPort = educationRepositoryPort;
    }

    @Override
    public List<Education> getListEducation() {

        List<Education> listEducation = educationRepositoryPort.getListEducation();

        if (listEducation.isEmpty()){
            throw new ExceptionAlert("There are no created educations");
        }

        return listEducation;
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {

        List<Education> listEducation = educationRepositoryPort.findByEducationDeleted(deleted);

        if (listEducation.isEmpty()){
            throw new ExceptionAlert("There are no created educations");
        }

        return listEducation;
    }
}
