package com.keax.application.usecases.Institution;

import com.keax.domain.ports.in.Institution.UpdateInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;
import java.util.Optional;
import java.util.Objects;

@Component
public class UpdateInstitutionUseCaseImpl implements UpdateInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution updateInstitution(Long institution_id, Institution institution) {

        institution.setInstitutionName(institution.getInstitutionName().toUpperCase());

        institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institution_id, false).orElseThrow(
                () -> new ExceptionAlert("The institution to be updated does not exist")
        );

        Optional<Institution> findName = institutionRepositoryPort.findByInstitutionNameAndInstitutionDeleted(institution.getInstitutionName(), false);

        if (findName.isPresent() && !Objects.equals(findName.get().getInstitutionId(), institution_id)){
            throw new ExceptionAlert("The name of the institution to be updated is already registered");
        }

        institution.setInstitutionId(institution_id);
        institution.setInstitutionDeleted(false);
        return institutionRepositoryPort.updateInstitution(institution_id, institution);
    }

}
