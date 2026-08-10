package com.keax.course.domain.ports.out;

import com.keax.course.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryPort {

    Course createCourse(Course course);

    Course updateCourse(Course course);

    Course deleteCourse(Course course);

    List<Course> findAll();

    List<Course> findVisible();

    Optional<Course> findById(Long courseId);

    Optional<Course> findByNameAndInstitutionId(
            String courseName,
            Long institutionId
    );

    Optional<Course> findByPosition(int position);
}
