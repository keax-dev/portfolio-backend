package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.keax.domain.models.Institution;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;
import java.util.Optional;

@Component
public class UploadImageUseCaseImpl implements UploadImageUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Institution uploadImageInstitution(Long institution_id, MultipartFile img) {

        if (img == null || img.isEmpty()){
            throw new ExceptionMessage("The img is required");
        }

        String contentType = img.getContentType();
        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        Optional<Institution> optional = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(institution_id, false);
        if (optional.isEmpty()){
            throw new ExceptionAlert("The institution to be updated does not exist");
        }

        try {

            var resultUpload = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.asMap("folder", "Institutions"));
            String imageUrl = resultUpload.get("secure_url").toString();

            Institution institution = optional.get();
            institution.setInstitutionUrl(imageUrl);

            return institutionRepositoryPort.updateInstitution(institution_id, institution);

        } catch (Exception e) {
            throw  new ExceptionAlert("An error occurred while uploading the institution's image");
        }
    }

}
