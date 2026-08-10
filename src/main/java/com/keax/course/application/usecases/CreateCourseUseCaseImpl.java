package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.CreateCourseUseCase;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.ports.out.InstitutionReferencePort;
import com.keax.shared.domain.text.TextNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateCourseUseCaseImpl implements CreateCourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;
    private final InstitutionReferencePort institutionReferencePort;

    @Override
    public Course createCourse(Course course) {
        if (!institutionReferencePort.existsActiveInstitution(course.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        String normalizedName = TextNormalizer.uppercase(course.getCourseName());
        String normalizedNameEn = TextNormalizer.uppercase(course.getCourseNameEn());
        courseRepositoryPort.findByNameAndInstitutionId(
                normalizedName,
                course.getInstitutionId()
        ).ifPresent(existing -> {
            throw new ResourceConflictException(
                    "There is already a course with this name and institution"
            );
        });
        courseRepositoryPort.findByPosition(
                course.getCoursePosition()
        ).ifPresent(existing -> {
            throw new ResourceConflictException(
                    "There is already a course with this position"
            );
        });

        course.setCourseId(null);
        course.setCourseName(normalizedName);
        course.setCourseNameEn(normalizedNameEn);
        course.setCourseCertificateImg(null);
        course.setCourseCertificateUrl(TextNormalizer.trimToNull(course.getCourseCertificateUrl()));
        course.setCourseVisible(Objects.requireNonNullElse(course.getCourseVisible(), true));

        return courseRepositoryPort.createCourse(course);
    }
}
