package com.keax.application.usecases.Institution;

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
    public Optional<Institution> updateInstitution(Long institution_id, Institution institution) {
        return institutionRepositoryPort.updateInstitution(institution_id, institution);
    }

}
