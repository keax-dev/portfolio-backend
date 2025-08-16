package com.keax.infrastructure.adapters;

import com.keax.domain.models.Education;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.infrastructure.entities.EducationEntity;
import com.keax.infrastructure.entities.InstitutionEntity;
import com.keax.infrastructure.repositories.JpaEducationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaEducationRepositoryAdapter implements EducationRepositoryPort {

    private final JpaEducationRepository jpaEducationRepository;

    public JpaEducationRepositoryAdapter(JpaEducationRepository jpaEducationRepository) {
        this.jpaEducationRepository = jpaEducationRepository;
    }

    @Override
    public Education createEducation(Education education) {
        EducationEntity saved = jpaEducationRepository.save(fromDomainModel(education));
        return toDomainModel(saved);
    }

    @Override
    public Education updateEducation(Education education) {
        EducationEntity update = jpaEducationRepository.save(fromDomainModel(education));
        return toDomainModel(update);
    }

    @Override
    public List<Education> getListEducation() {
        return jpaEducationRepository.findAll().stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Education deleteEducation(Education education) {
        education.setEducationDeleted(true);
        EducationEntity deleted = jpaEducationRepository.save(fromDomainModel(education));
        return toDomainModel(deleted);
    }

    @Override
    public Optional<Education> findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(String educationTitle, Boolean deleted, Long institutionId) {
        return jpaEducationRepository.findByEducationTitleAndEducationDeletedAndInstitution_InstitutionId(educationTitle, deleted, institutionId).map(this::toDomainModel);
    }

    @Override
    public List<Education> findByEducationDeleted(Boolean deleted) {
        return jpaEducationRepository.findByEducationDeleted(deleted).stream().map(this::toDomainModel).collect(Collectors.toList());
    }

    @Override
    public Boolean existsByEducationIdAndEducationDeleted(Long education_id, Boolean deleted) {
        return jpaEducationRepository.existsByEducationIdAndEducationDeleted(education_id, deleted);
    }

    @Override
    public Optional<Education> findByEducationIdAndEducationDeleted(Long education_id, Boolean deleted) {
        return jpaEducationRepository.findByEducationIdAndEducationDeleted(education_id, deleted).map(this::toDomainModel);
    }

    private Education toDomainModel(EducationEntity educationEntity){
        return new Education(
                educationEntity.getEducationId(),
                educationEntity.getEducationTitle(),
                educationEntity.getEducationPlace(),
                educationEntity.getEducationStart(),
                educationEntity.getEducationEnd(),
                educationEntity.getEducationDeleted(),
                educationEntity.getInstitution().getInstitutionId(),
                educationEntity.getInstitution().getInstitutionName()
        );
    }

    private EducationEntity fromDomainModel(Education education){

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setInstitutionId(education.getInstitutionId());

        return  new EducationEntity(
                education.getEducationId(),
                education.getEducationTitle(),
                education.getEducationPlace(),
                education.getEducationStart(),
                education.getEducationEnd(),
                education.getEducationDeleted(),
                institutionEntity
        );
    }

}
