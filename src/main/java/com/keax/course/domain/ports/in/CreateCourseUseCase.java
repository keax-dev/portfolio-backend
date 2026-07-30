package com.keax.course.domain.ports.in;

import com.keax.course.domain.model.Course;

public interface CreateCourseUseCase {

    Course createCourse(Course course);
}
