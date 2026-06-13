package com.keax.uploadimage.application.validation;

import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.uploadimage.domain.model.ImageFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImageFileValidatorTest {

    @Test
    void acceptsJpegSignature() {
        ImageFile image = new ImageFile("photo.jpg", "image/jpeg", new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00
        });

        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void acceptsPngSignature() {
        ImageFile image = new ImageFile("icon.png", "image/png", new byte[] {
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        });

        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void acceptsWebpSignature() {
        ImageFile image = new ImageFile("banner.webp", "image/webp", new byte[] {
                0x52, 0x49, 0x46, 0x46,
                0x00, 0x00, 0x00, 0x00,
                0x57, 0x45, 0x42, 0x50
        });

        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void rejectsSpoofedImageContent() {
        ImageFile image = new ImageFile("fake.png", "image/png", "not an image".getBytes());

        assertThrows(ExceptionMessage.class, () -> ImageFileValidator.validate(image, "Image is required"));
    }

}
