package com.keax.course.infrastructure.out.persistence.mapper;

import com.keax.course.domain.model.Course;
import com.keax.course.infrastructure.out.persistence.entity.CourseEntity;
import com.keax.institution.infrastructure.out.persistence.mapper.InstitutionPersistenceMapper;

public final class CoursePersistenceMapper {

    private CoursePersistenceMapper() {
    }

    public static Course toDomain(CourseEntity entity) {
        return new Course(
                entity.getCourseId(),
                entity.getCourseName(),
                entity.getCourseNameEn(),
                entity.getCourseCertificateImg(),
                entity.getCourseCertificateUrl(),
                entity.getCoursePosition(),
                entity.getInstitution().getInstitutionId(),
                entity.getInstitution().getInstitutionName(),
                entity.getInstitution().getInstitutionNameEs(),
                entity.getVersion()
        );
    }

    public static CourseEntity toEntity(Course course) {
        return new CourseEntity(
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseNameEn(),
                course.getCourseCertificateImg(),
                course.getCourseCertificateUrl(),
                course.getCoursePosition(),
                false,
                InstitutionPersistenceMapper.toReference(course.getInstitutionId()),
                course.getVersion()
        );
    }
}
