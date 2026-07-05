package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageProjectUseCase;
import com.keax.project.domain.ports.out.ProjectRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
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

        ImageFileValidator.validate(img, "The img is required");

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(
                projectId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The project entered was not found")
        );

        String oldImageUrl = project.getProjectPicture();
        String newImageUrl = null;

        try {
            newImageUrl = imageStoragePort.upload(img, "Projects");
            project.setProjectPicture(newImageUrl);

            Project updatedProject = projectRepositoryPort.updateProject(project);
            imageStoragePort.delete(oldImageUrl);
            return updatedProject;
        } catch (Exception e) {
            imageStoragePort.delete(newImageUrl);
            throw new ExternalServiceException("An error occurred while uploading the project's image", e);
        }
    }

}
