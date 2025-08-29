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
    public Education updateEducation(Long education_id, Education education) {

        if(!institutionRepositoryPort.existsByInstitutionIdAndInstitutionDeleted(education.getInstitutionId(), false)){
            throw new ExceptionAlert("The institution entered was not found");
        }

        if (!educationRepositoryPort.existsByEducationIdAndEducationDeleted(education_id, false)){
            throw new ExceptionAlert("The education to be updated was not found");
        }

        education.setEducationTitle(education.getEducationTitle().toUpperCase());
        education.setEducationPlace(education.getEducationPlace().toUpperCase());
        education.setEducationStart(education.getEducationStart().toUpperCase());
        education.setEducationEnd(education.getEducationEnd().toUpperCase());
        education.setEducationId(education_id);
        education.setEducationDeleted(false);

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(education.getEducationTitle(), false, education.getInstitutionId()).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), education_id)){
                        throw new ExceptionAlert("The educational title to be updated is already registered in this category");
                    }
                }
        );

        return educationRepositoryPort.updateEducation(education);
    }

}
