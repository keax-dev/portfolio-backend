package com.keax.infrastructure.adapters;

import com.keax.infrastructure.repositories.JpaEducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.infrastructure.entities.InstitutionEntity;
import com.keax.domain.ports.out.EducationRepositoryPort;
import com.keax.infrastructure.entities.EducationEntity;
import org.springframework.stereotype.Repository;
import com.keax.domain.models.Education;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;

@Repository
public class JpaEducationRepositoryAdapter implements EducationRepositoryPort {

    @Autowired
    private JpaEducationRepository jpaEducationRepository;

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
    public Optional<Education> findByEducationIdAndEducationDeleted(Long educationId, Boolean deleted) {
        return jpaEducationRepository.findByEducationIdAndEducationDeleted(educationId, deleted).map(this::toDomainModel);
    }

    @Override
    public Optional<Education> findByEducationPositionAndEducationDeleted(int position, Boolean deleted) {
        return jpaEducationRepository.findByEducationPositionAndEducationDeleted(position, deleted).map(this::toDomainModel);
    }

    @Override
    public Boolean existsByInstitution_InstitutionIdAndEducationDeleted(Long institutionId, Boolean deleted) {
        return jpaEducationRepository.existsByInstitution_InstitutionIdAndEducationDeleted(institutionId, deleted);
    }

    private Education toDomainModel(EducationEntity educationEntity){
        return new Education(
                educationEntity.getEducationId(),
                educationEntity.getEducationTitle(),
                educationEntity.getEducationPlace(),
                educationEntity.getEducationStart(),
                educationEntity.getEducationEnd(),
                educationEntity.getEducationPosition(),
                educationEntity.getEducationDeleted(),
                educationEntity.getInstitution().getInstitutionId(),
                educationEntity.getInstitution().getInstitutionName(),
                educationEntity.getInstitution().getInstitutionUrl()
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
                education.getEducationPosition(),
                education.getEducationDeleted(),
                institutionEntity
        );
    }

}
