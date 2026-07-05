package com.keax.uploadimage.application.validation;

import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.uploadimage.domain.model.ImageFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Verifica que la validacion de imagenes inspeccione tanto el MIME declarado
 * como la firma binaria de JPEG, PNG y WEBP.
 */
class ImageFileValidatorTest {

    @Test
    void acceptsJpegSignature() {
        // Arrange: se construye el encabezado magico minimo de un JPEG.
        ImageFile image = new ImageFile("photo.jpg", "image/jpeg", new byte[] {
                (byte) 0xFF, (byte) 0xD8, (byte) 0xFF, 0x00
        });

        // Act y Assert: MIME y firma coherentes son aceptados.
        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void acceptsPngSignature() {
        // Arrange: se construye la firma estandar de ocho bytes de PNG.
        ImageFile image = new ImageFile("icon.png", "image/png", new byte[] {
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        });

        // Act y Assert: MIME y firma coherentes son aceptados.
        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void acceptsWebpSignature() {
        // Arrange: se incluyen los marcadores RIFF y WEBP en sus posiciones.
        ImageFile image = new ImageFile("banner.webp", "image/webp", new byte[] {
                0x52, 0x49, 0x46, 0x46,
                0x00, 0x00, 0x00, 0x00,
                0x57, 0x45, 0x42, 0x50
        });

        // Act y Assert: MIME y firma coherentes son aceptados.
        assertDoesNotThrow(() -> ImageFileValidator.validate(image, "Image is required"));
    }

    @Test
    void rejectsSpoofedImageContent() {
        // Arrange: el MIME afirma PNG, pero el contenido es texto.
        ImageFile image = new ImageFile("fake.png", "image/png", "not an image".getBytes());

        // Act y Assert: la firma falsa impide enviar el archivo a Cloudinary.
        assertThrows(ExceptionMessage.class, () -> ImageFileValidator.validate(image, "Image is required"));
    }

}
