package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.shared.domain.exceptions.ResourceConflictException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.domain.model.ImageFile;
import com.keax.project.domain.model.ProjectImage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadImageProjectUseCaseImpl implements UploadImageProjectUseCase {
    private final ProjectRepositoryPort projectRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    @Override
    public Project uploadProjectImages(Long projectId, List<ImageFile> images) {
        if (images == null || images.isEmpty()) {
            throw new ExceptionAlert("At least one project image is required");
        }
        images.forEach(image -> ImageFileValidator.validate(image, "The project image is required"));

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The project entered was not found")
        );

        List<ProjectImage> currentImages = project.getProjectImages();
        if (currentImages == null) {
            currentImages = new ArrayList<>();
            project.setProjectImages(currentImages);
        }
        if (currentImages.size() + images.size() > 3) {
            throw new ResourceConflictException("A project can have at most 3 images");
        }

        List<String> uploadedUrls = new ArrayList<>();

        try {
            int nextPosition = currentImages.stream()
                    .map(ProjectImage::getPosition)
                    .max(Comparator.naturalOrder())
                    .orElse(0) + 1;

            for (ImageFile image : images) {
                String url = imageStoragePort.upload(image, "Projects");
                uploadedUrls.add(url);
                currentImages.add(new ProjectImage(null, url, nextPosition++));
            }

            return projectRepositoryPort.updateProject(project);
        } catch (Exception e) {
            uploadedUrls.forEach(imageStoragePort::delete);
            throw new ExternalServiceException("An error occurred while uploading the project's images", e);
        }
    }

    @Override
    public Project deleteProjectImage(Long projectId, Long projectImageId) {
        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(projectId, false)
                .orElseThrow(() -> new ResourceNotFoundException("The project entered was not found"));

        if (project.getProjectImages() == null || project.getProjectImages().size() <= 1) {
            throw new ResourceConflictException("A project must have at least 1 image");
        }

        ProjectImage image = project.getProjectImages().stream()
                .filter(current -> Objects.equals(current.getProjectImageId(), projectImageId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("The project image was not found"));

        project.getProjectImages().remove(image);

        try {
            Project updatedProject = projectRepositoryPort.updateProject(project);
            imageStoragePort.delete(image.getUrl());
            return updatedProject;
        } catch (Exception e) {
            throw new ExternalServiceException("An error occurred while deleting the project's image", e);
        }
    }

}
