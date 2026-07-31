package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.RetrieveCourseUseCase;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RetrieveCourseUseCaseImpl implements RetrieveCourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;

    @Override
    public List<Course> getListCourse() {
        return courseRepositoryPort.findAll();
    }
}
