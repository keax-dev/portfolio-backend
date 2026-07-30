package com.keax.course.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.shared.domain.ports.out.InstitutionReferencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CourseUseCasesTest {

    private CourseRepositoryPort repository;
    private InstitutionReferencePort institutionReferencePort;

    @BeforeEach
    void setUp() {
        repository = mock(CourseRepositoryPort.class);
        institutionReferencePort = mock(InstitutionReferencePort.class);
    }

    @Test
    void createsCourseForAnActiveInstitution() {
        Course input = course(
                null,
                "Spring Boot",
                "Spring Boot Course",
                "client-image",
                " https://udemy.test/certificate/123 ",
                null,
                3L
        );
        when(institutionReferencePort.existsActiveInstitution(3L)).thenReturn(true);
        when(repository.findByCourseNameAndCourseDeletedAndInstitutionId(
                "SPRING BOOT",
                false,
                3L
        )).thenReturn(Optional.empty());
        when(repository.createCourse(any())).thenAnswer(invocation -> {
            Course saved = invocation.getArgument(0);
            saved.setCourseId(8L);
            return saved;
        });

        Course result = new CreateCourseUseCaseImpl(repository, institutionReferencePort)
                .createCourse(input);

        assertEquals(8L, result.getCourseId());
        assertEquals("SPRING BOOT", result.getCourseName());
        assertEquals("SPRING BOOT COURSE", result.getCourseNameEn());
        assertFalse(result.getCourseDeleted());
        assertEquals(null, result.getCourseCertificateImg());
        assertEquals("https://udemy.test/certificate/123", result.getCourseCertificateUrl());
    }

    @Test
    void rejectsDuplicateCourseAtTheSameInstitution() {
        Course input = course(null, "Angular", "Angular", null, null, null, 2L);
        when(institutionReferencePort.existsActiveInstitution(2L)).thenReturn(true);
        when(repository.findByCourseNameAndCourseDeletedAndInstitutionId(
                "ANGULAR",
                false,
                2L
        )).thenReturn(Optional.of(course(
                1L,
                "ANGULAR",
                "ANGULAR",
                "certificate",
                null,
                false,
                2L
        )));

        assertThrows(
                ResourceConflictException.class,
                () -> new CreateCourseUseCaseImpl(repository, institutionReferencePort)
                        .createCourse(input)
        );
        verify(repository, never()).createCourse(any());
    }

    @Test
    void rejectsCourseWhenInstitutionDoesNotExist() {
        Course input = course(null, "Docker", "Docker", null, null, null, 99L);
        when(institutionReferencePort.existsActiveInstitution(99L)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> new CreateCourseUseCaseImpl(repository, institutionReferencePort)
                        .createCourse(input)
        );
        verify(repository, never()).createCourse(any());
    }

    @Test
    void updatesMetadataWithoutLosingTheCertificate() {
        Course stored = course(
                5L,
                "JAVA",
                "JAVA",
                "https://cdn.test/java.png",
                "https://certificate.test/java",
                false,
                1L
        );
        Course changes = course(
                null,
                "Java moderno",
                "Modern Java",
                null,
                " https://certificate.test/modern-java ",
                null,
                2L
        );
        when(repository.findByCourseIdAndCourseDeleted(5L, false))
                .thenReturn(Optional.of(stored));
        when(institutionReferencePort.existsActiveInstitution(2L)).thenReturn(true);
        when(repository.findByCourseNameAndCourseDeletedAndInstitutionId(
                "JAVA MODERNO",
                false,
                2L
        )).thenReturn(Optional.empty());
        when(repository.updateCourse(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Course result = new UpdateCourseUseCaseImpl(repository, institutionReferencePort)
                .updateCourse(5L, changes);

        assertEquals("JAVA MODERNO", result.getCourseName());
        assertEquals("MODERN JAVA", result.getCourseNameEn());
        assertEquals(2L, result.getInstitutionId());
        assertEquals("https://cdn.test/java.png", result.getCourseCertificateImg());
        assertEquals("https://certificate.test/modern-java", result.getCourseCertificateUrl());
    }

    @Test
    void deletesAndRetrievesCoursesThroughTheirPorts() {
        Course stored = course(7L, "CLOUD", "CLOUD", "certificate", null, false, 1L);
        when(repository.findByCourseIdAndCourseDeleted(7L, false))
                .thenReturn(Optional.of(stored));
        when(repository.deleteCourse(stored)).thenAnswer(invocation -> {
            Course deleted = invocation.getArgument(0);
            deleted.setCourseDeleted(true);
            return deleted;
        });
        when(repository.getListCourse()).thenReturn(List.of(stored));

        Course deleted = new DeleteCourseUseCaseImpl(repository).deleteCourse(7L);
        List<Course> courses = new RetrieveCourseUseCaseImpl(repository).getListCourse();

        assertTrue(deleted.getCourseDeleted());
        assertEquals(List.of(stored), courses);
    }

    private Course course(
            Long id,
            String name,
            String nameEn,
            String certificateImg,
            String certificateUrl,
            Boolean deleted,
            Long institutionId
    ) {
        return new Course(
                id,
                name,
                nameEn,
                certificateImg,
                certificateUrl,
                deleted,
                institutionId,
                "UDEMY",
                "UDEMY"
        );
    }
}
