package com.keax.application.usecases.Education;

import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.models.Education;
import com.keax.domain.ports.in.Education.CreateEducationUseCase;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.ports.out.InstitutionRepositoryPort;

public class CreateEducationUseCaseImpl implements CreateEducationUseCase {

    private final EducationRepositoryPort educationRepositoryPort;
    private final InstitutionRepositoryPort institutionRepositoryPort;

    public CreateEducationUseCaseImpl(EducationRepositoryPort educationRepositoryPort, InstitutionRepositoryPort institutionRepositoryPort) {
        this.educationRepositoryPort = educationRepositoryPort;
        this.institutionRepositoryPort = institutionRepositoryPort;
    }

    @Override
    public Education createEducation(Education education) {

        if(!institutionRepositoryPort.existsByInstitutionIdAndInstitutionDeleted(education.getInstitutionId(), false)){
            throw new ExceptionAlert("The institution entered was not found");
        }

        education.setEducationTitle(education.getEducationTitle().toUpperCase());
        education.setEducationPlace(education.getEducationPlace().toUpperCase());
        education.setEducationStart(education.getEducationStart().toUpperCase());
        education.setEducationEnd(education.getEducationEnd().toUpperCase());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(education.getEducationTitle(), false, education.getInstitutionId()).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already an education with this title and institution");
                }
        );

        return educationRepositoryPort.createEducation(education);
    }
}
