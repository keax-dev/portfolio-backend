package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageInstitutionUseCase;
import com.keax.institution.domain.ports.out.InstitutionRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.uploadimage.application.services.ImagePersistenceCoordinator;
import com.keax.uploadimage.application.services.ImageCleanupProcessor;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.institution.domain.model.Institution;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadImageInstitutionUseCaseImpl implements UploadImageInstitutionUseCase {
    private final InstitutionRepositoryPort institutionRepositoryPort;
    private final ImageStoragePort imageStoragePort;
    private final ImagePersistenceCoordinator imagePersistenceCoordinator;
    private final ImageCleanupProcessor imageCleanupProcessor;

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
        String newImageUrl = imageStoragePort.upload(img, "Institutions");
        institution.setInstitutionUrl(newImageUrl);
        java.util.List<String> obsoleteUrls = oldImageUrl == null || oldImageUrl.isBlank()
                ? java.util.List.of()
                : java.util.List.of(oldImageUrl);
        Institution updatedInstitution;
        try {
            updatedInstitution = imagePersistenceCoordinator.updateInstitution(
                    institution,
                    obsoleteUrls
            );
        } catch (RuntimeException ex) {
            imageCleanupProcessor.deleteOrEnqueue(java.util.List.of(newImageUrl));
            throw ex;
        }
        imageCleanupProcessor.processQueuedUrls(obsoleteUrls);
        return updatedInstitution;
    }

}
