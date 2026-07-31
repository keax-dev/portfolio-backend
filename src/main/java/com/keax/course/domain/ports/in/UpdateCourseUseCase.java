package com.keax.course.domain.ports.in;

import com.keax.course.domain.model.Course;

public interface UpdateCourseUseCase {

    Course updateCourse(Long courseId, Course course);
}
