package com.keax.uploadimage.infrastructure.in.web.mapper;

import com.keax.shared.domain.exceptions.ExceptionMessage;
import org.springframework.web.multipart.MultipartFile;
import com.keax.uploadimage.domain.model.ImageFile;
import java.io.IOException;

public final class ImageFileWebMapper {

    public static ImageFile toDomain(MultipartFile file) {
        if (file == null) {
            return null;
        }

        try {
            return new ImageFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        } catch (IOException ex) {
            throw new ExceptionMessage("The image could not be read");
        }
    }

}
