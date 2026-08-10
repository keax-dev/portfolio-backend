package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.UpdateCourseUseCase;
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
public class UpdateCourseUseCaseImpl implements UpdateCourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;
    private final InstitutionReferencePort institutionReferencePort;

    @Override
    public Course updateCourse(Long courseId, Course course) {
        Course existingCourse = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The course to be updated was not found"
                ));

        if (!institutionReferencePort.existsActiveInstitution(course.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        String normalizedName = TextNormalizer.uppercase(course.getCourseName());
        String normalizedNameEn = TextNormalizer.uppercase(course.getCourseNameEn());
        courseRepositoryPort.findByNameAndInstitutionId(
                normalizedName,
                course.getInstitutionId()
        ).ifPresent(duplicate -> {
            if (!Objects.equals(duplicate.getCourseId(), existingCourse.getCourseId())) {
                throw new ResourceConflictException(
                        "There is already a course with this name and institution"
                );
            }
        });
        courseRepositoryPort.findByPosition(
                course.getCoursePosition()
        ).ifPresent(duplicate -> {
            if (!Objects.equals(duplicate.getCourseId(), existingCourse.getCourseId())) {
                throw new ResourceConflictException(
                        "There is already a course with this position"
                );
            }
        });

        existingCourse.setCourseName(normalizedName);
        existingCourse.setCourseNameEn(normalizedNameEn);
        existingCourse.setCourseCertificateUrl(TextNormalizer.trimToNull(
                course.getCourseCertificateUrl()
        ));
        existingCourse.setCoursePosition(course.getCoursePosition());
        existingCourse.setCourseVisible(Objects.requireNonNullElse(course.getCourseVisible(), true));
        existingCourse.setInstitutionId(course.getInstitutionId());
        return courseRepositoryPort.updateCourse(existingCourse);
    }
}
