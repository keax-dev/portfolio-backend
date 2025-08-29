package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.CreateInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;

@Component
public class CreateInstitutionUseCaseImpl implements CreateInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution createInstitution(Institution institution) {

        institution.setInstitutionName(institution.getInstitutionName().toUpperCase());

        if (institutionRepositoryPort.existsByInstitutionNameAndInstitutionDeleted(institution.getInstitutionName(),false)){
            throw new ExceptionAlert("Ya existe una institución con el nombre ingresado");
        }
        return institutionRepositoryPort.saveInstitution(institution);
    }

}
