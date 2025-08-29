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
    public Boolean deleteInstitution(Long institution_id) {
        if (institutionRepositoryPort.existsByInstitutionIdAndInstitutionDeleted(institution_id, false)){
            return institutionRepositoryPort.deleteInstitution(institution_id);
        }

        throw new ExceptionAlert("No existe la institución a eliminar.");
    }

}
