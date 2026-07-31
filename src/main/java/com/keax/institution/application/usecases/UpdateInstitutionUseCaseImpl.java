package com.keax.institution.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.domain.ports.in.UpdateInstitutionUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.institution.domain.model.Institution;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateInstitutionUseCaseImpl implements UpdateInstitutionUseCase {
    private final InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution updateInstitution(Long institutionId, Institution institution) {

        Institution institutionUpdate = institutionRepositoryPort.findById(institutionId).orElseThrow(
                () -> new ResourceNotFoundException("The institution to be updated does not exist")
        );

        institutionUpdate.setInstitutionName(TextNormalizer.uppercase(institution.getInstitutionName()));
        institutionRepositoryPort.findByName(
                institutionUpdate.getInstitutionName()
        ).ifPresent(
                e ->{
                    if (!Objects.equals(e.getInstitutionId(), institutionUpdate.getInstitutionId())){
                        throw new ResourceConflictException("The name of the institution to be updated is already registered");
                    }
                }
        );

        institutionUpdate.setInstitutionId(institutionId);
        institutionUpdate.setInstitutionNameEs(TextNormalizer.uppercase(institution.getInstitutionNameEs()));
        institutionUpdate.setInstitutionUrl(TextNormalizer.trimToNull(institution.getInstitutionUrl()));

        return institutionRepositoryPort.updateInstitution(institutionUpdate);
    }

}
