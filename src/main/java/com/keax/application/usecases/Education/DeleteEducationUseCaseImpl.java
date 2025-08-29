package com.keax.application.usecases.Education;

import com.keax.domain.ports.in.Education.DeleteEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Education;
import java.util.Optional;

@Component
public class DeleteEducationUseCaseImpl implements DeleteEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Override
    public Education deleteEducation(Long education_id) {

        Optional<Education> educationFind = educationRepositoryPort.findByEducationIdAndEducationDeleted(education_id, false);

        if (educationFind.isEmpty()){
            throw new ExceptionAlert("The institution to delete was not found");
        }

        Education education = educationFind.get();
        education.setEducationDeleted(true);

        return educationRepositoryPort.deleteEducation(education);
    }

}
