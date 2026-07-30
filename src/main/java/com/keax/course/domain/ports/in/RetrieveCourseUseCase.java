package com.keax.course.domain.ports.in;

import com.keax.course.domain.model.Course;

import java.util.List;

public interface RetrieveCourseUseCase {

    List<Course> getListCourse();
}
