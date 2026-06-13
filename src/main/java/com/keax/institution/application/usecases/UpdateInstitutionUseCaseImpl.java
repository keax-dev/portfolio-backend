package com.keax.institution.application.usecases;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.domain.ports.in.UpdateInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.institution.domain.model.Institution;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
public class UpdateInstitutionUseCaseImpl implements UpdateInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution updateInstitution(Long institutionId, Institution institution) {

        Institution institutionUpdate = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The institution to be updated does not exist")
        );

        institutionUpdate.setInstitutionName(institution.getInstitutionName().toUpperCase());
        institutionRepositoryPort.findByInstitutionNameAndInstitutionDeleted(
                institutionUpdate.getInstitutionName(),
                false
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getInstitutionId(), institutionUpdate.getInstitutionId())){
                        throw new ResourceConflictException("The name of the institution to be updated is already registered");
                    }
                }
        );

        institutionUpdate.setInstitutionId(institutionId);
        institutionUpdate.setInstitutionNameEs(institution.getInstitutionNameEs().toUpperCase());
        institutionUpdate.setInstitutionDeleted(false);

        return institutionRepositoryPort.updateInstitution(institutionUpdate);
    }

}
