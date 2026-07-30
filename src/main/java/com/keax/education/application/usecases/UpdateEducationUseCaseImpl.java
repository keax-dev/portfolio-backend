package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.shared.domain.ports.out.InstitutionReferencePort;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.UpdateEducationUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.education.domain.model.Education;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateEducationUseCaseImpl implements UpdateEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;
    private final InstitutionReferencePort institutionReferencePort;

    @Override
    public Education updateEducation(Long educationId, Education education) {

        Education educationUpdate = educationRepositoryPort.findById(educationId).orElseThrow(
                () -> new ResourceNotFoundException("The education to be updated was not found")
        );

        if (!institutionReferencePort.existsActiveInstitution(education.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        educationUpdate.setEducationTitle(TextNormalizer.uppercase(education.getEducationTitle()));
        educationUpdate.setInstitutionId(education.getInstitutionId());

        educationRepositoryPort.findByTitleAndInstitutionId(
                educationUpdate.getEducationTitle(),
                educationUpdate.getInstitutionId()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ResourceConflictException("The educational title to be updated is already registered in this category");
                    }
                }
        );

        educationUpdate.setEducationPosition(education.getEducationPosition());
        educationRepositoryPort.findByPosition(
                educationUpdate.getEducationPosition()
        ).ifPresent(
                e -> {
                    if (!Objects.equals(e.getEducationId(), educationUpdate.getEducationId())){
                        throw new ResourceConflictException("There is already an education with this position");
                    }
                }
        );

        educationUpdate.setEducationTitleEs(TextNormalizer.uppercase(education.getEducationTitleEs()));
        educationUpdate.setEducationPlace(TextNormalizer.uppercase(education.getEducationPlace()));
        educationUpdate.setEducationEnd(TextNormalizer.uppercase(education.getEducationEnd()));
        educationUpdate.setEducationEndEs(TextNormalizer.uppercase(education.getEducationEndEs()));
        educationUpdate.setEducationStart(toUpperCaseOrNull(education.getEducationStart()));
        educationUpdate.setEducationStartEs(toUpperCaseOrNull(education.getEducationStartEs()));
        return educationRepositoryPort.updateEducation(educationUpdate);
    }

    private static String toUpperCaseOrNull(String value) {
        return TextNormalizer.uppercase(value);
    }

}
