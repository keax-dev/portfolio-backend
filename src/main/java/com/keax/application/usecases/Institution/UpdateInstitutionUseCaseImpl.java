package com.keax.application.usecases.Institution;

import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.models.Institution;
import com.keax.domain.ports.in.Institution.UpdateInstitutionUseCase;
import com.keax.domain.ports.out.InstitutionRepositoryPort;

import java.util.Optional;

public class UpdateInstitutionUseCaseImpl implements UpdateInstitutionUseCase {

    private final InstitutionRepositoryPort institutionRepositoryPort;

    public UpdateInstitutionUseCaseImpl(InstitutionRepositoryPort institutionRepositoryPort) {
        this.institutionRepositoryPort = institutionRepositoryPort;
    }

    @Override
    public Institution updateInstitution(Long institution_id, Institution institution) {

        institution.setInstitutionName(institution.getInstitutionName().toUpperCase());

        if (!institutionRepositoryPort.existsByInstitutionIdAndInstitutionDeleted(institution_id, false)){
            throw new ExceptionAlert("No existe la institución a actualizar");
        }

        Optional<Institution> findName = institutionRepositoryPort.findByInstitutionNameAndInstitutionDeleted(institution.getInstitutionName(), false);

        if (findName.isPresent() && findName.get().getInstitutionId() != institution_id){
            throw new ExceptionAlert("El nombre de la institución a actualizar ya se encuentra registrado");
        }

        institution.setInstitutionId(institution_id);
        institution.setInstitutionDeleted(false);
        return institutionRepositoryPort.updateInstitution(institution_id, institution);
    }

}
