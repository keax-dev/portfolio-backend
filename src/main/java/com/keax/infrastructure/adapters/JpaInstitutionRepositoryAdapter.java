package com.keax.infrastructure.adapters;

import com.keax.domain.models.Institution;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.infrastructure.entities.InstitutionEntity;
import com.keax.infrastructure.repositories.JpaInstitutionRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaInstitutionRepositoryAdapter implements InstitutionRepositoryPort {

    private final JpaInstitutionRepository jpaInstitutionRepository;

    public JpaInstitutionRepositoryAdapter(JpaInstitutionRepository jpaInstitutionRepository) {
        this.jpaInstitutionRepository = jpaInstitutionRepository;
    }

    @Override
    public Institution save(Institution institution) {
        InstitutionEntity institutionEntity = InstitutionEntity.fromDomainModel(institution);
        InstitutionEntity saved = jpaInstitutionRepository.save(institutionEntity);
        return saved.toDomainModel();
    }

    @Override
    public Optional<Institution> updateInstitution(Long institution_id, Institution institution) {

        if (jpaInstitutionRepository.existsById(institution_id)){
            institution.setInstitution_id(institution_id);
            InstitutionEntity institutionEntity = InstitutionEntity.fromDomainModel(institution);
            InstitutionEntity updateInstitutionEntity = jpaInstitutionRepository.save(institutionEntity);
            return Optional.of(updateInstitutionEntity.toDomainModel());
        }

        return Optional.empty();
    }

    @Override
    public List<Institution> getListInstitution() {
        return jpaInstitutionRepository.findAll().stream().map(
                InstitutionEntity::toDomainModel
        ).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteInstitution(Long institution_id) {

        Optional<InstitutionEntity> optional = jpaInstitutionRepository.findById(institution_id);

        if (optional.isPresent()){

            InstitutionEntity institutionEntity = optional.get();
            institutionEntity.setInstitution_deleted(true);

            jpaInstitutionRepository.save(institutionEntity);

            return true;
        }

        return false;
    }
}
