package com.keax.education.infrastructure.out.persistence.adapter;

import com.keax.education.infrastructure.out.persistence.mapper.EducationPersistenceMapper;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.education.domain.model.Education;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public class EducationPersistenceAdapter implements EducationRepositoryPort {

    @Autowired
    private JpaEducationRepository jpaEducationRepository;

    @Override
    public Education createEducation(Education education) {
        EducationEntity saved = jpaEducationRepository.save(
                EducationPersistenceMapper.toEntity(education)
        );
        return EducationPersistenceMapper.toDomain(saved);
    }

    @Override
    public Education updateEducation(Education education) {
        EducationEntity updated = jpaEducationRepository.save(
                EducationPersistenceMapper.toEntity(education)
        );
        return EducationPersistenceMapper.toDomain(updated);
    }

    @Override
    public Education deleteEducation(Education education) {
        EducationEntity deleted = jpaEducationRepository.save(
                EducationPersistenceMapper.toEntity(education)
        );
        return EducationPersistenceMapper.toDomain(deleted);
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {
        return jpaEducationRepository.findByEducationDeleted(deleted).stream()
                .map(EducationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Education> getListEducation() {
        return jpaEducationRepository.findAll().stream()
                .map(EducationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Education> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId) {
        return jpaEducationRepository.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(
                educationTitle,
                deleted,
                institutionId
        ).map(EducationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Education> findByEducationIdAndEducationDeleted(Long educationId, Boolean deleted) {
        return jpaEducationRepository.findByEducationIdAndEducationDeleted(
                educationId,
                deleted
        ).map(EducationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Education> findByEducationPositionAndEducationDeleted(int position, Boolean deleted) {
        return jpaEducationRepository.findByEducationPositionAndEducationDeleted(
                position,
                deleted
        ).map(EducationPersistenceMapper::toDomain);
    }

    @Override
    public Boolean existsByInstitution_InstitutionIdAndEducationDeleted(Long institutionId, Boolean deleted) {
        return jpaEducationRepository.existsByInstitution_InstitutionIdAndEducationDeleted(
                institutionId,
                deleted
        );
    }

}
