package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.RetrieveInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;
import java.util.List;

@Component
public class RetrieveInstitutionUseCaseImpl implements RetrieveInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public List<Institution> getListInstitution() {

        List<Institution> institutions = institutionRepositoryPort.getListInstitution();

        if (institutions.isEmpty()){
            throw new ExceptionAlert("There are no created institutions");
        }

        return institutions;
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {

        List<Institution> institutions = institutionRepositoryPort.findByInstitutionDeleted(deleted);

        if (institutions.isEmpty()){
            throw new ExceptionAlert("There are no created institutions");
        }

        return institutions;
    }

}
