package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaInstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import com.keax.infrastructure.entities.InstitutionEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Institution;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaInstitutionRepositoryAdapter implements InstitutionRepositoryPort {

    @Autowired
    private JpaInstitutionRepository jpaInstitutionRepository;

    @Override
    public Institution saveInstitution(Institution institution) {
        InstitutionEntity saved = jpaInstitutionRepository.save(fromDomainModel(institution));
        return toDomainModel(saved);
    }

    @Override
    public Institution updateInstitution(Institution institution) {
        InstitutionEntity update = jpaInstitutionRepository.save(fromDomainModel(institution));
        return  toDomainModel(update);
    }

    @Override
    public List<Institution> getListInstitution() {
        return jpaInstitutionRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Institution deleteInstitution(Institution institution) {
        InstitutionEntity deleted = jpaInstitutionRepository.save(fromDomainModel(institution));
        return  toDomainModel(deleted);
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionDeleted(deleted)
                .stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionNameAndInstitutionDeleted(institutionName, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institutionId, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionIdAndInstitutionDeleted(institutionId, deleted).map(this::toDomainModel);
    }

    private Institution toDomainModel(InstitutionEntity institutionEntity){
        return new Institution(
                institutionEntity.getInstitutionId(),
                institutionEntity.getInstitutionName(),
                institutionEntity.getInstitutionNameEs(),
                institutionEntity.getInstitutionUrl(),
                institutionEntity.getInstitutionDeleted()
        );
    }

    private InstitutionEntity fromDomainModel(Institution institution){
        return  new InstitutionEntity(
                institution.getInstitutionId(),
                institution.getInstitutionName(),
                institution.getInstitutionNameEs(),
                institution.getInstitutionUrl(),
                institution.getInstitutionDeleted()
        );
    }

}
