package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.domain.ports.in.UploadImageProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.project.domain.model.Project;

@Service
@Transactional
public class UploadImageProjectUseCaseImpl implements UploadImageProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private ImageStoragePort imageStoragePort;

    @Override
    public Project uploadImageProject(Long projectId, ImageFile img) {

        validateImage(img, "The img is required");

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        try {
            project.setProjectPicture(
                    imageStoragePort.upload(img, "Projects")
            );

            return projectRepositoryPort.updateProject(project);
        } catch (Exception e) {
            throw new ExceptionAlert("An error occurred while uploading the project's image");
        }
    }

    private void validateImage(ImageFile img, String requiredMessage) {
        if (img == null || img.isEmpty()) {
            throw new ExceptionMessage(requiredMessage);
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }
    }

}
