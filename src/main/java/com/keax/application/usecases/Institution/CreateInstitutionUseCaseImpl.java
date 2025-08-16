package com.keax.application.usecases.Institution;

import com.keax.domain.exceptions.ExceptionAlert;
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

        institution.setInstitutionName(institution.getInstitutionName().toUpperCase());

        if (institutionRepositoryPort.existsByInstitutionNameAndInstitutionDeleted(institution.getInstitutionName(),false)){
            throw new ExceptionAlert("Ya existe una institución con el nombre ingresado");
        }
        return institutionRepositoryPort.saveInstitution(institution);
    }

}
