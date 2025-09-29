package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.DeleteInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;

@Component
public class DeleteInstitutionUseCaseImpl implements DeleteInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Autowired
    private EducationRepositoryPort educationRepositoryPort;

    @Override
    public Institution deleteInstitution(Long institutionId) {

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institutionId, false).orElseThrow(
                () -> new ExceptionAlert("The institution to be eliminated does not exist")
        );

        if (educationRepositoryPort.existsByInstitution_InstitutionIdAndEducationDeleted(institutionId, false)){
            throw new ExceptionAlert("The institution cannot be deleted because it has associated records");
        }

        institution.setInstitutionDeleted(true);

        return institutionRepositoryPort.deleteInstitution(institution);
    }

}
