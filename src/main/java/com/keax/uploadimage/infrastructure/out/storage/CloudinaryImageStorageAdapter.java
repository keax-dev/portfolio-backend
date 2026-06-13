package com.keax.uploadimage.infrastructure.out.storage;

import com.keax.uploadimage.domain.ports.out.ImageStoragePort;
import org.springframework.beans.factory.annotation.Autowired;
import com.keax.uploadimage.domain.model.ImageFile;
import org.springframework.stereotype.Component;
import com.cloudinary.utils.ObjectUtils;
import com.cloudinary.Cloudinary;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
public class CloudinaryImageStorageAdapter implements ImageStoragePort {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryImageStorageAdapter.class);
    private static final String UPLOAD_PATH = "/upload/";

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

    @Override
    public void delete(String imageUrl) {
        String publicId = extractPublicId(imageUrl);

        if (publicId == null) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception ex) {
            log.warn("Image delete failed for {}", imageUrl, ex);
        }
    }

    static String extractPublicId(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        int uploadIndex = imageUrl.indexOf(UPLOAD_PATH);
        if (uploadIndex < 0) {
            return null;
        }

        String path = imageUrl.substring(uploadIndex + UPLOAD_PATH.length());
        int queryIndex = path.indexOf('?');
        if (queryIndex >= 0) {
            path = path.substring(0, queryIndex);
        }

        path = path.replaceFirst("^v\\d+/", "");
        int extensionIndex = path.lastIndexOf('.');
        if (extensionIndex > 0) {
            path = path.substring(0, extensionIndex);
        }

        return path.isBlank() ? null : path;
    }

}
