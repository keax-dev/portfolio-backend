package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageProfileUseCase;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.uploadimage.application.services.ImagePersistenceCoordinator;
import com.keax.uploadimage.application.services.ImageCleanupProcessor;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UploadImageProfileUseCaseImpl implements UploadImageProfileUseCase {
    private final ProfileRepositoryPort profileRepositoryPort;
    private final ImageStoragePort imageStoragePort;
    private final ImagePersistenceCoordinator imagePersistenceCoordinator;
    private final ImageCleanupProcessor imageCleanupProcessor;

    @Override
    public Profile uploadImageProfile(ImageFile img) {

        ImageFileValidator.validate(img, "Profile picture is required");

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ResourceNotFoundException("Profile not created");
        }

        Profile profile = profileList.getFirst();
        String oldImageUrl = profile.getProfilePicture();
        String newImageUrl = imageStoragePort.upload(img, "Profile");
        profile.setProfilePicture(newImageUrl);
        java.util.List<String> obsoleteUrls = oldImageUrl == null || oldImageUrl.isBlank()
                ? java.util.List.of()
                : java.util.List.of(oldImageUrl);
        Profile updatedProfile;
        try {
            updatedProfile = imagePersistenceCoordinator.updateProfile(
                    profile,
                    obsoleteUrls
            );
        } catch (RuntimeException ex) {
            imageCleanupProcessor.deleteOrEnqueue(java.util.List.of(newImageUrl));
            throw ex;
        }
        imageCleanupProcessor.processQueuedUrls(obsoleteUrls);
        return updatedProfile;
    }

}
