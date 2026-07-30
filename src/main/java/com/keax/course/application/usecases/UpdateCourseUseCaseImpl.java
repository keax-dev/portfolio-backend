package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.in.UpdateCourseUseCase;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.ports.out.InstitutionReferencePort;
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
        Course existingCourse = courseRepositoryPort.findByCourseIdAndCourseDeleted(courseId, false)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The course to be updated was not found"
                ));

        if (!institutionReferencePort.existsActiveInstitution(course.getInstitutionId())) {
            throw new ResourceNotFoundException("The institution entered was not found");
        }

        String normalizedName = course.getCourseName().trim().toUpperCase();
        String normalizedNameEn = course.getCourseNameEn().trim().toUpperCase();
        courseRepositoryPort.findByCourseNameAndCourseDeletedAndInstitutionId(
                normalizedName,
                false,
                course.getInstitutionId()
        ).ifPresent(duplicate -> {
            if (!Objects.equals(duplicate.getCourseId(), existingCourse.getCourseId())) {
                throw new ResourceConflictException(
                        "There is already a course with this name and institution"
                );
            }
        });

        existingCourse.setCourseName(normalizedName);
        existingCourse.setCourseNameEn(normalizedNameEn);
        existingCourse.setCourseCertificateUrl(normalizeOptionalUrl(
                course.getCourseCertificateUrl()
        ));
        existingCourse.setInstitutionId(course.getInstitutionId());
        existingCourse.setCourseDeleted(false);

        return courseRepositoryPort.updateCourse(existingCourse);
    }

    private String normalizeOptionalUrl(String url) {
        return url == null || url.isBlank() ? null : url.trim();
    }
}
