package com.keax.institution.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.domain.ports.in.CreateInstitutionUseCase;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.institution.domain.model.Institution;
import com.keax.shared.domain.text.TextNormalizer;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateInstitutionUseCaseImpl implements CreateInstitutionUseCase {
    private final InstitutionRepositoryPort institutionRepositoryPort;

    @Override
    public Institution createInstitution(Institution institution) {

        institution.setInstitutionName(TextNormalizer.uppercase(institution.getInstitutionName()));

        institutionRepositoryPort.findByName(
                institution.getInstitutionName()
        ).ifPresent(
                e ->{
                    throw new ResourceConflictException("An institution with the entered name already exists");
                }
        );

        institution.setInstitutionNameEs(TextNormalizer.uppercase(institution.getInstitutionNameEs()));
        institution.setInstitutionUrl(TextNormalizer.trimToNull(institution.getInstitutionUrl()));

        institution.setInstitutionId(null);

        return institutionRepositoryPort.saveInstitution(institution);
    }

}
