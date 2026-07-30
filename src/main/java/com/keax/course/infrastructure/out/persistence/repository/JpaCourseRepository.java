package com.keax.course.infrastructure.out.persistence.repository;

import com.keax.course.infrastructure.out.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaCourseRepository extends JpaRepository<CourseEntity, Long> {

    List<CourseEntity> findByCourseDeletedOrderByCourseNameAsc(Boolean deleted);

    List<CourseEntity> findAllByOrderByCourseNameAsc();

    Optional<CourseEntity> findByCourseIdAndCourseDeleted(Long courseId, Boolean deleted);

    Optional<CourseEntity> findByCourseNameAndCourseDeletedAndInstitution_InstitutionId(
            String courseName,
            Boolean deleted,
            Long institutionId
    );
}
