package com.keax.course.infrastructure.out.persistence.repository;

import com.keax.course.infrastructure.out.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface JpaCourseRepository extends JpaRepository<CourseEntity, Long> {

    @EntityGraph(attributePaths = "institution")
    List<CourseEntity> findAllByOrderByCoursePositionAsc();

    @Override
    @EntityGraph(attributePaths = "institution")
    Optional<CourseEntity> findById(Long courseId);

    @EntityGraph(attributePaths = "institution")
    Optional<CourseEntity> findByCourseNameAndInstitution_InstitutionId(
            String courseName,
            Long institutionId
    );

    @EntityGraph(attributePaths = "institution")
    Optional<CourseEntity> findByCoursePosition(int position);
}
