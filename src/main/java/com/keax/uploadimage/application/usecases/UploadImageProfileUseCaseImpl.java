package com.keax.uploadimage.application.usecases;

import com.keax.uploadimage.domain.ports.in.UploadImageProfileUseCase;
import com.keax.profile.domain.ports.out.ProfileRepositoryPort;
import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.shared.domain.exceptions.ExceptionAlert;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.keax.profile.domain.model.Profile;
import java.util.List;

@Service
@Transactional
public class UploadImageProfileUseCaseImpl implements UploadImageProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Autowired
    private ImageStoragePort imageStoragePort;

    @Override
    public Profile uploadImageProfile(ImageFile img) {

        validateImage(img, "Profile picture is required");

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ExceptionMessage("Profile not created");
        }

        try {
            Profile profile = profileList.getFirst();
            profile.setProfilePicture(
                    imageStoragePort.upload(img, "Profile")
            );

            return profileRepositoryPort.saveProfile(profile);
        } catch (Exception e) {
            throw new ExceptionAlert("An error occurred while uploading the profile's image");
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
