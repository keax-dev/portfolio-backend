package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.UpdateInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;
import java.util.Objects;

@Component
public class UpdateInstitutionUseCaseImpl implements UpdateInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution updateInstitution(Long institutionId, Institution institution) {

        Institution institutionUpdate = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institutionId, false).orElseThrow(
                () -> new ExceptionAlert("The institution to be updated does not exist")
        );

        institutionUpdate.setInstitutionName(institution.getInstitutionName().toUpperCase());

        institutionRepositoryPort.findByInstitutionNameAndInstitutionDeleted(institutionUpdate.getInstitutionName(), false).ifPresent(
                e ->{
                    if (!Objects.equals(e.getInstitutionId(), institutionUpdate.getInstitutionId())){
                        throw new ExceptionAlert("The name of the institution to be updated is already registered");
                    }
                }
        );

        institutionUpdate.setInstitutionId(institutionId);
        institutionUpdate.setInstitutionNameEs(institution.getInstitutionNameEs().toUpperCase());
        institutionUpdate.setInstitutionDeleted(false);

        return institutionRepositoryPort.updateInstitution(institutionUpdate);
    }

}
