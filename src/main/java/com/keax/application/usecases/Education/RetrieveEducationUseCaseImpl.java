package com.keax.application.usecases.Education;

import com.keax.domain.ports.in.Education.RetrieveEducationUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Education;
import java.util.List;

@Component
public class RetrieveEducationUseCaseImpl implements RetrieveEducationUseCase {

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Override
    public List<Education> getListEducation() {

        List<Education> listEducation = educationRepositoryPort.getListEducation();

        return validateNotEmpty(listEducation);
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {

        List<Education> listEducation = educationRepositoryPort.findByEducationDeleted(deleted);

        return validateNotEmpty(listEducation);
    }

    private List<Education> validateNotEmpty(List<Education> educationList) {

        if (educationList.isEmpty()) {
            throw new ExceptionAlert("There are no created educations");
        }

        return educationList;
    }

}
