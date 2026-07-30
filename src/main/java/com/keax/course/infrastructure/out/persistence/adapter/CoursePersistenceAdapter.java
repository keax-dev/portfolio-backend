package com.keax.course.infrastructure.out.persistence.adapter;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.course.infrastructure.out.persistence.entity.CourseEntity;
import com.keax.course.infrastructure.out.persistence.mapper.CoursePersistenceMapper;
import com.keax.course.infrastructure.out.persistence.repository.JpaCourseRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoursePersistenceAdapter implements CourseRepositoryPort {

    private final JpaCourseRepository jpaCourseRepository;
    private final EntityManager entityManager;

    @Override
    public Course createCourse(Course course) {
        CourseEntity saved = jpaCourseRepository.saveAndFlush(
                CoursePersistenceMapper.toEntity(course)
        );
        entityManager.refresh(saved);
        return CoursePersistenceMapper.toDomain(saved);
    }

    @Override
    public Course updateCourse(Course course) {
        CourseEntity updated = jpaCourseRepository.saveAndFlush(
                CoursePersistenceMapper.toEntity(course)
        );
        entityManager.refresh(updated);
        return CoursePersistenceMapper.toDomain(updated);
    }

    @Override
    public Course deleteCourse(Course course) {
        jpaCourseRepository.deleteById(course.getCourseId());
        jpaCourseRepository.flush();
        course.setCourseDeleted(true);
        return course;
    }

    @Override
    public List<Course> findByCourseDeleted(Boolean deleted) {
        return jpaCourseRepository.findByCourseDeletedOrderByCourseNameAsc(deleted)
                .stream()
                .map(CoursePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Course> getListCourse() {
        return jpaCourseRepository.findAllByOrderByCourseNameAsc()
                .stream()
                .map(CoursePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Course> findByCourseIdAndCourseDeleted(Long courseId, Boolean deleted) {
        return jpaCourseRepository.findByCourseIdAndCourseDeleted(courseId, deleted)
                .map(CoursePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Course> findByCourseNameAndCourseDeletedAndInstitutionId(
            String courseName,
            Boolean deleted,
            Long institutionId
    ) {
        return jpaCourseRepository
                .findByCourseNameAndCourseDeletedAndInstitution_InstitutionId(
                        courseName,
                        deleted,
                        institutionId
                )
                .map(CoursePersistenceMapper::toDomain);
    }
}
