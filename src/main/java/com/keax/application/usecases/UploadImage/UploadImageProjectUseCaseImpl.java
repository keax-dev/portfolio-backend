package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageProjectUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.ports.out.ProjectRepositoryPort;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import com.cloudinary.utils.ObjectUtils;
import com.keax.domain.models.Project;
import com.cloudinary.Cloudinary;

@Component
public class UploadImageProjectUseCaseImpl implements UploadImageProjectUseCase {

    @Autowired
    private ProjectRepositoryPort projectRepositoryPort;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Project uploadImageProject(Long projectId, MultipartFile img) {

        if (img == null || img.isEmpty()){
            throw new ExceptionMessage("The img is required");
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        Project project = projectRepositoryPort.findByProjectIdAndProjectDeleted(projectId,false).orElseThrow(
                () -> new ExceptionAlert("The project entered was not found")
        );

        try {

            var resultUpload = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.asMap("folder", "Projects"));
            String imageUrl = resultUpload.get("secure_url").toString();

            project.setProjectPicture(imageUrl);

            return projectRepositoryPort.updateProject(project);
        } catch (Exception e) {
            throw  new ExceptionAlert("An error occurred while uploading the project's image");
        }
    }

}
