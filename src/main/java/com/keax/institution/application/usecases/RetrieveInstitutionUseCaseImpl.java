package com.keax.institution.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.in.RetrieveInstitutionUseCase;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.institution.domain.model.Institution;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveInstitutionUseCaseImpl implements RetrieveInstitutionUseCase {
    private final InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public List<Institution> getListInstitution() {

        return institutionRepositoryPort.getListInstitution();
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {

        return institutionRepositoryPort.findByInstitutionDeleted(deleted);
    }

}
