package com.keax.institution.application.usecases;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.domain.ports.in.DeleteInstitutionUseCase;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.institution.domain.model.Institution;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeleteInstitutionUseCaseImpl implements DeleteInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Override
    public Institution deleteInstitution(Long institutionId) {

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The institution to be eliminated does not exist")
        );

        if (educationRepositoryPort.existsByInstitution_InstitutionIdAndEducationDeleted(institutionId, false)){
            throw new ExceptionAlert("The institution cannot be deleted because it has associated records");
        }

        institution.setInstitutionDeleted(true);

        return institutionRepositoryPort.deleteInstitution(institution);
    }

}
