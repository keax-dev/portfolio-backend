package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.education.domain.model.Education;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveEducationUseCaseImpl implements RetrieveEducationUseCase {
    private final EducationRepositoryPort educationRepositoryPort;

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
