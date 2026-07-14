package com.keax.technology.infrastructure.out.persistence.repository;

import com.keax.technology.infrastructure.out.persistence.entity.TechnologyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface JpaTechnologyRepository extends JpaRepository<TechnologyEntity, Long> {

    List<TechnologyEntity> findByTechnologyDeleted(Boolean deleted);

    @EntityGraph(attributePaths = "projectEntityList")
    @Query("""
            select distinct technology
            from TechnologyEntity technology
            where technology.technologyDeleted = :deleted
            order by technology.technologyPosition
            """)
    List<TechnologyEntity> findWithProjectsByTechnologyDeleted(@Param("deleted") Boolean deleted);

    Optional<TechnologyEntity> findByTechnologyNameAndTechnologyDeleted(String technologyName, Boolean deleted);
    Optional<TechnologyEntity> findByTechnologyIdAndTechnologyDeleted(Long technologyId, Boolean deleted);
    Optional<TechnologyEntity> findByTechnologyPositionAndTechnologyDeleted(int position, Boolean deleted);

}
