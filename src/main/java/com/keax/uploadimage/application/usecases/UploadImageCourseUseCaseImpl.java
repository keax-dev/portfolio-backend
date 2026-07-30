package com.keax.uploadimage.application.usecases;

import com.keax.course.domain.model.Course;
import com.keax.course.domain.ports.out.CourseRepositoryPort;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.application.services.ImageCleanupProcessor;
import com.keax.uploadimage.application.services.ImagePersistenceCoordinator;
import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.uploadimage.domain.ports.in.UploadImageCourseUseCase;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadImageCourseUseCaseImpl implements UploadImageCourseUseCase {

    private final CourseRepositoryPort courseRepositoryPort;
    private final ImageStoragePort imageStoragePort;
    private final ImagePersistenceCoordinator imagePersistenceCoordinator;
    private final ImageCleanupProcessor imageCleanupProcessor;

    @Override
    public Course uploadImageCourse(Long courseId, ImageFile image) {
        ImageFileValidator.validate(image, "The certificate image is required");

        Course course = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "The course to be updated does not exist"
                ));

        String oldImageUrl = course.getCourseCertificateImg();
        String newImageUrl = imageStoragePort.upload(image, "Certificates");
        course.setCourseCertificateImg(newImageUrl);
        List<String> obsoleteUrls = oldImageUrl == null || oldImageUrl.isBlank()
                ? List.of()
                : List.of(oldImageUrl);

        Course updatedCourse;
        try {
            updatedCourse = imagePersistenceCoordinator.updateCourse(course, obsoleteUrls);
        } catch (RuntimeException exception) {
            imageCleanupProcessor.deleteOrEnqueue(List.of(newImageUrl));
            throw exception;
        }

        imageCleanupProcessor.processQueuedUrls(obsoleteUrls);
        return updatedCourse;
    }
}
