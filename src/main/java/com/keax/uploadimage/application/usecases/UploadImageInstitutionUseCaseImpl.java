package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageInstitutionUseCase;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
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

        ImageFileValidator.validate(img, "The img is required");

        Institution institution = institutionRepositoryPort.findByInstitutionIdAndInstitutionDeleted(
                institutionId,
                false
        ).orElseThrow(
                () -> new ResourceNotFoundException("The institution to be updated does not exist")
        );

        String oldImageUrl = institution.getInstitutionUrl();
        String newImageUrl = null;

        try {
            newImageUrl = imageStoragePort.upload(img, "Institutions");
            institution.setInstitutionUrl(newImageUrl);

            Institution updatedInstitution = institutionRepositoryPort.updateInstitution(institution);
            imageStoragePort.delete(oldImageUrl);
            return updatedInstitution;
        } catch (Exception e) {
            imageStoragePort.delete(newImageUrl);
            throw new ExternalServiceException("An error occurred while uploading the institution's image", e);
        }
    }

}
