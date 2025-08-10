package com.keax.application.usecases.Institution;

import com.keax.domain.models.Institution;
import com.keax.domain.ports.in.Institution.RetrieveInstitutionUseCase;
import com.keax.domain.ports.out.InstitutionRepositoryPort;

import java.util.List;

public class RetrieveInstitutionUseCaseImpl implements RetrieveInstitutionUseCase {

    private final InstitutionRepositoryPort institutionRepositoryPort;

    public RetrieveInstitutionUseCaseImpl(InstitutionRepositoryPort institutionRepositoryPort) {
        this.institutionRepositoryPort = institutionRepositoryPort;
    }

    @Override
    public List<Institution> getListInstitution() {
        return institutionRepositoryPort.getListInstitution();
    }

}
