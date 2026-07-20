package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.shared.domain.ports.out.EducationInstitutionReferencePort;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.UpdateEducationUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.education.domain.model.Education;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateEducationUseCaseImpl implements UpdateEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;
    private final EducationInstitutionReferencePort educationInstitutionReferencePort;

    @Override
    public Education updateEducation(Long educationId, Education education) {

        Education educationUpdate = educationRepositoryPort.findByEducationIdAndEducationDeleted(
                educationId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The education to be updated was not found")
        );

        if (!educationInstitutionReferencePort.existsActiveInstitution(education.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        educationUpdate.setEducationTitle(education.getEducationTitle().toUpperCase());
        educationUpdate.setInstitutionId(education.getInstitutionId());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                educationUpdate.getEducationTitle(),
                false,
                educationUpdate.getInstitutionId()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ResourceConflictException("The educational title to be updated is already registered in this category");
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
                        throw new ResourceConflictException("There is already an education with this position");
                    }
                }
        );

        educationUpdate.setEducationTitleEs(education.getEducationTitleEs().toUpperCase());
        educationUpdate.setEducationPlace(education.getEducationPlace().toUpperCase());
        educationUpdate.setEducationEnd(education.getEducationEnd().toUpperCase());
        educationUpdate.setEducationEndEs(education.getEducationEndEs().toUpperCase());
        educationUpdate.setEducationStart(toUpperCaseOrNull(education.getEducationStart()));
        educationUpdate.setEducationStartEs(toUpperCaseOrNull(education.getEducationStartEs()));
        educationUpdate.setEducationDeleted(false);

        return educationRepositoryPort.updateEducation(educationUpdate);
    }

    private static String toUpperCaseOrNull(String value) {
        return value == null || value.isBlank() ? null : value.toUpperCase();
    }

}
