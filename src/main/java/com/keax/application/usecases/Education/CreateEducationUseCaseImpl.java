package com.keax.application.usecases.Education;

import com.keax.domain.ports.in.Education.CreateEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Education;

@Component
public class CreateEducationUseCaseImpl implements CreateEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Education createEducation(Education education) {

        education.setEducationTitle(education.getEducationTitle().toUpperCase());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(education.getEducationTitle(), false, education.getInstitutionId()).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already an education with this title and institution");
                }
        );

        educationRepositoryPort.findByEducationPositionAndEducationDeleted(education.getEducationPosition(), false).ifPresent(
                e -> {
                    throw new ExceptionAlert("There is already an education with this position");
                }
        );

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(education.getInstitutionId(), false).orElseThrow(
                () -> new ExceptionAlert("The institution entered was not found")
        );

        education.setEducationPlace(education.getEducationPlace().toUpperCase());
        education.setEducationStart(education.getEducationStart().toUpperCase());
        education.setEducationEnd(education.getEducationEnd().toUpperCase());
        education.setEducationId(null);

        return educationRepositoryPort.createEducation(education);
    }

}
