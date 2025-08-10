package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.DeleteInstitutionUseCase;
import com.keax.domain.ports.out.InstitutionRepositoryPort;

public class DeleteInstitutionUseCaseImpl implements DeleteInstitutionUseCase {

    private final InstitutionRepositoryPort institutionRepositoryPort;

    public DeleteInstitutionUseCaseImpl(InstitutionRepositoryPort institutionRepositoryPort) {
        this.institutionRepositoryPort = institutionRepositoryPort;
    }

    @Override
    public Boolean deleteInstitution(Long institution_id) {
        return institutionRepositoryPort.deleteInstitution(institution_id);
    }

}
