package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaInstitutionRepository;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.infrastructure.entities.InstitutionEntity;
import com.keax.domain.models.Institution;

import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

public class JpaInstitutionRepositoryAdapter implements InstitutionRepositoryPort {

    private final JpaInstitutionRepository jpaInstitutionRepository;

    public JpaInstitutionRepositoryAdapter(JpaInstitutionRepository jpaInstitutionRepository) {
        this.jpaInstitutionRepository = jpaInstitutionRepository;
    }

    @Override
    public Institution saveInstitution(Institution institution) {
        InstitutionEntity saved = jpaInstitutionRepository.save(fromDomainModel(institution));
        return toDomainModel(saved);
    }

    @Override
    public Institution updateInstitution(Long institution_id, Institution institution) {
        InstitutionEntity update = jpaInstitutionRepository.save(fromDomainModel(institution));
        return  toDomainModel(update);
    }

    @Override
    public List<Institution> getListInstitution() {
        return jpaInstitutionRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Boolean deleteInstitution(Long institution_id) {

        Optional<InstitutionEntity> optional = jpaInstitutionRepository.findById(institution_id);

        if (optional.isPresent()){
            InstitutionEntity institutionEntity = optional.get();
            institutionEntity.setInstitutionDeleted(true);
            jpaInstitutionRepository.save(institutionEntity);
            return true;
        }

        return false;
    }

    @Override
    public Boolean existsByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted) {
        return jpaInstitutionRepository.existsByInstitutionNameAndInstitutionDeleted(institutionName, deleted);
    }

    @Override
    public Boolean existsByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted) {
        return jpaInstitutionRepository.existsByInstitutionIdAndInstitutionDeleted(institution_id, deleted);
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionDeleted(deleted)
                .stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Institution> findById(Long institution_id) {
        return jpaInstitutionRepository.findById(institution_id).map(this::toDomainModel);
    }

    @Override
    public Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionNameAndInstitutionDeleted(institutionName, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institution_id, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionIdAndInstitutionDeleted(institution_id, deleted).map(this::toDomainModel);
    }

    private Institution toDomainModel(InstitutionEntity institutionEntity){
        return new Institution(
                institutionEntity.getInstitutionId(),
                institutionEntity.getInstitutionName(),
                institutionEntity.getInstitutionUrl(),
                institutionEntity.getInstitutionDeleted()
        );
    }

    private InstitutionEntity fromDomainModel(Institution institution){
        return  new InstitutionEntity(
                institution.getInstitutionId(),
                institution.getInstitutionName(),
                institution.getInstitutionUrl(),
                institution.getInstitutionDeleted()
        );
    }

}
