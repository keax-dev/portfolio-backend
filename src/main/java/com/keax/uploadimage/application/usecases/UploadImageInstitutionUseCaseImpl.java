package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.domain.ports.in.UploadImageInstitutionUseCase;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.institution.domain.model.Institution;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UploadImageInstitutionUseCaseImpl implements UploadImageInstitutionUseCase {

    @Autowired
    private InstitutionRepositoryPort institutionRepositoryPort;

    @Autowired
    private ImageStoragePort imageStoragePort;

    @Override
    public Institution uploadImageInstitution(Long institutionId, ImageFile img) {

        validateImage(img, "The img is required");

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                false
        ).orElseThrow(
                () -> new ExceptionAlert("The institution to be updated does not exist")
        );

        try {
            institution.setInstitutionUrl(
                    imageStoragePort.upload(img, "Institutions")
            );

            return institutionRepositoryPort.updateInstitution(institution);
        } catch (Exception e) {
            throw new ExceptionAlert("An error occurred while uploading the institution's image");
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
