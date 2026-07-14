package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.DeleteEducationUseCase;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.education.domain.model.Education;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteEducationUseCaseImpl implements DeleteEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;

    @Override
    public Education deleteEducation(Long educationId) {

        Education education = educationRepositoryPort.findByEducationIdAndEducationDeleted(
                educationId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The institution to delete was not found")
        );

        education.setEducationDeleted(true);

        return educationRepositoryPort.deleteEducation(education);
    }

}
