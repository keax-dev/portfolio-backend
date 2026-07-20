package com.keax.education.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.education.domain.ports.in.RetrieveEducationUseCase;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
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

        return educationRepositoryPort.getListEducation();
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {

        return educationRepositoryPort.findByEducationDeleted(deleted);
    }

}
