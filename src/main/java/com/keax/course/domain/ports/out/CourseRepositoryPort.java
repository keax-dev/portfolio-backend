package com.keax.course.domain.ports.out;

import com.keax.course.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepositoryPort {

    Course createCourse(Course course);

    Course updateCourse(Course course);

    Course deleteCourse(Course course);

    List<Course> findByCourseDeleted(Boolean deleted);

    List<Course> getListCourse();

    Optional<Course> findByCourseIdAndCourseDeleted(Long courseId, Boolean deleted);

    Optional<Course> findByCourseNameAndCourseDeletedAndInstitutionId(
            String courseName,
            Boolean deleted,
            Long institutionId
    );
}
