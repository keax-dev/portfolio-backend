package com.keax.application.usecases.Institution;

import com.keax.domain.exceptions.ExceptionGlobal;
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
        if (institutionRepositoryPort.existsByInstitutionNameIgnoreCase(institution.getInstitution_name())){
            throw new ExceptionGlobal("Ya existe una institución con el nombre ingresado.");
        }
        return institutionRepositoryPort.save(institution);
    }

}
