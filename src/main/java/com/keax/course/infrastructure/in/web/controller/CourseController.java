package com.keax.course.infrastructure.in.web.controller;

import com.keax.course.domain.ports.in.CreateCourseUseCase;
import com.keax.course.domain.ports.in.DeleteCourseUseCase;
import com.keax.course.domain.ports.in.RetrieveCourseUseCase;
import com.keax.course.domain.ports.in.UpdateCourseUseCase;
import com.keax.course.infrastructure.in.web.dto.CourseDTO;
import com.keax.course.infrastructure.in.web.mapper.CourseWebMapper;
import com.keax.shared.infrastructure.in.web.dto.ApiResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CreateCourseUseCase createCourseUseCase;
    private final UpdateCourseUseCase updateCourseUseCase;
    private final RetrieveCourseUseCase retrieveCourseUseCase;
    private final DeleteCourseUseCase deleteCourseUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CourseDTO>> create(
            @Valid @RequestBody CourseDTO course
    ) {
        ApiResponseDTO<CourseDTO> response = new ApiResponseDTO<>(
                true,
                "Course was created successfully",
                CourseWebMapper.fromDomain(
                        createCourseUseCase.createCourse(CourseWebMapper.toDomain(course))
                )
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<ApiResponseDTO<CourseDTO>> update(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseDTO course
    ) {
        ApiResponseDTO<CourseDTO> response = new ApiResponseDTO<>(
                true,
                "Course was updated successfully",
                CourseWebMapper.fromDomain(
                        updateCourseUseCase.updateCourse(courseId, CourseWebMapper.toDomain(course))
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<CourseDTO>>> list() {
        ApiResponseDTO<List<CourseDTO>> response = new ApiResponseDTO<>(
                true,
                "Courses were found successfully",
                retrieveCourseUseCase.getListCourse()
                        .stream()
                        .map(CourseWebMapper::fromDomain)
                        .toList()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<ApiResponseDTO<CourseDTO>> delete(@PathVariable Long courseId) {
        deleteCourseUseCase.deleteCourse(courseId);
        ApiResponseDTO<CourseDTO> response = new ApiResponseDTO<>(
                true,
                "Course was deleted successfully",
                null
        );
        return ResponseEntity.ok(response);
    }
}
