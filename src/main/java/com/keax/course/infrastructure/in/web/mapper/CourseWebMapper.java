package com.keax.course.infrastructure.in.web.mapper;

import com.keax.course.domain.model.Course;
import com.keax.course.infrastructure.in.web.dto.CourseDTO;

public final class CourseWebMapper {

    private CourseWebMapper() {
    }

    public static Course toDomain(CourseDTO dto) {
        return new Course(
                dto.getCourseId(),
                dto.getCourseName(),
                dto.getCourseNameEn(),
                dto.getCourseCertificateImg(),
                dto.getCourseCertificateUrl(),
                dto.getCourseDeleted(),
                dto.getInstitutionId(),
                dto.getInstitutionName(),
                dto.getInstitutionNameEs()
        );
    }

    public static CourseDTO fromDomain(Course course) {
        return new CourseDTO(
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseNameEn(),
                course.getCourseCertificateImg(),
                course.getCourseCertificateUrl(),
                course.getCourseDeleted(),
                course.getInstitutionId(),
                course.getInstitutionName(),
                course.getInstitutionNameEs()
        );
    }
}
