package com.keax.uploadimage.infrastructure.out.storage;

import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.stereotype.Component;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;

@Component
public class CloudinaryImageStorageAdapter implements ImageStoragePort {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public String upload(ImageFile imageFile, String folder) {
        try {
            var resultUpload = cloudinary.uploader().upload(
                    imageFile.getBytes(),
                    ObjectUtils.asMap("folder", folder)
            );

            return resultUpload.get("secure_url").toString();
        } catch (Exception ex) {
            throw new IllegalStateException("Image upload failed", ex);
        }
    }

}
