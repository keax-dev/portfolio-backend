package com.keax.education.application.usecases;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.UpdateEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.education.domain.model.Education;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
public class UpdateEducationUseCaseImpl implements UpdateEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Education updateEducation(Long educationId, Education education) {

        Education educationUpdate = educationRepositoryPort.findByEducationIdAndEducationDeleted(
                educationId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The education to be updated was not found")
        );

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                education.getInstitutionId(),
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The institution entered was not found")
        );

        educationUpdate.setEducationTitle(education.getEducationTitle().toUpperCase());
        educationUpdate.setInstitutionId(education.getInstitutionId());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                educationUpdate.getEducationTitle(),
                false,
                educationUpdate.getInstitutionId()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ExceptionAlert("The educational title to be updated is already registered in this category");
                    }
                }
        );

        educationUpdate.setEducationPosition(education.getEducationPosition());
        educationRepositoryPort.findByEducationPositionAndEducationDeleted(
                educationUpdate.getEducationPosition(),
                false
        ).ifPresent(
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
            educationUpdate.setEducationStartEs(toUpperCaseOrNull(education.getEducationStartEs()));
        }

        return educationRepositoryPort.updateEducation(educationUpdate);
    }

    private static String toUpperCaseOrNull(String value) {
        return value == null || value.isBlank() ? null : value.toUpperCase();
    }

}
