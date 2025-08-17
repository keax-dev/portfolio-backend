package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageUseCase;
import com.keax.domain.ports.out.InstitutionRepositoryPort;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import com.keax.domain.models.Institution;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;
import java.util.Optional;

public class UploadImageUseCaseImpl implements UploadImageUseCase {

    private final InstitutionRepositoryPort institutionRepositoryPort;
    private final Cloudinary cloudinary;

    public UploadImageUseCaseImpl(InstitutionRepositoryPort institutionRepositoryPort, Cloudinary cloudinary) {
        this.institutionRepositoryPort = institutionRepositoryPort;
        this.cloudinary = cloudinary;
    }

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
