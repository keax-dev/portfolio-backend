package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.CreateEducationUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.education.domain.model.Education;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateEducationUseCaseImpl implements CreateEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;
    private final InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Education createEducation(Education education) {

        education.setEducationTitle(education.getEducationTitle().toUpperCase());

        educationRepositoryPort.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                education.getEducationTitle(),
                false,
                education.getInstitutionId()
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already an education with this title and institution");
                }
        );

        educationRepositoryPort.findByEducationPositionAndEducationDeleted(
                education.getEducationPosition(),
                false
        ).ifPresent(
                e -> {
                    throw new ResourceConflictException("There is already an education with this position");
                }
        );

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                education.getInstitutionId(),
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The institution entered was not found")
        );

        education.setEducationTitleEs(education.getEducationTitleEs().toUpperCase());
        education.setEducationPlace(education.getEducationPlace().toUpperCase());
        education.setEducationStart(toUpperCaseOrNull(education.getEducationStart()));
        education.setEducationStartEs(toUpperCaseOrNull(education.getEducationStartEs()));
        education.setEducationEnd(education.getEducationEnd().toUpperCase());
        education.setEducationEndEs(education.getEducationEndEs().toUpperCase());

        education.setEducationId(null);
        education.setEducationDeleted(false);

        return educationRepositoryPort.createEducation(education);
    }

    private static String toUpperCaseOrNull(String value) {
        return value == null || value.isBlank() ? null : value.toUpperCase();
    }

}
