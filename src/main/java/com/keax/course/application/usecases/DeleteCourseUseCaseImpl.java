package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.DeleteCourseUseCase;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DeleteCourseUseCaseImpl implements DeleteCourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;

    @Override
    public Course deleteCourse(Long courseId) {
        Course course = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The course to be deleted was not found"
                ));

        return courseRepositoryPort.deleteCourse(course);
    }
}
