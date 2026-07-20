package com.keax.technology.infrastructure.out.persistence.repository;

import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface JpaTechnologyRepository extends JpaRepository<TechnologyEntity, Long> {

    List<TechnologyEntity> findByTechnologyDeletedOrderByTechnologyNameAsc(Boolean deleted);

    Optional<TechnologyEntity> findByTechnologyNameAndTechnologyDeleted(String technologyName, Boolean deleted);
    Optional<TechnologyEntity> findByTechnologyIdAndTechnologyDeleted(Long technologyId, Boolean deleted);

}
