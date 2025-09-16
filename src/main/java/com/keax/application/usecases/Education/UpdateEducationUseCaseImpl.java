package com.keax.application.usecases.Education;

import com.keax.domain.ports.in.Education.UpdateEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Education;
import java.util.Objects;

@Component
public class UpdateEducationUseCaseImpl implements UpdateEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Education updateEducation(Long educationId, Education education) {

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(education.getInstitutionId(), false).orElseThrow(
                () -> new ExceptionAlert("The institution entered was not found")
        );

        educationRepositoryPort.findByEducationIdAndEducationDeleted(educationId, false).orElseThrow(
                () -> new ExceptionAlert("The education to be updated was not found")
        );

        education.setEducationTitle(education.getEducationTitle().toUpperCase());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(education.getEducationTitle(), false, education.getInstitutionId()).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), educationId)){
                        throw new ExceptionAlert("The educational title to be updated is already registered in this category");
                    }
                }
        );

        education.setEducationPlace(education.getEducationPlace().toUpperCase());
        education.setEducationStart(education.getEducationStart().toUpperCase());
        education.setEducationEnd(education.getEducationEnd().toUpperCase());
        education.setEducationId(educationId);
        education.setEducationDeleted(false);

        return educationRepositoryPort.updateEducation(education);
    }

}
