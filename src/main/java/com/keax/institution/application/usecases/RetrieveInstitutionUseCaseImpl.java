package com.keax.institution.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.in.RetrieveInstitutionUseCase;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import org.springframework.transaction.annotation.Transactional;
import com.keax.shared.domain.exceptions.ExceptionAlert;
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

        List<Institution> institutions = institutionRepositoryPort.getListInstitution();

        return validateNotEmpty(institutions);
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {

        List<Institution> institutions = institutionRepositoryPort.findByInstitutionDeleted(deleted);

        return validateNotEmpty(institutions);
    }

    private List<Institution> validateNotEmpty(List<Institution> institutionList) {

        if (institutionList.isEmpty()) {
            throw new ExceptionAlert("There are no created institutions");
        }

        return institutionList;
    }

}
