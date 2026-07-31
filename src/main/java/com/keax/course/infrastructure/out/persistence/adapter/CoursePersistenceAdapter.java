package com.keax.course.infrastructure.out.persistence.adapter;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.course.infrastructure.out.persistence.entity.CourseEntity;
import com.keax.course.infrastructure.out.persistence.mapper.CoursePersistenceMapper;
import com.keax.course.infrastructure.out.persistence.repository.JpaCourseRepository;
import com.keax.institution.infrastructure.out.persistence.entity.InstitutionEntity;
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
        CourseEntity saved = jpaCourseRepository.saveAndFlush(toEntity(course));
        entityManager.refresh(saved);
        return CoursePersistenceMapper.toDomain(saved);
    }

    @Override
    public Course updateCourse(Course course) {
        CourseEntity updated = jpaCourseRepository.saveAndFlush(toEntity(course));
        entityManager.refresh(updated);
        return CoursePersistenceMapper.toDomain(updated);
    }

    @Override
    public Course deleteCourse(Course course) {
        jpaCourseRepository.deleteById(course.getCourseId());
        jpaCourseRepository.flush();
        return course;
    }

    @Override
    public List<Course> findAll() {
        return jpaCourseRepository.findAllByOrderByCoursePositionAsc()
                .stream()
                .map(CoursePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Course> findById(Long courseId) {
        return jpaCourseRepository.findById(courseId)
                .map(CoursePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Course> findByNameAndInstitutionId(
            String courseName,
            Long institutionId
    ) {
        return jpaCourseRepository
                .findByCourseNameAndInstitution_InstitutionId(
                        courseName,
                        institutionId
                )
                .map(CoursePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Course> findByPosition(int position) {
        return jpaCourseRepository.findByCoursePosition(position)
                .map(CoursePersistenceMapper::toDomain);
    }

    private CourseEntity toEntity(Course course) {
        CourseEntity entity = CoursePersistenceMapper.toEntity(course);
        entity.setInstitution(entityManager.getReference(
                InstitutionEntity.class,
                course.getInstitutionId()
        ));
        return entity;
    }
}
