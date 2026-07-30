package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.shared.domain.ports.out.InstitutionReferencePort;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.CreateEducationUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.education.domain.model.Education;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateEducationUseCaseImpl implements CreateEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;
    private final InstitutionReferencePort institutionReferencePort;

    @Override
    public Education createEducation(Education education) {

        education.setEducationTitle(TextNormalizer.uppercase(education.getEducationTitle()));

        educationRepositoryPort.findByTitleAndInstitutionId(
                education.getEducationTitle(),
                education.getInstitutionId()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already an education with this title and institution");
                }
        );

        educationRepositoryPort.findByPosition(
                education.getEducationPosition()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already an education with this position");
                }
        );

        if (!institutionReferencePort.existsActiveInstitution(education.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        education.setEducationTitleEs(TextNormalizer.uppercase(education.getEducationTitleEs()));
        education.setEducationPlace(TextNormalizer.uppercase(education.getEducationPlace()));
        education.setEducationStart(toUpperCaseOrNull(education.getEducationStart()));
        education.setEducationStartEs(toUpperCaseOrNull(education.getEducationStartEs()));
        education.setEducationEnd(TextNormalizer.uppercase(education.getEducationEnd()));
        education.setEducationEndEs(TextNormalizer.uppercase(education.getEducationEndEs()));

        education.setEducationId(null);

        return educationRepositoryPort.createEducation(education);
    }

    private static String toUpperCaseOrNull(String value) {
        return TextNormalizer.uppercase(value);
    }

}
