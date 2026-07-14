package com.keax.institution.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.institution.domain.model.Institution;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import com.keax.institution.infrastructure.out.persistence.mapper.InstitutionPersistenceMapper;
import com.keax.institution.infrastructure.out.persistence.repository.JpaInstitutionRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InstitutionPersistenceAdapter implements InstitutionRepositoryPort {
    private final JpaInstitutionRepository jpaInstitutionRepository;

    @Override
    public Institution saveInstitution(Institution institution) {
        InstitutionEntity saved = jpaInstitutionRepository.save(
                InstitutionPersistenceMapper.toEntity(institution)
        );
        return InstitutionPersistenceMapper.toDomain(saved);
    }

    @Override
    public Institution updateInstitution(Institution institution) {
        InstitutionEntity updated = jpaInstitutionRepository.save(
                InstitutionPersistenceMapper.toEntity(institution)
        );
        return InstitutionPersistenceMapper.toDomain(updated);
    }

    @Override
    public Institution deleteInstitution(Institution institution) {
        InstitutionEntity deleted = jpaInstitutionRepository.save(
                InstitutionPersistenceMapper.toEntity(institution)
        );
        return InstitutionPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<Institution> getListInstitution() {
        return jpaInstitutionRepository.findByInstitutionDeleted(false).stream()
                .map(InstitutionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Institution> findByInstitutionDeleted(Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionDeleted(deleted).stream()
                .map(InstitutionPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Institution> findByInstitutionNameAndInstitutionDeleted(String institutionName, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionNameAndInstitutionDeleted(
                institutionName,
                deleted
        ).map(InstitutionPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Institution> findByInstitutionIdAndInstitutionDeleted(Long institutionId, Boolean deleted) {
        return jpaInstitutionRepository.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                deleted
        ).map(InstitutionPersistenceMapper::toDomain);
    }

}
