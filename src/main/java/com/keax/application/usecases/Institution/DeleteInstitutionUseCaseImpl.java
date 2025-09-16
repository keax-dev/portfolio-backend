package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.DeleteInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;

@Component
public class DeleteInstitutionUseCaseImpl implements DeleteInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Boolean deleteInstitution(Long institutionId) {

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institutionId, false).orElseThrow(
                () -> new ExceptionAlert("The institution to be eliminated does not exist")
        );

        return institutionRepositoryPort.deleteInstitution(institutionId);
    }

}
