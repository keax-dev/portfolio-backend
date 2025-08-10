package com.keax.application.services;

import com.keax.domain.models.Institution;
import com.keax.domain.ports.in.Institution.CreateInstitutionUseCase;
import com.keax.domain.ports.in.Institution.DeleteInstitutionUseCase;
import com.keax.domain.ports.in.Institution.RetrieveInstitutionUseCase;
import com.keax.domain.ports.in.Institution.UpdateInstitutionUseCase;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public class InstitutionService implements CreateInstitutionUseCase, UpdateInstitutionUseCase, RetrieveInstitutionUseCase, DeleteInstitutionUseCase {


    private final CreateInstitutionUseCase createInstitutionUseCase;
    private final UpdateInstitutionUseCase updateInstitutionUseCase;
    private final RetrieveInstitutionUseCase retrieveInstitutionUseCase;
    private final DeleteInstitutionUseCase deleteInstitutionUseCase;

    public InstitutionService(CreateInstitutionUseCase createInstitutionUseCase, UpdateInstitutionUseCase updateInstitutionUseCase, RetrieveInstitutionUseCase retrieveInstitutionUseCase, DeleteInstitutionUseCase deleteInstitutionUseCase) {
        this.createInstitutionUseCase = createInstitutionUseCase;
        this.updateInstitutionUseCase = updateInstitutionUseCase;
        this.retrieveInstitutionUseCase = retrieveInstitutionUseCase;
        this.deleteInstitutionUseCase = deleteInstitutionUseCase;
    }

    @Override
    public Institution createInstitution(Institution institution) {
        return createInstitutionUseCase.createInstitution(institution);
    }

    @Override
    public Optional<Institution> updateInstitution(Long institution_id, Institution institution) {
        return updateInstitutionUseCase.updateInstitution(institution_id, institution);
    }

    @Override
    public Boolean deleteInstitution(Long institution_id) {
        return deleteInstitutionUseCase.deleteInstitution(institution_id);
    }

    @Override
    public List<Institution> getListInstitution() {
        return retrieveInstitutionUseCase.getListInstitution();
    }

}
