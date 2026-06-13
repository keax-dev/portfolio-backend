package com.keax.uploadimage.application.validation;

import com.keax.shared.domain.exceptions.ExceptionMessage;
import com.keax.uploadimage.domain.model.ImageFile;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public final class ImageFileValidator {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private ImageFileValidator() {
    }

    public static void validate(ImageFile img, String requiredMessage) {
        if (img == null || img.isEmpty()) {
            throw new ExceptionMessage(requiredMessage);
        }

        if (!ALLOWED_CONTENT_TYPES.contains(img.getContentType())) {
            throw new ExceptionMessage("Image format not allowed (JPG, PNG, WEBP only)");
        }

        if (!hasValidSignature(img)) {
            throw new ExceptionMessage("The image content does not match an allowed image format");
        }
    }

    private static boolean hasValidSignature(ImageFile img) {
        byte[] bytes = img.getBytes();

        return switch (img.getContentType()) {
            case "image/jpeg" -> isJpeg(bytes);
            case "image/png" -> isPng(bytes);
            case "image/webp" -> isWebp(bytes);
            default -> false;
        };
    }

    private static boolean isJpeg(byte[] bytes) {
        return bytes.length >= 3
                && unsigned(bytes[0]) == 0xFF
                && unsigned(bytes[1]) == 0xD8
                && unsigned(bytes[2]) == 0xFF;
    }

    private static boolean isPng(byte[] bytes) {
        byte[] signature = new byte[] {
                (byte) 0x89, 0x50, 0x4E, 0x47,
                0x0D, 0x0A, 0x1A, 0x0A
        };

        return startsWith(bytes, signature);
    }

    private static boolean isWebp(byte[] bytes) {
        return bytes.length >= 12
                && "RIFF".equals(new String(bytes, 0, 4, StandardCharsets.US_ASCII))
                && "WEBP".equals(new String(bytes, 8, 4, StandardCharsets.US_ASCII));
    }

    private static boolean startsWith(byte[] bytes, byte[] signature) {
        if (bytes.length < signature.length) {
            return false;
        }

        for (int i = 0; i < signature.length; i++) {
            if (bytes[i] != signature[i]) {
                return false;
            }
        }

        return true;
    }

    private static int unsigned(byte value) {
        return value & 0xFF;
    }

}
