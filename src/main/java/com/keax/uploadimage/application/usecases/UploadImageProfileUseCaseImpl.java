package com.keax.uploadimage.application.usecases;

import lombok.RequiredArgsConstructor;

import com.keax.uploadimage.application.validation.ImageFileValidator;
import com.keax.uploadimage.domain.ports.in.UploadImageProfileUseCase;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import com.keax.shared.domain.exceptions.ExternalServiceException;
import com.keax.shared.domain.exceptions.ResourceNotFoundException;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadImageProfileUseCaseImpl implements UploadImageProfileUseCase {
    private final ProfileRepositoryPort profileRepositoryPort;
    private final ImageStoragePort imageStoragePort;

    @Override
    public Profile uploadImageProfile(ImageFile img) {

        ImageFileValidator.validate(img, "Profile picture is required");

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ResourceNotFoundException("Profile not created");
        }

        String newImageUrl = null;

        try {
            Profile profile = profileList.getFirst();
            String oldImageUrl = profile.getProfilePicture();
            newImageUrl = imageStoragePort.upload(img, "Profile");
            profile.setProfilePicture(newImageUrl);

            Profile updatedProfile = profileRepositoryPort.saveProfile(profile);
            imageStoragePort.delete(oldImageUrl);
            return updatedProfile;
        } catch (Exception e) {
            imageStoragePort.delete(newImageUrl);
            throw new ExternalServiceException("An error occurred while uploading the profile's image", e);
        }
    }

}
