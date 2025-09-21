package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageInstitutionUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;

@Component
public class UploadImageInstitutionUseCaseImpl implements UploadImageInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Institution uploadImageInstitution(Long institutionId, MultipartFile img) {

        if (img == null || img.isEmpty()){
            throw new ExceptionMessage("The img is required");
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institutionId, false).orElseThrow(
                () -> new ExceptionAlert("The institution to be updated does not exist")
        );

        try {

            var resultUpload = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.asMap("folder", "Institutions"));
            String imageUrl = resultUpload.get("secure_url").toString();

            institution.setInstitutionUrl(imageUrl);

            return institutionRepositoryPort.updateInstitution(institution);
        } catch (Exception e) {
            throw  new ExceptionAlert("An error occurred while uploading the institution's image");
        }
    }

}
