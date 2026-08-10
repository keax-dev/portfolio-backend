package com.keax.education.infrastructure.out.persistence.adapter;

import lombok.RequiredArgsConstructor;

import com.keax.education.infrastructure.out.persistence.mapper.EducationPersistenceMapper;
import com.keax.education.infrastructure.out.persistence.repository.JpaEducationRepository;
import com.keax.education.infrastructure.out.persistence.entity.EducationEntity;
import com.keax.education.domain.ports.out.EducationRepositoryPort;
import com.keax.education.domain.model.Education;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EducationPersistenceAdapter implements EducationRepositoryPort {
    private final JpaEducationRepository jpaEducationRepository;
    private final EntityManager entityManager;

    @Override
    public Education createEducation(Education education) {
        EducationEntity saved = jpaEducationRepository.save(toEntity(education));
        return EducationPersistenceMapper.toDomain(saved);
    }

    @Override
    public Education updateEducation(Education education) {
        EducationEntity updated = jpaEducationRepository.save(toEntity(education));
        return EducationPersistenceMapper.toDomain(updated);
    }

    @Override
    public Education deleteEducation(Education education) {
        jpaEducationRepository.deleteById(education.getEducationId());
        jpaEducationRepository.flush();
        return education;
    }

    @Override
    public List<Education> findAll() {
        return jpaEducationRepository.findAllByOrderByEducationPositionAsc().stream()
                .map(EducationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Education> findVisible() {
        return jpaEducationRepository.findAllByEducationVisibleTrueOrderByEducationPositionAsc()
                .stream()
                .map(EducationPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Education> findByTitleAndInstitutionId(String educationTitle, Long institutionId) {
        return jpaEducationRepository.findByEducationTitleAndInstitution_InstitutionId(
                educationTitle,
                institutionId
        ).map(EducationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Education> findById(Long educationId) {
        return jpaEducationRepository.findById(educationId)
                .map(EducationPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Education> findByPosition(int position) {
        return jpaEducationRepository.findByEducationPosition(position)
                .map(EducationPersistenceMapper::toDomain);
    }

    private EducationEntity toEntity(Education education) {
        EducationEntity entity = EducationPersistenceMapper.toEntity(education);
        entity.setInstitution(entityManager.getReference(
                InstitutionEntity.class,
                education.getInstitutionId()
        ));
        return entity;
    }

}
