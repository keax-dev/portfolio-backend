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

        Education educationUpdate = educationRepositoryPort.findByEducationIdAndEducationDeleted(educationId, false).orElseThrow(
                () -> new ExceptionAlert("The education to be updated was not found")
        );

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(education.getInstitutionId(), false).orElseThrow(
                () -> new ExceptionAlert("The institution entered was not found")
        );

        educationUpdate.setEducationTitle(education.getEducationTitle().toUpperCase());
        educationUpdate.setInstitutionId(education.getInstitutionId());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(educationUpdate.getEducationTitle(), false, educationUpdate.getInstitutionId()).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ExceptionAlert("The educational title to be updated is already registered in this category");
                    }
                }
        );

        educationUpdate.setEducationPosition(education.getEducationPosition());

        educationRepositoryPort.findByEducationPositionAndEducationDeleted(educationUpdate.getEducationPosition(), false).ifPresent(
                e -> {
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ExceptionAlert("There is already an education with this position");
                    }
                }
        );

        educationUpdate.setEducationTitleEs(education.getEducationTitleEs().toUpperCase());
        educationUpdate.setEducationPlace(education.getEducationPlace().toUpperCase());
        educationUpdate.setEducationEnd(education.getEducationEnd().toUpperCase());
        educationUpdate.setEducationEndEs(education.getEducationEndEs().toUpperCase());
        educationUpdate.setEducationDeleted(false);

        if (education.getEducationStart() != null && !education.getEducationStart().isEmpty()){
            educationUpdate.setEducationStart(education.getEducationStart().toUpperCase());
            educationUpdate.setEducationStartEs(education.getEducationStartEs().toUpperCase());
        }

        return educationRepositoryPort.updateEducation(educationUpdate);
    }

}
