package com.keax.institution.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.domain.ports.in.DeleteInstitutionUseCase;
import com.keax.shared.domain.ports.out.EducationInstitutionReferencePort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.institution.domain.model.Institution;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteInstitutionUseCaseImpl implements DeleteInstitutionUseCase {
    private final InstitutionRepositoryPort institutionRepositoryPort;
    private final EducationInstitutionReferencePort educationInstitutionReferencePort;

    @Override
    public Institution deleteInstitution(Long institutionId) {

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The institution to be eliminated does not exist")
        );

        if (educationInstitutionReferencePort.existsActiveEducationForInstitution(institutionId)){
            throw new ResourceConflictException("The institution cannot be deleted because it has associated records");
        }

        institution.setInstitutionDeleted(true);

        return institutionRepositoryPort.deleteInstitution(institution);
    }

}
