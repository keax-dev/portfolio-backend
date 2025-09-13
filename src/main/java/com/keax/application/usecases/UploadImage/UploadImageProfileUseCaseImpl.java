package com.keax.application.usecases.UploadImage;

import com.keax.domain.ports.in.UploadImage.UploadImageProfileUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.domain.ports.out.ProfileRepositoryPort;
import org.springframework.web.multipart.MultipartFile;
import com.keax.domain.exceptions.ExceptionMessage;
import com.keax.domain.exceptions.ExceptionAlert;
import org.springframework.stereotype.Component;
import com.cloudinary.utils.ObjectUtils;
import com.keax.domain.models.Profile;
import com.cloudinary.Cloudinary;
import java.util.List;

@Component
public class UploadImageProfileUseCaseImpl implements UploadImageProfileUseCase {

    @Autowired
    private ProfileRepositoryPort profileRepositoryPort;

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Profile uploadImageProfile(MultipartFile img) {

        if (img == null || img.isEmpty()){
            throw new ExceptionMessage("Profile picture is required");
        }

        String contentType = img.getContentType();

        if (contentType == null || !(contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp"))) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        List<Profile> profileList = profileRepositoryPort.getListProfile();

        if (profileList.isEmpty()){
            throw new ExceptionMessage("Profile not created");
        }

        try {
            var resultUpload = cloudinary.uploader().upload(img.getBytes(), ObjectUtils.asMap("folder", "Profile"));
            String imageUrl = resultUpload.get("secure_url").toString();

            Profile profile = profileList.getFirst();
            profile.setProfilePicture(imageUrl);

            return profileRepositoryPort.saveProfile(profile);
        } catch (Exception e) {
            throw  new ExceptionAlert("An error occurred while uploading the institution's image");
        }
    }

}
