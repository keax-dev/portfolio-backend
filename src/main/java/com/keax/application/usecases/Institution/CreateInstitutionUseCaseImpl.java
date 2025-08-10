package com.keax.application.usecases.Institution;

import com.keax.domain.models.Institution;
import com.keax.domain.ports.in.Institution.CreateInstitutionUseCase;
import com.keax.domain.ports.out.InstitutionRepositoryPort;

public class CreateInstitutionUseCaseImpl implements CreateInstitutionUseCase {

    private final InstitutionRepositoryPort institutionRepositoryPort;

    public CreateInstitutionUseCaseImpl(InstitutionRepositoryPort institutionRepositoryPort) {
        this.institutionRepositoryPort = institutionRepositoryPort;
    }

    @Override
    public Institution createInstitution(Institution institution) {
        return institutionRepositoryPort.save(institution);
    }

}
