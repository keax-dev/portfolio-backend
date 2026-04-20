package com.keax.education.application.usecases;

import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.ports.in.DeleteEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.education.domain.model.Education;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteEducationUseCaseImpl implements DeleteEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Override
    public Education deleteEducation(Long educationId) {

        Education education = educationRepositoryPort.findByEducationIdAndEducationDeleted(
                educationId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The institution to delete was not found")
        );

        education.setEducationDeleted(true);

        return educationRepositoryPort.deleteEducation(education);
    }

}
